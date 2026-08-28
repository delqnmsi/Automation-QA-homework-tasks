package threads.models;

import threads.services.DownloadInfo;

public class SimpleDownloadInfo implements DownloadInfo {

    private final int size;
    private final String originalFileName;
    private final String fileKey;
    private final String downloadURL;

    public SimpleDownloadInfo(int size, String originalFileName, String fileKey, String downloadURL) {
        this.size = size;
        this.originalFileName = originalFileName;
        this.fileKey = fileKey;
        this.downloadURL = downloadURL;
    }

    public static SimpleDownloadInfo downloadInfoOf(String originalFileName, int size) {
        return new SimpleDownloadInfo(
                size,
                originalFileName,
                "key-" + originalFileName,
                "http://download/" + originalFileName);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String getOriginalFileName() {
        return originalFileName;
    }

    @Override
    public String getFileKey() {
        return fileKey;
    }

    @Override
    public String getDownloadURL() {
        return downloadURL;
    }

    @Override
    public String toString() {
        return "SimpleDownloadInfo{name='" + originalFileName + "', size=" + size + "}";
    }
}
