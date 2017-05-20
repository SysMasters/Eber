package com.eber.bfs.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eber.bfs.EBERApp;
import com.eber.bfs.R;
import com.eber.bfs.base.BaseActivity;
import com.eber.bfs.bean.Member;
import com.eber.bfs.http.HttpUrls;
import com.eber.bfs.http.StringCallback2;
import com.eber.bfs.ui.register.FillInformationActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
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

    private ArrayList<Member> members;
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
            protected void convert(final ViewHolder holder, final Member member, int position) {
                ImageView headImage = holder.getView(R.id.local_member_head);
                TextView tvName = holder.getView(R.id.local_member_name);
                ImageView ivDel = holder.getView(R.id.local_member_del);
                ImageView ivSelected = holder.getView(R.id.local_member_selected);
                final boolean flag = (boolean) tvRight.getTag();
                // 当前用户、父用户、添加成员，不能删除
                if (position == 1 ||TextUtils.equals(member.parentId,member.id)||TextUtils.equals(EBERApp.nowUser.id+"",member.id) || position == members.size() - 1) {
                    ivDel.setVisibility(View.GONE);
                } else {
                    ivDel.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
                }
                // 默认第二项为当前选中用户(前边需要集合排序，第一为主账户，第二位当前用户)
                ivSelected.setVisibility(position == 1 && !TextUtils.isEmpty(member.id) ? View.VISIBLE : View.INVISIBLE);
                ivDel.setTag(position);
                ivDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteUser(member.id, holder.getAdapterPosition());
                    }
                });
                if (TextUtils.equals("-1", member.sex)) {
                    headImage.setImageResource(R.mipmap.ic_local_member_add);
                    headImage.setTag("add");
                    headImage.setOnClickListener(clickListener);
                } else {
                    if (TextUtils.equals("1", member.sex)) {// 1:男
                        headImage.setImageResource(R.mipmap.ic_local_member_man);
                    } else if (TextUtils.equals("2", member.sex)) {
                        headImage.setImageResource(R.mipmap.ic_local_member_woman);
                    }
                    holder.setOnClickListener(R.id.local_member_head, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (flag){
                                return;
                            }
                            setNowuser(member);
                            setResult();
                            finish();
                        }
                    });
                }
                tvName.setText(member.userName);

            }
        });
    }

    /**
     * 设置当前用户
     */
    private void setNowuser(Member member) {
        EBERApp.nowUser.id = Integer.parseInt(member.id);
        EBERApp.nowUser.sex = Integer.parseInt(member.sex);
        try {
            EBERApp.nowUser.height = (int) Double.parseDouble(member.height);
        } catch (Exception e) {
            EBERApp.nowUser.height = 0;
        }
        EBERApp.nowUser.birthday = member.birthday;
    }

    private void initData() {
        members = (ArrayList<Member>) JSONArray.parseArray(getIntent().getStringExtra("members"), Member.class);
        if (members == null) {
            return;
        }
        setNowuserForSecond(members);
        members.add(new Member("", "", "-1", "添加成员"));

    }

    @Override
    public void setListener() {

    }

    /**
     * 设置当前用户为第二个
     */
    private void setNowuserForSecond(List<Member> members) {
        for (int i = 0; i < members.size(); i++) {
            if (TextUtils.equals(members.get(i).id, EBERApp.nowUser.id + "") && members.size() >= 2) {
                Member m = members.get(i);
                members.remove(i);
                members.add(1, m);
                break;
            }
        }
    }

    private void deleteUser(String memberId, final int position) {
        param = new HashMap<>();
        param.put("memberId", memberId);
        netUtils.get(HttpUrls.DELETEMEMBER, true, param, new StringCallback2() {
            @Override
            public void onSuccess(String... result) {
                members.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_back:
                    setResult();
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
                        Intent intent = new Intent(mContext, FillInformationActivity.class);
                        intent.putExtra("isCreateChidUser", true);
                        startActivityForResult(intent, 111);
                    }
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            Member member = JSONObject.parseObject(data.getStringExtra("member"), Member.class);
            setNowuser(member);
            if (mAdapter.getDatas().size() >= 2) {
                mAdapter.getDatas().add(1, member);
            } else {
                mAdapter.getDatas().add(member);
            }
            mAdapter.notifyDataSetChanged();
        }
    }
    
    private void setResult(){
        Intent intent = new Intent();
        intent.putExtra("members",  members);
        setResult(12,intent);
    }

    @Override
    public void onBackPressed() {
        setResult();
        finish();
    }
}
