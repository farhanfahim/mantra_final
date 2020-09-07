package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.pagingadapter.PagingDelegate;
import com.tekrevol.mantra.adapters.recyleradapters.HomeChildLaughSeeAllAdapter;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.receiving_model.Categories;
import com.tekrevol.mantra.models.wrappers.WebResponse;
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

public class ChildrenSeeAllFragment extends BaseFragment implements OnItemClickListener, PagingDelegate.OnPageListener {

    @BindView(R.id.progressBar_activitymain)
    ProgressBar progressBarActivitymain;
    private int offset;
    private static int limit = 11;
    private int x = 0;

    Unbinder unbinder;
    @BindView(R.id.recyclerview_movie_lines)
    ShimmerRecyclerView recyclerviewMovieLines;
    @BindView(R.id.categories_name)
    TextView categoriesName;

    private HomeChildLaughSeeAllAdapter homeChildLaughAdapter;
    private ArrayList<Categories> arrChildrenLaugh;
    private int categoryid;
    private String categoryname;
    Call<WebResponse<Object>> categoriesCall;

    public static ChildrenSeeAllFragment newInstance(int id, String name) {

        Bundle args = new Bundle();
        ChildrenSeeAllFragment fragment = new ChildrenSeeAllFragment();
        fragment.categoryid = id;
        fragment.categoryname = name;
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_movieline_view_all;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle(categoryname + " Category");
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrChildrenLaugh = new ArrayList<>();
        homeChildLaughAdapter = new HomeChildLaughSeeAllAdapter(getContext(), arrChildrenLaugh, this);

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

        bindRecyclerView();
        if (onCreated) {
            return;
        }
        getCategories(limit, 0);


    }

    private void bindRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        recyclerviewMovieLines.setLayoutManager(mLayoutManager1);
        ((DefaultItemAnimator) recyclerviewMovieLines.getItemAnimator()).setSupportsChangeAnimations(false);
        PagingDelegate pagingDelegate = new PagingDelegate.Builder(homeChildLaughAdapter)
                .attachTo(recyclerviewMovieLines)
                .listenWith(ChildrenSeeAllFragment.this)
                .build();
        recyclerviewMovieLines.setAdapter(homeChildLaughAdapter);

    }

    private void getCategories(int limit, int offset) {

        if (x == 0) {
            recyclerviewMovieLines.showShimmer();

        }

        Map<String, Object> mquery = new HashMap<>();
        mquery.put(WebServiceConstants.PATH_PARENTID, categoryid);
        mquery.put(WebServiceConstants.Q_PARAM_LIMIT, limit);
        mquery.put(WebServiceConstants.Q_PARAM_OFFSET, offset);
        categoriesCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_CATEGORY, mquery, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {

                categoriesName.setText(categoryname);
                Type type = new TypeToken<ArrayList<Categories>>() {
                }.getType();
                // Categories cat = type.

                ArrayList<Categories> categories = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type);

                if (x == 0) {
                    recyclerviewMovieLines.hideShimmer();
                }

                arrChildrenLaugh.addAll(categories);
                homeChildLaughAdapter.notifyDataSetChanged();
                onDonePaging();
            }

            @Override
            public void onError(Object object) {

                if (recyclerviewMovieLines == null) {
                    return;
                }

                recyclerviewMovieLines.hideShimmer();


            }
        });

    }

    @Override
    public void setListeners() {

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
    public void onItemClick(int position, Object object, View view, String adapterName) {
        Categories model = (Categories) object;
        getBaseActivity().addDockableFragment(CategoryViewAllFragment.newInstance(model.getId(), model.getName(),model.getParentId()), false);

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
    public void onPage(int i) {

        if (offset < i) {

            offset = i;

            x++;
            progressBarActivitymain.setVisibility(View.VISIBLE);

            getCategories(limit, i);
        }
    }

    @Override
    public void onDonePaging() {

        if (progressBarActivitymain != null) {
            progressBarActivitymain.setVisibility(View.GONE);
        }


    }
}
