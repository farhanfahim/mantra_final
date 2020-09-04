package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.sending_model.ResetPasswordSendingModel;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.AnyEditTextView;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class VerifyFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.btnBack)
    LinearLayout btnBack;
    @BindView(R.id.edt_code)
    AnyEditTextView edtCode;
    @BindView(R.id.btn_verify)
    AnyTextView btnVerify;
    @BindView(R.id.timer)
    AnyTextView timer;
    @BindView(R.id.time_layout)
    LinearLayout timeLayout;
    @BindView(R.id.ResendPassword)
    AnyTextView ResendPassword;
    @BindView(R.id.contParentLayout)
    RelativeLayout contParentLayout;
    CountDownTimer countDownTimer;
    Call<WebResponse<Object>> webCall;

    public static VerifyFragment newInstance() {

        Bundle args = new Bundle();
        VerifyFragment fragment = new VerifyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getDrawerLockMode() {
        return 0;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_verifycode;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        countDownTimer = new CountDownTimer(20000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("Resend verification code in 00: " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                timer.setText("Resend verification code in 00:00");
                ResendPassword.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();

    }

    @OnClick({R.id.btnBack, R.id.btn_verify, R.id.ResendPassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_verify:
                verifycodeAPI();
                break;
            case R.id.ResendPassword:
                forgotPasswordAPI();
                break;
        }
    }


    /**
     * this method execute when user click resend code
     */

    private void forgotPasswordAPI() {

        String txtEmail;
        txtEmail = sharedPreferenceManager.getString(AppConstants.KEY_CURRENT_USER_EMAIL);

        // key , fileTypeValue
        Map<String, Object> query = new HashMap<>();
        query.put(WebServiceConstants.Q_PARAM_EMAIL, txtEmail);

        webCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_FORGET_PASSWORD, query, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UIHelper.showToast(getContext(), webResponse.message);
            }

            @Override
            public void onError(Object object) {

            }
        });

    }

    /**
     * this method execute to verify code
     */

    private void verifycodeAPI() {

        ResetPasswordSendingModel resetPasswordSendingModel = new ResetPasswordSendingModel();
        resetPasswordSendingModel.setVerificationCode(edtCode.getStringTrimmed());

        sharedPreferenceManager.putValue(AppConstants.KEY_CODE, edtCode.getStringTrimmed());

        getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_VERIFY_RESET_CODE, resetPasswordSendingModel.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UIHelper.showToast(getContext(), webResponse.message);
                getBaseActivity().addDockableFragment(ResetPasswordFragment.newInstance(), true);
            }

            @Override
            public void onError(Object object) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        if (webCall != null) {
            webCall.cancel();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
