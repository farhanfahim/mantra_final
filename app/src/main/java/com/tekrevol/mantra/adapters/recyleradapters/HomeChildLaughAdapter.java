package com.tekrevol.mantra.adapters.recyleradapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout;
import com.jcminarro.roundkornerlayout.RoundKornerRelativeLayout;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.libraries.imageloader.ImageLoaderHelper;
import com.tekrevol.mantra.models.receiving_model.Categories;
import com.tekrevol.mantra.widget.AnyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class HomeChildLaughAdapter extends RecyclerView.Adapter<HomeChildLaughAdapter.ViewHolder> {


    private final OnItemClickListener onItemClick;


    private Context activity;
    private List<Categories> arrData;

    public HomeChildLaughAdapter(Context activity, List<Categories> arrData, OnItemClickListener onItemClickListener) {
        this.arrData = arrData;
        this.activity = activity;
        this.onItemClick = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.customlist_childrenlaugh, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        Categories model = arrData.get(i);
        holder.categoriesName.setText(model.getName());
        ImageLoaderHelper.loadImageWithAnimations(((ViewHolder) holder).imgicon, model.getIconImageUrl(), false);


        holder.time.setText("");
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
        if (model.getIconColor() != null) {
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
                    holder.iconbgcolor.setBackgroundColor(Color.argb(alpha,red,green,blue));
                }else{
                    holder.iconbgcolor.setBackgroundColor(Color.rgb(red,green,blue));
                }


            }else{
                holder.iconbgcolor.setBackgroundColor(Color.parseColor(model.getColor()));
            }
        } else {
            holder.iconbgcolor.setBackgroundColor(Color.parseColor("D5B4B5"));

        }

     /*   FontDrawable drawable = new FontDrawable(activity, R.string.fa_paper_plane_solid, true, false);
        drawable.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
*/
// white color to icon
/*        GradientDrawable drawable = (GradientDrawable) holder.iconbgcolor.getBackground();
        drawable.setColor(Color.parseColor(model.getIconColor()));*/

      /*  holder.imgicon.setImageDrawable(drawable);*/
        setListener(holder, model);

    }

    private void setListener(final ViewHolder holder, final Categories model) {
        holder.contPlay.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, HomeChildLaughAdapter.class.getSimpleName()));

    }


    @Override
    public int getItemCount() {
        return arrData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout2)
        RelativeLayout layout2;
        @BindView(R.id.categories_name)
        AnyTextView categoriesName;
        @BindView(R.id.time)
        AnyTextView time;
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
