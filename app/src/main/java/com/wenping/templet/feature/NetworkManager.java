package com.wenping.templet.feature;

import android.support.annotation.NonNull;
import android.util.Log;

import com.jakewharton.rxrelay.PublishRelay;
import com.wenping.templet.common.Funcs;
import com.wenping.templet.common.Results;
import com.wenping.templet.feature.api.Gif;
import com.wenping.templet.feature.api.GiphyApi;
import com.wenping.templet.feature.api.TrendingGifsResponse;

import java.util.List;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 *
 */
public class NetworkManager {
    private final PublishRelay<Void> refreshRelay = PublishRelay.create();

    private final PublishRelay<LoadingState> loadingStateRelay = PublishRelay.create();
    private final PublishRelay<List<Gif>> trendingGifsRelay = PublishRelay.create();

    private final CompositeSubscription subscription = new CompositeSubscription();

    private final GiphyApi giphyApi;
    private final Scheduler ioScheduler;

    /**
     * @param giphyApi
     * @param ioScheduler 子线程
     */
    public NetworkManager(@NonNull final GiphyApi giphyApi, @NonNull final Scheduler ioScheduler) {
        this.giphyApi = giphyApi;
        this.ioScheduler = ioScheduler;
    }

    @NonNull
    public Observable<LoadingState> onLoadingStateChanged() {
        return loadingStateRelay;
    }

    @NonNull
    public PublishRelay<List<Gif>> onDataChanged() {
        return trendingGifsRelay;
    }

    public void setup() {
//        final Observable<Result<TrendingGifsResponse>> result = refreshRelay
//                .doOnNext(ignored -> loadingStateRelay.call(LoadingState.LOADING))
//                .flatMap(ignored -> giphyApi.latestTrending().subscribeOn(ioScheduler))
//                .share();

        Observable<Result<TrendingGifsResponse>> result = refreshRelay.doOnNext(new Action1<Void>() {
            @Override
            public void call(Void ignored) {
                //加载
                loadingStateRelay.call(LoadingState.LOADING);
            }
        }).flatMap(new Func1<Void, Observable<Result<TrendingGifsResponse>>>() {

            @Override
            public Observable<Result<TrendingGifsResponse>> call(Void ignored) {
                // TODO: 子线程中执行获取列表数据
                return giphyApi.latestList().subscribeOn(ioScheduler);
            }
        }).share();

       /* subscription.add(result.filter(Results.isSuccessful())
                .map(listResult -> listResult.response().body().gifs())
                .doOnNext(trendingGifsRelay::call)
                .subscribe(ignored -> loadingStateRelay.call(LoadingState.IDLE),
                        throwable -> Log.e("TrendingNetworkManager",
                                "Failed to parse and show latest trending gifs",
                                throwable)));*/

        subscription.add(result.filter(Results.isSuccessful())
                .map(new Func1<Result<TrendingGifsResponse>, List<Gif>>() {
                    @Override
                    public List<Gif> call(Result<TrendingGifsResponse> listResult) {
                        List<Gif> gifs = listResult.response().body().gifs();
                        return gifs;
                    }
                }).doOnNext(new Action1<List<Gif>>() {
                    @Override
                    public void call(List<Gif> gifs) {
                        trendingGifsRelay.call(gifs);
                    }
                    //  subscribe(final Action1<? super T> onNext, final Action1<Throwable> onError)
                }).subscribe(new Action1<List<Gif>>() {
                    @Override
                    public void call(List<Gif> gifs) {
                        loadingStateRelay.call(LoadingState.IDLE);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("TrendingNetworkManager",
                                "Failed to parse and show latest trending gifs",
                                throwable);
                    }
                }));

        /*subscription.add(result.filter(Funcs.not(Results.isSuccessful()))
                .subscribe(ignored -> loadingStateRelay.call(LoadingState.ERROR),
                        throwable -> Log.e("TrendingNetworkManager",
                                "Failed to retrieve latest trending gifs",
                                throwable)));*/

        subscription.add(result.filter(Funcs.not(Results.isSuccessful()))
                .subscribe(new Action1<Result<TrendingGifsResponse>>() {
                    @Override
                    public void call(Result<TrendingGifsResponse> trendingGifsResponseResult) {
                        loadingStateRelay.call(LoadingState.ERROR);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("TrendingNetworkManager",
                                "Failed to retrieve latest trending gifs",
                                throwable);
                    }
                }));

    }

    public void refresh() {
        refreshRelay.call(null);
    }

    public void teardown() {
        subscription.clear();
    }
}
