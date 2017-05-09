package com.eber.ui.my;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.utils.SPKey;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by 烛九阴 on 2017/5/6.
 * 我的提醒
 */

public class MyRemindAct extends BaseActivity {

    @ViewInject(R.id.title_content)
    TextView title;
    @ViewInject(R.id.remind_no_state)
    TextView tvState;
    @ViewInject(R.id.remind_zao_img)
    ImageView imgZao;
    @ViewInject(R.id.remind_zhong_img)
    ImageView imgZhong;
    @ViewInject(R.id.remind_wan_img)
    ImageView imgWan;
    @ViewInject(R.id.remind_shequ_img)
    ImageView imgShequ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_myremind_act);
        super.onCreate(savedInstanceState);
        title.setText("我的提醒");
        String r = EBERApp.spUtil.getStringData(SPKey.REMIND);
        if (!r.equals("")){
            String[] rs = r.split(",");
            zao = Boolean.parseBoolean(rs[0]);
            zhong = Boolean.parseBoolean(rs[1]);
            wan = Boolean.parseBoolean(rs[2]);
            shequ = Boolean.parseBoolean(rs[3]);
            setViewVaule(zao, zhong, wan, shequ);
        }
    }

    private void setViewVaule(boolean zao, boolean zhong, boolean wan, boolean shequ) {
        if (zao){
            imgZao.setImageResource(R.mipmap.icon_on);
        }else{
            imgZao.setImageResource(R.mipmap.icon_off);
        }
        if (zhong){
            imgZhong.setImageResource(R.mipmap.icon_on);
        }else{
            imgZhong.setImageResource(R.mipmap.icon_off);
        }
        if (wan){
            imgWan.setImageResource(R.mipmap.icon_on);
        }else{
            imgWan.setImageResource(R.mipmap.icon_off);
        }
        if (shequ){
            imgShequ.setImageResource(R.mipmap.icon_on);
        }else{
            imgShequ.setImageResource(R.mipmap.icon_off);
        }
        if (zao || zhong || wan || shequ){
            tvState.setText("已开启");
        }else{
            tvState.setText("已关闭");
        }
        EBERApp.spUtil.putData(SPKey.REMIND, zao+","+zhong+","+wan+","+shequ);
    }

    @Override
    public void setListener() {
        imgZao.setOnClickListener(clickLis);
        imgZhong.setOnClickListener(clickLis);
        imgWan.setOnClickListener(clickLis);
        imgShequ.setOnClickListener(clickLis);
    }

    private boolean zao;
    private boolean zhong;
    private boolean wan;
    private boolean shequ;

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.remind_zao_img:
                    zao = !zao;
                    setViewVaule(zao, zhong, wan, shequ);
                    break;
                case R.id.remind_zhong_img:
                    zhong = !zhong;
                    setViewVaule(zao, zhong, wan, shequ);
                    break;
                case R.id.remind_wan_img:
                    wan = !wan;
                    setViewVaule(zao, zhong, wan, shequ);
                    break;
                case R.id.remind_shequ_img:
                    shequ = !shequ;
                    setViewVaule(zao, zhong, wan, shequ);
                    break;
            }
        }
    };
}
