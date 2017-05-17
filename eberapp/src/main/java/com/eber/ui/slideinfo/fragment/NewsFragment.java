package com.eber.ui.slideinfo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eber.EBERApp;
import com.eber.R;
import com.eber.bean.SlideInfo;
import com.eber.views.NotDragSeekBar;

public class NewsFragment extends Fragment {
	private String channelName;
	private View view;
	SlideInfo si = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view==null){		//优化View减少View的创建次数
			Log.i("channelName","channelName:"+channelName);
			if (channelName.equals("体检报告")) {
				view = inflater.inflate(R.layout.fragment_slide_first, null);
				ImageView img = (ImageView) view.findViewById(R.id.f_slide_f_img);
				TextView tvType = (TextView) view.findViewById(R.id.f_slide_f_type_tv);
				TextView tvScore = (TextView) view.findViewById(R.id.f_slide_f_score_tv);
				TextView tvAge = (TextView) view.findViewById(R.id.f_slide_f_age_tv);
				TextView tvWeight = (TextView) view.findViewById(R.id.f_slide_f_weight_tv);
				TextView tvTime = (TextView) view.findViewById(R.id.f_slide_f_time_tv);
				TextView tvBodyType = (TextView) view.findViewById(R.id.f_slide_f_tv);
				TextView tvM1 = (TextView) view.findViewById(R.id.f_slide_f_m_1);
				TextView tvM2 = (TextView) view.findViewById(R.id.f_slide_f_m_2);
				TextView tvM3 = (TextView) view.findViewById(R.id.f_slide_f_m_3);
				TextView tvM4 = (TextView) view.findViewById(R.id.f_slide_f_m_4);
				TextView tvM5 = (TextView) view.findViewById(R.id.f_slide_f_m_5);
				TextView tvM6 = (TextView) view.findViewById(R.id.f_slide_f_m_6);
				TextView tvM7 = (TextView) view.findViewById(R.id.f_slide_f_m_7);
				TextView tvM8 = (TextView) view.findViewById(R.id.f_slide_f_m_8);
				TextView tvM9 = (TextView) view.findViewById(R.id.f_slide_f_m_9);
				TextView tvStype = (TextView) view.findViewById(R.id.f_slide_f_s_type_tv);
				TextView tvStypeContent = (TextView) view.findViewById(R.id.f_slide_f_s_type_content_tv);
				TextView tvSeat = (TextView) view.findViewById(R.id.f_slide_f_s_eat_jianyi_tv);
				TextView tvSeatContent = (TextView) view.findViewById(R.id.f_slide_f_s_eat_jianyi_content_tv);
				TextView tvSexe = (TextView) view.findViewById(R.id.f_slide_f_s_exe_jianyi_tv);
				TextView tvSexeContent = (TextView) view.findViewById(R.id.f_slide_f_s_exe_jianyi_content_tv);
				JSONArray ja = JSON.parseArray(si.reprot);
				String bodyType = ja.getString(0);
				tvBodyType.setText(bodyType);
				if (bodyType.equals("偏瘦型")){
					img.setImageResource(R.mipmap.ico_xiaoshou_x);
					tvM1.setBackgroundColor(getActivity().getResources().getColor(R.color.slide_first_color_red));
				}else if (bodyType.equals("低脂肪型")){
					img.setImageResource(R.mipmap.ico_dizhifang_x);
					tvM2.setBackgroundColor(getActivity().getResources().getColor(R.color.slide_first_color_yello));
				}else if (bodyType.equals("运动健美型")){
					img.setImageResource(R.mipmap.ico_yundongyuan_x);
					tvM3.setBackgroundColor(getActivity().getResources().getColor(R.color.slide_first_color_blue));
				}else if (bodyType.equals("肌肉不足型")){
					img.setImageResource(R.mipmap.ico_jiroubuzu_x);
					tvM4.setBackgroundColor(getActivity().getResources().getColor(R.color.slide_first_color_yello));
				}else if (bodyType.equals("标准型")){
					img.setImageResource(R.mipmap.ico_biaozhun_x);
					tvM5.setBackgroundColor(getActivity().getResources().getColor(R.color.slide_first_color_blue));
				}else if (bodyType.equals("标准运动型")){
					img.setImageResource(R.mipmap.ico_biaozhunjirou_x);
					tvM6.setBackgroundColor(getActivity().getResources().getColor(R.color.slide_first_color_blue));
				}else if (bodyType.equals("隐性胖")){
					img.setImageResource(R.mipmap.ico_yinxingfeipang_x);
					tvM7.setBackgroundColor(getActivity().getResources().getColor(R.color.slide_first_color_yello));
				}else if (bodyType.equals("脂肪过多型")){
					img.setImageResource(R.mipmap.ico_zhifangguoduo_x);
					tvM8.setBackgroundColor(getActivity().getResources().getColor(R.color.slide_first_color_red));
				}else if (bodyType.equals("肥胖型")){
					img.setImageResource(R.mipmap.ico_feipang_x);
					tvM9.setBackgroundColor(getActivity().getResources().getColor(R.color.slide_first_color_red));
				}
				tvType.setText(ja.getString(0));
				tvScore.setText(si.score);
				tvAge.setText(si.bodyAge);
				tvWeight.setText(si.standardWeight);
				tvTime.setText(si.updateTime.substring(2, 10));
				tvStype.setText(ja.getString(0));
				tvStypeContent.setText(ja.getString(1));
				tvSeat.setText(ja.getString(2));
				tvSeatContent.setText(ja.getString(3));
				tvSexe.setText(ja.getString(4));
				tvSexeContent.setText(ja.getString(5));
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
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_insufficient_face));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_standard_face));
					else if (si.name.equals("微胖"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_insufficient_face));
					else if (si.name.equals("肥胖"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_severity_face));
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
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_insufficient_face));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_standard_face));
					else if (si.name.equals("微胖"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_insufficient_face));
					else if (si.name.equals("肥胖"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_severity_face));
				}
				if (channelName.equals("蛋白质")) {
					img.setImageResource(R.mipmap.ico_danbaizhi);
					ndsb.setProgressDrawable(getActivity().getResources()
							.getDrawable(R.mipmap.ico_danbaizhi_sb_bg));
					if (si.name.equals("不足"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_insufficient_face));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_standard_face));
					else if (si.name.equals("优秀"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_excellent_face));
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
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_insufficient_face));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_standard_face));
					else if (si.name.equals("优秀"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_excellent_face));
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
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_insufficient_face));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_standard_face));
					else if (si.name.equals("优秀"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_excellent_face));
				}
				if (channelName.equals("皮下脂肪")) {
					img.setImageResource(R.mipmap.ico_pixiazhifang);
					ndsb.setProgressDrawable(getActivity().getResources()
							.getDrawable(R.mipmap.ico_pixiazhifang_sb_bg));
					if (si.name.equals("偏低"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_insufficient_face));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_standard_face));
					else if (si.name.equals("偏高"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_insufficient_face));
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
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_insufficient_face));
					else if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_standard_face));
					else if (si.name.equals("优秀"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_excellent_face));
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
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_insufficient_face));
					else if (si.name.equals("达标"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_excellent_face));
				}
				if (channelName.equals("内脏脂肪等级")) {
					img.setImageResource(R.mipmap.ico_neizangzhifang);
					ndsb.setProgressDrawable(getActivity().getResources()
							.getDrawable(R.mipmap.ico_neizangzhifang_sb_bg));
					if (si.name.equals("标准"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_standard_face));
					else if (si.name.equals("偏高"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_insufficient_face));
					else if (si.name.equals("危险"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_severity_face));
				}
				if (channelName.equals("骨质疏松风险")) {
					img.setImageResource(R.mipmap.ico_guzhisusong);
					ndsb.setProgressDrawable(getActivity().getResources()
							.getDrawable(R.mipmap.ico_guzhisusong_sb_bg));
					if (si.name.equals("低风险"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_excellent_face));
					else if (si.name.equals("中风险"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_insufficient_face));
					else if (si.name.equals("高风险"))
						ndsb.setThumb(getActivity().getResources().getDrawable(R.mipmap.ico_severity_face));
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
