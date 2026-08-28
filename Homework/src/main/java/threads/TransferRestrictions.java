package threads;

import threads.services.DownloadInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class TransferRestrictions {

    public static final long MAX_DOWNLOAD_SIZE_ALLOWED_100MB = 100L * 1024 * 1024;

    public static final Set<String> FORBIDDEN_EXTENSIONS =
            Set.of("cmd", "com", "dll", "dmg", "exe", "iso", "jar", "js");

    public static String extensionOf(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("File name must not be null");
        }
        int dot = fileName.lastIndexOf('.');
        boolean noDot = dot < 0;
        boolean dotIsFirstChar = dot == 0;
        boolean dotIsLastChar = dot == fileName.length() - 1;
        if (noDot || dotIsFirstChar || dotIsLastChar) {
            return "";
        }
        return fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    public static boolean isExtensionAllowed(String fileName) {
        return !FORBIDDEN_EXTENSIONS.contains(extensionOf(fileName));
    }

    public static void checkAllowedExtensions(String fileName) {
        if (!isExtensionAllowed(fileName)) {
            throw new TransferException("File extension not allowed: " + fileName);
        }
    }

    public static boolean areFileNamesUnique(List<String> fileNames) {
        Set<String> seen = new HashSet<>();
        for (String name : fileNames) {
            if (!seen.add(name)) {
                return false;
            }
        }
        return true;
    }

    public static void checkFileNamesUnique(List<String> fileNames) {
        Set<String> seen = new HashSet<>();
        for (String name : fileNames) {
            if (!seen.add(name)) {
                throw new TransferException("Duplicate file name: " + name);
            }
        }
    }

    public static boolean fileFitsMaxDownloadSizeAllowed(int size) {
        return size <= MAX_DOWNLOAD_SIZE_ALLOWED_100MB;
    }

    public static void checkFileFitsMaxDownloadSizeAllowed(String fileName, int size) {
        if (!fileFitsMaxDownloadSizeAllowed(size)) {
            throw new TransferException("File is larger than the "
                    + MAX_DOWNLOAD_SIZE_ALLOWED_100MB + " byte download max size allowed: " + fileName + " (" + size + " bytes)");
        }
    }

    public static void checkCombinedRestrictions(List<DownloadInfo> infos) {
        if (infos == null || infos.isEmpty()) {
            throw new IllegalArgumentException("Files not found");
        }

        checkFileNamesUnique(infos.stream().map(DownloadInfo::getOriginalFileName).toList());

        for (DownloadInfo info : infos) {
            checkAllowedExtensions(info.getOriginalFileName());
            checkFileFitsMaxDownloadSizeAllowed(info.getOriginalFileName(), info.getSize());
        }
    }
}
