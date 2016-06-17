package com.wenping.templet.feature.api;


import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import rx.Observable;

public interface GiphyApi {
    // 导入 compile 'com.squareup.retrofit2:retrofit:2.1.0'
    @GET("v1/gifs/trending?api_key=dc6zaTOxFJmzC&rating=g")
    Observable<Result<TrendingGifsResponse>> latestList();

}
