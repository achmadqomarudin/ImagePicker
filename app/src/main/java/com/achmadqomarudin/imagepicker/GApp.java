package com.achmadqomarudin.imagepicker;

import android.app.Application;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.xutils.image.ImageOptions;
import org.xutils.x;

public class GApp extends Application {

    public static DisplayImageOptions imageLoaderOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_default_image)    //Set the picture displayed during download
            .showImageForEmptyUri(R.drawable.ic_default_image)  //Set the picture displayed when Uri is empty or wrong
            .showImageOnFail(R.drawable.ic_default_image)       //Set the picture displayed when an error occurs during picture loading/decoding
            .cacheInMemory(true)                                //Set whether downloaded pictures are cached in memory
            .cacheOnDisk(true)                                  //Set whether downloaded pictures are cached in SD card
            .build();                                           //Build complete

    public static ImageOptions xUtilsOptions = new ImageOptions.Builder()
            .setIgnoreGif(false)                                //Whether to ignore pictures in GIF format
            .setImageScaleType(ImageView.ScaleType.FIT_CENTER)  //Zoom mode
            .setLoadingDrawableId(R.drawable.ic_default_image)  //Pictures shown in download
            .setFailureDrawableId(R.drawable.ic_default_image)  //Picture displayed when download failed
            .build();                                           //Get ImageOptions object

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(this);

        ImageLoader.getInstance().init(config); //Universal ImageLoader initialization
        x.Ext.init(this);                  //xUtils3 initialization
    }
}