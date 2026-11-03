package threads.services;

import java.io.InputStream;
import java.util.List;

public interface DownloadService {

    List<DownloadInfo> getDownloadInfos(long packageId);

    InputStream download(DownloadInfo info) throws Exception;
}
