package com.wenping.templet.feature.giflist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.auto.value.AutoValue;
import com.wenping.templet.common.base.BasePresenter;
import com.wenping.templet.common.base.PresenterView;
import com.wenping.templet.feature.api.Gif;
import com.wenping.templet.feature.*;

import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func2;

class TrendingPresenter extends BasePresenter<TrendingPresenter.View> {
    private final NetworkManager trendingNetworkManager;
    private final Scheduler uiScheduler;

    TrendingPresenter(@NonNull final NetworkManager trendingNetworkManager,
                      @NonNull final Scheduler uiScheduler) {
        this.trendingNetworkManager = trendingNetworkManager;
        this.uiScheduler = uiScheduler;
    }

    @Override
    public void onViewAttached(@NonNull final View view) {

        super.onViewAttached(view);

        trendingNetworkManager.setup();

        //combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2,
        // Func2<? super T1,? super T2, ? extends R> combineFunction)
        Observable<LoadingState> loadingStateObservable = trendingNetworkManager.onLoadingStateChanged();
        Observable<List<Gif>> listObservable = trendingNetworkManager.onDataChanged().startWith(Observable.just(null));
        addToAutoUnsubscribe(Observable.combineLatest(loadingStateObservable, listObservable, new Func2<LoadingState, List<Gif>, LoadingStateWithData>() {
            @Override
            public LoadingStateWithData call(LoadingState loadingState, List<Gif> gifs) {
                return LoadingStateWithData.create(loadingState, gifs);
            }
        }).subscribeOn(uiScheduler).subscribe(new Action1<LoadingStateWithData>() {
            @Override
            public void call(LoadingStateWithData loadingStateWithData) {
                final LoadingState loadingState = loadingStateWithData.loadingState();
                final List<Gif> data = loadingStateWithData.trendingGifs();

                if (loadingState == LoadingState.LOADING) {
                    if (data == null) {
                        view.showLoading();
                    } else {
                        view.showIncrementalLoading();
                    }
                } else {
                    view.hideLoading();
                    view.hideIncrementalLoading();

                    if (loadingState == LoadingState.IDLE) {
                        if (data == null) {
                            view.showEmpty();
                        } else {
                            // // TODO: 数据
                            view.setTrendingGifs(data);
                        }
                    } else if (loadingState == LoadingState.ERROR) {
                        if (data == null) {
                            view.showError();
                        } else {
                            view.showIncrementalError();
                        }
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("TrendingPresenter", "Failed to update UI");
            }
        }));



        // TODO: combineLatest 是将参数一:bservable<LoadingState> 和参数二:Observable<List<Gif>> 传递给参数三进行组合
    /**
        addToAutoUnsubscribe(Observable.combineLatest(trendingNetworkManager.onLoadingStateChanged(),
                trendingNetworkManager.onDataChanged().startWith(Observable.just(null)),
                LoadingStateWithData::create)
                .observeOn(uiScheduler)  // 主线程
                .subscribe(loadingStateWithData -> {
                    final LoadingState loadingState = loadingStateWithData.loadingState();
                    final List<Gif> data = loadingStateWithData.trendingGifs();

                    if (loadingState == LoadingState.LOADING) {
                        if (data == null) {
                            view.showLoading();
                        } else {
                            view.showIncrementalLoading();
                        }
                    } else {
                        view.hideLoading();
                        view.hideIncrementalLoading();

                        if (loadingState == LoadingState.IDLE) {
                            if (data == null) {
                                view.showEmpty();
                            } else {
                                // // TODO: 数据
                                view.setTrendingGifs(data);
                            }
                        } else if (loadingState == LoadingState.ERROR) {
                            if (data == null) {
                                view.showError();
                            } else {
                                view.showIncrementalError();
                            }
                        }
                    }
                },throwable -> Log.e("TrendingPresenter", "Failed to update UI")));
                */
//        addToAutoUnsubscribe(view.onRefreshAction()
//                .startWith(Observable.just(null))
//                .subscribe(ignored -> trendingNetworkManager.refresh(),
//                        throwable -> Log.e("TrendingPresenter", "Failed to refresh")));

        addToAutoUnsubscribe(view.onRefreshAction().startWith(Observable.<Void>just(null)).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                trendingNetworkManager.refresh();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("TrendingPresenter", "Failed to refresh");
            }
        }));

//        addToAutoUnsubscribe(view.onGifClicked().subscribe(view::goToGif));

        addToAutoUnsubscribe(view.onGifClicked().subscribe(new Action1<Gif>() {
            @Override
            public void call(Gif gif) {
                // TODO: 详情界面
                view.goToGif(gif);
            }
        }));

    }

    @Override
    public void onViewDetached() {
        trendingNetworkManager.teardown();
        super.onViewDetached();
    }


    @AutoValue
    abstract static class LoadingStateWithData {
        static LoadingStateWithData create(@NonNull final LoadingState loadingState,
                                           @Nullable final List<Gif> gifs) {
            return new AutoValue_TrendingPresenter_LoadingStateWithData(loadingState, gifs);
        }

        abstract LoadingState loadingState();

        @Nullable
        abstract List<Gif> trendingGifs();
    }


    interface View extends PresenterView {
        /**
         * 列表刷新动作
         * @return
         */
        @NonNull
        Observable<Void> onRefreshAction();

        /**
         * 列表 item 点击
         * @return
         */
        @NonNull
        Observable<Gif> onGifClicked();

        /**
         * 设置列表数据
         * @param gifs
         */
        void setTrendingGifs(@NonNull final List<Gif> gifs);

        /**
         * 没有数据界面显示
         */
        void showEmpty();

        /**
         * 数据加载中出现错误界面显示
         */
        void showError();

        /**
         * 显示正在加载中
         */
        void showLoading();

        /**
         * 隐藏正在加载中
         */
        void hideLoading();

        /**
         * 刷新加载时出现错误
         */
        void showIncrementalError();

        /**
         * 显示 下拉刷新 加载
         */
        void showIncrementalLoading();

        /**
         * 隐藏 下拉刷新 加载
         */
        void hideIncrementalLoading();

        /**
         * 跳转到详情
         * @param gif
         */
        void goToGif(@NonNull final Gif gif);
    }

}
