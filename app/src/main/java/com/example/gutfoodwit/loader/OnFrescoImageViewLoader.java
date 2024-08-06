package com.example.gutfoodwit.loader;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.why168.listener.OnLoadImageViewListener;

public class OnFrescoImageViewLoader implements OnLoadImageViewListener {

    @Override
    public SimpleDraweeView createImageView(Context context) {
        return new SimpleDraweeView(context);
    }

    @Override
    public void onLoadImageView(ImageView imageView, Object parameter) {
        Uri uri = Uri.parse((String) parameter);
        imageView.setImageURI(uri);
    }
}
