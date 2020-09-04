package com.tekrevol.mantra.fragments;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.pagingadapter.PagingDelegate;
import com.tekrevol.mantra.adapters.recyleradapters.ProfileMediaAdapter;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.enums.FragmentName;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.models.sending_model.UserAction;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.TitleBar;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

import static com.tekrevol.mantra.constatnts.WebServiceConstants.PATH_ACTIONS;
import static com.tekrevol.mantra.constatnts.WebServiceConstants.PATH_MEDIA;

public class ProfileSeeAllFragment extends BaseFragment implements OnItemClickListener, PagingDelegate.OnPageListener {

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
    Call<WebResponse<Object>> favCall, unfavCall, deleteCall;
    private Boolean boolFav = false;
    int fileTypeValue = AppConstants.FILE_TYPE_PUBLIC;

    private ProfileMediaAdapter homeMovieLinesAdapter;
    private ArrayList<MediaModel> arrMovieLines;
    private FragmentName titleName;

    public static ProfileSeeAllFragment newInstance(FragmentName titleName) {

        Bundle args = new Bundle();
        ProfileSeeAllFragment fragment = new ProfileSeeAllFragment();
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
        titleBar.showSaveHome(getHomeActivity());
        if (titleName.equals(FragmentName.SharedMantra)) {
            titleBar.setTitle("Public Mantras");
        } else if (titleName.equals(FragmentName.FavouriteMantra)) {
            titleBar.setTitle("Favourite Mantras");
        } else if (titleName.equals(FragmentName.DraftMantra)) {
            titleBar.setTitle("Private Mantras");
        } else {
            titleBar.setTitle("");
        }
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrMovieLines = new ArrayList<>();
        homeMovieLinesAdapter = new ProfileMediaAdapter(getContext(), arrMovieLines, titleName, this);

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

        arrMovieLines.clear();
        bindRecyclerView();
        /*if (onCreated) {
            return;
        }*/
        getMovieLines(limit, 0);

    }

    private void bindRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerviewMovieLines.setLayoutManager(mLayoutManager1);
        ((DefaultItemAnimator) recyclerviewMovieLines.getItemAnimator()).setSupportsChangeAnimations(false);
        PagingDelegate pagingDelegate = new PagingDelegate.Builder(homeMovieLinesAdapter)
                .attachTo(recyclerviewMovieLines)
                .listenWith(ProfileSeeAllFragment.this)
                .build();
        recyclerviewMovieLines.setAdapter(homeMovieLinesAdapter);
        recyclerviewMovieLines.setItemViewType((type, position) -> R.layout.shimmer_customlist_listsearch);

    }


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

    private void getMovieLines(int limit, int offset) {

        Map<String, Object> mquery = new HashMap<>();
        if (x == 0) {
            recyclerviewMovieLines.showShimmer();
        }

        if (titleName.equals(FragmentName.SharedMantra)) {
            mquery.put(WebServiceConstants.Q_SHARE, 1);
        } else if (titleName.equals(FragmentName.FavouriteMantra)) {
            boolFav = true;
            mquery.put(WebServiceConstants.Q_FAVOURITE, 1);
        } else if (titleName.equals(FragmentName.DraftMantra)) {
            mquery.put(WebServiceConstants.Q_DRAFT, 1);
        } else {
            return;
        }

        mquery.put(WebServiceConstants.Q_PARAM_LIMIT, limit);
        mquery.put(WebServiceConstants.Q_PARAM_OFFSET, offset);
        mquery.put(WebServiceConstants.Q_PARAM_ORDERBY, AppConstants.ORDER_BY_ID);
        mquery.put(WebServiceConstants.Q_PARAM_SORTED, AppConstants.SORTED_BY);
        mediaCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_MEDIA, mquery, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                Type type = new TypeToken<ArrayList<MediaModel>>() {
                }.getType();
                ArrayList<MediaModel> arrayList = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type);


                if (x == 0) {
                    recyclerviewMovieLines.hideShimmer();
                }
                arrMovieLines.addAll(arrayList);
                homeMovieLinesAdapter.notifyDataSetChanged();
                onDonePaging();
            }

            @Override
            public void onError(Object object) {
                if (recyclerviewMovieLines == null) {
                    return;
                }
                recyclerviewMovieLines.hideShimmer();
            }
        });

    }


    @Override
    public void onItemClick(int position, Object object, View view, String adapterName) {

        boolean isNotNull = UIHelper.testArrayListAndPosition(arrMovieLines, position);

        if (isNotNull) {

            switch (view.getId()) {
                case R.id.layout1:
                    for (MediaModel mediaModel : arrMovieLines) {
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            //  selected = false;
                            mediaModel.setMedia1(false);
                            arrMovieLines.get(position).setMedia1(false);
                            homeMovieLinesAdapter.notifyDataSetChanged();
                        }
                    }
                    for (MediaModel mediaModel : arrMovieLines) {
                        mediaModel.setChoice1(false);
                    }
                    arrMovieLines.get(position).setChoice1(true);
                    homeMovieLinesAdapter.notifyDataSetChanged();
                    break;
                case R.id.imgbtn_close:
                    for (MediaModel mediaModel : arrMovieLines) {
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            //  selected = false;
                            mediaModel.setMedia1(false);
                            arrMovieLines.get(position).setMedia1(false);
                            homeMovieLinesAdapter.notifyDataSetChanged();
                        }
                    }
                    arrMovieLines.get(position).setChoice1(false);
                    homeMovieLinesAdapter.notifyItemChanged(position);
                    break;
                case R.id.reminder:
                    MediaModel mediaReminder = (MediaModel) object;
                    getBaseActivity().addDockableFragment(MantraDetailFragment.newInstance(mediaReminder, FragmentName.ProfileSeeAllFragment, fileTypeValue, mediaReminder.getName(), mediaReminder.getFileAbsoluteUrl(), mediaReminder.getMediaLength()), true);
                    break;
                case R.id.detail:
                    MediaModel detailmodel = (MediaModel) object;
                    getBaseActivity().addDockableFragment(DetailFragment.newInstance(detailmodel), true);
                    break;
                case R.id.fav:
                    MediaModel media = (MediaModel) object;
                    onFavClick(media.getId(), position);
                    arrMovieLines.get(position).setIsFavourite(0);
                    homeMovieLinesAdapter.notifyItemChanged(position);
                    break;
                case R.id.unfav:
                    MediaModel media2 = (MediaModel) object;
                    onUnFavClick(media2.getId());
                    arrMovieLines.get(position).setIsFavourite(1);
                    homeMovieLinesAdapter.notifyItemChanged(position);
                    break;
                case R.id.imgbtn_delete:
                    UIHelper.showAlertDialog("Are you sure you want to delete Mantra?", "Delete Mantra", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MediaModel mediadelete = (MediaModel) object;
                            deleteApi(mediadelete.getId(), position);
                        }
                    }, getContext());

                    break;
                case R.id.playMedia:
                    if ((arrMovieLines.get(position).isMedia1())) {
                        arrMovieLines.get(position).setMedia1(false);
                        mediaPlayer.release();
                        homeMovieLinesAdapter.notifyItemChanged(position);
                    } else {
                        for (MediaModel mediaModel : arrMovieLines) {
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                //  selected = false;
                                mediaModel.setMedia1(false);
                                arrMovieLines.get(position).setMedia1(false);
                                homeMovieLinesAdapter.notifyDataSetChanged();
                            }
                        }
                        arrMovieLines.get(position).setMedia1(true);
                        homeMovieLinesAdapter.notifyDataSetChanged();
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

                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    arrMovieLines.get(position).setMedia1(false);
                                    homeMovieLinesAdapter.notifyItemChanged(position);
                                    mp.release();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    break;
            }
        }
    }

    private void deleteApi(long id, int position) {

        deleteCall = getBaseWebServices(true).deleteAPIAnyObject(PATH_MEDIA + "/" + id, "", new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UIHelper.showToast(getContext(), webResponse.message);
                if (mediaPlayer != null) {
                    try {
                        for (MediaModel mediaModel : arrMovieLines) {
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                //  selected = false;
                                mediaModel.setMedia1(false);
                                arrMovieLines.get(position).setMedia1(false);
                                homeMovieLinesAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                arrMovieLines.remove(position);
                homeMovieLinesAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onError(Object object) {

            }
        });

    }


    @Override
    public void onPage(int i) {

        if (offset < i) {

            offset = i;

            x++;
            progressBarActivitymain.setVisibility(View.VISIBLE);

            getMovieLines(limit, i);
        }
    }

    @Override
    public void onDonePaging() {

        if (progressBarActivitymain != null) {
            progressBarActivitymain.setVisibility(View.GONE);
        }

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
        if (mediaCall != null) {
            mediaCall.cancel();
        }
        if (deleteCall != null) {
            deleteCall.cancel();
        }

        unbinder.unbind();


        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void onUnFavClick(long id) {

        UserAction userAction = new UserAction();
        userAction.setActionType(AppConstants.ACTION_UNFAV);
        userAction.setMediaId(id);
        unfavCall = getBaseWebServices(true).postAPIAnyObject(PATH_ACTIONS, userAction.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UIHelper.showToast(getContext(), webResponse.message);
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    private void onFavClick(long id, int position) {

        UserAction userAction = new UserAction();
        userAction.setActionType(AppConstants.ACTION_FAV);
        userAction.setMediaId(id);
        favCall = getBaseWebServices(true).postAPIAnyObject(PATH_ACTIONS, userAction.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UIHelper.showToast(getContext(), webResponse.message);
                if (boolFav) {
                    try {
                        for (MediaModel mediaModel : arrMovieLines) {
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                //  selected = false;
                                mediaModel.setMedia1(false);
                                arrMovieLines.get(position).setMedia1(false);
                                homeMovieLinesAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    arrMovieLines.remove(position);
                    homeMovieLinesAdapter.notifyItemRemoved(position);
                }
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

}
