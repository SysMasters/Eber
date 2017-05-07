package com.eber.ui.slideinfo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.eber.EBERApp;
import com.eber.R;
import com.eber.bean.SlideInfo;
import com.eber.views.NotDragSeekBar;

public class NewsFragment extends Fragment {
	private String channelName;
	private View view;
	SlideInfo si = null;

//	private void setNotDragSeekBar(NotDragSeekBar ndsb, double[] parm, double value){
//		ndsb.setMax(100);
//		int num = 100/(parm.length+1);
//		int imgNum = 0;
//		for (int i = 0; i < parm.length; i++){
//			if (i == 0){		// 第一个值
//				if (value < parm[i]) {
//					ndsb.setProgress((int) (num / parm[i] * value));
//					imgNum ++;
//					ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
//					break;
//				}
//			}
//			if (i != parm.length-1 && i != 0){		// 中间的值
//				imgNum ++;
//				if (value > parm[i] && value <parm[i+1]){
//					ndsb.setProgress((int) ((i+1) * num + num / (parm[i+1]-parm[i]) * (value - parm[i])));
//					ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
//					break;
//				}
//			}
//			if (i == parm.length -1){		// 最后一个值
//				if (value > parm[i]){
//					ndsb.setProgress((int) (num * parm.length + num / (100 - parm[i]) * (value - parm[i])));
//					imgNum ++;
//					ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
//					break;
//				}
//			}
//		}
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view==null){		//优化View减少View的创建次数
			Log.i("channelName","channelName:"+channelName);
			if (channelName.equals("体检报告")) {
				view = inflater.inflate(R.layout.fragment_slide_first, null);
			}else {
				view = inflater.inflate(R.layout.fragment_slide, null);
				TextView tvContent = (TextView) view.findViewById(R.id.f_slide_context_tv);
				ImageView img = (ImageView) view.findViewById(R.id.f_slide_img);
				TextView tvIndName = (TextView) view.findViewById(R.id.f_slide_indicate_name_tv);
				TextView tvMeaning = (TextView) view.findViewById(R.id.f_slide_meaning_tv);
				TextView tvName = (TextView) view.findViewById(R.id.f_slide_name_tv);
				NotDragSeekBar ndsb = (NotDragSeekBar) view.findViewById(R.id.f_slide_ndsb);
				TextView tvValue = (TextView) view.findViewById(R.id.f_slide_value_tv);
				tvIndName.setText(si.indicateName);
				tvValue.setText("("+si.value+")");
				tvMeaning.setText(si.meaning);
				tvContent.setText(si.context);
				tvName.setText(si.indicateName+si.name);
				ndsb.setMax(1000);
				try {
					ndsb.setProgress((int) (Double.parseDouble(si.rate) * 10));
				}catch (Exception e){
					ndsb.setProgress(0);
				}

				if (channelName.equals("BMI")) {
					img.setImageResource(R.mipmap.ico_bmi);
					ndsb.setProgressDrawable(getActivity().getResources()
							.getDrawable(R.mipmap.ico_bmi_sb_bg));
					if (si.name.equals("偏瘦"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("微胖"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("肥胖"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
				}
				if (channelName.equals("脂肪率")) {
					img.setImageResource(R.mipmap.ico_zhifanglv);
					if (EBERApp.user.sex == 1) {
						ndsb.setProgressDrawable(getActivity().getResources()
								.getDrawable(R.mipmap.ico_zhifanglv_nan_sb_bg));
					} else {
						ndsb.setProgressDrawable(getActivity().getResources()
								.getDrawable(R.mipmap.ico_zhifanglv_nv_sb_bg));
					}
					if (si.name.equals("偏瘦"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("微胖"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("肥胖"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
				}
				if (channelName.equals("蛋白质")) {
					img.setImageResource(R.mipmap.ico_danbaizhi);
					ndsb.setProgressDrawable(getActivity().getResources()
							.getDrawable(R.mipmap.ico_danbaizhi_sb_bg));
					if (si.name.equals("不足"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("优秀"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
				}
				if (channelName.equals("体水分")) {
					img.setImageResource(R.mipmap.ico_tishuifen);
					if (EBERApp.user.sex == 1) {
						ndsb.setProgressDrawable(getActivity().getResources()
								.getDrawable(R.mipmap.ico_tishuifen_nan_sb_bg));
					} else {
						ndsb.setProgressDrawable(getActivity().getResources()
								.getDrawable(R.mipmap.ico_tishuifen_nv_sb_bg));
					}
					if (si.name.equals("不足"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("优秀"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
				}
				if (channelName.equals("肌肉量")) {
					img.setImageResource(R.mipmap.ico_jirouliang);
					if (EBERApp.user.sex == 1) {
						ndsb.setProgressDrawable(getActivity().getResources()
								.getDrawable(R.mipmap.ico_jirouliang_nan_sb_bg));
					} else {
						ndsb.setProgressDrawable(getActivity().getResources()
								.getDrawable(R.mipmap.ico_jirouliang_nv_sb_bg));
					}
					if (si.name.equals("不足"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("优秀"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
				}
				if (channelName.equals("皮下脂肪")) {
					img.setImageResource(R.mipmap.ico_pixiazhifang);
					ndsb.setProgressDrawable(getActivity().getResources()
							.getDrawable(R.mipmap.ico_pixiazhifang_sb_bg));
					if (si.name.equals("偏低"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("偏高"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
				}
				if (channelName.equals("骨量")) {
					img.setImageResource(R.mipmap.ico_guliang);
					if (EBERApp.user.sex == 1) {
						ndsb.setProgressDrawable(getActivity().getResources()
								.getDrawable(R.mipmap.ico_guliang_nan_sb_bg));
					}else {
						ndsb.setProgressDrawable(getActivity().getResources()
								.getDrawable(R.mipmap.ico_guliang_nv_sb_bg));
					}
					if (si.name.equals("不足"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("优秀"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
				}
				if (channelName.equals("基础代谢")) {
					img.setImageResource(R.mipmap.ico_jichudaixie);
					if (EBERApp.user.sex == 1) {
						ndsb.setProgressDrawable(getActivity().getResources()
								.getDrawable(R.mipmap.ico_jichudaixie_nan_sb_bg));
					}else {
						ndsb.setProgressDrawable(getActivity().getResources()
								.getDrawable(R.mipmap.ico_jichudaixie_nv_sb_bg));
					}
					if (si.name.equals("未达标"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("达标"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
				}
				if (channelName.equals("内脏脂肪等级")) {
					img.setImageResource(R.mipmap.ico_neizangzhifang);
					ndsb.setProgressDrawable(getActivity().getResources()
							.getDrawable(R.mipmap.ico_neizangzhifang_sb_bg));
					if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("偏高"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("危险"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
				}
				if (channelName.equals("骨质酥松风险")) {
					img.setImageResource(R.mipmap.ico_guzhisusong);
					ndsb.setProgressDrawable(getActivity().getResources()
							.getDrawable(R.mipmap.ico_guzhisusong_sb_bg));
					if (si.name.equals("低风险"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("中风险"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
					else if (si.name.equals("高风险"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
				}
			}
		}
		ViewGroup parent=(ViewGroup)view.getParent();
		if(parent!=null){//如果View已经添加到容器中，要进行删除，否则会报错
			parent.removeView(view);
		}
		return view;
	}
	@Override
	public void setArguments(Bundle bundle) {
		channelName=bundle.getString("name");
		si = (SlideInfo) bundle.getSerializable("slide_info");
	}
	
}
