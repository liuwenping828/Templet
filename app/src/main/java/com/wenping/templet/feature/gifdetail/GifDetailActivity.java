package com.wenping.templet.feature.gifdetail;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wenping.templet.R;
import com.wenping.templet.common.base.BaseActivity;
import com.wenping.templet.common.base.BasePresenter;
import com.wenping.templet.feature.api.Gif;

import butterknife.BindView;

import static com.wenping.templet.feature.gifdetail.GifDetailModule.gifPresenter;


public class GifDetailActivity extends BaseActivity<GifDetailPresenter.View, GifDetailComponent>
        implements GifDetailPresenter.View {
    private static final String EXTRA_TRENDING_GIF = "trending_gif";

    @BindView(R.id.gif_imageview) ImageView gifImageView;

    private GifDetailPresenter presenter;

    public static void start(@NonNull final Context context, @NonNull final Gif gif) {
        final Intent intent = new Intent(context, GifDetailActivity.class);
        intent.putExtra(EXTRA_TRENDING_GIF, gif);
        context.startActivity(intent);
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_gif;
    }

    @Override protected GifDetailComponent createComponent() {
        return () -> gifPresenter(getIntent().getParcelableExtra(EXTRA_TRENDING_GIF));
    }

    @Override protected void inject(@NonNull final GifDetailComponent component) {
        presenter = component.getPresenter();
    }

    @NonNull
    @Override protected BasePresenter<GifDetailPresenter.View> getPresenter() {
        return presenter;
    }

    @NonNull
    @Override protected GifDetailPresenter.View getPresenterView() {
        return this;
    }

    @Override public void showGif(@NonNull final Gif gif) {
        Glide.with(this).load(gif.downsizedGif()).into(gifImageView);
    }
}
