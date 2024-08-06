package com.example.gutfoodwit.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.base.BaseFragment;
import com.github.why168.LoopViewPagerLayout;
import com.github.why168.listener.OnBannerItemClickListener;
import com.github.why168.loader.OnDefaultImageViewLoader;
import com.github.why168.modle.BannerInfo;
import com.github.why168.modle.IndicatorLocation;
import com.github.why168.modle.LoopStyle;
import com.github.why168.utils.L;

import java.util.ArrayList;

public class EmptyLoopViewPagerFragment extends BaseFragment implements OnBannerItemClickListener {
    private LoopViewPagerLayout mLoopViewPagerLayout;

    public static EmptyLoopViewPagerFragment getInstance() {
        return new EmptyLoopViewPagerFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_loop_viewpager;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mLoopViewPagerLayout = (LoopViewPagerLayout) view.findViewById(R.id.mLoopViewPagerLayout);
    }

    @Override
    protected void initData() {

        mLoopViewPagerLayout.setLoop_ms(2000);//轮播的速度(毫秒)
        mLoopViewPagerLayout.setLoop_duration(1000);//滑动的速率(毫秒)
        mLoopViewPagerLayout.setLoop_style(LoopStyle.Empty);//轮播的样式-默认empty
        mLoopViewPagerLayout.setIndicatorLocation(IndicatorLocation.Center);//指示器位置-中Center
        mLoopViewPagerLayout.setNormalBackground(R.drawable.normal_background);//默认指示器颜色
        mLoopViewPagerLayout.setSelectedBackground(R.drawable.selected_background);//选中指示器颜色
        L.e("LoopViewPager Empty 参数设置完毕");


        mLoopViewPagerLayout.initializeData(mActivity);

        ArrayList<BannerInfo> bannerInfos = new ArrayList<>();
        bannerInfos.add(new BannerInfo<Integer>(R.drawable.meishi1, "第一张图片"));
        bannerInfos.add(new BannerInfo<Integer>(R.drawable.meishi2, "第二张图片"));
        bannerInfos.add(new BannerInfo<Integer>(R.drawable.meishi3, "第三张图片"));
        bannerInfos.add(new BannerInfo<Integer>(R.drawable.meishi4, "第四张图片"));
        bannerInfos.add(new BannerInfo<Integer>(R.drawable.meishi5, "第五张图片"));


        mLoopViewPagerLayout.setOnLoadImageViewListener(new OnDefaultImageViewLoader() {
            @Override
            public void onLoadImageView(ImageView view, Object object) {
                Glide
                        .with(view.getContext())
                        .load(object)
                        .centerCrop()
                        .error(R.mipmap.ic_launcher)
                        .crossFade()
                        .into(view);
            }
        });
        mLoopViewPagerLayout.setOnBannerItemClickListener(this);


        mLoopViewPagerLayout.setLoopData(bannerInfos);
    }

    @Override
    public void onBannerClick(int index, ArrayList<BannerInfo> banner) {
        Toast.makeText(mActivity, "index = " + index + " title = " + banner.get(index).title, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {

        mLoopViewPagerLayout.startLoop();
        super.onStart();
    }

    @Override
    public void onStop() {
        
        mLoopViewPagerLayout.stopLoop();
        super.onStop();
    }


}
