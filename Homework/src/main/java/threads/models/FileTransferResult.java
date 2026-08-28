package threads.models;

import java.time.Duration;

public class FileTransferResult {

    private final String fileName;
    private final Duration uploadTime;
    private final boolean success;
    private final String errorMessage;

    private FileTransferResult(String fileName, Duration uploadTime, boolean success, String errorMessage) {
        this.fileName = fileName;
        this.uploadTime = uploadTime;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public static FileTransferResult success(String fileName, Duration uploadTime) {
        return new FileTransferResult(fileName, uploadTime, true, null);
    }

    public static FileTransferResult failure(String fileName, Duration uploadTime, String errorMessage) {
        return new FileTransferResult(fileName, uploadTime, false, errorMessage);
    }

    public String getFileName() {
        return fileName;
    }

    public Duration getUploadTime() {
        return uploadTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "FileTransferResult{name='" + fileName + '\''
                + ", uploadTime=" + uploadTime.toMillis() + "ms"
                + ", " + (success ? "SUCCESS" : "FAILURE: " + errorMessage)
                + '}';
    }
}
