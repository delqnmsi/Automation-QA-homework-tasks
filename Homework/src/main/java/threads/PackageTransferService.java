package threads;

import threads.models.FileTransferResult;
import threads.models.PackageTransferReport;
import threads.services.DownloadInfo;
import threads.services.DownloadService;
import threads.services.UploadService;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Orchestrates moving every file of a package from the Download service (A) to the
 * Upload service (B), concurrently, while enforcing {@link TransferRestrictions}.
 *
 * <p>Design notes:
 * <ul>
 *     <li><b>Fail-fast:</b> {@link TransferRestrictions#checkCombinedRestrictions(List)} runs
 *     synchronously, before a single worker thread is scheduled, so a bad package never wastes
 *     any download/upload work.</li>
 *     <li><b>Bounded memory:</b> each file transfer is delegated to a {@link FileTransferer},
 *     which is responsible for keeping combined in-flight bytes under budget regardless of how
 *     many files run in parallel. This class only orchestrates - it fetches metadata, validates,
 *     fans the work out, and collects results; it has no opinion on how a single file transfer is
 *     made memory-safe.</li>
 *     <li><b>Bounded CPU/threads:</b> transfers run on a fixed-size worker pool, so performance
 *     scales with parallelism without spawning unbounded OS threads for large packages.</li>
 *     <li><b>Safe result collection:</b> each file transfer is isolated - one file failing never
 *     aborts the others - and results are joined back from the futures into a single
 *     {@link PackageTransferReport}.</li>
 * </ul>
 */
public class PackageTransferService implements AutoCloseable {

    private static final int DEFAULT_PARALLELISM = Math.max(2, Runtime.getRuntime().availableProcessors());

    private final DownloadService downloadService;
    private final FileTransferer transferer;
    private final ExecutorService executor;
    private final boolean ownsExecutor;

    public PackageTransferService(DownloadService downloadService, UploadService uploadService) {
        this(downloadService, uploadService, Executors.newFixedThreadPool(DEFAULT_PARALLELISM), true);
    }

    public PackageTransferService(DownloadService downloadService, UploadService uploadService, ExecutorService executor) {
        this(downloadService, uploadService, executor, false);
    }

    private PackageTransferService(DownloadService downloadService,
                                     UploadService uploadService,
                                     ExecutorService executor,
                                     boolean ownsExecutor) {
        this.downloadService = Objects.requireNonNull(downloadService, "downloadService must not be null");
        Objects.requireNonNull(uploadService, "uploadService must not be null");
        this.transferer = new MemoryBoundedFileTransferer(
                downloadService, uploadService, (int) TransferRestrictions.MAX_DOWNLOAD_SIZE_ALLOWED_100MB);
        this.executor = Objects.requireNonNull(executor, "executor must not be null");
        this.ownsExecutor = ownsExecutor;
    }

    /**
     * Fetches the package's file metadata, validates it against {@link TransferRestrictions}
     * (fail-fast, no I/O performed yet), then transfers every file from download to upload in
     * parallel and returns a structured report of the outcome.
     *
     * @throws TransferException       if any file violates a transfer restriction
     * @throws IllegalArgumentException if the package has no files
     */
    public PackageTransferReport transferPackage(long packageId) {
        List<DownloadInfo> infos = downloadService.getDownloadInfos(packageId);

        // Validate before scheduling any work: a rule violation must waste zero download/upload effort.
        TransferRestrictions.checkCombinedRestrictions(infos);

        Instant overallStart = Instant.now();

        List<CompletableFuture<FileTransferResult>> futures = infos.stream()
                .map(info -> CompletableFuture.supplyAsync(() -> transferer.transfer(info), executor))
                .collect(Collectors.toList());

        List<FileTransferResult> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        return new PackageTransferReport(results, Duration.between(overallStart, Instant.now()));
    }

    /**
     * Shuts down the worker pool, if this service created it itself. A pool supplied by the
     * caller (via {@link #PackageTransferService(DownloadService, UploadService, ExecutorService)})
     * is left running, since the caller owns its lifecycle.
     */
    @Override
    public void close() {
        if (ownsExecutor) {
            executor.shutdown();
        }
    }
}
