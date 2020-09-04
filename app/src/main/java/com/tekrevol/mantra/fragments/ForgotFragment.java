package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.AnyEditTextView;
import com.tekrevol.mantra.widget.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class ForgotFragment extends BaseFragment {


    @BindView(R.id.btnBack)
    LinearLayout btnBack;
    @BindView(R.id.btn_forgot)
    TextView btnForgot;
    @BindView(R.id.contParentLayout)
    RelativeLayout contParentLayout;
    Unbinder unbinder;
    @BindView(R.id.txt_email)
    AnyEditTextView txtEmail;
    Call<WebResponse<Object>> webCall;

    public static ForgotFragment newInstance() {

        Bundle args = new Bundle();
        ForgotFragment fragment = new ForgotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getDrawerLockMode() {
        return 0;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_forgotpassword;
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

    @Override
    public void onDestroyView() {
        if (webCall != null) {
            webCall.cancel();
        }

        unbinder.unbind();


        super.onDestroyView();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.btnBack, R.id.btn_forgot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_forgot:
                forgotPasswordAPI();
                break;
        }
    }

    private void forgotPasswordAPI() {

        if(!txtEmail.testValidity())
        {
            UIHelper.showAlertDialog(getContext(),"Please enter valid email address");
            return;

        }
        sharedPreferenceManager.putValue(AppConstants.KEY_CURRENT_USER_EMAIL,txtEmail.getStringTrimmed());
        Map<String, Object> query = new HashMap<>();
        query.put(WebServiceConstants.Q_PARAM_EMAIL,txtEmail.getStringTrimmed());

        webCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_FORGET_PASSWORD, query, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UIHelper.showToast(getContext(),webResponse.message);
                getBaseActivity().addDockableFragment(VerifyFragment.newInstance(), true);

            }

            @Override
            public void onError(Object object) {

            }
        });
    }
}
