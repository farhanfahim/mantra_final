package com.tekrevol.mantra.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.recyleradapters.ScheduleMantraAdapter;
import com.tekrevol.mantra.broadcast.AlarmReceiver;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.callbacks.OnSubItemClickListener;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.enums.DBModelTypes;
import com.tekrevol.mantra.enums.FragmentName;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.managers.ObjectBoxManager;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.ReminderModel;
import com.tekrevol.mantra.models.database.AlarmModel;
import com.tekrevol.mantra.models.database.GeneralDBModel;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.models.receiving_model.UserModel;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.TitleBar;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

public class ScheduleMantraFragment extends BaseFragment implements OnItemClickListener, OnSubItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.categories_name)
    TextView categoriesName;
    @BindView(R.id.recyclerview_movie_lines)
    ShimmerRecyclerView recyclerviewMovieLines;
    @BindView(R.id.progressBar_activitymain)
    ProgressBar progressBarActivitymain;
    Call<WebResponse<Object>> mediaCall;
    private int offset;
    private static int limit = 11;
    private int x = 0;
    private MediaPlayer mediaPlayer;
    private Boolean selected = false;
    private List<GeneralDBModel> arrayListTest;

  /*  private ProfileSeeAllAdapter homeChildLaughAdapter;
    private ArrayList<DummyAdapterModel> arrChildrenLaugh;*/


    private ScheduleMantraAdapter scheduleMantraAdapter;
    private ArrayList<MediaModel> arrMovieLines;
    private FragmentName titleName;

    public static ScheduleMantraFragment newInstance(FragmentName titleName) {

        Bundle args = new Bundle();
        ScheduleMantraFragment fragment = new ScheduleMantraFragment();
        fragment.setArguments(args);
        fragment.titleName = titleName;
        return fragment;
    }


    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_movieline_view_all;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("Schedule Mantra");
        titleBar.showSaveHome(getHomeActivity());
        // titleBar.showSidebar(getBaseActivity());
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrMovieLines = new ArrayList<>();
        scheduleMantraAdapter = new ScheduleMantraAdapter(getContext(), arrMovieLines, this, this);

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

        bindRecyclerView();
        if (onCreated) {
            return;
        }
        getScheduleMantra();

    }

    private void bindRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerviewMovieLines.setLayoutManager(mLayoutManager1);
        ((DefaultItemAnimator) recyclerviewMovieLines.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerviewMovieLines.setAdapter(scheduleMantraAdapter);
        recyclerviewMovieLines.setItemViewType((type, position) -> R.layout.shimmer_customlist_listsearch);
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


    }

    private void getScheduleMantra() {

        Calendar calendar = Calendar.getInstance();
        //Returns current time in millis
        long currentTime = calendar.getTimeInMillis();

        ArrayList<MediaModel> arrayList = ObjectBoxManager.INSTANCE.getAllScheduledMantraMediaModels(getContext());
        arrayListTest = ObjectBoxManager.INSTANCE.getAllScheduledMantraMediaModelsTest();


        for (MediaModel arrMedia : arrayList) {
            ArrayList<AlarmModel> arrayList1 = new ArrayList<>();
            for (AlarmModel arrAlarm : arrMedia.getAlarms()){
                if (arrAlarm.getUnixDTTM() >= currentTime) {
                    arrayList1.add(arrAlarm);
                    /*deleteParent(arrMedia, pos);*/
                }
            }
            arrMedia.setArrAlarms(arrayList1);
            arrMovieLines.add(arrMedia);
        }

        scheduleMantraAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(int position, Object object, View view, String adapterName) {

        switch (view.getId()) {
            /*case R.id.imgbtn_delete:
                MediaModel mediaModel = (MediaModel) object;
                UIHelper.showAlertDialog("Are you sure you want to delete?", "Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(mediaModel, position);
                    }
                }, getContext());
                break;
            */case R.id.playMedia:
                if ((arrMovieLines.get(position).isMedia1())) {
                    arrMovieLines.get(position).setMedia1(false);
                    mediaPlayer.release();
                    scheduleMantraAdapter.notifyItemChanged(position);
                } else {
                    for (MediaModel mediaModel1 : arrMovieLines) {
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            //  selected = false;
                            mediaModel1.setMedia1(false);
                            arrMovieLines.get(position).setMedia1(false);
                            scheduleMantraAdapter.notifyDataSetChanged();
                        }
                    }
                    arrMovieLines.get(position).setMedia1(true);
                    scheduleMantraAdapter.notifyDataSetChanged();
                    //    mediaAdapter.notifyItemChanged(position);
                    mediaPlayer = new MediaPlayer();
                    try {
                        MediaModel model = (MediaModel) object;
                        mediaPlayer.setDataSource(model.getFileAbsoluteUrl());
                        /*mediaPlayer.prepare();
                        mediaPlayer.start();*/
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });

                        //   selected = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            arrMovieLines.get(position).setMedia1(false);
                            scheduleMantraAdapter.notifyItemChanged(position);
                            mediaPlayer.release();
                            //selected = false;
                        }
                    });

                }
                break;
        }

        //   getBaseActivity().addDockableFragment(CategoryViewAllFragment.newInstance(model.getId()), true);

    }

    private void delete(MediaModel mediaModel, int position) {
        for (AlarmModel alarm : mediaModel.getAlarms()) {
            dismissAlarm(alarm.getAlarmId(), getContext());
        }
        long id = arrayListTest.get(arrayListTest.size() - 1 - position).id;
        //long id = arrayListTest.get(position).id;
//        ObjectBoxManager.INSTANCE.removeGeneralDBModel(mediaModel.getId());

        ObjectBoxManager.INSTANCE.removeGeneralDBModel(id);
        arrMovieLines.remove(position);
        //getBaseActivity().popBackStack();
        scheduleMantraAdapter.notifyDataSetChanged();
    }

    private void deleteParent(MediaModel mediaModel, int position) {
        for (AlarmModel alarm : mediaModel.getAlarms()) {
            dismissAlarm(alarm.getAlarmId(), getContext());
        }
        long id = arrayListTest.get(arrayListTest.size() - 1 - position).id;
        //long id = arrayListTest.get(position).id;
//        ObjectBoxManager.INSTANCE.removeGeneralDBModel(mediaModel.getId());
        ObjectBoxManager.INSTANCE.removeGeneralDBModel(id);
        arrMovieLines.remove(position);
        getBaseActivity().popBackStack();
        scheduleMantraAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {

        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        unbinder.unbind();
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onItemClick(int parentPosition, int childPosition, Object object, View view, String adapterName) {
        UIHelper.showAlertDialog("Are you sure you want to delete schedule time?", "Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long id = arrayListTest.get(arrayListTest.size() - 1 - parentPosition).id;
                Log.d("SizeBefore", String.valueOf(arrMovieLines.size()));
                MediaModel mediaModel = arrMovieLines.get(parentPosition);
                int alarmId = mediaModel.getAlarms().get(childPosition).getAlarmId();
                dismissAlarm(alarmId, getContext());
                mediaModel.getAlarms().remove(childPosition);
                ObjectBoxManager.INSTANCE.putGeneralDBModel(id, sharedPreferenceManager.getCurrentUser().getId(), mediaModel.toString(), DBModelTypes.SCHEDULED_MANTRA);
                //arrMovieLines.remove((arrMovieLines.get(parentPosition).getAlarms().get(childPosition)));
                Log.d("SizeAfter", String.valueOf(arrMovieLines.size()));
                scheduleMantraAdapter.notifyDataSetChanged();
                if (arrMovieLines.get(parentPosition).getAlarms().size() == 0) {

                    getBaseActivity().popBackStack();
                }
            }
        }, getContext());

    }

    private void dismissAlarm(long alarmId, Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent newIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) alarmId, newIntent, 0);
        alarmMgr.cancel(alarmIntent);
    }


}
