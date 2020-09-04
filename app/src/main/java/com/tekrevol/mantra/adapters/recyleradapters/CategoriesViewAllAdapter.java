package com.tekrevol.mantra.adapters.recyleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.models.DummyAdapterModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class CategoriesViewAllAdapter extends RecyclerView.Adapter<CategoriesViewAllAdapter.ViewHolder> {


    private final OnItemClickListener onItemClick;


    private Context activity;
    private List<DummyAdapterModel> arrData;

    public CategoriesViewAllAdapter(Context activity, List<DummyAdapterModel> arrData, OnItemClickListener onItemClickListener) {
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
        DummyAdapterModel model = arrData.get(i);
        if (model.isChoice1()) {
          //  AnimationHelper.getInstance(activity).showCategoryOptionBar(holder.layout2);
            holder.layout2.setVisibility(View.VISIBLE);
        } else {
          //  AnimationHelper.getInstance(activity).showCategoryOptionBar(holder.layout2);
            holder.layout2.setVisibility(View.GONE);
        }
        setListener(holder, model);
    }

    private void setListener(final ViewHolder holder, final DummyAdapterModel model) {
        holder.imgbtnSetting.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, HomeDailyMantraAdapter.class.getSimpleName()));
        holder.imgbtnClose.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, HomeDailyMantraAdapter.class.getSimpleName()));
        holder.detail.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, HomeDailyMantraAdapter.class.getSimpleName()));

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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
