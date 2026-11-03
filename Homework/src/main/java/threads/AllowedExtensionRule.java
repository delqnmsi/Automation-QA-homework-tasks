package threads;

import threads.services.DownloadInfo;

import java.util.List;

final class AllowedExtensionRule implements TransferRule {

    @Override
    public void check(List<DownloadInfo> infos) {
        for (DownloadInfo info : infos) {
            TransferRestrictions.checkAllowedExtensions(info.getOriginalFileName());
        }
    }
}
