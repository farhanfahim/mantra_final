package com.tekrevol.mantra.adapters.recyleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.models.DummyAdapterModel;
import com.tekrevol.mantra.widget.AnyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class HomeMovieLinesAdapter extends RecyclerView.Adapter<HomeMovieLinesAdapter.ViewHolder> {


    private final OnItemClickListener onItemClick;



    private Context activity;
    private List<DummyAdapterModel> arrData;

    public HomeMovieLinesAdapter(Context activity, List<DummyAdapterModel> arrData, OnItemClickListener onItemClickListener) {
        this.arrData = arrData;
        this.activity = activity;
        this.onItemClick = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.customlist_movielines, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        DummyAdapterModel model = arrData.get(i);
        setListener(holder, model);
    }

    private void setListener(final ViewHolder holder, final DummyAdapterModel model) {
    }


    @Override
    public int getItemCount() {
        return arrData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_moviename_movielines)
        AnyTextView txtMovienameMovielines;
        @BindView(R.id.txt_mtime_movielines)
        AnyTextView txtMtimeMovielines;
        @BindView(R.id.img_playbtn_movielines)
        ImageView imgPlaybtnMovielines;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
