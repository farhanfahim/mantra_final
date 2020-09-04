package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.widget.TitleBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoryDetailFragment extends BaseFragment implements OnItemClickListener {


    Unbinder unbinder;

    public static CategoryDetailFragment newInstance() {

        Bundle args = new Bundle();
        CategoryDetailFragment fragment = new CategoryDetailFragment();
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
        titleBar.setTitle("Movie Lines");
        // titleBar.showSidebar(getBaseActivity());
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        if(onCreated)
        {
            return;
        }

    }

    private void bindRecyclerView() {


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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(int position, Object object, View view, String adapterName) {

    }

}
