package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.helperclasses.validator.PasswordValidation;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.sending_model.ChangePasswordSendingModel;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.AnyEditTextView;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class ChangePasswordFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.btnBack)
    LinearLayout btnBack;
    @BindView(R.id.currentpass)
    AnyEditTextView currentpass;
    @BindView(R.id.edtNewpassword)
    AnyEditTextView edtNewpassword;
    @BindView(R.id.edtCnfrmpassword)
    AnyEditTextView edtCnfrmpassword;
    @BindView(R.id.btn_reset)
    AnyTextView btnReset;
    @BindView(R.id.contParentLayout)
    RelativeLayout contParentLayout;

    Call<WebResponse<Object>> webCall;

    public static ChangePasswordFragment newInstance() {

        Bundle args = new Bundle();
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getDrawerLockMode() {
        return 0;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_changepassword;
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

        edtCnfrmpassword.addValidator(new PasswordValidation(edtNewpassword));

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



    @OnClick({R.id.btnBack, R.id.btn_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_reset:
                changepasswordAPI();
                break;
        }
    }

    private void changepasswordAPI() {

        if (!currentpass.testValidity()) {
            UIHelper.showAlertDialog(getContext(), "Please enter valid current password");
            return;
        }
        if (!edtNewpassword.testValidity()) {
            UIHelper.showAlertDialog(getContext(), "Please enter valid new password");
            return;
        }
        if (!edtCnfrmpassword.testValidity()) {
            UIHelper.showAlertDialog(getContext(), "Please enter valid confirm password");
            return;
        }

        ChangePasswordSendingModel changePasswordSendingModel = new ChangePasswordSendingModel();
        changePasswordSendingModel.setAuthorization(getToken());
        changePasswordSendingModel.setCurrentPassword(currentpass.getStringTrimmed());
        changePasswordSendingModel.setPassword(edtNewpassword.getStringTrimmed());
        changePasswordSendingModel.setPasswordConfirmation(edtCnfrmpassword.getStringTrimmed());

        webCall = getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_CHANGE_PASSWORD, changePasswordSendingModel.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UIHelper.showToast(getContext(),webResponse.message);
                getActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onError(Object object) {

            }
        });
    }
}
