package threads.services;

public interface DownloadInfo {

    int getSize();

    String getOriginalFileName();

    String getFileKey();

    String getDownloadURL();
}
