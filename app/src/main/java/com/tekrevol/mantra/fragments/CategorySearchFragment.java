package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.pagingadapter.PagingDelegate;
import com.tekrevol.mantra.adapters.recyleradapters.SearchCategoryAdapter;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.ui.helper.KeyboardHelper;
import com.tekrevol.mantra.models.receiving_model.SubCategories;
import com.tekrevol.mantra.widget.AnyEditTextView;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CategorySearchFragment extends BaseFragment implements OnItemClickListener, PagingDelegate.OnPageListener {

    @BindView(R.id.progressBar_activitymain)
    ProgressBar progressBarActivitymain;

    Unbinder unbinder;
    @BindView(R.id.recyclerview_childrenlaugh)
    ShimmerRecyclerView recyclerviewMovieLines;
    @BindView(R.id.txtsearch)
    AnyEditTextView txtsearch;
    @BindView(R.id.btnclear)
    ImageView btnclear;
    private boolean isSearchBarEmpty = true;
    private SearchCategoryAdapter homeChildLaughAdapter;
    private ArrayList<SubCategories> arrSubCategories = new ArrayList<>();

    public static CategorySearchFragment newInstance(ArrayList<SubCategories> arrSubCategories) {

        Bundle args = new Bundle();
        CategorySearchFragment fragment = new CategorySearchFragment();
        fragment.arrSubCategories = arrSubCategories;
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_search_categories;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("Search Category");
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //arrChildrenLaugh = new ArrayList<>();
        homeChildLaughAdapter = new SearchCategoryAdapter(getContext(), arrSubCategories, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isSearchBarEmpty) {
            btnclear.setVisibility(View.GONE);
        } else {
            btnclear.setVisibility(View.VISIBLE);
        }


        KeyboardHelper.showSoftKeyboardForcefully(getContext(), txtsearch);
        KeyboardHelper.showSoftKeyboard(getContext(), txtsearch);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindRecyclerView();


    }

    private void bindRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        recyclerviewMovieLines.setLayoutManager(mLayoutManager1);
        ((DefaultItemAnimator) recyclerviewMovieLines.getItemAnimator()).setSupportsChangeAnimations(false);
        PagingDelegate pagingDelegate = new PagingDelegate.Builder(homeChildLaughAdapter)
                .attachTo(recyclerviewMovieLines)
                .listenWith(CategorySearchFragment.this)
                .build();
        recyclerviewMovieLines.setAdapter(homeChildLaughAdapter);
        homeChildLaughAdapter.notifyDataSetChanged();

    }

    @Override
    public void setListeners() {
        txtsearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                homeChildLaughAdapter.getFilter().filter(s);
                if (txtsearch.getStringTrimmed().equalsIgnoreCase("")) {
                    isSearchBarEmpty = true;
                    btnclear.setVisibility(View.GONE);

                } else {
                    isSearchBarEmpty = false;
                    btnclear.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
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
        SubCategories model = (SubCategories) object;
        getBaseActivity().addDockableFragment(CategoryViewAllFragment.newInstance(model.getId(), model.getName()), false);

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPage(int i) {

    }

    @Override
    public void onDonePaging() {

    }

    @OnClick(R.id.btnclear)
    public void onViewClicked() {
        txtsearch.setText("");
        isSearchBarEmpty = true;
        btnclear.setVisibility(View.GONE);
        KeyboardHelper.hideSoftKeyboard(getContext(), btnclear);
    }
}
