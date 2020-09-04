package com.tekrevol.mantra.adapters.recyleradapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout;
import com.jcminarro.roundkornerlayout.RoundKornerRelativeLayout;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.pagingadapter.PagingAdapter;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.libraries.imageloader.ImageLoaderHelper;
import com.tekrevol.mantra.models.receiving_model.SubCategories;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class SearchCategoryAdapter extends PagingAdapter {


    private final OnItemClickListener onItemClick;

    private Context activity;
    private List<SubCategories> arrData;
    private List<SubCategories> filteredData;
    private Filter mFilter = new ItemFilter();

    public SearchCategoryAdapter(Context activity, List<SubCategories> arrData, OnItemClickListener onItemClickListener) {
        this.arrData = arrData;
        this.filteredData = arrData;
        this.activity = activity;
        this.onItemClick = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        itemView = LayoutInflater.from(activity).inflate(R.layout.categories_see_all, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        super.onBindViewHolder(holder, i);
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;

            // Do your normal binding
            SubCategories model = filteredData.get(i);

            viewHolder.categoriesName.setText(model.getName());
            viewHolder.time.setText("");
            //ImageLoaderHelper.loadImageWithAnimations(((ViewHolder) holder).imgicon, model.getIconImageUrl(), false);

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
                        viewHolder.contPlay.setBackgroundColor(Color.argb(alpha,red,green,blue));
                    }else{
                        viewHolder.contPlay.setBackgroundColor(Color.rgb(red,green,blue));
                    }


                }else{
                    viewHolder.contPlay.setBackgroundColor(Color.parseColor(model.getColor()));
                }
            } else {
                viewHolder.contPlay.setBackgroundColor(Color.parseColor("E9D6D7"));

            }
               /* if (model.getIconColor() != null) {
                    viewHolder.iconbgcolor.setBackgroundColor(Color.parseColor(model.getIconColor()));
                } else {
                    viewHolder.iconbgcolor.setBackgroundColor(Color.parseColor("D5B4B5"));

                }*/

            setListener(viewHolder, model);


        }

    }

    private void setListener(final ViewHolder holder, final SubCategories model) {
        holder.contPlay.setOnClickListener(view -> onItemClick.onItemClick(holder.getAdapterPosition(), model, view, HomeDailyMantraAdapter.class.getSimpleName()));

    }


    @Override
    public int getPagingLayout() {
        return 0;
    }

    @Override
    public int getPagingItemCount() {
        return arrData.size();
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<SubCategories> list = arrData;

            int count = list.size();

//            final ArrayList<String> nlist = new ArrayList<String>(count);
            final ArrayList<SubCategories> filterData = new ArrayList<SubCategories>();

            String filterableString1;
            String filterableString2;

            for (int i = 0; i < count; i++) {
                filterableString1 = list.get(i).getName();
                if (filterableString1.toLowerCase().contains(filterString)
                ) {
//                    nlist.add(filterableString);
                    filterData.add(list.get(i));
                }
            }

            results.values = filterData;
            results.count = filterData.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<SubCategories>) results.values;
            notifyDataSetChanged();

        }

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

    public int getCount() {
        if (filteredData == null) {
            return 0;
        }
        return filteredData.size();
    }

    public SubCategories getItem(int position) {
        return filteredData.get(position);
    }

    public Filter getFilter() {
        return mFilter;
    }
}
