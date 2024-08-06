package com.example.gutfoodwit.loader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.example.gutfoodwit.R;
import com.github.why168.listener.OnLoadImageViewListener;

public class OnGlideImageViewLoader implements OnLoadImageViewListener {

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void onLoadImageView(ImageView view, Object parameter) {
        Glide
                .with(view.getContext())
                .load(parameter)
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .crossFade()
                .into(view);
    }
}
