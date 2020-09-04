package com.tekrevol.mantra.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.pagingadapter.PagingDelegate;
import com.tekrevol.mantra.adapters.recyleradapters.MovieLineSeeAllAdapter;
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

public class MovieLinesSeeAllFragment extends BaseFragment implements OnItemClickListener, PagingDelegate.OnPageListener {

    @BindView(R.id.progressBar_activitymain)
    ProgressBar progressBarActivitymain;
    private int offset;
    private static int limit = 11;
    private int x = 0;
    private MediaPlayer mediaPlayer;
    //private Boolean selected = false;
    Unbinder unbinder;
    @BindView(R.id.recyclerview_movie_lines)
    ShimmerRecyclerView recyclerviewMovieLines;
    Call<WebResponse<Object>> mediaCall;
    private MovieLineSeeAllAdapter homeMovieLinesAdapter;
    Call<WebResponse<Object>> favCall, unfavCall;

    private ArrayList<MediaModel> arrMovieLines;
    int fileTypeValue = AppConstants.FILE_TYPE_PUBLIC;

    public static MovieLinesSeeAllFragment newInstance() {

        Bundle args = new Bundle();
        MovieLinesSeeAllFragment fragment = new MovieLinesSeeAllFragment();
        fragment.setArguments(args);
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
        titleBar.setTitle("Sounds of Nature");
        // titleBar.showSidebar(getBaseActivity());
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrMovieLines = new ArrayList<>();
        homeMovieLinesAdapter = new MovieLineSeeAllAdapter(getContext(), arrMovieLines, this);

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
        //   arrMovieLines.addAll(Constants.getDailyMantra());

        bindRecyclerView();
        /*if (onCreated) {
            return;
        }*/
        getMovieLines(limit, 0);

    }

    private void bindRecyclerView() {

        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerviewMovieLines.setLayoutManager(mLayoutManager2);
        ((DefaultItemAnimator) recyclerviewMovieLines.getItemAnimator()).setSupportsChangeAnimations(false);

        PagingDelegate pagingDelegate = new PagingDelegate.Builder(homeMovieLinesAdapter)
                .attachTo(recyclerviewMovieLines)
                .listenWith(MovieLinesSeeAllFragment.this)
                .build();
        recyclerviewMovieLines.setAdapter(homeMovieLinesAdapter);
        recyclerviewMovieLines.setItemViewType((type, position) -> R.layout.shimmer_customlist_listsearch);
        //   homeMovieLinesAdapter.notifyDataSetChanged();
        //   homeMovieLinesAdapter.notifyDataSetChanged();


    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
                case R.id.detail:
                    MediaModel model2 = (MediaModel) object;
                    getBaseActivity().addDockableFragment(DetailFragment.newInstance(model2), false);
                    break;
                case R.id.reminder:
                    MediaModel mediaReminder = (MediaModel) object;
                    getBaseActivity().addDockableFragment(MantraDetailFragment.newInstance(mediaReminder, FragmentName.MovieLinesSeeAllFragment, fileTypeValue, mediaReminder.getName(), mediaReminder.getFileAbsoluteUrl(), mediaReminder.getMediaLength()), false);
                    break;
                case R.id.fav:
                    MediaModel media = (MediaModel) object;
                    onFavClick(media.getId());
                    arrMovieLines.get(position).setIsFavourite(0);
                    homeMovieLinesAdapter.notifyItemChanged(position);
                    break;
                case R.id.unfav:
                    MediaModel media2 = (MediaModel) object;
                    onUnFavClick(media2.getId());
                    arrMovieLines.get(position).setIsFavourite(1);
                    homeMovieLinesAdapter.notifyItemChanged(position);
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
                                mediaModel.setMedia1(false);
                                arrMovieLines.get(position).setMedia1(false);
                                homeMovieLinesAdapter.notifyDataSetChanged();
                            }
                        }
                        arrMovieLines.get(position).setMedia1(true);
                        homeMovieLinesAdapter.notifyDataSetChanged();
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
                                    mediaPlayer.release();
                                    //selected = false;
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

    private void onFavClick(long id) {

        UserAction userAction = new UserAction();
        userAction.setActionType(AppConstants.ACTION_FAV);
        userAction.setMediaId(id);
        favCall = getBaseWebServices(true).postAPIAnyObject(PATH_ACTIONS, userAction.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UIHelper.showToast(getContext(), webResponse.message);
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    private void getMovieLines(int limit, int offset) {

        if (x == 0) {
            recyclerviewMovieLines.showShimmer();
        }

        Map<String, Object> mquery = new HashMap<>();
        mquery.put(WebServiceConstants.Q_MOVIELINE, 1);
        mquery.put(WebServiceConstants.Q_PARAM_LIMIT, limit);
        mquery.put(WebServiceConstants.Q_PARAM_OFFSET, offset);
        mquery.put(WebServiceConstants.Q_PARAM_ORDERBY, AppConstants.ORDER_BY_ID);
        mquery.put(WebServiceConstants.Q_PARAM_SORTED, AppConstants.SORTED_BY);

        mediaCall = getBaseWebServices(false)  .getAPIAnyObject(WebServiceConstants.PATH_MEDIA, mquery, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                Type type = new TypeToken<ArrayList<MediaModel>>() {
                }.getType();
                ArrayList<MediaModel> arrayList = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type);

                /*   arrMovieLines.clear();*/
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
        if (favCall != null) {
            favCall.cancel();
        }

        if (unfavCall != null) {
            unfavCall.cancel();
        }

        unbinder.unbind();
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
    public void onClick(View view) {

    }
}
