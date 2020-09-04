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
public class MovieLineAdapter extends RecyclerView.Adapter<MovieLineAdapter.ViewHolder> {

    private final OnItemClickListener onItemClick;


    private Context activity;
    private List<MediaModel> arrData;

    public MovieLineAdapter(Context activity, List<MediaModel> arrData, OnItemClickListener onItemClickListener) {
        this.arrData = arrData;
        this.activity = activity;
        this.onItemClick = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.customlist_listsearch, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        MediaModel model = arrData.get(i);
        holder.title.setText(model.getName());
        String date = DateManager.ConvertSecondToHHMMString(model.getMediaLength());
        holder.time.setText(date);
        ImageLoaderHelper.loadImageWithAnimations(((ViewHolder) holder).imgmedia, model.getUser().getUserDetails().getImageUrl(), false);

       /* if (model.getImage() != null) {
            ImageLoaderHelper.loadImageWithouAnimation(((ViewHolder) holder).imgmedia, model.getImageUrl(), false);
        } else if (model.getCategory() != null) {
            ImageLoaderHelper.loadImageWithouAnimation(((ViewHolder) holder).imgmedia, model.getCategory().getImageUrl(), false);

        }*/
        if (model.isChoice1()) {
            //  AnimationHelper.getInstance(activity).showCategoryOptionBar(holder.layout2);
            holder.layout2.setVisibility(View.VISIBLE);
        } else {
            //  AnimationHelper.getInstance(activity).showCategoryOptionBar(holder.layout2);
            holder.layout2.setVisibility(View.GONE);
        }

        if (model.isMedia1()) {
            holder.playMedia.setImageResource(R.drawable.pause);
            //   holder.playMedia.setBackgroundResource(R.drawable.playing);
        } else {
            holder.playMedia.setImageResource(R.drawable.icon_play_light);
            //   holder.playMedia.setBackgroundResource(R.drawable.icon_play_light);
        }
        if (model.getIsFavourite() == 1) {
            holder.fav.setVisibility(View.VISIBLE);
            holder.unfav.setVisibility(View.GONE);
        } else {
            holder.fav.setVisibility(View.GONE);
            holder.unfav.setVisibility(View.VISIBLE);
        }

        setListener(holder, model);
    }

    private void setListener(final ViewHolder holder, final MediaModel model) {
        holder.layout1.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, MovieLineAdapter.class.getSimpleName()));
        holder.imgbtnClose.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, MovieLineAdapter.class.getSimpleName()));
        holder.detail.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, HomeDailyMantraAdapter.class.getSimpleName()));
        holder.playMedia.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, MovieLineAdapter.class.getSimpleName()));
        holder.fav.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, MovieLineAdapter.class.getSimpleName()));
        holder.unfav.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, MovieLineAdapter.class.getSimpleName()));
        holder.reminder.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, MovieLineAdapter.class.getSimpleName()));
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
        @BindView(R.id.fav)
        ImageButton fav;
        @BindView(R.id.unfav)
        ImageButton unfav;
        @BindView(R.id.reminder)
        ImageButton reminder;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
