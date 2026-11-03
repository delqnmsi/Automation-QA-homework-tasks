package threads.testutils;

import threads.models.SimpleDownloadInfo;
import threads.services.DownloadInfo;
import threads.services.DownloadService;
import threads.services.UploadService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class FakeTransferServices implements DownloadService, UploadService {

    private final List<DownloadInfo> infos;
    private final Map<String, Long> downloadDelaysMillis = new ConcurrentHashMap<>();
    private final Set<String> failingDownloads = ConcurrentHashMap.newKeySet();
    private final Set<String> failingUploads = ConcurrentHashMap.newKeySet();

    private final AtomicInteger inFlightBytes = new AtomicInteger();
    private final AtomicInteger peakInFlightBytes = new AtomicInteger();
    private final List<String> uploadedKeys = new CopyOnWriteArrayList<>();

    public FakeTransferServices(List<DownloadInfo> infos) {
        this.infos = infos;
    }

    public FakeTransferServices delayDownload(String fileName, long millis) {
        downloadDelaysMillis.put(fileName, millis);
        return this;
    }

    public FakeTransferServices failDownload(String fileName) {
        failingDownloads.add(fileName);
        return this;
    }

    public FakeTransferServices failUpload(String fileName) {
        failingUploads.add(fileName);
        return this;
    }

    public List<String> getUploadedKeys() {
        return uploadedKeys;
    }

    public int getPeakInFlightBytes() {
        return peakInFlightBytes.get();
    }


    @Override
    public List<DownloadInfo> getDownloadInfos(long packageId) {
        return infos;
    }

    @Override
    public InputStream download(DownloadInfo info) throws Exception {
        if (failingDownloads.contains(info.getOriginalFileName())) {
            throw new RuntimeException("Simulated download failure for " + info.getOriginalFileName());
        }

        peakInFlightBytes.updateAndGet(previousPeak ->
                Math.max(previousPeak, inFlightBytes.addAndGet(info.getSize())));

        Long delay = downloadDelaysMillis.get(info.getOriginalFileName());
        if (delay != null) {
            Thread.sleep(delay);
        }

        return new ByteArrayInputStream(new byte[info.getSize()]);
    }

    @Override
    public void doUpload(String key, InputStream data, int size) throws Exception {
        data.readAllBytes();
        inFlightBytes.addAndGet(-size);

        if (failingUploads.stream().anyMatch(key::contains)) {
            throw new RuntimeException("Simulated upload failure for key " + key);
        }
        uploadedKeys.add(key);
    }
}
