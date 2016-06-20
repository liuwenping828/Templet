package com.wenping.templet.feature.gifdetail;

import android.support.annotation.NonNull;

import com.wenping.templet.feature.api.Gif;

public class GifDetailModule {
    static GifDetailPresenter gifPresenter(@NonNull final Gif gif) {
        return new GifDetailPresenter(gif);
    }
}
