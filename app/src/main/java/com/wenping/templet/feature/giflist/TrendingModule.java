package com.wenping.templet.feature.giflist;

import com.squareup.moshi.Moshi;
import com.wenping.templet.feature.NetworkManager;
import com.wenping.templet.feature.api.GiphyApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TrendingModule {
    private static TrendingPresenter presenter;

    static TrendingPresenter trendingGifsPresenter() {
        if (presenter == null) {
            presenter = new TrendingPresenter(trendingGifManager(), AndroidSchedulers.mainThread());
        }
        return presenter;
    }

    private static NetworkManager trendingGifManager() {
        return new NetworkManager(giphyApi(), Schedulers.io());
    }

    private static GiphyApi giphyApi() {
        final Moshi moshi = new Moshi.Builder().add(new AutoValueMoshiAdapterFactory()).build();

        final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.giphy.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();

        return retrofit.create(GiphyApi.class);
    }
}
