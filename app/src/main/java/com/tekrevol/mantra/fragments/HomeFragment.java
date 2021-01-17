package com.tekrevol.mantra.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.gson.reflect.TypeToken;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.recyleradapters.FavMediaAdapter;
import com.tekrevol.mantra.adapters.recyleradapters.HomeChildLaughAdapter;
import com.tekrevol.mantra.adapters.recyleradapters.HomeDailyMantraAdapter;
import com.tekrevol.mantra.adapters.recyleradapters.MovieLineAdapter;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.enums.FragmentName;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.receiving_model.Categories;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.models.sending_model.UserAction;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

import static com.tekrevol.mantra.constatnts.WebServiceConstants.PATH_ACTIONS;

public class HomeFragment extends BaseFragment implements OnItemClickListener {


    @BindView(R.id.imgCancel)
    ImageView imgCancel;
    @BindView(R.id.relativeLayoutAds)
    RelativeLayout relativeLayoutAds;
    private PublisherAdView mPublisherAdView;
    AdLoader adLoader;
    Call<WebResponse<Object>> categoriesCall;
    Call<WebResponse<Object>> mediaCall;
    Call<WebResponse<Object>> dailymantraCall;
    Call<WebResponse<Object>> favCall, unfavCall;
    int fileTypeValue = AppConstants.FILE_TYPE_PUBLIC;
    @BindView(R.id.recyclerview_dailymantra)
    ShimmerRecyclerView recyclerviewDailymantra;
    @BindView(R.id.txt_seeall_childrenlaugh)
    TextView txtSeeallChildrenlaugh;
    @BindView(R.id.recyclerview_childrenlaugh)
    ShimmerRecyclerView recyclerviewChildrenlaugh;
    @BindView(R.id.txt_seeall_movieline)
    TextView txtSeeallMovieline;
    @BindView(R.id.recyclerview_movie_lines)
    ShimmerRecyclerView recyclerviewMovieLines;
    Unbinder unbinder;
    @BindView(R.id.txt_seeall_favorite)
    AnyTextView txtSeeallFavorite;
    @BindView(R.id.rvFavHomeScreen)
    ShimmerRecyclerView rvFavHomeScreen;
    @BindView(R.id.imgbtn_home_tab)
    ImageButton imgbtnHomeTab;
    @BindView(R.id.btn_categories)
    ImageButton btnCategories;
    @BindView(R.id.btn_search)
    ImageButton btnSearch;
    @BindView(R.id.layoutFav)
    LinearLayout layoutFav;
    @BindView(R.id.btnSchedule)
    ImageButton btnSchedule;
    @BindView(R.id.btnMyMantra)
    ImageButton btnMyMantra;
    private Boolean selected = false;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayer1;
    private FragmentName titleName = FragmentName.FavouriteMantra;
    private HomeDailyMantraAdapter homeDailyMantraAdapter;
    private ArrayList<MediaModel> arrDailyMantra;
    private HomeChildLaughAdapter homeChildLaughAdapter;
    private ArrayList<Categories> arrChildrenLaugh;
    private MovieLineAdapter homeMovieLinesAdapter;
    private FavMediaAdapter profileMediaAdapter;
    private ArrayList<MediaModel> arrMovieLines;
    private ArrayList<MediaModel> arrFavourite;
    private ImageButton btn_search, btn_categories, imgbtn_home_tab;
    private LinearLayout layoutFavv;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_UNLOCKED;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("");
        //titleBar.showButtonsInHome();
        titleBar.showSidebar(getBaseActivity());
        titleBar.showSaveHome(getHomeActivity());
      /*  titleBar.btnHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().addDockableFragment(LogsMantraFragment.newInstance(FragmentName.ScheduledMantra),true);
            }
        });*/
        titleBar.showHome(getBaseActivity());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Mantra@321
        // ads:adUnitId="ca-app-pub-3544514323093823/6165184689">
        arrDailyMantra = new ArrayList<>();
        arrChildrenLaugh = new ArrayList<>();
        arrMovieLines = new ArrayList<>();
        arrFavourite = new ArrayList<>();
        profileMediaAdapter = new FavMediaAdapter(getContext(), arrFavourite, titleName, this);
        homeMovieLinesAdapter = new MovieLineAdapter(getContext(), arrMovieLines, this);
        homeDailyMantraAdapter = new HomeDailyMantraAdapter(getContext(), arrDailyMantra, this);
        homeChildLaughAdapter = new HomeChildLaughAdapter(getContext(), arrChildrenLaugh, this);
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
        //getHomeActivity().showBottomBar();

        btn_search = view.findViewById(R.id.btn_search);
        layoutFavv = view.findViewById(R.id.layoutFav);
        btnMyMantra = view.findViewById(R.id.btnMyMantra);
        imgbtn_home_tab = view.findViewById(R.id.imgbtn_home_tab);
        btn_categories = view.findViewById(R.id.btn_categories);
        bindRecyclerView();


      /*  mPublisherAdView = (PublisherAdView) getView().findViewById(R.id.fluid_view);
        PublisherAdRequest publisherAdRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(publisherAdRequest);*/
        // mPublisherAdView = (PublisherAdView) getView().findViewById(R.id.fluid_view);

        /*TemplateView template = getView().findViewById(R.id.my_template);
        adLoader = new AdLoader.Builder(getContext(), "ca-app-pub-3940256099942544/2247696110")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.

                        template.setNativeAd(unifiedNativeAd);

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        adLoader.loadAd(new PublisherAdRequest.Builder().build());*/


    }

    private void getFavorite() {

        arrFavourite.clear();
        Map<String, Object> mquery = new HashMap<>();
        rvFavHomeScreen.showShimmer();
        mquery.put(WebServiceConstants.Q_FAVOURITE, 1);
        mquery.put(WebServiceConstants.Q_PARAM_LIMIT, 3);
        mquery.put(WebServiceConstants.Q_PARAM_OFFSET, 0);
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

                if (arrayList.size() > 0) {
                    layoutFavv.setVisibility(View.VISIBLE);
                    rvFavHomeScreen.setVisibility(View.VISIBLE);
                } else {
                    layoutFavv.setVisibility(View.GONE);
                    rvFavHomeScreen.setVisibility(View.GONE);
                }
                rvFavHomeScreen.hideShimmer();
                arrFavourite.addAll(arrayList);
                profileMediaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object object) {
                if (rvFavHomeScreen == null) {
                    return;
                }
                rvFavHomeScreen.hideShimmer();
            }
        });


    }

    private void getDailyMantraa() {

        recyclerviewDailymantra.showShimmer();

        Map<String, Object> mquery = new HashMap<>();
        mquery.put(WebServiceConstants.ADMIN_MANTRA, 1);
        mquery.put(WebServiceConstants.Q_PARAM_LIMIT, 5);
        mquery.put(WebServiceConstants.Q_PARAM_ORDERBY, AppConstants.ORDER_BY_ID);
        mquery.put(WebServiceConstants.Q_PARAM_SORTED, AppConstants.SORTED_BY);


        dailymantraCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_MEDIA, mquery, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                Type type = new TypeToken<ArrayList<MediaModel>>() {
                }.getType();
                ArrayList<MediaModel> arrayList = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type);


                arrDailyMantra.clear();
                arrDailyMantra.addAll(arrayList);
                homeDailyMantraAdapter.notifyDataSetChanged();
                recyclerviewDailymantra.hideShimmer();
            }

            @Override
            public void onError(Object object) {
                if (recyclerviewDailymantra == null) {
                    return;
                }
                recyclerviewDailymantra.hideShimmer();
            }
        });

    }

    private void getMovieLines() {

        recyclerviewMovieLines.showShimmer();
        Map<String, Object> mquery = new HashMap<>();
        mquery.put(WebServiceConstants.Q_MOVIELINE, 1);
        mquery.put(WebServiceConstants.Q_PARAM_LIMIT, 3);
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


                arrMovieLines.clear();
                arrMovieLines.addAll(arrayList);
                homeMovieLinesAdapter.notifyDataSetChanged();

                txtSeeallMovieline.setVisibility(View.VISIBLE);
                recyclerviewMovieLines.hideShimmer();
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

    private void getCategories() {

        recyclerviewChildrenlaugh.showShimmer();

        Map<String, Object> mquery = new HashMap<>();
        mquery.put(WebServiceConstants.Q_PARAM_ROOT, 1);
        mquery.put(WebServiceConstants.Q_PARAM_LIMIT, 11);

        categoriesCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_CATEGORY, mquery, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                Type type = new TypeToken<ArrayList<Categories>>() {
                }.getType();
                ArrayList<Categories> arrayList = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type);


                arrChildrenLaugh.clear();
                arrChildrenLaugh.addAll(arrayList);
                homeChildLaughAdapter.notifyDataSetChanged();

                txtSeeallChildrenlaugh.setVisibility(View.VISIBLE);
                recyclerviewChildrenlaugh.hideShimmer();
            }

            @Override
            public void onError(Object object) {

                if (recyclerviewChildrenlaugh == null) {
                    return;
                }
                recyclerviewChildrenlaugh.hideShimmer();

            }
        });

    }

    private void bindRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        recyclerviewDailymantra.setLayoutManager(mLayoutManager);
        ((DefaultItemAnimator) recyclerviewDailymantra.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerviewDailymantra.setAdapter(homeDailyMantraAdapter);
        recyclerviewDailymantra.setItemViewType((type, position) -> R.layout.customlist_dailymantra_shimmer);

        recyclerviewChildrenlaugh.setLayoutManager(mLayoutManager1);
        ((DefaultItemAnimator) recyclerviewChildrenlaugh.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerviewChildrenlaugh.setAdapter(homeChildLaughAdapter);
        recyclerviewChildrenlaugh.setItemViewType((type, position) -> R.layout.shimmer_view);
        recyclerviewMovieLines.setLayoutManager(mLayoutManager2);


        ((DefaultItemAnimator) recyclerviewMovieLines.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerviewMovieLines.setAdapter(homeMovieLinesAdapter);
        recyclerviewMovieLines.setItemViewType((type, position) -> R.layout.shimmer_customlist_listsearch);

        rvFavHomeScreen.setLayoutManager(mLayoutManager3);
        ((DefaultItemAnimator) rvFavHomeScreen.getItemAnimator()).setSupportsChangeAnimations(false);
        rvFavHomeScreen.setAdapter(profileMediaAdapter);
        rvFavHomeScreen.setItemViewType((type, position) -> R.layout.shimmer_customlist_listsearch);

        getCategories();
        getFavorite();
        getDailyMantraa();
        getMovieLines();
    }

    @Override
    public void setListeners() {

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().popStackTill(1);
                profileMediaAdapter.resetChoice();
                getBaseActivity().addFragment(SearchFragment.newInstance(), false);
            }
        });

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileMediaAdapter.resetChoice();
                getBaseActivity().addFragment(ScheduleMantraFragment.newInstance(FragmentName.ScheduledMantra), true);
            }
        });

        btnMyMantra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileMediaAdapter.resetChoice();
                getBaseActivity().addFragment(TabFragment.newInstance(), true);
            }
        });

        btn_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().popStackTill(1);
                profileMediaAdapter.resetChoice();
                getBaseActivity().addFragment(CategoriesFragment.newInstance(), false);
            }
        });
        imgbtn_home_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().popStackTill(1);
                profileMediaAdapter.resetChoice();
                getBaseActivity().addFragment(AddMantraFragment.newInstance(), false);
            }
        });

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

    @OnClick({R.id.txt_seeall_childrenlaugh, R.id.txt_seeall_movieline, R.id.txt_seeall_favorite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_seeall_childrenlaugh:
                profileMediaAdapter.resetChoice();
                getBaseActivity().addFragment(CategoriesFragment.newInstance(), true);
                break;
            case R.id.txt_seeall_movieline:
                profileMediaAdapter.resetChoice();
                getBaseActivity().addFragment(MovieLinesSeeAllFragment.newInstance(), true);
                break;
            case R.id.txt_seeall_favorite:
                profileMediaAdapter.resetChoice();
                getBaseActivity().addFragment(ProfileSeeAllFragment.newInstance(FragmentName.FavouriteMantra), true);
                break;
        }
    }

    @Override
    public void onItemClick(int position, Object object, View view, String adapterName) {

        boolean isNotNull = UIHelper.testArrayListAndPosition(arrMovieLines, position);
        boolean isFavNotNull = UIHelper.testArrayListAndPosition(arrFavourite, position);
        if (HomeDailyMantraAdapter.class.getSimpleName().equals(adapterName)) {
            MediaModel model = (MediaModel) object;
            profileMediaAdapter.resetChoice();
            getBaseActivity().addFragment(DetailFragment.newInstance(model), true);
        } else if (HomeChildLaughAdapter.class.getSimpleName().equals(adapterName)) {
            Categories model = (Categories) object;
            profileMediaAdapter.resetChoice();
            getBaseActivity().addFragment(ChildrenSeeAllFragment.newInstance(model.getId(), model.getName()), true);
        } else if (FavMediaAdapter.class.getSimpleName().equals(adapterName)) {
            if (isFavNotNull) {
                switch (view.getId()) {
                    case R.id.layout1:
                        if (arrMovieLines.size() > 0) {
                            for (MediaModel mediaModel : arrMovieLines) {
                                mediaModel.setChoice1(false);
                            }
                            homeMovieLinesAdapter.notifyDataSetChanged();
                        }
                        for (MediaModel mediaModel : arrFavourite) {
                            if (mediaPlayer1 != null) {
                                mediaPlayer1.release();
                                mediaModel.setMedia1(false);
                                arrFavourite.get(position).setMedia1(false);
                                profileMediaAdapter.notifyDataSetChanged();
                            }
                        }
                        for (MediaModel mediaModel1 : arrMovieLines) {
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                mediaModel1.setMedia1(false);
                                if (position < arrMovieLines.size()) {
                                    arrMovieLines.get(position).setMedia1(false);
                                }
                                //
                                homeMovieLinesAdapter.notifyDataSetChanged();
                            }
                        }
                        for (MediaModel mediaModel : arrFavourite) {
                            mediaModel.setChoice1(false);
                        }
                        arrFavourite.get(position).setChoice1(true);
                        profileMediaAdapter.notifyDataSetChanged();
                        break;
                    case R.id.imgbtn_close:
                        for (MediaModel mediaModel : arrFavourite) {
                            if (mediaPlayer1 != null) {
                                mediaPlayer1.release();
                                mediaModel.setMedia1(false);
                                arrFavourite.get(position).setMedia1(false);
                                profileMediaAdapter.notifyDataSetChanged();
                            }
                        }
                        for (MediaModel mediaModel1 : arrMovieLines) {
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                mediaModel1.setMedia1(false);
                                if (position < arrMovieLines.size()) {
                                    arrMovieLines.get(position).setMedia1(false);
                                }
                                //
                                homeMovieLinesAdapter.notifyDataSetChanged();
                            }
                        }
                        arrFavourite.get(position).setChoice1(false);
                        profileMediaAdapter.notifyItemChanged(position);
                        break;
                    case R.id.reminder:
                        MediaModel mediaReminder = (MediaModel) object;
                        profileMediaAdapter.resetChoice();
                        getBaseActivity().addFragment(MantraDetailFragment.newInstance(mediaReminder, FragmentName.HomeFragment, fileTypeValue, mediaReminder.getName(), mediaReminder.getFileAbsoluteUrl(), mediaReminder.getMediaLength()), true);
                        break;
                    case R.id.fav:
                        MediaModel media = (MediaModel) object;
                        onFavClick(media.getId(), 0, position);
                        arrFavourite.get(position).setIsFavourite(0);
                        profileMediaAdapter.notifyItemChanged(position);
                        break;
                    case R.id.unfav:
                        MediaModel media2 = (MediaModel) object;
                        onUnFavClick(media2.getId());
                        arrFavourite.get(position).setIsFavourite(1);
                        profileMediaAdapter.notifyItemChanged(position);
                        break;
                    case R.id.playMedia:
                        if ((arrFavourite.get(position).isMedia1())) {
                            arrFavourite.get(position).setMedia1(false);
                            mediaPlayer1.release();
                            profileMediaAdapter.notifyItemChanged(position);
                        } else {
                            for (MediaModel mediaModel : arrFavourite) {
                                if (mediaPlayer1 != null) {
                                    mediaPlayer1.release();
                                    mediaModel.setMedia1(false);
                                    arrFavourite.get(position).setMedia1(false);
                                    profileMediaAdapter.notifyDataSetChanged();
                                }
                            }
                            for (MediaModel mediaModel1 : arrMovieLines) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                    mediaModel1.setMedia1(false);
                                    if (position < arrMovieLines.size()) {
                                        arrMovieLines.get(position).setMedia1(false);
                                    }
                                    //
                                    homeMovieLinesAdapter.notifyDataSetChanged();
                                }
                            }
                            arrFavourite.get(position).setMedia1(true);
                            profileMediaAdapter.notifyDataSetChanged();
                            mediaPlayer1 = new MediaPlayer();
                            try {
                                MediaModel model = (MediaModel) object;
                                mediaPlayer1.setDataSource(model.getFileAbsoluteUrl());
                                /*mediaPlayer.prepare();
                                mediaPlayer.start();*/
                                mediaPlayer1.prepareAsync();
                                mediaPlayer1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mediaPlayer) {
                                        mediaPlayer1.start();
                                    }
                                });

                                mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        arrFavourite.get(position).setMedia1(false);
                                        profileMediaAdapter.notifyItemChanged(position);
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

        } else if (MovieLineAdapter.class.getSimpleName().equals(adapterName)) {
            if (isNotNull) {
                switch (view.getId()) {
                    case R.id.layout1:
                        if (arrFavourite.size() > 0) {
                            for (MediaModel mediaModel : arrFavourite) {
                                mediaModel.setChoice1(false);
                            }
                            profileMediaAdapter.notifyDataSetChanged();

                        }
                        for (MediaModel mediaModel : arrMovieLines) {
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                mediaModel.setMedia1(false);
                                arrMovieLines.get(position).setMedia1(false);
                                homeMovieLinesAdapter.notifyDataSetChanged();
                            }
                        }
                        for (MediaModel mediaModel1 : arrFavourite) {
                            if (mediaPlayer1 != null) {
                                mediaPlayer1.release();
                                mediaModel1.setMedia1(false);
                                if (position < arrFavourite.size()) {
                                    arrFavourite.get(position).setMedia1(false);
                                }
                                //
                                profileMediaAdapter.notifyDataSetChanged();
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
                                mediaModel.setMedia1(false);
                                arrMovieLines.get(position).setMedia1(false);
                                homeMovieLinesAdapter.notifyDataSetChanged();
                            }
                        }
                        for (MediaModel mediaModel1 : arrFavourite) {
                            if (mediaPlayer1 != null) {
                                mediaPlayer1.release();
                                mediaModel1.setMedia1(false);
                                if (position < arrFavourite.size()) {
                                    arrFavourite.get(position).setMedia1(false);
                                }
                                //
                                profileMediaAdapter.notifyDataSetChanged();
                            }
                        }
                        arrMovieLines.get(position).setChoice1(false);
                        homeMovieLinesAdapter.notifyItemChanged(position);
                        break;
                    case R.id.reminder:
                        MediaModel mediaReminder = (MediaModel) object;
                        profileMediaAdapter.resetChoice();
                        getBaseActivity().addFragment(MantraDetailFragment.newInstance(mediaReminder, FragmentName.HomeFragment, fileTypeValue, mediaReminder.getName(), mediaReminder.getFileAbsoluteUrl(), mediaReminder.getMediaLength()), true);
                        break;
                    case R.id.fav:
                        MediaModel media = (MediaModel) object;
                        onFavClick(media.getId(), 1, position);
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
                            for (MediaModel mediaModel1 : arrFavourite) {
                                if (mediaPlayer1 != null) {
                                    mediaPlayer1.release();
                                    mediaModel1.setMedia1(false);
                                    if (position < arrFavourite.size()) {
                                        arrFavourite.get(position).setMedia1(false);
                                    }
                                    //
                                    profileMediaAdapter.notifyDataSetChanged();
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
        if (mediaPlayer1 != null) {
            try {
                mediaPlayer1.release();
                mediaPlayer1 = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (categoriesCall != null) {
            categoriesCall.cancel();
        }
        if (mediaCall != null) {
            mediaCall.cancel();
        }

        if (dailymantraCall != null) {
            dailymantraCall.cancel();
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

    private void onUnFavClick(long id) {

        UserAction userAction = new UserAction();
        userAction.setActionType(AppConstants.ACTION_UNFAV);
        userAction.setMediaId(id);
        unfavCall = getBaseWebServices(true).postAPIAnyObject(PATH_ACTIONS, userAction.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UIHelper.showToast(getContext(), webResponse.message);
                if (arrFavourite.size() < 3) {
                    if (mediaPlayer != null) {
                        try {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (mediaPlayer1 != null) {
                        try {
                            mediaPlayer1.release();
                            mediaPlayer1 = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    getFavorite();
                }
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    private void onFavClick(long id, int i, int position) {

        UserAction userAction = new UserAction();
        userAction.setActionType(AppConstants.ACTION_FAV);
        userAction.setMediaId(id);
        favCall = getBaseWebServices(true).postAPIAnyObject(PATH_ACTIONS, userAction.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                UIHelper.showToast(getContext(), webResponse.message);
                if (mediaPlayer != null) {
                    try {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (mediaPlayer1 != null) {
                    try {
                        mediaPlayer1.release();
                        mediaPlayer1 = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (i == 0) {
                    getMovieLines();
                }
                getFavorite();

            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    @OnClick(R.id.imgCancel)
    public void onViewClicked() {
        relativeLayoutAds.setVisibility(View.GONE);

    }

}
