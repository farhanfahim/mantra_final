package com.tekrevol.mantra.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.fragments.abstracts.GenericContentFragment;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.receiving_model.Slug;
import com.tekrevol.mantra.models.sending_model.LoginSendingModel;
import com.tekrevol.mantra.models.wrappers.UserModelWrapper;
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

/**
 * Created by hamza.ahmed on 7/19/2018.
 */

public class LoginFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.txt_username)
    AnyEditTextView txtUsername;
    @BindView(R.id.txt_password)
    AnyEditTextView txtPassword;
    @BindView(R.id.txt_forgot)
    TextView txtForgot;
    @BindView(R.id.checkbox)
    ImageButton checkbox;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.btn_facebook)
    ImageButton btnFacebook;
    @BindView(R.id.txt_signup)
    TextView txtSignup;
    @BindView(R.id.contParentLayout)
    RelativeLayout contParentLayout;
    @BindView(R.id.btnBack)
    LinearLayout btnBack;
    @BindView(R.id.uncheckbox)
    ImageButton uncheckbox;
    @BindView(R.id.checked)
    CheckBox checked;
    @BindView(R.id.contTermsAndConditions)
    AnyTextView contTermsAndConditions;
    private Boolean isChecked;
    private Boolean saveLogin;
    Call<WebResponse<Object>> webCall;
    Call<WebResponse<Object>> call;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;


    public static LoginFragment newInstance() {
        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getDrawerLockMode() {
        return 0;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_login_v2;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.GONE);
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", getContext().MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        if (webCall != null) {
            webCall.cancel();
        }
        if (call != null) {
            call.cancel();
        }

        unbinder.unbind();


        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.txt_forgot, R.id.checkbox, R.id.btn_login, R.id.btn_facebook, R.id.uncheckbox, R.id.contTermsAndConditions, R.id.txt_signup, R.id.btnBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_forgot:
                getBaseActivity().addDockableFragment(ForgotFragment.newInstance(), true);
                break;
            case R.id.checkbox:
                checkbox.setVisibility(View.GONE);
                uncheckbox.setVisibility(View.VISIBLE);
                isChecked = false;
                break;
            case R.id.contTermsAndConditions:
                Map<String, Object> queryMap = new HashMap<>();

                call = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_PAGES + "/" + AppConstants.KEY_TERMS, queryMap, new WebServices.IRequestWebResponseAnyObjectCallBack() {
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
                break;
            case R.id.uncheckbox:
                uncheckbox.setVisibility(View.GONE);
                checkbox.setVisibility(View.VISIBLE);
                isChecked = true;
                break;
            case R.id.btnBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_login:
                loginUpAPI();
                break;
            case R.id.btn_facebook:
                loginFacebookAPI();
                break;
            case R.id.txt_signup:
                getBaseActivity().popBackStack();
                getBaseActivity().addDockableFragment(SignupFragment.newInstance(), true);
                break;
        }
    }

    private void saveUserInfo() {

        loginPrefsEditor.putBoolean("saveLogin", true);
        loginPrefsEditor.putString("username", txtUsername.getStringTrimmed());
        loginPrefsEditor.putString("password", txtPassword.getStringTrimmed());
        loginPrefsEditor.commit();
    }


    /**
     * this method is used when social login
     */
    private void loginFacebookAPI() {
        getMainActivity().fbSignIn();
    }

    /**
     * this method is used when login with username and pass
     */
    private void loginUpAPI() {

        if (txtUsername.testValidity() && txtPassword.testValidity()) {


            if (!checked.isChecked()) {
                UIHelper.showAlertDialog(getContext(), "Please accept Term of Use");
                return;
            }
            LoginSendingModel loginSendingModel = new LoginSendingModel();
            loginSendingModel.setEmail(txtUsername.getStringTrimmed());
            loginSendingModel.setDeviceToken("abc");
            loginSendingModel.setDeviceType(AppConstants.DEVICE_OS_ANDROID);
            loginSendingModel.setPassword(txtPassword.getStringTrimmed());

            webCall = getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_LOGIN, loginSendingModel.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
                @Override
                public void requestDataResponse(WebResponse<Object> webResponse) {

                    UIHelper.showToast(getContext(), webResponse.message);
                    UserModelWrapper userModelWrapper = getGson().fromJson(getGson().toJson(webResponse.result), UserModelWrapper.class);
                    getBaseActivity().onSuccessfullLogin(userModelWrapper.getUser());
                }

                @Override
                public void onError(Object object) {

                }
            });


        }
    }

}
