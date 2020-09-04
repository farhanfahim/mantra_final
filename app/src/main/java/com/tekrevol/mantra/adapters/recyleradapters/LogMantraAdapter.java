package com.tekrevol.mantra.adapters.recyleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.widget.AnyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class LogMantraAdapter extends RecyclerView.Adapter<LogMantraAdapter.ViewHolder> {
    private Context activity;
    private ArrayList<MediaModel> arrData;

    public LogMantraAdapter(Context activity) {

        this.activity = activity;
    }

    public void setData(ArrayList<MediaModel> arrData){
        this.arrData = arrData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_log, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        MediaModel model = arrData.get(i);
        holder.txtReminderText.setText(model.getReminderText());
        //holder.txtCategory.setText(model.getCategoryId());
        holder.txtDate1.setText(model.getDate().toString());
        //holder.txtAlarmId.setText(model.getAlarms().get(i).getAlarmId());
        holder.txtDate2.setText(model.getAlarms().get(i).getDateTime());
    }

    @Override
    public int getItemCount() {
        return arrData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtReminderText)
        AnyTextView txtReminderText;
        @BindView(R.id.txtCategory)
        AnyTextView txtCategory;
        @BindView(R.id.txtDate1)
        AnyTextView txtDate1;
        @BindView(R.id.txtAlarmId)
        AnyTextView txtAlarmId;
        @BindView(R.id.txtDate2)
        AnyTextView txtDate2;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
