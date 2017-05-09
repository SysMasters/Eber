package com.eber.ui.find;

import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.alibaba.fastjson.JSONArray;
import com.eber.R;
import com.eber.adapters.FindAdapter;
import com.eber.base.BaseFragment;
import com.eber.bean.FindArticle;
import com.eber.bean.FindImg;
import com.eber.http.HttpUrls;
import com.eber.http.StringCallback2;
import com.eber.ui.WebActivity;
import com.eber.views.LoadMoreListView;
import com.eber.views.SlideShowView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindFragment extends BaseFragment {

    @ViewInject(R.id.find_lv)
    private LoadMoreListView lv;
    private SlideShowView slideShowView;
    int page = 1;

    private FindAdapter mAdapter;
    @Override
    public int bindLayout() {
        return R.layout.fragment_find;
    }
    private  List<FindArticle> mFindArticles = new ArrayList<>();

    @Override
    public void onBusiness() {
        mAdapter = new FindAdapter(mActivity,  mFindArticles);
        View view=  mActivity.getLayoutInflater().inflate(R.layout.view_find_header,null);
        slideShowView = (SlideShowView) view.findViewById(R.id.slideshowView);
       lv.addHeaderView(view);
        lv.setAdapter(mAdapter);
        lv.setLoadMoreView(mActivity.getLayoutInflater().from(mActivity).inflate(R.layout.view_listview_loadmore, null));
        lv.setRefreshListener(new LoadMoreListView.RefreshListener() {
            @Override
            public void refresh(int pageNumber, int sampleSize, LoadMoreListView listView, BaseAdapter adapter) {
                loadData(true);
            }
        });
        lv.setOnItemClickListener(itemClickLis);
        loadData(false);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void loadData(final boolean isLoadMore){
        if (isLoadMore){
            page++;
        }
        param = new HashMap<>();
        param.put("freshtime",String.valueOf(page));
        netUtils.get(HttpUrls.FINDARTICLEALL, !isLoadMore, param, new StringCallback2("imgurlArray","articleArray") {
            @Override
            public void onSuccess(String... result) {
                List<FindArticle> findArticles = JSONArray.parseArray(result[1], FindArticle.class);
                mAdapter.refresh(findArticles);
                if (!isLoadMore){
                    List<FindImg> findImgs = JSONArray.parseArray(result[0], FindImg.class);
                    slideShowView.startPlay(findImgs);
                }else{
                    lv.setLoadingRoutineFinished();
                }
            }
        });
    }

    private AdapterView.OnItemClickListener itemClickLis = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FindArticle item = mFindArticles.get(position-1);
            WebActivity.startActivity(mActivity,String.format(HttpUrls.FINDARTICLEBYID,item.id),item.title);
        }
    };

}
