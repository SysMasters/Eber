package com.eber.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.eber.R;

/**
 * Created by wxd on 2017/5/7.
 */

public class LoadMoreListView  extends ListView implements AbsListView.OnScrollListener {
    private Parameters mParameters;
    private int mCurrentPage;
    private RefreshListener mRefreshListener;
    private int mLastVisibleEnd;
    private View mFooter;
    private boolean hasFooter;
    private boolean isRoutineFinished;


    public LoadMoreListView(Context context) {
        super(context);
        initializeParameters(null);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeParameters(attrs);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeParameters(attrs);
        init();
    }

    private void init() {
        setOnScrollListener(this);
        hasFooter = false;
        isRoutineFinished = false;
        mLastVisibleEnd = -1;
        mCurrentPage = 0;
    }

    private void initializeParameters(AttributeSet attrs) {
        mParameters = new Parameters();
        if (attrs == null) return;
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.LoadMoreListView);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.LoadMoreListView_sampleSize:
                    mParameters.setSampleSize(a.getInt(attr, 10));
                    break;
            }
        }
        a.recycle();
    }

    public void setLoadMoreView(View view) {
        if (mFooter != null) removeFooterView(mFooter);
        mFooter = view;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public Parameters getParameters() {
        return mParameters;
    }

    public void setParameters(Parameters parameters) {
        mParameters = parameters;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastItem = firstVisibleItem + visibleItemCount;

        if (lastItem == totalItemCount) {
            if (lastItem != mLastVisibleEnd) {
                mLastVisibleEnd = lastItem;
                triggerLastVisible();
            }
        }
    }

    private void triggerLastVisible() {
        if (!hasFooter || isRoutineFinished) return;
        mCurrentPage++;
        if (mRefreshListener != null)
            mRefreshListener.refresh(mCurrentPage, mParameters.getSampleSize(), this, (BaseAdapter) ((HeaderViewListAdapter) getAdapter()).getWrappedAdapter());
    }

    public RefreshListener getRefreshListener() {
        return mRefreshListener;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        mRefreshListener = refreshListener;
    }

    public void setLoadingRoutineFinished() {
        isRoutineFinished = true;
        removeFooter();
    }

    @Override
    public void setAdapter(final ListAdapter adapter) {
        super.setAdapter(adapter);
        isRoutineFinished = false;
        mCurrentPage = 0;
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if (!isRoutineFinished) notifyDataChanged();
            }
        });
        notifyDataChanged();
    }

    private void addFooter() {
        if (mFooter == null) return;
        addFooterView(mFooter);
        hasFooter = true;
    }

    private void notifyDataChanged() {
        if (getAdapter() == null) return;
        if (getAdapter().getCount() >= mParameters.getSampleSize() * mCurrentPage && !hasFooter)
            addFooter();
        else if (getAdapter().getCount() < mParameters.getSampleSize() * mCurrentPage && hasFooter)
            setLoadingRoutineFinished();
    }

    private void removeFooter() {
        if (mFooter == null) return;
        mLastVisibleEnd--;
        removeFooterView(mFooter);
        hasFooter = false;
    }

    public class Parameters {
        private int mSampleSize;

        public int getSampleSize() {
            return mSampleSize;
        }

        public void setSampleSize(int sampleSize) {
            mSampleSize = sampleSize;
        }
    }

    public interface RefreshListener {
        /**
         * @param pageNumber page number
         * @param sampleSize sample size defined on xml parameters
         * @param listView   the listview itself
         * @param adapter    the adapter that you need to repopulate
         */
        void refresh(int pageNumber, int sampleSize, LoadMoreListView listView, BaseAdapter adapter);
    }
}
