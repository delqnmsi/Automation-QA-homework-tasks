package threads;

import threads.services.DownloadInfo;

import java.util.List;

final class UniqueFileNamesRule implements TransferRule {

    @Override
    public void check(List<DownloadInfo> infos) {
        TransferRestrictions.checkFileNamesUnique(infos.stream().map(DownloadInfo::getOriginalFileName).toList());
    }
}
