package es.voghdev.pdfviewpager.library;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

/**
 * @Description:
 * @author: mick
 * @CreateAt: 7/7/21 10:44 AM
 * @UpdateUser: mick
 * @UpdateDate: 7/7/21 10:44 AM
 * @UpdateRemark:
 */
public class RemotePDFView extends LinearLayout implements DownloadFile.Listener {
    private RemotePDFViewPager remotePDFViewPager;
    private PDFPagerAdapter adapter;
    private OnPdfLoadListener mLoadListener;

    public RemotePDFView(Context context) {
        this(context, null);
    }

    public RemotePDFView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(getContext(), FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
        if (mLoadListener != null) {
            mLoadListener.onLoadSuccess(destinationPath);
        }
    }

    @Override
    public void onFailure(Exception e) {
        if (mLoadListener != null) {
            mLoadListener.onLoadFail(e);
        }
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
        if (mLoadListener != null && mLoadListener instanceof onPdfLoadProgressListener) {
            ((onPdfLoadProgressListener) mLoadListener).onProgressUpdate(progress, total);
        }
    }

    private void updateLayout() {
        removeAllViewsInLayout();
        addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void setLoadListener(OnPdfLoadListener loadListener) {
        mLoadListener = loadListener;
    }

    /**
     * 开始加载
     * @param url
     */
    public void loadPdf(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (getChildCount()>0) {
            removeAllViewsInLayout();
        }
        remotePDFViewPager = new RemotePDFViewPager(getContext(), url, this);
        remotePDFViewPager.setId(R.id.pdfViewPager_lib);
    }


    interface OnPdfLoadListener {
        void onLoadFail(Exception e);

        void onLoadSuccess(String destPath);
    }

    interface onPdfLoadProgressListener extends OnPdfLoadListener {
        void onProgressUpdate(int progress, int total);
    }
}
