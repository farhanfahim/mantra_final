package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.fragments.abstracts.GenericContentFragment;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.receiving_model.Slug;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class LandingFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.btn_signup)
    TextView btnSignup;
    @BindView(R.id.contParentLayout)
    RelativeLayout contParentLayout;
    @BindView(R.id.terms)
    AnyTextView terms;
    Call<WebResponse<Object>> aboutCall;


    public static LandingFragment newInstance() {

        Bundle args = new Bundle();
        LandingFragment fragment = new LandingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getDrawerLockMode() {
        return 0;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_landing;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {

        titleBar.setVisibility(View.GONE);
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


    @OnClick({R.id.btn_login, R.id.btn_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                getBaseActivity().addDockableFragment(LoginFragment.newInstance(), true);
                break;
            case R.id.btn_signup:
                getBaseActivity().addDockableFragment(SignupFragment.newInstance(), true);

                break;
        }
    }

    @OnClick(R.id.terms)
    public void onViewClicked() {
        Map<String, Object> queryMap = new HashMap<>();

        aboutCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_PAGES + "/" + AppConstants.KEY_TERMS, queryMap, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {


                Slug pagesModel = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , Slug.class);

                getBaseActivity().addDockableFragment(GenericContentFragment.newInstance(pagesModel.getTitle(), pagesModel.getContent(), true), false);

            }

            @Override
            public void onError(Object object) {

            }
        });

    }

    @Override
    public void onDestroyView() {

        if (aboutCall != null) {
            aboutCall.cancel();
        }
        unbinder.unbind();


        super.onDestroyView();

    }

}
