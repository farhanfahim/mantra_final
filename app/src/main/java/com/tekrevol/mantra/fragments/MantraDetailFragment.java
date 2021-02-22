package com.tekrevol.mantra.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.downloader.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.recyleradapters.DateAdapter;
import com.tekrevol.mantra.adapters.recyleradapters.ScheduleMantraAdapter;
import com.tekrevol.mantra.broadcast.AlarmReceiver;
import com.tekrevol.mantra.callbacks.OnCalendarUpdate;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.enums.FileType;
import com.tekrevol.mantra.enums.FragmentName;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.RunTimePermissions;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.managers.DateManager;
import com.tekrevol.mantra.managers.FileManager;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.managers.retrofit.entities.MultiFileModel;
import com.tekrevol.mantra.models.IntWrapper;
import com.tekrevol.mantra.models.SpinnerModel;
import com.tekrevol.mantra.models.database.AlarmModel;
import com.tekrevol.mantra.models.receiving_model.Categories;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.models.receiving_model.SubCategories;
import com.tekrevol.mantra.models.sending_model.UploadMedia;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.roomdatabase.DatabaseClient;
import com.tekrevol.mantra.widget.AnyEditTextView;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class MantraDetailFragment extends BaseFragment implements OnItemClickListener {

    Unbinder unbinder;
    Call<WebResponse<Object>> categoriesCall;
    Call<WebResponse<Object>> uploadMediaCall;
    @BindView(R.id.mantratitle)
    AnyEditTextView mantratitle;
    @BindView(R.id.txtmantracategory)
    AnyTextView txtmantracategory;
    @BindView(R.id.imgspinner)
    ImageView imgspinner;
    @BindView(R.id.txtnote)
    AnyEditTextView txtnote;
    @BindView(R.id.cbScheduledMantra)
    CheckBox cbScheduledMantra;
    @BindView(R.id.txtremindertitle)
    AnyEditTextView txtremindertitle;
    @BindView(R.id.addDate)
    ImageView addDate;
    @BindView(R.id.daterecyclerview)
    RecyclerView daterecyclerview;
    @BindView(R.id.layoutscheduled)
    LinearLayout layoutscheduled;
    @BindView(R.id.btn_login)
    AnyTextView btnLogin;
    @BindView(R.id.save)
    LinearLayout save;
    @BindView(R.id.txtmmediacategory)
    AnyTextView txtmmediacategory;
    @BindView(R.id.mmediacategory)
    LinearLayout mmediacategory;
    @BindView(R.id.mantracategory)
    LinearLayout mantracategory;
    @BindView(R.id.scheduleMantraLayout)
    LinearLayout scheduleMantraLayout;

    Call<WebResponse<Object>> webCall;
    private static String dirPath;
    private int downloadId;
    private String file = null;
    private String fileName, strtitle;
    private int fileTypeValue = 0;
    int secs;
    private ArrayList<SpinnerModel> spinnerModelArrayList = new ArrayList<>();
    private ArrayList<SubCategories> arrCategories = new ArrayList<>();
   //// private ObjectBoxManager objectBoxManager;
    private DateAdapter dateAdapter;
    private ArrayList<AlarmModel> arrDate;
    private FragmentName fragmentName;
    MediaModel mediaReminder;
    int idFromSpinner;
    File recordedMantra;
    private KProgressHUD mDialog;

    private Long locationClick = Long.valueOf(0);

    private Random random = new Random();

    public static MantraDetailFragment newInstance(MediaModel mediaReminder, FragmentName fragmentName, int fileTypeValue, String strtitle, String fileName, int secs) {

        Bundle args = new Bundle();
        MantraDetailFragment fragment = new MantraDetailFragment();
        fragment.fileTypeValue = fileTypeValue;
        fragment.strtitle = strtitle;
        fragment.secs = secs;
        fragment.mediaReminder = mediaReminder;
        fragment.fragmentName = fragmentName;
        fragment.fileName = fileName;
        fragment.setArguments(args);
        return fragment;
    }

    public static MantraDetailFragment newInstance(FragmentName fragmentName, int fileTypeValue, String strtitle, String fileName, int secs) {
        Bundle args = new Bundle();
        MantraDetailFragment fragment = new MantraDetailFragment();
        fragment.fileTypeValue = fileTypeValue;
        fragment.strtitle = strtitle;
        fragment.secs = secs;
        fragment.fragmentName = fragmentName;
        fragment.fileName = fileName;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_schedulemantra;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("Mantra");
        // titleBar.showSidebar(getBaseActivity());
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////objectBoxManager = ObjectBoxManager.getInstance((BaseApplication) getActivity().getApplicationContext());
        PRDownloader.initialize(getHomeActivity().getApplicationContext());
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getActivity(), config);

        spinnerModelArrayList = new ArrayList<>();
        arrCategories = new ArrayList<>();
        arrDate = new ArrayList<>();
        dateAdapter = new DateAdapter(getContext(), arrDate, this);
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

    private void bindCategories() {

        Map<String, Object> mquery = new HashMap<>();
        mquery.put(WebServiceConstants.Q_PARAM_ROOT, 1);

        categoriesCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_CATEGORY, mquery, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                Type type = new TypeToken<ArrayList<Categories>>() {
                }.getType();
                ArrayList<Categories> arrayList = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type);

                arrCategories.clear();
                for (Categories categories : arrayList) {
                    arrCategories.addAll(categories.getChildren());
                }

               /* arrCategories.clear();
                arrCategories.addAll(arrayList);*/
                spinnerModelArrayList.clear();

                for (SubCategories categories : arrCategories) {
                    spinnerModelArrayList.add(new SpinnerModel(categories.getName()));
                    /*if (categories.getParentId() == AppConstants.PRIVATE_CATEGORY) {
                        fileTypeValue = AppConstants.FILE_TYPE_PRIVATE;
                        spinnerModelArrayList.add(new SpinnerModel(categories.getName() + " / Private"));
                    } else {
                        fileTypeValue = AppConstants.FILE_TYPE_PUBLIC;
                        spinnerModelArrayList.add(new SpinnerModel(categories.getName()));
                    }*/

                }
            }

            @Override
            public void onError(Object object) {

            }
        });

    }

    private void bindData() {

        if (fragmentName.equals(FragmentName.AddMantraFragment)) {
            mmediacategory.setVisibility(View.GONE);
            mantracategory.setVisibility(View.VISIBLE);
            setData();
            bindCategories();
        } else {
            mantratitle.setFocusable(false);
            mantratitle.setEnabled(false);
            mmediacategory.setVisibility(View.VISIBLE);
            mantracategory.setVisibility(View.GONE);
            txtnote.setFocusable(false);
            txtnote.setEnabled(false);
            setData();
            txtnote.setText(mediaReminder.getDescription());
            txtmmediacategory.setText(mediaReminder.getCategory().getName());
            scheduleMantraLayout.setVisibility(View.GONE);
            layoutscheduled.setVisibility(View.VISIBLE);
            daterecyclerview.setVisibility(View.VISIBLE);
        }

    }

    private void setData() {
        mantratitle.setText(strtitle);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        daterecyclerview.setLayoutManager(mLayoutManager);
        daterecyclerview.setAdapter(dateAdapter);

    }

    @Override
    public void setListeners() {
        cbScheduledMantra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layoutscheduled.setVisibility(View.VISIBLE);
                    daterecyclerview.setVisibility(View.VISIBLE);
                } else {
                    layoutscheduled.setVisibility(View.GONE);
                    daterecyclerview.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onDestroyView() {

        if (categoriesCall != null) {
            categoriesCall.cancel();
        }
        if (uploadMediaCall != null) {
            uploadMediaCall.cancel();
        }

        unbinder.unbind();

        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.addDate, R.id.save, R.id.imgspinner, R.id.txtmantracategory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addDate:
                setReminder();

                break;
            case R.id.save:
                if (RunTimePermissions.isAllPermissionGiven(getContext(), getBaseActivity(), true)) {
                    onSaveClicked();
                }

                break;
            case R.id.imgspinner:
            case R.id.txtmantracategory:
                UIHelper.showSpinnerDialog(MantraDetailFragment.this, spinnerModelArrayList, "Selected Category"
                        , txtmantracategory, null, null, new IntWrapper(0));
                break;
        }
    }

    private void setReminder() {
        Calendar calendar = Calendar.getInstance();
        //Returns current time in millis
        long currentTime = calendar.getTimeInMillis();
        DateManager.showDateTimePicker(getContext(), null, new OnCalendarUpdate() {
            @Override
            public void onCalendarUpdate(Calendar calendar, String formattedString) {
//                UIHelper.showToast(getContext(), formattedString);

                    AlarmModel alarmModel = new AlarmModel();
                    int alarmRandomId = random.nextInt(10000);
                    alarmModel.setAlarmId(alarmRandomId);
                    alarmModel.setDateTime(formattedString);
                    alarmModel.setUnixDTTM(calendar.getTimeInMillis());
                    if (calendar.getTimeInMillis() > currentTime ){
                    arrDate.add(alarmModel);
                    }else{
                        Toast.makeText(getContext(),"Invalid date or time",Toast.LENGTH_SHORT).show();
                    }
                    dateAdapter.notifyDataSetChanged();

            }
        }, true);
    }

    private void onSaveClicked() {

        // Validations
        if (!mantratitle.testValidity()) {
            UIHelper.showShortToastInCenter(getContext(), "Please write Mantra Title");
            return;
        }

        if (fragmentName.equals(FragmentName.AddMantraFragment)) {
            if (txtmantracategory.getStringTrimmed().isEmpty()) {
                UIHelper.showShortToastInCenter(getContext(), "Please select category");
                return;
            }

            if (cbScheduledMantra.isChecked()) {

                if (txtremindertitle.getStringTrimmed().isEmpty()) {
                    UIHelper.showShortToastInCenter(getContext(), "Please write Reminder Title");
                    return;
                }
/*
                if (!arrDate.isEmpty()) {

                    for (AlarmModel alarmModel : arrDate) {
                        alarmModel.setAlarmId(sharedPreferenceManager.incAndGetCurrentAlarmId());
                        Log.d(TAG, "ALARM: id" + alarmModel.getAlarmId());
                        scheduleAlarm(mediaModel.getId(), alarmModel);
                    }

                    mediaModel.setReminderText(txtremindertitle.getStringTrimmed());
                    mediaModel.setArrAlarms(arrDate);
                    mediaModel.setFileLocalPath(fileName);
                    ObjectBoxManager.INSTANCE.putGeneralDBModel(mediaModel.getId(), mediaModel.toString(), DBModelTypes.SCHEDULED_MANTRA);

                }*/
            }

            idFromSpinner = getIdFromSpinner();

            if (idFromSpinner == -1) {
                UIHelper.showShortToastInCenter(getContext(), "Invalid ID");
                return;
            }

            if (!FileManager.isFileExits(fileName)) {
                UIHelper.showShortToastInCenter(getContext(), "Recorded file not found");
                return;
            }

            recordedMantra = new File(fileName);
            UploadMedia uploadMedia = new UploadMedia();
            uploadMedia.setName(mantratitle.getStringTrimmed());
            uploadMedia.setDescription(txtnote.getStringTrimmed());
            uploadMedia.setMediaLength(String.valueOf(secs));
            uploadMedia.setFileType(fileTypeValue);
            uploadMedia.setCategoryId(idFromSpinner);

//            File file1 = new File(fileName);
//            String extension = FileManager.getExtension(fileName);
//            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            /*if (fragmentName.equals(FragmentName.AddMantraFragment)) {

                uploadMedia.setCategoryId(idFromSpinner);
            } else {

                uploadMedia.setCategoryId(mediaReminder.getCategory().getId());
            }*/
            ArrayList<MultiFileModel> arrMultiFileModel = new ArrayList<>();

            // Adding Images
            arrMultiFileModel.add(new MultiFileModel(recordedMantra, FileType.AUDIO, WebServiceConstants.WSC_KEY_ATTACHMENT_FILE));
            postMantraAPI(uploadMedia, arrMultiFileModel);
        } else {

            scheduleMedia();

        }

//        if (Helper.isNetworkConnected(getContext(), false)) {
//            postMantraAPI(uploadMedia, arrMultiFileModel);
//        } else {
//
//            saveWithoutNet(uploadMedia);
//
//        }


    }

    private void scheduleMedia() {

        if (txtremindertitle.getStringTrimmed().isEmpty()) {

            locationClick = SystemClock.elapsedRealtime();
            UIHelper.showShortToastInCenter(getContext(), "Please write Reminder Title");
            return;
        }

        if (!arrDate.isEmpty()) {

            if (SystemClock.elapsedRealtime() - locationClick < 2000) {
                return;
            }
            locationClick = SystemClock.elapsedRealtime();
            downloadFile(mediaReminder);

        } else {
            UIHelper.showShortToastInCenter(getContext(), "Select Date");
            return;
        }


    }


    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }


    private void downloadFile(MediaModel mediaModel) {

        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/VoiceRecorderMantra/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }
        dirPath = root.getAbsolutePath() + "/VoiceRecorderMantra/Audios/";

        fileName = mediaModel.getName() + "_" + mediaModel.getId() + ".mp3";
        File mediaFile = new File(root.getAbsolutePath() + "/VoiceRecorderMantra/Audios/" + fileName);
        if (mediaFile.exists()) {
            alarmMedia();
        } else {

            PRDownloader.download(mediaModel.getFileAbsoluteUrl(), dirPath, fileName)
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {

                            mDialog = UIHelper.getProgressHUD(getContext());

                            if (!((Activity) getActivity()).isFinishing())
                                mDialog.show();
                        }


                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {

                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {

                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {


                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            alarmMedia();
                        }

                        @Override
                        public void onError(Error error) {

                            dismissDialog();


                        }


                    });
        }
    }

    private void alarmMedia() {
        String path = Utils.getPath(dirPath, fileName);

        mediaReminder.setFileAbsoluteUrl(path);
        mediaReminder.setDate(DateManager.getCurrentDate());
        mediaReminder.setReminderText(txtremindertitle.getStringTrimmed());
        mediaReminder.setArrAlarm(arrDate);

        for (AlarmModel alarmModel : mediaReminder.getArrAlarm()) {
            scheduleAlarm(mediaReminder, alarmModel);

        }

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                MediaModel media = new MediaModel();
                media.setCurrentUserId(sharedPreferenceManager.getCurrentUser().getId());
                media.setId(mediaReminder.getId());
                media.setArrAlarm(mediaReminder.getArrAlarm());
                media.setCategoryId(mediaReminder.getCategoryId());
                media.setDate(mediaReminder.getDate());
                media.setDescription(mediaReminder.getDescription());
                media.setFileAbsoluteUrl(mediaReminder.getFileAbsoluteUrl());
                media.setFileLocalPath(mediaReminder.getFileLocalPath());
                media.setFileMime(mediaReminder.getFileMime());
                media.setFilePath(mediaReminder.getFilePath());
                media.setFileUrl(mediaReminder.getFileUrl());
                media.setFileType(mediaReminder.getFileType());
                media.setIconImageUrl(mediaReminder.getIconImageUrl());
                media.setImage(mediaReminder.getImage());
                media.setImageUrl(mediaReminder.getImageUrl());
                media.setMediaLength(mediaReminder.getMediaLength());
                media.setName(mediaReminder.getName());
                media.setOriginalPlaylist(mediaReminder.getOriginalPlaylist());
                media.setReminderText(mediaReminder.getReminderText());
                media.setUserId(mediaReminder.getUserId());


                //adding to database
                DatabaseClient.getInstance(getContext()).getAppDatabase()
                        .mediaDao()
                        .insert(media);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }

        SaveTask st = new SaveTask();
        st.execute();
        dismissDialog();
        getBaseActivity().popStackTill(1);
    }

  /*  private void saveWithoutNet(UploadMedia uploadMedia) {


        MediaModel mediaModel = new MediaModel();
        mediaModel.setName(uploadMedia.getName());
        mediaModel.setDescription(uploadMedia.getDescription());
        mediaModel.setMediaLength(secs);
        mediaModel.setFileType(String.valueOf(uploadMedia.getFileType()));
        mediaModel.setCategoryId(uploadMedia.getCategoryId());

        Category category = new Category();
        category.setId(uploadMedia.getCategoryId());
        category.setName(txtmantracategory.getStringTrimmed());

        mediaModel.setCategory(category);


        if (cbScheduledMantra.isChecked()) {
            if (!arrDate.isEmpty()) {

                for (AlarmModel alarmModel : arrDate) {
                    alarmModel.setAlarmId(sharedPreferenceManager.incAndGetCurrentAlarmId());
                    scheduleAlarm(mediaModel.getId(), alarmModel);
                }

                mediaModel.setDate(DateManager.getCurrentDate());
                mediaModel.setReminderText(txtremindertitle.getStringTrimmed());
                mediaModel.setArrAlarms(arrDate);
                mediaModel.setFileLocalPath(fileName);
                ObjectBoxManager.INSTANCE.putGeneralDBModel(mediaModel.getId(), mediaModel.toString(), DBModelTypes.SCHEDULED_MANTRA);
            } else {

                UIHelper.showShortToastInCenter(getContext(), "Select Date");
                return;

            }
        }

        getBaseActivity().popStackTill(1);
       *//* UIHelper.showAlertDialogWithCallback("Mantra Scheduled Successfully", "Mantra Added", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getBaseActivity().popStackTill(1);
            }
        }, getContext());*//*
    }*/

    private void postMantraAPI(UploadMedia uploadMedia, ArrayList<MultiFileModel> arrMultiFileModel) {
        getBaseWebServices(true).postMultipartAPI(WebServiceConstants.PATH_MEDIA, arrMultiFileModel, uploadMedia.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {

                MediaModel media  = GsonFactory.getSimpleGson().fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result), MediaModel.class);

                media.setCurrentUserId(sharedPreferenceManager.getCurrentUser().getId());
                media.setArrAlarm(arrDate);
                media.setDate(DateManager.getCurrentDate());
                media.setFileLocalPath(fileName);
                media.setReminderText(txtremindertitle.getStringTrimmed());


                if (cbScheduledMantra.isChecked()) {
                    if (!arrDate.isEmpty()) {

                        for (AlarmModel alarmModel : arrDate) {
                            scheduleAlarm(media, alarmModel);
                        }

                        class SaveTask extends AsyncTask<Void, Void, Void> {

                            @Override
                            protected Void doInBackground(Void... voids) {


                                //adding to database
                                DatabaseClient.getInstance(getContext()).getAppDatabase()
                                        .mediaDao()
                                        .insert(media);

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);

                            }
                        }

                        SaveTask st = new SaveTask();
                        st.execute();
                        dismissDialog();

                    } else {

                        UIHelper.showShortToastInCenter(getContext(), "Select Date");
                        return;
                    }
                }

                getBaseActivity().popStackTill(1);

              /*  UIHelper.showAlertDialogWithCallback(webResponse.message, "Mantra Added", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getBaseActivity().popStackTill(1);
                    }
                }, getContext());
*/

//
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    private int getIdFromSpinner() {

        for (SubCategories category : arrCategories) {
            if (category.getName().equals(txtmantracategory.getStringTrimmed())) {
                return category.getId();
            }
        }

        return -1;
    }

    @Override
    public void onItemClick(int position, Object object, View view, String adapterName) {
        switch (view.getId()) {
            case R.id.txtcancel:
                arrDate.remove(position);
                dateAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * A custom method set the schedule alarm via AlarmManager.
     */
    private void scheduleAlarm(MediaModel media, AlarmModel alarmModel) {

        Gson gson = new Gson();
        Type type = new TypeToken<MediaModel>() {}.getType();
        String json = gson.toJson(media, type);

        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager) getBaseActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getBaseActivity(), AlarmReceiver.class);
        intent.putExtra(AppConstants.MEDIA_MODEL, json);
        intent.putExtra(AppConstants.GENERAL_DB_ID, media.getId());
        intent.putExtra(AppConstants.DB_ID, media.getDbId());
        intent.putExtra(AppConstants.ALARM_ID, alarmModel.getAlarmId());
        intent.putExtra(AppConstants.CURRENT_USER_ID, sharedPreferenceManager.getCurrentUser().getId());
        alarmIntent = PendingIntent.getBroadcast(getContext(),
                alarmModel.getAlarmId(), intent, FLAG_UPDATE_CURRENT);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmModel.getUnixDTTM(), alarmIntent);
        long triggerAtMillis = alarmModel.getUnixDTTM();
        if (SDK_INT > LOLLIPOP) {
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(triggerAtMillis, alarmIntent);
            alarmMgr.setAlarmClock(alarmClockInfo, alarmIntent);
        } else if (SDK_INT > KITKAT) {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, alarmModel.getUnixDTTM(), alarmIntent);
        } else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmModel.getUnixDTTM(), alarmIntent);
        }
    }

}
