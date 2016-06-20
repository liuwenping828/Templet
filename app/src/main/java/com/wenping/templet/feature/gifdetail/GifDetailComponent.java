package com.wenping.templet.feature.gifdetail;

import android.support.annotation.NonNull;

import com.wenping.templet.common.base.BaseComponent;


public interface GifDetailComponent extends BaseComponent {
    @NonNull
    GifDetailPresenter getPresenter();
}
