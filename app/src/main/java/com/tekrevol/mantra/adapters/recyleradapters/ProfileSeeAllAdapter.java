package com.tekrevol.mantra.adapters.recyleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jcminarro.roundkornerlayout.RoundKornerRelativeLayout;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.models.DummyAdapterModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class ProfileSeeAllAdapter extends RecyclerView.Adapter<ProfileSeeAllAdapter.ViewHolder> {


    private final OnItemClickListener onItemClick;
    
    private Context activity;
    private List<DummyAdapterModel> arrData;

    public ProfileSeeAllAdapter(Context activity, List<DummyAdapterModel> arrData, OnItemClickListener onItemClickListener) {
        this.arrData = arrData;
        this.activity = activity;
        this.onItemClick = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        itemView = LayoutInflater.from(activity).inflate(R.layout.categories_see_all, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        DummyAdapterModel model = arrData.get(i);
        /*holder.categoriesName.setText(arrData.get(i).getName());
        holder.time.setText("");*/
        setListener(holder, model);
    }

    private void setListener(final ViewHolder holder, final DummyAdapterModel model) {
        holder.contPlay.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, HomeDailyMantraAdapter.class.getSimpleName()));

    }


    @Override
    public int getItemCount() {
        return arrData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout2)
        RelativeLayout layout2;
        @BindView(R.id.categories_name)
        TextView categoriesName;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.layout3)
        RelativeLayout layout3;
        @BindView(R.id.cont_play)
        RoundKornerRelativeLayout contPlay;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
