package download;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;

import com.ticket.validation.terminal.R;
import com.ticket.validation.terminal.util.ToastUtil;

public abstract class FileDownloadManager {

    protected DownloadManager mZMDownloadManager;
    protected Context mContext;

    public FileDownloadManager(Context context) {
        this.mContext = context;
        mZMDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public long download(String url, String displayName) {
        preDowload(url, displayName);
        return execDownload(url, displayName);
    }

    public abstract void preDowload(String url, String displayName);

    public abstract void postDowload(int downloadStatus, String filePath, String url, String displayName);

    public abstract String getBaseDownloadPath();

    public abstract String getFileName(String url);

    protected long execDownload(String url, String displayName) {
        try {
            Uri resource = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(resource);
            request.setVisibleInDownloadsUi(true);
            request.setTitle(displayName);
            request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
            request.setDestinationInExternalPublicDir(getBaseDownloadPath(), getFileName(url));
            request.setMimeType("application/vnd.android.package-archive");
            request.setDescription(displayName);
            request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            long id = mZMDownloadManager.enqueue(request);
            ToastUtil.showToast(mContext, R.string.download_start);
            return id;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return -100;

    }

    private DownloadModel queryDownloadStatus(long dowloadItemId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(dowloadItemId);
        Cursor c = mZMDownloadManager.query(query);
        if (c.moveToFirst()) {
            DownloadModel downloadModel = new DownloadModel();

            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                case DownloadManager.STATUS_PENDING:
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    break;
                case DownloadManager.STATUS_FAILED:
                    mZMDownloadManager.remove(dowloadItemId);
                    break;
            }

            String title = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
            String filePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
            String url = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI));

            downloadModel.status = status;
            downloadModel.title = title;
            downloadModel.filePath = filePath;
            downloadModel.url = url;
            return downloadModel;
        }
        return null;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long itemId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadModel downloadModel = queryDownloadStatus(itemId);

            if (downloadModel != null) {
                postDowload(downloadModel.status, downloadModel.filePath, downloadModel.url, downloadModel.title);
            }
        }
    };

    class DownloadModel {
        private int status;
        private String title, url, filePath;
    }
}
