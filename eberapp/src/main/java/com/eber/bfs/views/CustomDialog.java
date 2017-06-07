package com.eber.bfs.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.eber.bfs.R;

/**
 * Created by wxd on 2017/6/4.
 */

public class CustomDialog {
    private void showAgeDialog(Context context, String str1, String str2, String str3){
        final Dialog dialog1 = new Dialog(context);

        View contentView1 = LayoutInflater.from(context).inflate(
                R.layout.dialog_age_remind, null);
        dialog1.setContentView(contentView1);
        dialog1.setCanceledOnTouchOutside(true);
        Button btn = (Button) contentView1.findViewById(R.id.age_remind_dialog_btn);
        TextView tv = (TextView) contentView1.findViewById(R.id.age_remind_dialog_msg);
        String msg = str1 + "<font color='#AB6B04'>" + str2 + "</font>" + str3;
        tv.setText(Html.fromHtml(msg));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }
}
