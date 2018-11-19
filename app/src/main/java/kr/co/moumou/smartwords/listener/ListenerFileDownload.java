package kr.co.moumou.smartwords.listener;

public interface ListenerFileDownload {
    void onStartFileDownload();
    void onEndFileDownload();
    void onStartDownload();
    void onDownloading(int progress);
    void onDownloadComplete();
    void onStartDecompress();
    void onDecompressing(int progress);
    void onDecompressComplete();
    void onDownloadCancel();
    void onException(String e);
}
