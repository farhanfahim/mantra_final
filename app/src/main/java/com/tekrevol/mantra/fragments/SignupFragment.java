package com.tekrevol.mantra.fragments;

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

import androidx.annotation.Nullable;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.fragments.abstracts.GenericContentFragment;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.helperclasses.validator.PasswordValidation;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.receiving_model.Slug;
import com.tekrevol.mantra.models.sending_model.SignupSendingModel;
import com.tekrevol.mantra.models.wrappers.UserModelWrapper;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.AnyEditTextView;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class SignupFragment extends BaseFragment {


    @BindView(R.id.btnBack)
    LinearLayout btnBack;
    @BindView(R.id.btn_signup)
    TextView btnSignup;
    @BindView(R.id.txtLogin)
    TextView txtLogin;
    Unbinder unbinder;
    @BindView(R.id.txt_username)
    AnyEditTextView txtUsername;
    @BindView(R.id.txt_email)
    AnyEditTextView txtEmail;
    @BindView(R.id.txt_password)
    AnyEditTextView txtPassword;
    @BindView(R.id.txt_confirm_password)
    AnyEditTextView txtConfirmPassword;
    @BindView(R.id.btn_facebook)
    ImageButton btnFacebook;
    Call<WebResponse<Object>> webCall;
    @BindView(R.id.checked)
    CheckBox checked;
    @BindView(R.id.contTermsAndConditions)
    AnyTextView contTermsAndConditions;
    @BindView(R.id.contParentLayout)
    RelativeLayout contParentLayout;
    Call<WebResponse<Object>> call;
    private File fileTemporaryProfilePicture;

    public static SignupFragment newInstance() {

        Bundle args = new Bundle();

        SignupFragment fragment = new SignupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getDrawerLockMode() {
        return 0;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_signup;
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

        txtPassword.addValidator(new PasswordValidation(txtConfirmPassword));
    }


    @OnClick({R.id.btnBack, R.id.contTermsAndConditions, R.id.btn_signup, R.id.txtLogin, R.id.btn_facebook})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_signup:
                /*getBaseActivity().finish();
                getBaseActivity().openActivity(HomeActivity.class);*/
                signUpAPI();
                break;
            case R.id.txtLogin:
                getBaseActivity().popBackStack();
                getBaseActivity().addDockableFragment(LoginFragment.newInstance(), true);
                break;
            case R.id.btn_facebook:
                getMainActivity().fbSignIn();
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
        }
    }

    /**
     * this method is used when signup with username and pass
     */

    private void signUpAPI() {

        if (txtUsername.testValidity() && txtEmail.testValidity() && txtPassword.testValidity() && txtConfirmPassword.testValidity()) {

            if (!checked.isChecked()) {
                UIHelper.showAlertDialog(getContext(), "Please accept Term of Use");
                return;
            }

            SignupSendingModel signupSendingModel = new SignupSendingModel();
            signupSendingModel.setDeviceToken("abc");
            signupSendingModel.setDeviceType(AppConstants.DEVICE_OS_ANDROID);
            signupSendingModel.setEmail(txtEmail.getStringTrimmed());
            signupSendingModel.setName(txtUsername.getStringTrimmed());
            signupSendingModel.setPassword(txtPassword.getStringTrimmed());
            signupSendingModel.setPasswordConfirmation(txtConfirmPassword.getStringTrimmed());

            webCall = getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_REGISTER, signupSendingModel.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
                @Override
                public void requestDataResponse(WebResponse<Object> webResponse) {
                    UIHelper.showToast(getContext(), webResponse.message);

                    UserModelWrapper userModelWrapper = getGson().fromJson(getGson().toJson(webResponse.result), UserModelWrapper.class);
                    getBaseActivity().onSuccessfullLogin(userModelWrapper.getUser());

//                   sharedPreferenceManager.putObject(AppConstants.KEY_CURRENT_USER_MODEL,userModelWrapper.getUser() );
//                   sharedPreferenceManager.putValue(AppConstants.KEY_CURRENT_USER_ID, userModelWrapper.getUser().getId());
//                   sharedPreferenceManager.putValue(AppConstants.KEY_TOKEN,userModelWrapper.getUser().getAccessToken());
//
//                   getBaseActivity().openActivity(HomeActivity.class);
//                   getBaseActivity().finish();
                }

                @Override
                public void onError(Object object) {

                }
            });
        }

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
}
