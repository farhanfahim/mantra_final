package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.recyleradapters.LogMantraAdapter;
import com.tekrevol.mantra.enums.FragmentName;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.managers.ObjectBoxManager;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.widget.TitleBar;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LogsMantraFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.logRecyclerView)
    ShimmerRecyclerView logRecyclerView;


    LogMantraAdapter logMantraAdapter;
    ArrayList<MediaModel> arrMovieLines;
    FragmentName titleName;

    public static LogsMantraFragment newInstance(FragmentName titleName) {

        Bundle args = new Bundle();
        LogsMantraFragment fragment = new LogsMantraFragment();
        fragment.setArguments(args);
        fragment.titleName = titleName;
        return fragment;
    }


    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_log_mantra;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("Log Mantra");
        titleBar.showSaveHome(getHomeActivity());
        titleBar.showSidebar(getBaseActivity());
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrMovieLines = new ArrayList<>();
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
        getScheduleMantra();

    }

    private void bindRecyclerView() {
        logMantraAdapter = new LogMantraAdapter(getContext());

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        logRecyclerView.setLayoutManager(mLayoutManager1);
        ((DefaultItemAnimator) logRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        logRecyclerView.setAdapter(logMantraAdapter);
        logRecyclerView.setItemViewType((type, position) -> R.layout.shimmer_customlist_listsearch);
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


    }

    private void getScheduleMantra() {

        ArrayList<MediaModel> arrayList = ObjectBoxManager.INSTANCE.getAllScheduledMantraMediaModels(getContext());
        arrMovieLines.addAll(arrayList);
        logMantraAdapter.setData(arrMovieLines);
    }


    @Override
    public void onDestroyView() {

        unbinder.unbind();
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
