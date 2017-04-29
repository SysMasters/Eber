package com.eber.ui.find;

import android.widget.ListView;

import com.eber.R;
import com.eber.adapters.FindAdapter;
import com.eber.base.BaseFragment;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

public class FindFragment extends BaseFragment {

    @ViewInject(R.id.find_lv)
    private ListView lv;

    private CommonAdapter<String> mAdapter;
    @Override
    public int bindLayout() {
        return R.layout.fragment_find;
    }

    @Override
    public void onBusiness() {
//        mAdapter = new FindAdapter(mActivity, R.layout.layout_find_article_item, datas);
//        lv.setAdapter(mAdapter);
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initialize();
//    }
//
//    @SuppressLint("NewApi")
//    private void initialize() {
//
////        cycleViewPager = new CycleViewPager();
////        FragmentManager fragmentManager  =mActivity.getSupportFragmentManager();
////        FragmentTransaction ft = fragmentManager.beginTransaction();
////        ft.add(R.id.cycle_content,cycleViewPager);
//
////        cycleViewPager = (CycleViewPager) mActivity.getSupportFragmentManager()
////                .findFragmentById(R.id.cycle_content);
//
//        for (int i = 0; i < imageUrls.length; i++) {
//            ADInfo info = new ADInfo();
//            info.setUrl(imageUrls[i]);
//            info.setContent("图片-->" + i);
//            infos.add(info);
//        }
//
//        // 将最后一个ImageView添加进来
//        views.add(ViewFactory.getImageView(mActivity, infos.get(infos.size() - 1).getUrl()));
//        for (int i = 0; i < infos.size(); i++) {
//            views.add(ViewFactory.getImageView(mActivity, infos.get(i).getUrl()));
//        }
//        // 将第一个ImageView添加进来
//        views.add(ViewFactory.getImageView(mActivity, infos.get(0).getUrl()));
//
//        // 设置循环，在调用setData方法前调用
//        cycleViewPager.setCycle(true);
//
//        // 在加载数据前设置是否循环
//        cycleViewPager.setData(views, infos, mAdCycleViewListener);
//        //设置轮播
//        cycleViewPager.setWheel(true);
//
//        // 设置轮播时间，默认5000ms
//        cycleViewPager.setTime(2000);
//        //设置圆点指示图标组居中显示，默认靠右
////        cycleViewPager.setIndicatorCenter();
////        ft.commit();
//    }
//
//    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {
//
//        @Override
//        public void onImageClick(ADInfo info, int position, View imageView) {
//            if (cycleViewPager.isCycle()) {
//                position = position - 1;
//                Toast.makeText(mActivity,
//                        "position-->" + info.getContent(), Toast.LENGTH_SHORT)
//                        .show();
//            }
//
//        }
//
//    };
//
//    //	/**
//    //	 * 配置ImageLoder
//    //	 */
//    //	private void configImageLoader() {
//    //		// 初始化ImageLoader
//    //		@SuppressWarnings("deprecation")
//    //		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.icon_stub) // 设置图片下载期间显示的图片
//    //				.showImageForEmptyUri(R.mipmap.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
//    //				.showImageOnFail(R.mipmap.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
//    //				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
//    //				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
//    //				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
//    //				.build(); // 创建配置过得DisplayImageOption对象
//    //
//    //		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
//    //				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
//    //				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
//    //		ImageLoader.getInstance().init(config);
//    //	}
}
