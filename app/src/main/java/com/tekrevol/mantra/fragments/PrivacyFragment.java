package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.widget.TitleBar;

public class PrivacyFragment extends BaseFragment {


    public static PrivacyFragment newInstance() {

        Bundle args = new Bundle();

        PrivacyFragment fragment = new PrivacyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getDrawerLockMode() {
        return 0;
    }

    @Override
    protected int getFragmentLayout()  {
        return R.layout.fragment_privacy;
    }


    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("Privacy Policy");
        // titleBar.showSidebar(getBaseActivity());
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());

    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
