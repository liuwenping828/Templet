package com.wenping.templet.feature.giflist;

import android.support.annotation.NonNull;

import com.wenping.templet.common.base.BaseComponent;


interface TrendingComponent extends BaseComponent {
    @NonNull
    TrendingPresenter getPresenter();
}
