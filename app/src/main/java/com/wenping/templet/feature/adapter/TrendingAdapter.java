package com.wenping.templet.feature.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxrelay.PublishRelay;
import com.wenping.templet.R;
import com.wenping.templet.feature.api.Gif;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/20.
 */
public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.TrendingGifViewHolder> {

    private final List<Gif> gifs = new ArrayList<>();
    private final PublishRelay<Gif> gifClickedRelay;

    public TrendingAdapter(@NonNull final PublishRelay<Gif> gifClickedRelay) {
        this.gifClickedRelay = gifClickedRelay;
    }

    @Override
    public TrendingGifViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new TrendingGifViewHolder(inflater.inflate(R.layout.item_trending_gif,parent,false));
    }

    @Override
    public void onBindViewHolder(TrendingGifViewHolder holder, int position) {
        Gif gif = gifs.get(position);
        Glide.with(holder.imageView.getContext())
                .load(gif.downsizedImage())
                .centerCrop()
                .into(holder.imageView);
    }

    public void setGifs(List<Gif> list){
        gifs.clear();
        gifs.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return gifs.size();
    }

    class TrendingGifViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_trending_gif_imageview)
        ImageView imageView;

        TrendingGifViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item_trending_gif_imageview)
        void onGifClicked() {
            gifClickedRelay.call(gifs.get(getAdapterPosition()));
        }
    }
}
