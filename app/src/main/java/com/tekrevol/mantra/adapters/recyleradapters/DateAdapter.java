package com.tekrevol.mantra.adapters.recyleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.models.database.AlarmModel;
import com.tekrevol.mantra.widget.AnyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {

    private final OnItemClickListener onItemClick;


    private Context activity;
    private List<AlarmModel> arrData;

    public DateAdapter(Context activity, List<AlarmModel> arrData, OnItemClickListener onItemClickListener) {
        this.arrData = arrData;
        this.activity = activity;
        this.onItemClick = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.custom_date, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        AlarmModel model = arrData.get(i);
        holder.txtdate.setText(model.getDateTime());
        setListener(holder, model);
    }

    private void setListener(final ViewHolder holder, final AlarmModel model) {
        holder.txtcancel.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, DateAdapter.class.getSimpleName()));
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
