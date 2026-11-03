package threads;

import threads.services.DownloadInfo;

import java.util.List;

final class MaxFileSizeRule implements TransferRule {

    @Override
    public void check(List<DownloadInfo> infos) {
        for (DownloadInfo info : infos) {
            TransferRestrictions.checkFileFitsMaxDownloadSizeAllowed(info.getOriginalFileName(), info.getSize());
        }
    }
}
