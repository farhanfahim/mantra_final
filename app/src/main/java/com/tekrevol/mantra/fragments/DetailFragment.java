package com.tekrevol.mantra.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.enums.FragmentName;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.Helper;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.libraries.imageloader.ImageLoaderHelper;
import com.tekrevol.mantra.managers.DateManager;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.models.sending_model.UserAction;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;
import com.uzairiqbal.circulartimerview.CircularTimerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

import static com.tekrevol.mantra.constatnts.WebServiceConstants.PATH_ACTIONS;

public class DetailFragment extends BaseFragment implements OnItemClickListener {

    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.btnRepeat)
    ImageButton btnRepeat;
    @BindView(R.id.imgCategory)
    ImageView imgCategory;
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Handler customHandler = new Handler();
    @BindView(R.id.iconbgcolor)
    RoundKornerLinearLayout iconbgcolor;
    @BindView(R.id.progress_circular)
    CircularTimerView progressCircular;
    private MediaPlayer mediaPlayer;
    Unbinder unbinder;
    @BindView(R.id.cont_play)
    RelativeLayout contPlay;
    @BindView(R.id.layout1)
    RelativeLayout layout1;
    @BindView(R.id.layout2)
    RelativeLayout layout2;
    CountDownTimer countDownTimer;
    int i = 0;
    Call<WebResponse<Object>> favCall, likeCall, shareCall, unfavCall, unlikeCall;
    String date;
    MediaModel mediaModel;
    private String datee;
    @BindView(R.id.txttitle)
    AnyTextView txttitle;
    @BindView(R.id.btnfav)
    ImageButton btnfav;
    @BindView(R.id.btnunfav)
    ImageButton btnunfav;
    @BindView(R.id.btnplay)
    ImageView btnplay;
    @BindView(R.id.btnstop)
    ImageView btnstop;
    @BindView(R.id.txtcategories_name)
    AnyTextView txtcategoriesName;
    @BindView(R.id.txttime)
    AnyTextView txttime;
    @BindView(R.id.btnlike)
    ImageButton btnlike;
    @BindView(R.id.btnunlike)
    ImageButton btnunlike;
    @BindView(R.id.txtcreateddate)
    AnyTextView txtcreateddate;
    @BindView(R.id.txtdes)
    AnyTextView txtdes;
    @BindView(R.id.description)
    LinearLayout description;
    @BindView(R.id.sharebtn)
    ImageView sharebtn;
    private ArrayList<MediaModel> arrMovieLines;
    private FragmentName fragmentName;
    private Boolean bool = false;

    public static DetailFragment newInstance(MediaModel model) {

        Bundle args = new Bundle();
        DetailFragment fragment = new DetailFragment();
        fragment.mediaModel = model;
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailFragment newInstance(MediaModel mediaModel, FragmentName alarmSelectionFragment) {

        Bundle args = new Bundle();
        DetailFragment fragment = new DetailFragment();
        fragment.mediaModel = mediaModel;
        fragment.fragmentName = alarmSelectionFragment;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_detail;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("");
        titleBar.showBackButton(getBaseActivity());

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        bindData();

    }

    private void bindData() {
        progressCircular.setProgress(0);
        //seekbar.setEnabled(false);
        String myStrDate = mediaModel.getCreatedAt();
        String[] splitStr = myStrDate.split("\\s+");
        String datesplit = splitStr[0];
        System.out.println("Date " + datesplit);

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d = format.parse(datesplit);
            SimpleDateFormat serverFormat = new SimpleDateFormat(AppConstants.FORMAT_DATE_SHOW);
            datee = serverFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtcreateddate.setText(datee);

        txttitle.setText(mediaModel.getName());
        txtdes.setText(mediaModel.getDescription());
        date = DateManager.ConvertSecondToHHMMString(mediaModel.getMediaLength());
        txttime.setText("00:00/" + date);

        if (mediaModel.getImage() != null) {
            ImageLoaderHelper.loadImageWithouAnimation(imgCategory, mediaModel.getImageUrl(), false);
        } else if (mediaModel.getCategory() != null) {
            ImageLoaderHelper.loadImageWithouAnimation(imgCategory, mediaModel.getCategory().getImageUrl(), false);

        }

        if (mediaModel.getCategory() != null) {
            txtcategoriesName.setText(mediaModel.getCategory().getName());
            ImageLoaderHelper.loadImageWithAnimations(imgIcon, mediaModel.getCategory().getIconImageUrl(), false);

        } else {
            txtcategoriesName.setText("-");
        }
        if (mediaModel.getIsLike() == 0) {
            btnlike.setVisibility(View.GONE);
            btnunlike.setVisibility(View.GONE);
        } else {
            btnunlike.setVisibility(View.GONE);
            btnlike.setVisibility(View.GONE);
        }
        if (mediaModel.getIsFavourite() == 0) {
            btnfav.setVisibility(View.GONE);
            btnunfav.setVisibility(View.VISIBLE);
        } else {
            btnunfav.setVisibility(View.GONE);
            btnfav.setVisibility(View.VISIBLE);
        }
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            if (secs < mediaModel.getMediaLength()) {
                int mins = secs / 60;
                secs = secs % 60;

                //int milliseconds = (int) (updatedTime % 1000);
                txttime.setText("" + mins + ":"
                        + String.format("%02d", secs) + "/" + date);
                customHandler.postDelayed(this, 0);
            } else {
                txttime.setText("00:00/" + date);
                timeSwapBuff += timeInMilliseconds;
                startTime = 0L;
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updatedTime = 0L;
                customHandler.removeCallbacks(updateTimerThread);
            }
        }

    };


    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.drawable.barcelona_logo:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onItemClick(int position, Object object, View view, String adapterName) {

    }

    @OnClick({R.id.btnfav, R.id.btnunfav, R.id.btnplay, R.id.btnstop, R.id.btnlike, R.id.btnunlike, R.id.btnRepeat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnfav:
                favAPI();
                break;
            case R.id.btnunfav:
                unFavAPI();
                break;
            case R.id.btnRepeat:
                if (bool) {
                    btnRepeat.setColorFilter(getResources().getColor(R.color.txtGrey));
                    bool = false;
                } else {
                    btnRepeat.setColorFilter(getResources().getColor(R.color.colorAccent));
                    bool = true;
                }
                break;
            case R.id.btnplay:
                player();
                break;
            case R.id.btnstop:
                stopPlayer();
                progressCircular.setProgress(0);
                startTime = 0L;
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updatedTime = 0L;
                txttime.setText("00:00/" + date);
                customHandler.removeCallbacks(updateTimerThread);

                break;
            case R.id.btnlike:
                likeAPI();
                break;
            case R.id.btnunlike:
                unLikeAPI();
                break;
        }
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        btnplay.setVisibility(View.VISIBLE);
        btnstop.setVisibility(View.GONE);
        btnfav.setEnabled(true);
        btnRepeat.setClickable(true);
    }

    private void player() {
        btnfav.setEnabled(false);
        btnRepeat.setClickable(false);
        btnplay.setVisibility(View.GONE);
        btnstop.setVisibility(View.VISIBLE);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mediaModel.getFileAbsoluteUrl());
            /*mediaPlayer.prepare();
            mediaPlayer.start();*/
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    progressCircular.setMaxValue(mediaPlayer.getDuration());
                    //  seekbar.setEnabled(true);
                    progressCircular.setProgress(0);
                    updateSeekBar();
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                }
            });
            if (bool) {
                mediaPlayer.setLooping(true);
            } else {
                mediaPlayer.setOnCompletionListener(mp -> {
                    stopPlayer();
                    progressCircular.setProgress(0);
                    startTime = 0L;
                    timeInMilliseconds = 0L;
                    timeSwapBuff = 0L;
                    updatedTime = 0L;
                    txttime.setText("00:00/" + date);
                    customHandler.removeCallbacks(updateTimerThread);
                    //     seekbar.setEnabled(false);
                });
            }
            //CountDown(mediaModel.getMediaLength());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateSeekBar() {
        try {
            progressCircular.setProgress(mediaPlayer.getCurrentPosition());
            progressCircular.postDelayed(runnable, 50);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };

    private void likeAPI() {

        UserAction userAction = new UserAction();
        userAction.setActionType(AppConstants.ACTION_LIKE);
        userAction.setMediaId(mediaModel.getId());
        likeCall = getBaseWebServices(true).postAPIAnyObject(PATH_ACTIONS, userAction.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UIHelper.showToast(getContext(), webResponse.message);
                btnlike.setVisibility(View.GONE);
                btnunlike.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Object object) {

            }
        });

    }

    private void unLikeAPI() {

        UserAction userAction = new UserAction();
        userAction.setActionType(AppConstants.ACTION_UNLIKE);
        userAction.setMediaId(mediaModel.getId());
        unlikeCall = getBaseWebServices(true).postAPIAnyObject(PATH_ACTIONS, userAction.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UIHelper.showToast(getContext(), webResponse.message);
                btnunlike.setVisibility(View.GONE);
                btnlike.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Object object) {

            }
        });

    }

    private void favAPI() {

        UserAction userAction = new UserAction();
        userAction.setActionType(AppConstants.ACTION_FAV);
        userAction.setMediaId(mediaModel.getId());
        favCall = getBaseWebServices(true).postAPIAnyObject(PATH_ACTIONS, userAction.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                btnfav.setVisibility(View.GONE);
                btnunfav.setVisibility(View.VISIBLE);
                UIHelper.showToast(getContext(), webResponse.message);
            }

            @Override
            public void onError(Object object) {

            }
        });

    }

    private void unFavAPI() {

        UserAction userAction = new UserAction();
        userAction.setActionType(AppConstants.ACTION_UNFAV);
        userAction.setMediaId(mediaModel.getId());
        unfavCall = getBaseWebServices(true).postAPIAnyObject(PATH_ACTIONS, userAction.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                btnunfav.setVisibility(View.GONE);
                btnfav.setVisibility(View.VISIBLE);
                UIHelper.showToast(getContext(), webResponse.message);
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    @OnClick(R.id.sharebtn)
    public void onViewClicked() {


        UserAction userAction = new UserAction();
        userAction.setActionType(AppConstants.ACTION_SHARE);
        userAction.setMediaId(mediaModel.getId());
        shareCall = getBaseWebServices(true).postAPIAnyObject(PATH_ACTIONS, userAction.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {

                Helper.shareText(getContext(), mediaModel.getFileAbsoluteUrl());
            }

            @Override
            public void onError(Object object) {

            }
        });
    }


    @Override
    public void onDestroyView() {
        if (favCall != null) {
            favCall.cancel();
        }

        if (unfavCall != null) {
            unfavCall.cancel();
        }

        if (shareCall != null) {
            shareCall.cancel();
        }

        if (likeCall != null) {
            likeCall.cancel();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (unlikeCall != null) {
            unlikeCall.cancel();
        }
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (customHandler != null) {
            customHandler.removeCallbacks(updateTimerThread);

        }

        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
