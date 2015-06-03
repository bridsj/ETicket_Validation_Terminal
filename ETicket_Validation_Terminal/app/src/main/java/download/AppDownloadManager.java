package download;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.zuiapps.suite.utils.file.IOUtil;
import com.zuiapps.suite.utils.security.MD5Util;

import java.io.File;

public class AppDownloadManager extends FileDownloadManager {

    private static AppDownloadManager mAppDownloadManager = null;
    private String downloadDir = "/Download/";

    private AppDownloadManager(Context context) {
        super(context);
    }

    private AppDownloadManager(Context context, String downloadDir) {
        super(context);
        this.downloadDir = downloadDir;
    }

    public static AppDownloadManager getInstance(Context context) {
        if (mAppDownloadManager == null) {
            synchronized (AppDownloadManager.class) {
                mAppDownloadManager = new AppDownloadManager(context);
            }
        }
        return mAppDownloadManager;
    }

    public static AppDownloadManager getInstance(Context context, String downloadUrl) {
        if (mAppDownloadManager == null) {
            synchronized (AppDownloadManager.class) {
                mAppDownloadManager = new AppDownloadManager(context, downloadUrl);
            }
        }
        mAppDownloadManager.downloadDir = downloadUrl;

        String dir = IOUtil.getSDCardPath() + File.separator + downloadUrl;
        File downloadDir = new File(dir);
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }

        return mAppDownloadManager;
    }

    public String getDownloadFilePath(String url) {
        return IOUtil.getSDCardPath() + File.separator + downloadDir + getFileName(url);
    }

    @Override
    public String getBaseDownloadPath() {
        return downloadDir;
    }

    @Override
    public String getFileName(String url) {
        return MD5Util.crypt(url) + "_" + System.currentTimeMillis() + ".apk";
    }

    @Override
    public void preDowload(String url, String displayName) {
        // NO-OP
    }

    @Override
    public void postDowload(int downloadStatus, final String filePath, String url, String displayName) {
        if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } catch (Throwable t) {
            }
        }
    }

}
