package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.sending_model.FeedbackModel;
import com.tekrevol.mantra.models.sending_model.UserAction;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.AnyEditTextView;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

import static com.tekrevol.mantra.constatnts.WebServiceConstants.PATH_ACTIONS;
import static com.tekrevol.mantra.constatnts.WebServiceConstants.PATH_FEEDBACK;

public class FeedbackFragment extends BaseFragment {
    @BindView(R.id.btnRadio)
    RadioGroup btnRadio;
    @BindView(R.id.edtComment)
    AnyEditTextView edtComment;
    @BindView(R.id.btnSubmit)
    AnyTextView btnSubmit;
    @BindView(R.id.save)
    LinearLayout save;
    private RadioButton radioButton;
    Call<WebResponse<Object>> call;


    @Override
    public int getDrawerLockMode() {
        return 0;
    }

    public static FeedbackFragment newInstance() {

        Bundle args = new Bundle();
        FeedbackFragment fragment = new FeedbackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_feedback;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("Share Your Response");
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());
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

    @OnClick(R.id.btnSubmit)
    public void onViewClicked() {
        feedbackApi();
    }

    private void feedbackApi() {
        if (btnRadio.getCheckedRadioButtonId() == -1) {
            UIHelper.showAlertDialog(getContext(), "Please select from above expectation");
            return;
        }
        if (!edtComment.testValidity()) {
            UIHelper.showAlertDialog(getContext(), "Please enter your feedback");
            return;
        }

        int selectedId = btnRadio.getCheckedRadioButtonId();
        radioButton = (RadioButton) view.findViewById(selectedId);
        FeedbackModel userAction = new FeedbackModel();
        userAction.setUser_id(getCurrentUser().getId());

        if (selectedId == 1) {
            userAction.setExpectation_level(AppConstants.EXPECTATION_LEVEL_EXCEEDED);
        } else if (selectedId == 2) {
            userAction.setExpectation_level(AppConstants.EXPECTATION_LEVEL_MET);
        } else if (selectedId == 3) {
            userAction.setExpectation_level(AppConstants.EXPECTATION_LEVEL_BELOW);
        } else if (selectedId == 4) {
            userAction.setExpectation_level(AppConstants.EXPECTATION_LEVEL_NONE);
        }
        userAction.setFeedback(edtComment.getText().toString());

        call = getBaseWebServices(true).postAPIAnyObject(PATH_FEEDBACK, userAction.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UIHelper.showToast(getContext(), webResponse.message);
                getBaseActivity().popBackStack();
            }

            @Override
            public void onError(Object object) {

            }
        });


    }

    @Override
    public void onDestroyView() {
        if (call != null) {
            call.cancel();
        }
        super.onDestroyView();
    }
}
