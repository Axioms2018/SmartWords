package kr.co.moumou.smartwords.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.moumou.smartwords.R;
import kr.co.moumou.smartwords.controller.ControllerFileDownload;
import kr.co.moumou.smartwords.listener.ListenerFileDownload;
import kr.co.moumou.smartwords.util.LogUtil;
import kr.co.moumou.smartwords.vo.VoDownload;

/**
 * Created by 김민정 on 2018-04-01.
 */

public class DownloadDialog extends Dialog {

    private Context mContext;
    private ArrayList<VoDownload> arrayDownload = null;

    private TextView tv_download;
    private ProgressBar progressbar;
    private int totalDownloads = 0;
    private int currentDownload = 0;
    private DownloadDialogListener listener;
    private VoDownload downloadInfo;

    public enum DownloadType {
        None,
        ApkFile
    }

    private DownloadType currentDownloadType = DownloadType.None;

    public interface DownloadDialogListener{
        void onDownloadComplete();
    }

    public DownloadDialog(Context context, DownloadDialogListener listener, ArrayList<VoDownload> arrayDownload) {
        super(context);
        this.mContext = context;
        this.listener = listener;
        this.arrayDownload = arrayDownload;
        totalDownloads = arrayDownload.size();

    }

    public DownloadDialog(Context context, DownloadDialogListener listener, VoDownload voDownload) {
        super(context);
        this.mContext = context;
        this.listener = listener;
        this.downloadInfo = voDownload;
        currentDownloadType = DownloadType.ApkFile;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_download);

        setCanceledOnTouchOutside(false);

        tv_download = (TextView) findViewById(R.id.tv_download);
        progressbar = (ProgressBar) findViewById(R.id.progress);
        progressbar.setProgress(0);

}

    @Override
    public void cancel() {
        if(currentDownloadType == DownloadType.ApkFile) {
            ControllerFileDownload.getInstance(mContext).cancelDownload(this.arrayDownload.get(0).getTAG());
            super.cancel();
            return;
        }

        if (arrayDownload.size() <= currentDownload)
            return;

        ControllerFileDownload.getInstance(mContext).cancelDownload(arrayDownload.get(currentDownload).getTAG());
        ControllerFileDownload.getInstance(mContext).cancelDecompress();
        super.cancel();
    }

    public void downloadAPKFile() {
        if(null == this.arrayDownload) this.arrayDownload = new ArrayList<>();
        this.arrayDownload.clear();
        this.arrayDownload.add(downloadInfo);
        ControllerFileDownload.getInstance(mContext).setListener(listenerDownload);
        ControllerFileDownload.getInstance(mContext).downloadApk(downloadInfo);


    }


    /**
     * Download
     */
    public void download() {
        if(totalDownloads < (currentDownload + 1)) {
            listenerDownload.onEndFileDownload();
            return;
        }
        if(totalDownloads > 0) {
            //tv_title.setText(R.string.dialog_loading);
            ControllerFileDownload.getInstance(mContext).setListener(listenerDownload);
            ControllerFileDownload.getInstance(mContext).download(arrayDownload.get(currentDownload));
        }
    }

    /**
     * Decompress
     */
    private void decompress() {
        ControllerFileDownload.getInstance(mContext).setListener(listenerDownload);
        ControllerFileDownload.getInstance(mContext).descompress(arrayDownload.get(currentDownload));
    }

    /**
     * ListenerDownload
     */
    private ListenerFileDownload listenerDownload = new ListenerFileDownload() {

        @Override
        public void onStartFileDownload() {
//            if (!isShowing())
//                show();
        }

        @Override
        public void onEndFileDownload() {
            LogUtil.d("onEndFileDownload ------------------------------------------------------");
            dismiss();
            listener.onDownloadComplete();
        }

        @Override
        public void onStartDownload() {}

        @Override
        public void onDownloading(int progress) {
            //LogUtil.d("onDownloading");

            if (tv_download == null)
                return;

            tv_download.setText(progress + "%");
            progressbar.setProgress(progress);
        }

        @Override
        public void onDownloadComplete() {
            LogUtil.d("onDownloadComplete ------------------------------------------------------");
            ControllerFileDownload.getInstance(mContext).saveDownloadDB(arrayDownload.get(currentDownload));
            decompress();
        }

        @Override
        public void onStartDecompress() {}

        @Override
        public void onDecompressing(int progress) {

            if (tv_download == null)
                return;

            tv_download.setText(progress + "%");
            progressbar.setProgress(progress);
        }

        @Override
        public void onDecompressComplete() {
            LogUtil.d("onDecompressComplete ----------------------------------------------------");
            ControllerFileDownload.getInstance(mContext).saveDecompress(arrayDownload.get(currentDownload));
            currentDownload++;
            download();
        }

        @Override
        public void onDownloadCancel() {
            ControllerFileDownload.getInstance(mContext).cancelDownload(arrayDownload.get(currentDownload).getTAG());
        }

        @Override
        public void onException(String e) {
            LogUtil.d("onException " + e);
            LogUtil.d("error : " + "다운로드 중 오류가 발생했습니다. 본사에 문의 바랍니다.");

//            ToastUtil.show(mContext, R.string.dialog_download_error);
            dismiss();
        }
    };
}
