package threads;

import threads.services.DownloadInfo;

import java.util.List;

/**
 * A single restriction a package's files must satisfy before being transferred. New
 * restrictions are added by writing a new implementation and listing it in
 * {@link TransferRestrictions}'s rule set - neither {@code checkCombinedRestrictions} nor any
 * existing rule needs to change.
 */
interface TransferRule {

    void check(List<DownloadInfo> infos);
}
