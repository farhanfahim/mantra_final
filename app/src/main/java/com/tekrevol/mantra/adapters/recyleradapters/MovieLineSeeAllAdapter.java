package com.tekrevol.mantra.adapters.recyleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.pagingadapter.PagingAdapter;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.libraries.imageloader.ImageLoaderHelper;
import com.tekrevol.mantra.managers.DateManager;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.widget.AnyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class MovieLineSeeAllAdapter extends PagingAdapter {

    private final OnItemClickListener onItemClick;
    private Context activity;
    private List<MediaModel> arrData;

    public MovieLineSeeAllAdapter(Context activity, List<MediaModel> arrData, OnItemClickListener onItemClickListener) {
        this.arrData = arrData;
        this.activity = activity;
        this.onItemClick = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.customlist_listsearch, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        super.onBindViewHolder(holder, i);
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;

            // Do your normal binding
            MediaModel model = arrData.get(i);
            viewHolder.title.setText(model.getName());
            String date = DateManager.ConvertSecondToHHMMString(model.getMediaLength());
            viewHolder.time.setText(date);

            ImageLoaderHelper.loadImageWithAnimations(((ViewHolder) holder).imgmedia, model.getUser().getUserDetails().getImageUrl(), false);
/*
            if (model.getImage() != null) {
                ImageLoaderHelper.loadImageWithouAnimation(((ViewHolder) holder).imgmedia, model.getImageUrl(), false);
            } else if (model.getCategory() != null) {
                ImageLoaderHelper.loadImageWithouAnimation(((ViewHolder) holder).imgmedia, model.getCategory().getImageUrl(), false);

            }*/
            if (model.isChoice1()) {
                //  AnimationHelper.getInstance(activity).showCategoryOptionBar(holder.layout2);
                viewHolder.layout2.setVisibility(View.VISIBLE);
            } else {
                //  AnimationHelper.getInstance(activity).showCategoryOptionBar(holder.layout2);
                viewHolder.layout2.setVisibility(View.GONE);
            }

            if (model.isMedia1()) {
                viewHolder.playMedia.setImageResource(R.drawable.pause);
            } else {
                viewHolder.playMedia.setImageResource(R.drawable.icon_play_light);
            }

            if (model.getIsFavourite() == 1) {
                viewHolder.fav.setVisibility(View.VISIBLE);
                viewHolder.unfav.setVisibility(View.GONE);
            } else {
                viewHolder.fav.setVisibility(View.GONE);
                viewHolder.unfav.setVisibility(View.VISIBLE);
            }
            setListener(viewHolder, model);

        }

    }

    private void setListener(final ViewHolder holder, final MediaModel model) {
        holder.layout1.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, ""));
        holder.imgbtnClose.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, ""));
        holder.detail.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, ""));
        holder.playMedia.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, ""));
        holder.fav.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, ""));
        holder.unfav.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, ""));
        holder.reminder.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, ""));

    }


    @Override
    public int getPagingLayout() {
        return 0;
    }

    @Override
    public int getPagingItemCount() {
        return 0;
    }

    @Override
    public int getItemCount() {
        return arrData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout1)
        RelativeLayout layout1;
        @BindView(R.id.layout2)
        RelativeLayout layout2;
        @BindView(R.id.imgbtn_setting)
        ImageButton imgbtnSetting;
        @BindView(R.id.imgbtn_close)
        ImageButton imgbtnClose;
        @BindView(R.id.detail)
        ImageButton detail;
        @BindView(R.id.title)
        AnyTextView title;
        @BindView(R.id.time)
        AnyTextView time;
        @BindView(R.id.imgmedia)
        ImageView imgmedia;
        @BindView(R.id.playMedia)
        ImageButton playMedia;
        @BindView(R.id.reminder)
        ImageButton reminder;
        @BindView(R.id.fav)
        ImageButton fav;
        @BindView(R.id.unfav)
        ImageButton unfav;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
