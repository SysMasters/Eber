package com.eber.bfs.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eber.bfs.http.NetUtils;
import com.eber.bfs.listener.IBaseFragment;
import com.lidroid.xutils.ViewUtils;

import java.util.Map;

/**
 * Created by WangLibo on 2017/4/23.
 */

public abstract class BaseFragment extends Fragment implements IBaseFragment {

    private View view;
    protected com.eber.bfs.base.BaseActivity mActivity;
    protected NetUtils netUtils;
    protected Map<String, String> param;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(bindLayout(), container, false);
        }
        ViewUtils.inject(this, view);
        onBusiness();
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (com.eber.bfs.base.BaseActivity) context;
        netUtils = new NetUtils();
    }

    protected void startActivity(Class<? extends com.eber.bfs.base.BaseActivity> cls) {
        Intent intent = new Intent(mActivity, cls);
        startActivity(intent);
    }
}
