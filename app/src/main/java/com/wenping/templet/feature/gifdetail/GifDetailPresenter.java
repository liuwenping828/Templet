package com.wenping.templet.feature.gifdetail;

import android.support.annotation.NonNull;

import com.wenping.templet.common.base.BasePresenter;
import com.wenping.templet.common.base.PresenterView;
import com.wenping.templet.feature.api.Gif;


public class GifDetailPresenter extends BasePresenter<GifDetailPresenter.View> {
    private final Gif gif;

    GifDetailPresenter(@NonNull final Gif gif) {
        this.gif = gif;
    }

    @Override
    public void onViewAttached(@NonNull final View view) {
        super.onViewAttached(view);

        view.showGif(gif);
    }

    public interface View extends PresenterView {
        void showGif(@NonNull final Gif gif);
    }
}
