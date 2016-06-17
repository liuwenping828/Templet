package com.wenping.templet.common.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 基类Presenter
 * @param <V>  V: View接口继承 {@PresenterView}
 *           功能：提供数据加载，界面的跳转的显示在界面上的功能接口
 */
public class BasePresenter<V extends PresenterView> {
    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private V view;

    // @CallSuper 表示子类重写父类方法，在调用的时候希望也执行父类的方法。
    @CallSuper
    public void onViewAttached(@NonNull final V view) {
        if (this.view != null) {
            throw new IllegalStateException("View " + this.view + " is already attached. Cannot attach " + view);
        }
        this.view = view;
    }

    @CallSuper
    public void onViewDetached() {
        if (view == null) {
            throw new IllegalStateException("View is already detached");
        }
        view = null;
        compositeSubscription.clear();
    }

    @CallSuper
    protected final void addToAutoUnsubscribe(@NonNull final Subscription subscription) {
        compositeSubscription.add(subscription);
    }
}
