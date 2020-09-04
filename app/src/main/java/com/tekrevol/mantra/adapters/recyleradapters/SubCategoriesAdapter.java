package com.tekrevol.mantra.adapters.recyleradapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout;
import com.jcminarro.roundkornerlayout.RoundKornerRelativeLayout;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.callbacks.OnSubItemClickListener;
import com.tekrevol.mantra.libraries.imageloader.ImageLoaderHelper;
import com.tekrevol.mantra.models.receiving_model.SubCategories;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class SubCategoriesAdapter extends RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder> {


    private final OnSubItemClickListener onSubItemClickListener;
    private int parentPosition;
    private Context activity;
    private List<SubCategories> arrData;

    public SubCategoriesAdapter(Context activity, OnSubItemClickListener onSubItemClickListener) {
        this.onSubItemClickListener = onSubItemClickListener;

        this.activity = activity;
    }

    /*public SubCategoriesAdapter(Context activity, int parentPosition, List<SubCategories> arrData, OnSubItemClickListener onSubItemClickListener) {
            this.arrData = arrData;
            this.activity = activity;
            this.onSubItemClickListener = onSubItemClickListener;
        }
    */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.customlist_childrenlaugh, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        SubCategories model = arrData.get(i);

        holder.categoriesName.setText(arrData.get(i).getName());
        holder.time.setText("");
        // ImageLoaderHelper.loadImageWithAnimations(((ViewHolder) holder).imgicon, model.getIconImageUrl(), false);

        if (model.getColor() != null) {
            if (model.getColor().length() > 7){
                String str = model.getColor();
                str = str.replace("rgba(","");
                str = str.replace(")","");
                String[] splitedValues = str.split(",",4);
                int red = Integer.parseInt(splitedValues[0]);
                int blue =  Integer.parseInt(splitedValues[1]);
                int green =  Integer.parseInt(splitedValues[2]);
                float alpha = Float.parseFloat(splitedValues[3]);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    holder.contPlay.setBackgroundColor(Color.argb(alpha,red,green,blue));
                }else{
                    holder.contPlay.setBackgroundColor(Color.rgb(red,green,blue));
                }


            }else{
                holder.contPlay.setBackgroundColor(Color.parseColor(model.getColor()));
            }

        } else {
            holder.contPlay.setBackgroundColor(Color.parseColor("E9D6D7"));

        }
        /*if (model.getIconColor() != null) {
            holder.iconbgcolor.setBackgroundColor(Color.parseColor(model.getIconColor()));
        } else {
            holder.iconbgcolor.setBackgroundColor(Color.parseColor("D5B4B5"));

        }*/
        setListener(holder, model);
    }

    private void setListener(final ViewHolder holder, final SubCategories model) {
        holder.contPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubItemClickListener.onItemClick(parentPosition, holder.getAdapterPosition(), model, v, SubCategoriesAdapter.class.getSimpleName());
            }
        });
    }

    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public void setArrData(List<SubCategories> arrData) {
        this.arrData = arrData;
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
        @BindView(R.id.iconbgcolor)
        RoundKornerLinearLayout iconbgcolor;
        @BindView(R.id.imgicon)
        ImageView imgicon;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
