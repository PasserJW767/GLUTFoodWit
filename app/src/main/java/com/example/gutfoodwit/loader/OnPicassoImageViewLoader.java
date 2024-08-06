package com.example.gutfoodwit.loader;

import android.content.Context;
import android.widget.ImageView;

import com.example.gutfoodwit.R;
import com.github.why168.listener.OnLoadImageViewListener;
import com.github.why168.utils.L;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class OnPicassoImageViewLoader implements OnLoadImageViewListener {

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        return imageView;
    }

    @Override
    public void onLoadImageView(ImageView imageView, Object object) {
        Picasso
                .with(imageView.getContext())
                .load((String) object)
                .error(R.drawable.meishi1)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        L.e("Picasso ---> onSuccess");
                    }

                    @Override
                    public void onError() {
                        L.e("Picasso ---> onError");
                    }
                });
    }
}
