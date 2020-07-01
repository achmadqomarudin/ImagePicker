package com.achmadqomarudin.imagepicker.imageloader;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.achmadqomarudin.imagepicker.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)
                .load(Uri.fromFile(new File(path)))
                .error(R.drawable.ic_default_image)
                .placeholder(R.drawable.ic_default_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)
                .load(Uri.fromFile(new File(path)))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}
