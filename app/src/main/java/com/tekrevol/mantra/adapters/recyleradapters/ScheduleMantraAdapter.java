package com.tekrevol.mantra.adapters.recyleradapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import com.tekrevol.mantra.broadcast.AlarmReceiver;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.callbacks.OnSubItemClickListener;
import com.tekrevol.mantra.managers.DateManager;
import com.tekrevol.mantra.managers.ObjectBoxManager;
import com.tekrevol.mantra.models.database.AlarmModel;
import com.tekrevol.mantra.models.database.GeneralDBModel;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.widget.AnyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tekrevol.mantra.BaseApplication.getContext;

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

    public void updateMediaList(List<MediaModel> newlist) {
        arrData.clear();
        arrData.addAll(newlist);
        this.notifyDataSetChanged();
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

        holder.imgbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteParent(model,i);
            }
        });
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
    private void deleteParent(MediaModel mediaModel, int position) {
        for (AlarmModel alarm : mediaModel.getAlarms()) {
            dismissAlarm(alarm.getAlarmId(), getContext());
        }
        ArrayList<GeneralDBModel> arrayListTest = (ArrayList<GeneralDBModel>) ObjectBoxManager.INSTANCE.getAllScheduledMantraMediaModelsTest();

        long id = arrayListTest.get(arrayListTest.size() - 1 - position).id;
        //long id = arrayListTest.get(position).id;
//        ObjectBoxManager.INSTANCE.removeGeneralDBModel(mediaModel.getId());
        ObjectBoxManager.INSTANCE.removeGeneralDBModel(id);
        arrData.remove(position);
        notifyDataSetChanged();


    }

    private void dismissAlarm(long alarmId, Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent newIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) alarmId, newIntent, 0);
        alarmMgr.cancel(alarmIntent);
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
