package com.tekrevol.mantra.fragments.dialogs;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tekrevol.mantra.adapters.SpinnerDialogAdapter;
import com.tekrevol.mantra.helperclasses.ui.helper.KeyboardHelper;
import com.tekrevol.mantra.models.SpinnerModel;
import com.tekrevol.mantra.widget.AnyEditTextView;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.recyclerview_layout.CustomLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.tekrevol.mantra.R;

import com.tekrevol.mantra.callbacks.OnSpinnerItemClickListener;
import com.tekrevol.mantra.callbacks.OnSpinnerOKPressedListener;

import info.hoang8f.widget.FButton;


/**
 * Created by khanhamza on 21-Feb-17.
 */

public class SpinnerDialogFragment extends DialogFragment {

    String title;
    Unbinder unbinder;
    SpinnerDialogAdapter adapter;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.txtOK)
    FButton txtOK;
//    @BindView(R.id.edtSearchBar)
//    AnyEditTextView edtSearchBar;
//    @BindView(R.id.imgSearch)
//    ImageView imgSearch;
//    @BindView(R.id.contSearchBar)
//    LinearLayout contSearchBar;


    private ArrayList<SpinnerModel> arrData;

    private OnSpinnerItemClickListener onItemClickListener;
    private OnSpinnerOKPressedListener onSpinnerOKPressedListener;
    private int scrollToPosition;

    public SpinnerDialogFragment() {
    }

    public static SpinnerDialogFragment newInstance(String title, ArrayList<SpinnerModel> arrData,
                                                    OnSpinnerItemClickListener onItemClickListener, OnSpinnerOKPressedListener onSpinnerOKPressedListener, int scrollToPosition) {
        SpinnerDialogFragment frag = new SpinnerDialogFragment();

        Bundle args = new Bundle();
        frag.title = title;
        frag.arrData = arrData;
        frag.onItemClickListener = onItemClickListener;
        frag.scrollToPosition = scrollToPosition;
        frag.onSpinnerOKPressedListener = onSpinnerOKPressedListener;
        frag.setArguments(args);

        return frag;
    }


    @Override
    public void onStart() {
        super.onStart();
//        getDialog().getWindow()
//                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                        getResources().getDimensionPixelSize(R.dimen.x400dp));


        getDialog().getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spinner_popup, container);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KeyboardHelper.hideSoftKeyboard(getContext(), view);
        setListeners();
        adapter = new SpinnerDialogAdapter(getActivity(), arrData, onItemClickListener);
        adapter.notifyDataSetChanged();
        txtTitle.setText(title);

        bindView();
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void setListeners() {
//        edtSearchBar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                adapter.getFilter().filter(charSequence);
////                if (edtSearchBar.getStringTrimmed().length() == 0) {
//////                    imgClose.setVisibility(View.GONE);
////                } else {
//////                    imgClose.setVisibility(View.VISIBLE);
////                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

    private void bindView() {
        RecyclerView.LayoutManager mLayoutManager = new CustomLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(mLayoutManager);
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        int resId = R.anim.layout_animation_fall_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setAdapter(adapter);
        scrollToPosition(scrollToPosition);

    }

    public void scrollToPosition(int scrollToPosition) {
        if (scrollToPosition > -1) {
            recyclerView.scrollToPosition(scrollToPosition);
        } else {
            recyclerView.scrollToPosition(0);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.txtOK})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txtOK:
                if (onSpinnerOKPressedListener != null) {
                    for (SpinnerModel arrDatum : arrData) {
                        if (arrDatum.isSelected()) {
                            onSpinnerOKPressedListener.onItemSelect(arrDatum);
                            this.dismiss();
                            return;
                        }
                    }
                }
                this.dismiss();
                break;
        }
    }
}
