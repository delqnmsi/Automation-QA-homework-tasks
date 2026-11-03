package threads;

import threads.models.FileTransferResult;
import threads.services.DownloadInfo;
import threads.services.DownloadService;
import threads.services.UploadService;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.Semaphore;

/**
 * {@link FileTransferer} that gates concurrent transfers with a {@link Semaphore} sized to a
 * combined in-flight-bytes budget: permits equal to a file's size are acquired before streaming
 * and released once the upload completes (success or failure), so no amount of parallelism can
 * hold more than the budget's worth of file data in memory at once.
 */
public class MemoryBoundedFileTransferer implements FileTransferer {

    private final DownloadService downloadService;
    private final UploadService uploadService;
    private final Semaphore memoryBudget;

    public MemoryBoundedFileTransferer(DownloadService downloadService, UploadService uploadService, int memoryBudgetBytes) {
        this.downloadService = Objects.requireNonNull(downloadService, "downloadService must not be null");
        this.uploadService = Objects.requireNonNull(uploadService, "uploadService must not be null");
        this.memoryBudget = new Semaphore(memoryBudgetBytes, true);
    }

    @Override
    public FileTransferResult transfer(DownloadInfo info) {
        Instant start = Instant.now();
        int size = info.getSize();
        boolean permitAcquired = false;
        try {
            memoryBudget.acquire(size);
            permitAcquired = true;

            try (InputStream data = downloadService.download(info)) {
                uploadService.doUpload(info.getFileKey(), data, size);
            }

            return FileTransferResult.success(info.getOriginalFileName(), Duration.between(start, Instant.now()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return FileTransferResult.failure(info.getOriginalFileName(), Duration.between(start, Instant.now()),
                    "Interrupted while transferring: " + e.getMessage());
        } catch (Exception e) {
            return FileTransferResult.failure(info.getOriginalFileName(), Duration.between(start, Instant.now()),
                    e.getMessage());
        } finally {
            if (permitAcquired) {
                memoryBudget.release(size);
            }
        }
    }
}
