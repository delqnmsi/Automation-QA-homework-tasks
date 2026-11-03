package threads.models;

import java.time.Duration;
import java.util.List;

public class PackageTransferReport {

    private final List<FileTransferResult> fileResults;
    private final Duration totalDuration;

    public PackageTransferReport(List<FileTransferResult> fileResults, Duration totalDuration) {
        this.fileResults = List.copyOf(fileResults);
        this.totalDuration = totalDuration;
    }

    public List<FileTransferResult> getFileResults() {
        return fileResults;
    }

    public Duration getTotalDuration() {
        return totalDuration;
    }

    public long getSuccessCount() {
        return fileResults.stream().filter(FileTransferResult::isSuccess).count();
    }

    public long getFailureCount() {
        return fileResults.size() - getSuccessCount();
    }

    public List<FileTransferResult> getFailures() {
        return fileResults.stream().filter(result -> !result.isSuccess()).toList();
    }

    public boolean isFullSuccess() {
        return getFailureCount() == 0;
    }

    @Override
    public String toString() {
        return "PackageTransferReport{"
                + "files=" + fileResults.size()
                + ", succeeded=" + getSuccessCount()
                + ", failed=" + getFailureCount()
                + ", totalDuration=" + totalDuration.toMillis() + "ms"
                + ", results=" + fileResults
                + '}';
    }
}
