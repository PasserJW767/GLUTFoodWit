package com.github.why168.modle;

/**
 * BannerInfo
 *
 */
public class BannerInfo<T> {
    public T data;
    public String title;

    public BannerInfo(T data, String title) {
        this.data = data;
        this.title = title;
    }
}
