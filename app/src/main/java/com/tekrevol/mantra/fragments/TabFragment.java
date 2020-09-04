package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;
import com.tekrevol.opentab.adapters.tabadapter.TabAdapter;

import butterknife.BindView;
import butterknife.OnClick;

public class TabFragment extends BaseFragment {
    TabAdapter tabAdapter;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.btnBack)
    LinearLayout btnBack;
    @BindView(R.id.txtTitle)
    AnyTextView txtTitle;
    TitleBar tb;

    public static TabFragment newInstance() {

        Bundle args = new Bundle();
        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getDrawerLockMode() {
        return 0;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.frag_tablayout;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setVisibility(View.GONE);
     /*   titleBar.setTitle("My Mantra");
        titleBar.showSaveHome(getHomeActivity());
        // titleBar.showSidebar(getBaseActivity());
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());*/
    }


    @Override
    public void setListeners() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabAdapter = new TabAdapter(getChildFragmentManager());
        tabAdapter.addFragment(new PublicFragment(), "Public");
        tabAdapter.addFragment(new PrivateFragment(), "Private");
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @OnClick(R.id.btnBack)
    public void onViewClicked() {
        getBaseActivity().popBackStack();
    }
}
