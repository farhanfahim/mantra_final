package com.tekrevol.mantra.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
    RadioButton r1,r2,r3,r4;
    private RadioButton radioButton;
    Call<WebResponse<Object>> call;
    int selectedId = 0;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        r1 = view.findViewById(R.id.radioBtn1);
        r2 = view.findViewById(R.id.radioBtn2);
        r3 = view.findViewById(R.id.radioBtn3);
        r4 = view.findViewById(R.id.radioBtn4);

        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    selectedId = 1;
                }
            }
        });

        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    selectedId = 2;
                }
            }
        });

        r3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    selectedId = 3;
                }
            }
        });
        r4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    selectedId = 4;
                }
            }
        });

    }

    private void feedbackApi() {
        if (selectedId == 0) {
            UIHelper.showAlertDialog(getContext(), "Please select from above expectation");
            return;
        }
        if (!edtComment.testValidity()) {
            UIHelper.showAlertDialog(getContext(), "Please enter your feedback");
            return;
        }

        //selectedId = btnRadio.getCheckedRadioButtonId();
        //radioButton = (RadioButton) view.findViewById(selectedId);
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
