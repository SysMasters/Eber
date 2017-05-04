package com.eber.ui.slideinfo.db;

import com.eber.ui.slideinfo.entity.Channel;

import java.util.ArrayList;
import java.util.List;

public class ChannelDb {
	
	private static List<Channel>   selectedChannel=new ArrayList<Channel>();
	static{
		selectedChannel.add(new Channel("","BMI",0,"",""));
		selectedChannel.add(new Channel("","脂肪率",0,"",""));
		selectedChannel.add(new Channel("","蛋白质",0,"",""));
		selectedChannel.add(new Channel("","体水分",0,"",""));
		selectedChannel.add(new Channel("","肌肉量",0,"",""));
		selectedChannel.add(new Channel("","皮下脂肪",0,"",""));
		selectedChannel.add(new Channel("","骨量",0,"",""));
		selectedChannel.add(new Channel("","基础代谢",0,"",""));
		selectedChannel.add(new Channel("","内脏脂肪等级",0,"",""));
		selectedChannel.add(new Channel("","骨质酥松",0,"",""));
	}
	public static  List<Channel> getSelectedChannel(){
		 return selectedChannel;
	}
}
