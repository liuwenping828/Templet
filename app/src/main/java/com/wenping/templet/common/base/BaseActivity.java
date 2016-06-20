package com.wenping.templet.common.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 * @param <V>
 * @param <C>
 */
public abstract class BaseActivity<V extends PresenterView, C extends BaseComponent>
        extends AppCompatActivity {
    private Unbinder unbinder;

    @CallSuper
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inject(createComponent());
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        onViewCreated(savedInstanceState);
        // TrendingPersenter--->onViewAttached()
        getPresenter().onViewAttached(getPresenterView());
    }

    protected void onViewCreated(@Nullable final Bundle savedInstanceState) {

    }


    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract C createComponent();

    protected abstract void inject(@NonNull final C component);

    @NonNull
    protected abstract BasePresenter<V> getPresenter();

    @NonNull
    protected abstract V getPresenterView();

    @CallSuper
    @Override
    protected void onDestroy() {
        getPresenter().onViewDetached();
        unbinder.unbind();
        super.onDestroy();
    }
}