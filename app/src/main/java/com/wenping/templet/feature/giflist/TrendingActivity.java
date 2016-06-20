package com.wenping.templet.feature.giflist;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxrelay.PublishRelay;
import com.wenping.templet.R;
import com.wenping.templet.common.base.BaseActivity;
import com.wenping.templet.common.base.BasePresenter;
import com.wenping.templet.feature.adapter.TrendingAdapter;
import com.wenping.templet.feature.api.Gif;
import com.wenping.templet.feature.gifdetail.GifDetailActivity;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

public class TrendingActivity extends BaseActivity<TrendingPresenter.View, TrendingComponent>
        implements TrendingPresenter.View, NavigationView.OnNavigationItemSelectedListener {
    private final PublishRelay<Void> refreshRelay = PublishRelay.create();
    private final PublishRelay<Gif> gifClickedRelay = PublishRelay.create();

    @BindView(R.id.trending_root_viewgroup)
    ViewGroup rootViewGroup;
    @BindView(R.id.trending_gifs_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.trending_swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.trending_info_textview)
    TextView infoTextView;
    @BindView(R.id.trending_loading_progressbar)
    ProgressBar loadingProgressBar;
    @ColorInt
    @BindColor(R.color.colorAccent)
    int accent;
    @BindInt(R.integer.trending_gifs_columns)
    int numberOfColumns;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private TrendingPresenter presenter;
    private TrendingAdapter trendingAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trending_gifs;
    }


    // 1. // TODO: 2016/6/16 网络请求
    @Override
    @NonNull
    protected TrendingComponent createComponent() {
        return TrendingModule::trendingGifsPresenter;
    }

    // 2. // // TODO: 2016/6/16 注入
    @Override
    protected void inject(@NonNull final TrendingComponent component) {
        presenter = component.getPresenter();
    }

    @NonNull
    @Override
    protected BasePresenter<TrendingPresenter.View> getPresenter() {
        return presenter;
    }

    @NonNull
    @Override
    protected TrendingPresenter.View getPresenterView() {
        return this;
    }

    @Override
    protected void onViewCreated(@Nullable final Bundle savedInstanceState) {
        //----------------
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        //----------------

        trendingAdapter = new TrendingAdapter(gifClickedRelay);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(trendingAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        swipeRefreshLayout.setColorSchemeColors(accent);
        swipeRefreshLayout.setOnRefreshListener(() -> refreshRelay.call(null));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //-------------TrendingPresenter.View 接口实现--------------------
    @NonNull
    @Override
    public Observable<Gif> onGifClicked() {
        return gifClickedRelay;
    }

    // 1
    @NonNull
    @Override
    public Observable<Void> onRefreshAction() {
        return refreshRelay;
    }

    @Override
    public void showEmpty() {
        infoTextView.setText(R.string.trending_empty_state);
        infoTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        infoTextView.setText(R.string.trending_refresh_gifs_failed);
        infoTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showIncrementalError() {
        Snackbar.make(rootViewGroup, R.string.trending_incremental_error, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showIncrementalLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideIncrementalLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void goToGif(@NonNull final Gif gif) {
        GifDetailActivity.start(this, gif);
    }

    @Override
    public void setTrendingGifs(@NonNull final List<Gif> gifs) {
        // // TODO: Adapter 设置数据
        trendingAdapter.setGifs(gifs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
