package com.tekrevol.mantra.adapters.recyleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.pagingadapter.PagingAdapter;
import com.tekrevol.mantra.adapters.pagingadapter.PagingDelegate;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.callbacks.OnSubItemClickListener;
import com.tekrevol.mantra.fragments.ChildrenSeeAllFragment;
import com.tekrevol.mantra.fragments.SearchFragment;
import com.tekrevol.mantra.models.receiving_model.Categories;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class CategoriesAdapter extends PagingAdapter implements PagingDelegate.OnPageListener {


//    private SubCategoriesAdapter subCategoriesAdapter;
//    private ArrayList<DummyAdapterModel> arrCategory;

    private final OnItemClickListener onItemClick;
    private final OnSubItemClickListener onSubItemClickListener;

    private Context activity;
    private List<Categories> arrData;

    public CategoriesAdapter(Context activity, List<Categories> arrData, OnItemClickListener onItemClickListener, OnSubItemClickListener onSubItemClickListener) {
        this.arrData = arrData;
        this.activity = activity;
 /*       this.arrCategory = new ArrayList<>();
        subCategoriesAdapter = new SubCategoriesAdapter(activity, 0, arrCategory, onSubItemClickListener);
 */
        this.onItemClick = onItemClickListener;
        this.onSubItemClickListener = onSubItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.customlist_categories, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

        super.onBindViewHolder(holder, i);


        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;

            // Do your normal binding
            Categories model = arrData.get(i);
            SubCategoriesAdapter subCategoriesAdapter = new SubCategoriesAdapter(activity, onSubItemClickListener);
            subCategoriesAdapter.setParentPosition(holder.getAdapterPosition());
            subCategoriesAdapter.setArrData(model.getChildren());
            viewHolder.categoriesName.setText(model.getName());
            bindNestedRecyclerView(subCategoriesAdapter, viewHolder);
            setListener(viewHolder, model);

        }


      /*  SubCategoriesAdapter subCategoriesAdapter = new SubCategoriesAdapter(activity, onSubItemClickListener);
        subCategoriesAdapter.setParentPosition(holder.getAdapterPosition());
        subCategoriesAdapter.setArrData(model.getChildren());
        holder.categoriesName.setText(model.getName());
*//*sub
        arrCategory.clear();
        arrCategory.addAll(Constants.getDailyMantra());*//*
        bindNestedRecyclerView(subCategoriesAdapter, holder);
        setListener(holder, model);*/

    }


    private void bindNestedRecyclerView(SubCategoriesAdapter subCategoriesAdapter, ViewHolder holder) {


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerviewSubcategories.setLayoutManager(mLayoutManager);

        ((DefaultItemAnimator) holder.recyclerviewSubcategories.getItemAnimator()).setSupportsChangeAnimations(false);

        holder.recyclerviewSubcategories.setAdapter(subCategoriesAdapter);
        subCategoriesAdapter.notifyDataSetChanged();
    }

    private void setListener(final ViewHolder holder, final Categories model) {

        holder.viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(holder.getAdapterPosition(), model, v, CategoriesAdapter.class.getSimpleName());
            }
        });
    }


    @Override
    public int getPagingLayout() {
        return R.layout.customlist_categories;
    }

    @Override
    public int getPagingItemCount() {
        return arrData.size();
    }

    @Override
    public int getItemCount() {
        return arrData.size();
    }

    @OnClick(R.id.viewall)
    public void onViewClicked() {
    }

    @Override
    public void onPage(int offset) {

    }

    @Override
    public void onDonePaging() {

    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.categories_name)
        TextView categoriesName;
        @BindView(R.id.viewall)
        TextView viewall;
        @BindView(R.id.recyclerview_subcategories)
        RecyclerView recyclerviewSubcategories;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
