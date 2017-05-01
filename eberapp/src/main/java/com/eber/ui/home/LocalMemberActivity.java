package com.eber.ui.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.bean.Member;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangLibo on 2017/4/23.
 */

public class LocalMemberActivity extends BaseActivity {

    @ViewInject(R.id.title_content)
    private TextView tvContent;
    @ViewInject(R.id.title_back)
    private ImageView tvBack;
    @ViewInject(R.id.title_right)
    private TextView tvRight;
    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;

    private List<Member> members;
    private CommonAdapter<Member> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_local_member);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initData();
        tvBack.setOnClickListener(clickListener);
        tvRight.setOnClickListener(clickListener);
        tvContent.setText("本地成员");
        tvRight.setText("编辑");
        tvRight.setTag(false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(mAdapter = new CommonAdapter<Member>(this, R.layout.view_local_member_item, members) {
            @Override
            protected void convert(final ViewHolder holder, Member member, int position) {
                ImageView headImage = holder.getView(R.id.local_member_head);
                TextView tvName = holder.getView(R.id.local_member_name);
                ImageView ivDel = holder.getView(R.id.local_member_del);
                ImageView ivSelected = holder.getView(R.id.local_member_selected);
                boolean flag = (boolean) tvRight.getTag();
                if (position < 2 || position == members.size() - 1) {
                    ivDel.setVisibility(View.GONE);
                } else {
                    ivDel.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
                }
                // 默认第二项为当前选中用户(前边需要集合排序，第一为主账户，第二位当前用户)
                ivSelected.setVisibility(position == 1 ? View.VISIBLE : View.INVISIBLE);
                ivDel.setTag(position);
                ivDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        members.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                });
                if (TextUtils.equals("1", member.sex)) {// 1:男
                    headImage.setImageResource(R.mipmap.ic_local_member_man);
                } else if (TextUtils.equals("2", member.sex)) {
                    headImage.setImageResource(R.mipmap.ic_local_member_woman);
                } else {
                    headImage.setImageResource(R.mipmap.ic_local_member_add);
                    headImage.setTag("add");
                    headImage.setOnClickListener(clickListener);
                }
                tvName.setText(member.userName);
            }
        });
    }

    private void initData() {
        members = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Member member = new Member("" + i, "" + i, "1", "成员" + (i + 1));
            if (i == 2 || i == 4 || i == 7) {
                member.sex = "2";
            }
            members.add(member);
        }
        members.add(new Member("", "", "-1", "添加成员"));
    }

    @Override
    public void setListener() {

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_back:
                    finish();
                    break;
                case R.id.title_right:
                    boolean flag = (boolean) tvRight.getTag();
                    if (!flag) {
                        tvRight.setText("完成");
                    } else {
                        tvRight.setText("编辑");
                    }
                    tvRight.setTag(!flag);
                    mAdapter.notifyDataSetChanged();
                    break;
                case R.id.local_member_del:
                    int position = (int) v.getTag();
                    members.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    mAdapter.notifyItemRangeRemoved(position, members.size() - position);
                    break;
                case R.id.local_member_head:// 添加成员
                    if (TextUtils.equals("add", v.getTag() + "")) {
                        Toast.makeText(LocalMemberActivity.this, "添加成员", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

}
