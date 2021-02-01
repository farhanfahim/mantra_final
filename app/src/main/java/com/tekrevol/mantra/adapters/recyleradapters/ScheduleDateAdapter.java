package com.tekrevol.mantra.adapters.recyleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.callbacks.OnSubItemClickListener;
import com.tekrevol.mantra.models.database.AlarmModel;
import com.tekrevol.mantra.widget.AnyTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class ScheduleDateAdapter extends RecyclerView.Adapter<ScheduleDateAdapter.ViewHolder> {


    private int parentPosition;
    private Context activity;
    private List<AlarmModel> arrData;
    private final OnSubItemClickListener onSubItemClickListener;


    public ScheduleDateAdapter(Context activity, OnSubItemClickListener onSubItemClickListener) {

        this.activity = activity;
        this.onSubItemClickListener = onSubItemClickListener;
    }
//
//    public SubCategoriesAdapter(Context activity, int parentPosition, List<SubCategories> arrData, OnSubItemClickListener onSubItemClickListener) {
//        this.arrData = arrData;
//        this.activity = activity;
//        this.onSubItemClickListener = onSubItemClickListener;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_date_schedule, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        AlarmModel model = arrData.get(i);

        holder.txtdate.setText(model.getDateTime());
        setListener(holder, model);

    }


    private void setListener(final ViewHolder holder, final AlarmModel model) {
        holder.txtcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubItemClickListener.onItemClick(parentPosition, holder.getAdapterPosition(), model, v, ScheduleDateAdapter.class.getName());
                //     onSubItemClickListener.onItemClick(parentPosition, holder.getAdapterPosition(), model, v, ScheduleDateAdapter.class.getSimpleName());
            }
        });
    }

    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public void setArrData(List<AlarmModel> arrData) {
        Calendar calendar = Calendar.getInstance();
        //Returns current time in millis
        long currentTime = calendar.getTimeInMillis();

        ArrayList<AlarmModel> newAlarm  =new ArrayList<AlarmModel>();

        for (AlarmModel arr : arrData){
            if (arr.getUnixDTTM() > currentTime){
                newAlarm.add(arr);
            }
        }

        this.arrData = newAlarm;
    }


    @Override
    public int getItemCount() {
        return arrData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtdate)
        AnyTextView txtdate;
        @BindView(R.id.txtcancel)
        ImageView txtcancel;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
