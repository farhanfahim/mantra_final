package com.tekrevol.mantra.adapters.recyleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jcminarro.roundkornerlayout.RoundKornerRelativeLayout;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.libraries.imageloader.ImageLoaderHelper;
import com.tekrevol.mantra.managers.DateManager;
import com.tekrevol.mantra.models.receiving_model.MediaModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class HomeDailyMantraAdapter extends RecyclerView.Adapter<HomeDailyMantraAdapter.ViewHolder> {


    private final OnItemClickListener onItemClick;


    private Context activity;
    private List<MediaModel> arrData;

    public HomeDailyMantraAdapter(Context activity, List<MediaModel> arrData, OnItemClickListener onItemClickListener) {
        this.arrData = arrData;
        this.activity = activity;
        this.onItemClick = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.customlist_dailymantra, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        MediaModel model = arrData.get(i);

        holder.txtTitle.setText(model.getName());
        holder.genre.setText(model.getCategory().getName());
        String date = DateManager.ConvertSecondToHHMMString(model.getMediaLength());
        holder.time.setText(date);
        ImageLoaderHelper.loadImageWithAnimations(((ViewHolder) holder).imgIcon, model.getCategory().getIconImageUrl(), false);

        if (model.getImage() != null) {
            ImageLoaderHelper.loadImageWithouAnimation(((ViewHolder) holder).imgMantra, model.getImageUrl(), false);
        } else if (model.getCategory() != null) {
            ImageLoaderHelper.loadImageWithouAnimation(((ViewHolder) holder).imgMantra, model.getCategory().getImageUrl(), false);

        }
        setListener(holder, model);
    }

    private void setListener(final ViewHolder holder, final MediaModel model) {
        holder.contPlay1.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, HomeDailyMantraAdapter.class.getSimpleName()));
    }


    @Override
    public int getItemCount() {
        return arrData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_mantra)
        ImageView imgMantra;
        @BindView(R.id.daily_mantra_playbtn)
        ImageButton dailyMantraPlaybtn;
        @BindView(R.id.cont_play)
        RelativeLayout contPlay;
        @BindView(R.id.layout1)
        RelativeLayout layout1;
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.layout2)
        RelativeLayout layout2;
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.genre)
        TextView genre;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.layout3)
        RelativeLayout layout3;
        @BindView(R.id.cont_play1)
        RoundKornerRelativeLayout contPlay1;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
