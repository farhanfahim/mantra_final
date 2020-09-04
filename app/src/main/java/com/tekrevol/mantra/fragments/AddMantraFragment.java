package com.tekrevol.mantra.fragments;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.enums.FragmentName;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.RunTimePermissions;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.widget.AnyEditTextView;
import com.tekrevol.mantra.widget.TitleBar;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class AddMantraFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.img_continue)
    ImageView imgContinue;
    @BindView(R.id.pulsator)
    PulsatorLayout pulsator;
    @BindView(R.id.pulsator1)
    PulsatorLayout pulsator1;
    @BindView(R.id.pulsator2)
    PulsatorLayout pulsator2;
    @BindView(R.id.playBtn)
    ImageView playBtn;
    @BindView(R.id.playdisabvleBtn)
    ImageView playdisabvleBtn;
    @BindView(R.id.record)
    ImageView record;
    @BindView(R.id.uncheckbox)
    ImageButton uncheckbox;
    @BindView(R.id.checkbox)
    ImageButton checkbox;
    @BindView(R.id.title)
    AnyEditTextView title;
    @BindView(R.id.timer)
    TextView timer;
    @BindView(R.id.pulsatorx)
    PulsatorLayout pulsatorx;
    @BindView(R.id.pulsatory)
    PulsatorLayout pulsatory;
    @BindView(R.id.pulsatorz)
    PulsatorLayout pulsatorz;
    @BindView(R.id.recorderAnimationImage)
    ImageView recorderAnimationImage;
    private int lastProgress = 0;
    @BindView(R.id.pause)
    ImageView pause;
    private String fileName = null;
    private MediaRecorder mRecorder;
    CountDownTimer countDownTimer;
    private int RECORD_AUDIO_REQUEST_CODE = 123;
    public static final int RECORD_AUDIO = 0;
    public static final int READ_PHONE_STATE = 1;
    public static final int WRITE_EXTERNAL_STORAGE = 2;
    public static final int READ_EXTERNAL_STORAGE = 3;

    private MediaPlayer mPlayer, mediaduration;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private boolean isPlaying = false;
    int fileTypeValue = AppConstants.FILE_TYPE_PUBLIC;
    String strtitle;
    public TextView timerTextView;
    private long startHTime = 0L;
    private boolean recording = false;
    int secs;

    public static AddMantraFragment newInstance() {

        Bundle args = new Bundle();
        AddMantraFragment fragment = new AddMantraFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        popup();
    }

    private void popup() {
        UIHelper.showAlertDialog("You can record your mantra for 30 sec.", "Mantra", getContext());
    }

    @Override
    public int getDrawerLockMode() {
        return 0;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.recording;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("Add Mantra");
        titleBar.showResideMenu(getHomeActivity());
        //titleBar.showSidebar(getBaseActivity());
        titleBar.showBackButton(getBaseActivity());

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

        if (mRecorder != null) {
            //    mRecorder.stop();
            mRecorder.release();
        }
        if (mPlayer != null) {
            try {
                mPlayer.release();
                mPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (customHandler != null) {
            customHandler.removeCallbacks(updateTimerThread);

        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        unbinder.unbind();
        super.onDestroyView();

    }

    @OnClick(R.id.img_continue)
    public void onViewClicked() {

        if (recording == true) {
            save();
        }
    }

    private void save() {

        if (title.testValidity() && (fileName != null)) {
            strtitle = title.getStringTrimmed();
            Log.d("seconds", String.valueOf(secs));
            getBaseActivity().addDockableFragment(MantraDetailFragment.newInstance(FragmentName.AddMantraFragment, fileTypeValue, strtitle, fileName, secs), true);
        }
    }

    @OnClick({R.id.playBtn, R.id.playdisabvleBtn, R.id.record, R.id.pause, R.id.uncheckbox, R.id.checkbox})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.playBtn:
                isPlaying = false;
                stopPlaying();
                break;
            case R.id.playdisabvleBtn:
                if (recording == true) {
                    //  recording = false;
                    if (!isPlaying && fileName != null) {
                        isPlaying = true;
                        //playRecording();
                        disablePlayButton();
                        startPlaying();
                    } else {
                        isPlaying = false;
                        //disablePlayButton();
                        stopPlaying();
                    }
                }

                //disablePlayButton();
                break;
            case R.id.record:
//                if (ActivityCompat.checkSelfPermission(getHomeActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//
//                    ActivityCompat.requestPermissions(getHomeActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
//                            RECORD_AUDIO);

//                }


                if (RunTimePermissions.isAllPermissionGiven(getContext(), getBaseActivity(), true)) {
                    isPlaying = false;
                    stopPlaying();
                    onRecord();
                    startRecording();
                }
                break;
            case R.id.pause:
                onRecordPause();
                stopRecording();
                break;
            case R.id.uncheckbox:
                disableUncheck();
                break;
            case R.id.checkbox:
                disableCheck();
                break;
        }
    }

    private void disableCheck() {
        checkbox.setVisibility(View.GONE);
        uncheckbox.setVisibility(View.VISIBLE);
        fileTypeValue = AppConstants.FILE_TYPE_PUBLIC;

    }

    private void disableUncheck() {
        uncheckbox.setVisibility(View.GONE);
        checkbox.setVisibility(View.VISIBLE);
        fileTypeValue = AppConstants.FILE_TYPE_PRIVATE;
    }


    /**
     * this method change icon
     */
    private void disablePlayButton() {
        recorderAnimationImage.setVisibility(View.GONE);
        playBtn.setVisibility(View.VISIBLE);
        playdisabvleBtn.setVisibility(View.GONE);
    }

    /**
     * this method execute when the user need to listen recording
     */
    private void playRecording() {
        recorderAnimationImage.setVisibility(View.VISIBLE);
        pulsatorx.setVisibility(View.GONE);
        pulsatory.setVisibility(View.GONE);
        pulsatorz.setVisibility(View.GONE);
        playBtn.setVisibility(View.GONE);
        playdisabvleBtn.setVisibility(View.VISIBLE);
    }

    private void startPlaying() {
        pulsatorx.start();
        pulsatory.start();
        pulsatorz.start();
        pulsatorx.setVisibility(View.VISIBLE);
        pulsatory.setVisibility(View.VISIBLE);
        pulsatorz.setVisibility(View.VISIBLE);
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            Log.d("coundown seconds", String.valueOf(secs));
            CountDown(secs);
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // imageViewPlay.setImageResource(R.drawable.ic_play);
                    //disablePlayButton();
                    playRecording();
                    isPlaying = false;
                }
            });

        } catch (Exception e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button
        //   imageViewPlay.setImageResource(R.drawable.ic_pause);

     /*   seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( mPlayer!=null && fromUser ){
                    mPlayer.seekTo(progress);
                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
    }

    /**
     * this method change play icon, and pause animation
     */

    private void onRecordPause() {
        recording = true;
        pause.setVisibility(View.GONE);
        pulsator.setVisibility(View.GONE);
        pulsator1.setVisibility(View.GONE);
        pulsator2.setVisibility(View.GONE);
        record.setVisibility(View.VISIBLE);
    }

    private void onRecord() {

        recording = false;
        record.setVisibility(View.GONE);
        pulsator.start();
        pulsator1.start();
        pulsator2.start();
        pulsator.setVisibility(View.VISIBLE);
        pulsator1.setVisibility(View.VISIBLE);
        pulsator2.setVisibility(View.VISIBLE);
        pause.setVisibility(View.VISIBLE);
    }

    /**
     * execute when person start recording
     */

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        File root = Environment.getExternalStorageDirectory();
        // File file = new File(root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios");
        File file = new File(root.getAbsolutePath() + "/VoiceRecorderMantra/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }

        fileName = root.getAbsolutePath() + "/VoiceRecorderMantra/Audios/" + String.valueOf(System.currentTimeMillis() + ".mp3");
        // fileName = root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" + String.valueOf(System.currentTimeMillis() + ".mp3");
        Log.d("filename", fileName);

        mRecorder.setOutputFile(fileName);


        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //lastProgress = 0;
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        //stopPlaying();

        //    seekBar.setProgress(0);
        //   stopPlaying();
        // making the imageview a stop button
        //starting the chronometer
      /*  chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();*/
    }


    /**
     * timer is manage here, recording should not be > 15 secs
     */

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedTime / 1000);
            if (secs < 30) {
                int mins = secs / 60;
                if (secs < 30) {
                    secs = secs % 60;
                }
                //int milliseconds = (int) (updatedTime % 1000);
                timer.setText("" + mins + ":"
                        + String.format("%02d", secs));
                customHandler.postDelayed(this, 0);
            } else {
                timer.setText("0:30");
                timeSwapBuff += timeInMilliseconds;
              /*  long seconds = TimeUnit.MILLISECONDS.toSeconds(timeSwapBuff);
                Log.d("duration----------", String.valueOf(seconds));
                System.out.println(String.valueOf("-------------"+String.valueOf(seconds)));*/
                customHandler.removeCallbacks(updateTimerThread);
                onRecordPause();
                stopRecording();
            }
        }

    };


    /**
     * this method use for stopping playing user recording
     */
    private void stopPlaying() {

        timer.setText("00:00");
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        recording = true;
        try {

            mPlayer.release();
            mPlayer = null;
            playRecording();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //showing the play button
        // disablePlayButton();

    }

    /**
     * this method stop from recording
     */

    private void CountDown(int timeInMilliseconds) {
        countDownTimer = new CountDownTimer(timeInMilliseconds * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (timer != null) {
                    timer.setText("00: " + millisUntilFinished / 1000);
                }

            }

            public void onFinish() {
                if (timer != null) {
                    timer.setText("00:00");
                }


            }
        };
        countDownTimer.start();
    }


    private void stopRecording() {
        try {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;

            startTime = 0L;
            timeInMilliseconds = 0L;
            timeSwapBuff = 0L;
            updatedTime = 0L;

            customHandler.removeCallbacks(updateTimerThread);
            UIHelper.showToast(getContext(), "Recording saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
