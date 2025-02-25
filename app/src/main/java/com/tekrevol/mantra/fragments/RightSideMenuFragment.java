package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.activities.HomeActivity;
import com.tekrevol.mantra.callbacks.OnNewPacketReceivedListener;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by khanhamza on 09-May-17.
 */

public class RightSideMenuFragment extends BaseFragment implements OnNewPacketReceivedListener {

    Unbinder unbinder;
    @BindView(R.id.txtHome)
    AnyTextView txtHome;
    @BindView(R.id.txtCardSubscription)
    AnyTextView txtCardSubscription;
    @BindView(R.id.txtAbout)
    AnyTextView txtAbout;
    @BindView(R.id.txtPreferences)
    AnyTextView txtPreferences;
    @BindView(R.id.txtLogout)
    AnyTextView txtLogout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.imgBackground)
    ImageView imgBackground;

    public static RightSideMenuFragment newInstance() {

        Bundle args = new Bundle();

        RightSideMenuFragment fragment = new RightSideMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_sidebar_right;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ////        scrollToTop();
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_UNDEFINED;
    }


    public void scrollToTop() {
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, 0);
//                scrollView.fullScroll(View.FOCUS_UP);
            }
        });
    }


    @OnClick({R.id.txtHome, R.id.txtCardSubscription, R.id.txtAbout, R.id.txtLogout, R.id.txtPreferences})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.txtHome:
                if (getActivity() instanceof HomeActivity) {
                    getBaseActivity().reload();
                } else {
                    getBaseActivity().clearAllActivitiesExceptThis(HomeActivity.class);
                }
                break;

            case R.id.txtCardSubscription:
//                getBaseActivity().addDockableFragment(CardSubscriptionFragment.newInstance());
                showNextBuildToast();
                break;

            case R.id.txtAbout:
             //   getBaseActivity().addDockableFragment(GenericContentFragment.newInstance("About", ""), false);
                break;
            case R.id.txtLogout:
                logoutClick(this);
                break;
        }
    }


}
