package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.pagingadapter.PagingDelegate;
import com.tekrevol.mantra.adapters.recyleradapters.CategoriesAdapter;
import com.tekrevol.mantra.adapters.recyleradapters.SubCategoriesAdapter;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.callbacks.OnSubItemClickListener;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.receiving_model.Categories;
import com.tekrevol.mantra.models.receiving_model.SubCategories;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

public class CategoriesFragment extends BaseFragment implements OnItemClickListener, PagingDelegate.OnPageListener, OnSubItemClickListener {


    Unbinder unbinder;
    @BindView(R.id.recyclerview_categories)
    ShimmerRecyclerView recyclerviewCategories;
    @BindView(R.id.progressBar_activitymain)
    ProgressBar progressBarActivitymain;
    @BindView(R.id.txtsearch)
    AnyTextView txtsearch;
    @BindView(R.id.contSearch)
    RoundKornerLinearLayout contSearch;
    private String text;

    private CategoriesAdapter categoriesAdapter;
    private ArrayList<Categories> arrCategory;
    private ArrayList<Categories> arrSubCat;
    private ArrayList<SubCategories> arrSubCategories;

    Call<WebResponse<Object>> categoriesCall;
    private int offset;
    private static int limit = 11;
    private int x = 0;


    public static CategoriesFragment newInstance() {

        Bundle args = new Bundle();
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_categories;
    }

    /**
     * setting title bar here.
     *
     * @param titleBar
     */

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("Selection of Categories");
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHomeActivity().showBottomBar();
        arrCategory = new ArrayList<>();
        arrSubCategories = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(getContext(), arrCategory, this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // arrCategory.clear();
        // arrCategory.addAll(Constants.getDailyMantra());

        bindRecyclerView();

        if (onCreated) {
            return;
        }

        getCategories(limit, 0);
    }

    private void getCategories(int limit, int offset) {

        if (x == 0) {
            recyclerviewCategories.showShimmer();
        }

        Map<String, Object> mquery = new HashMap<>();
        mquery.put(WebServiceConstants.Q_PARAM_ROOT, 1);

        categoriesCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_CATEGORY, mquery, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                Type type = new TypeToken<ArrayList<Categories>>() {
                }.getType();
                ArrayList<Categories> arrayList = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type);

                if (x == 0) {
                    recyclerviewCategories.hideShimmer();
                }

                arrCategory.addAll(arrayList);
                arrSubCategories.clear();
                for (Categories categories : arrayList) {
                    for (SubCategories subCategories : categories.getChildren()) {
                        arrSubCategories.add(subCategories);
                    }
                }
                categoriesAdapter.notifyDataSetChanged();

                onDonePaging();
            }

            @Override
            public void onError(Object object) {

                if (recyclerviewCategories == null) {
                    return;
                }
                recyclerviewCategories.hideShimmer();

            }
        });

    }

    private void bindRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerviewCategories.setLayoutManager(mLayoutManager);
        ((DefaultItemAnimator) recyclerviewCategories.getItemAnimator()).setSupportsChangeAnimations(false);
        PagingDelegate pagingDelegate = new PagingDelegate.Builder(categoriesAdapter)
                .attachTo(recyclerviewCategories)
                .listenWith(CategoriesFragment.this)
                .build();

        //  categoriesAdapter.notifyDataSetChanged();
        recyclerviewCategories.setAdapter(categoriesAdapter);
        recyclerviewCategories.setItemViewType((type, position) -> R.layout.shimmer_view);

    }

    @Override
    public void setListeners() {
        contSearch.setOnClickListener(view1 -> getBaseActivity().addDockableFragment(CategorySearchFragment.newInstance(arrSubCategories), true));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.drawable.barcelona_logo:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onDestroyView() {
        if (categoriesCall != null) {
            categoriesCall.cancel();
        }

        unbinder.unbind();
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onItemClick(int position, Object object, View view, String adapterName) {
        Categories model = (Categories) object;
        if (CategoriesAdapter.class.getSimpleName().equals(adapterName)) {
            getBaseActivity().addDockableFragment(ChildrenSeeAllFragment.newInstance(model.getId(), model.getName()), true);
        }
    }

    @Override
    public void onItemClick(int parentPosition, int childPosition, Object object, View view, String adapterName) {

        SubCategories model = (SubCategories) object;
        if (SubCategoriesAdapter.class.getSimpleName().equals(adapterName)) {
            getBaseActivity().addDockableFragment(CategoryViewAllFragment.newInstance(model.getId(), model.getName()), true);
        }

    }


    @Override
    public void onPage(int i) {

    }

    @Override
    public void onDonePaging() {

    }
}
