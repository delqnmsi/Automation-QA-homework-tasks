package threads;

import threads.models.FileTransferResult;
import threads.services.DownloadInfo;

/**
 * Moves a single file from the download side to the upload side and reports the outcome.
 * Separated from {@link PackageTransferService} so the orchestrator's job stays "fetch, validate,
 * fan out, collect" while this contract's job is "safely move one file" - implementations are
 * free to add retries, different gating strategies, etc. without PackageTransferService changing.
 */
public interface FileTransferer {

    FileTransferResult transfer(DownloadInfo info);
}
