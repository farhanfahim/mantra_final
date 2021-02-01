package com.tekrevol.mantra.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.activities.AlarmActivity;
import com.tekrevol.mantra.activities.HomeActivity;
import com.tekrevol.mantra.enums.FragmentName;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;

public class AlarmSelectionFragment extends BaseFragment {
    @BindView(R.id.txtMantraTitle)
    AnyTextView txtMantraTitle;
    @BindView(R.id.txtDescription)
    AnyTextView txtDescription;
    @BindView(R.id.txtReminderNote)
    AnyTextView txtReminderNote;
    @BindView(R.id.imgReject)
    ImageView imgReject;
    @BindView(R.id.imgAccept)
    ImageView imgAccept;
    private MediaModel mediaModel;
    public static AlarmSelectionFragment newInstance(MediaModel mediaModel) {

        Bundle args = new Bundle();

        AlarmSelectionFragment fragment = new AlarmSelectionFragment();
        fragment.mediaModel = mediaModel;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getDrawerLockMode() {
        return 0;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_alarm_selection;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(GONE);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!(mediaModel.getName() == null)) {
            txtMantraTitle.setText(mediaModel.getName());

        } else {
            txtMantraTitle.setText("");
        }
        if (!(mediaModel.getDescription() == null)) {
            txtDescription.setText(mediaModel.getDescription());

        } else {
            txtDescription.setText("");
        }
        if (!(mediaModel.getReminderText() == null)) {
            txtReminderNote.setText(mediaModel.getReminderText());

        } else {
            txtReminderNote.setText("");
        }


    }

    @OnClick({R.id.imgReject, R.id.imgAccept})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgReject:
                if (getActivity() instanceof AlarmActivity) {
                    ((AlarmActivity) getActivity()).stopRingtone();
                    Intent i = new Intent(getContext(), HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    getBaseActivity().finish();
                }

                break;
            case R.id.imgAccept:
                if (getActivity() instanceof AlarmActivity) {
                    ((AlarmActivity) getActivity()).stopRingtone();
                    getBaseActivity().popBackStack();
                    getBaseActivity().addDockableFragment(DetailFragment.newInstance(mediaModel, FragmentName.AlarmSelectionFragment), false);
                }


                break;
        }
    }
}
