package com.eber.bfs.views;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.eber.bfs.R;

/**
 * Created by wxd on 2017/6/4.
 */

public class CustomDialog {
    public static void showAgeDialog(Context context, String str1, String str2, String str3){
        final AlertDialog builder = new AlertDialog.Builder(context)
                .create();
        builder.getWindow().setContentView(R.layout.dialog_age_remind);//设置弹出框加载的布局

        TextView tv_title = (TextView) builder.findViewById(R.id.age_remind_dialog_msg);
        tv_title.setText(String.format("<html>%s<font color='#0000FFF'>%s</font>%s</html>", str1, str2, str3));

        builder.getWindow()
                .findViewById(R.id.age_remind_dialog_btn)
                .setOnClickListener(new View.OnClickListener() {  //按钮点击事件
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
        builder.show();
    }
}
