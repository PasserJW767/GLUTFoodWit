package com.github.why168.listener;

import com.github.why168.modle.BannerInfo;

import java.util.ArrayList;

/**
 * Banner Click

 */
public interface OnBannerItemClickListener {
    /**
     * banner click
     *
     * @param index  subscript
     * @param banner bean
     */
    void onBannerClick(int index, ArrayList<BannerInfo> banner);
}
