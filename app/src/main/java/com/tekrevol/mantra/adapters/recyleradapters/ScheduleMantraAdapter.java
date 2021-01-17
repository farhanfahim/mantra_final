package com.tekrevol.mantra.adapters.recyleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.callbacks.OnSubItemClickListener;
import com.tekrevol.mantra.managers.DateManager;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.widget.AnyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class ScheduleMantraAdapter extends RecyclerView.Adapter<ScheduleMantraAdapter.ViewHolder> {

    private final OnItemClickListener onItemClick;

    private final OnSubItemClickListener onSubItemClickListener;

    private Context activity;
    private List<MediaModel> arrData;

    public ScheduleMantraAdapter(Context activity, List<MediaModel> arrData, OnItemClickListener onItemClickListener,OnSubItemClickListener onSubItemClickListener) {
        this.arrData = arrData;
        this.activity = activity;
        this.onItemClick = onItemClickListener;
        this.onSubItemClickListener = onSubItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.schedule_itemlist, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        MediaModel model = arrData.get(i);
        holder.title.setText(model.getName());
        String date = DateManager.ConvertSecondToHHMMString(model.getMediaLength ());
        holder.time.setText(date);

        /*if (model.getIsFavourite() == 1) {
            holder.fav.setVisibility(View.VISIBLE);
            holder.unfav.setVisibility(View.GONE);
        } else {
            holder.fav.setVisibility(View.GONE);
            holder.unfav.setVisibility(View.VISIBLE);
        }*/
        if (model.isMedia1()) {
            holder.playMedia.setImageResource(R.drawable.schedulepause);
            //   holder.playMedia.setBackgroundResource(R.drawable.playing);
        } else {
            holder.playMedia.setImageResource(R.drawable.scheduleplay);

            //     holder.playMedia.setBackgroundResource(R.drawable.icon_play_light);
        }

        ScheduleDateAdapter scheduleDateAdapter = new ScheduleDateAdapter(activity,onSubItemClickListener);
        scheduleDateAdapter.setParentPosition(holder.getAdapterPosition());
        scheduleDateAdapter.setArrData(model.getAlarms());
        bindNestedRecyclerView(scheduleDateAdapter, holder);
        setListener(holder, model);
    }

    private void bindNestedRecyclerView(ScheduleDateAdapter scheduleDateAdapter, ViewHolder holder) {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        holder.daterecyclerview.setLayoutManager(mLayoutManager);

        ((DefaultItemAnimator) holder.daterecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
        holder.daterecyclerview.setAdapter(scheduleDateAdapter);
        scheduleDateAdapter.notifyDataSetChanged();
    }

    private void setListener(final ViewHolder holder, final MediaModel model) {
        holder.playMedia.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, ""));
        holder.imgbtnDelete.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, ""));
    }

    @Override
    public int getItemCount() {
        return arrData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgmedia)
        ImageView imgmedia;
        @BindView(R.id.title)
        AnyTextView title;
        @BindView(R.id.time)
        AnyTextView time;
        @BindView(R.id.playMedia)
        ImageButton playMedia;
        @BindView(R.id.imgbtn_delete)
        ImageButton imgbtnDelete;
        @BindView(R.id.daterecyclerview)
        RecyclerView daterecyclerview;
        @BindView(R.id.layout1)
        RelativeLayout layout1;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
