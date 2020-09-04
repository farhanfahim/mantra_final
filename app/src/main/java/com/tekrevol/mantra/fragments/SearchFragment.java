package com.tekrevol.mantra.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.pagingadapter.PagingDelegate;
import com.tekrevol.mantra.adapters.recyleradapters.SearchAdapter;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.enums.FragmentName;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.ui.helper.KeyboardHelper;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.models.sending_model.UserAction;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.AnyEditTextView;
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

public class SearchFragment extends BaseFragment implements OnItemClickListener, PagingDelegate.OnPageListener {

    @BindView(R.id.progressBar_activitymain)
    ProgressBar progressBarActivitymain;
    @BindView(R.id.btnclear)
    ImageView btnclear;
    @BindView(R.id.txtsearch)
    AnyEditTextView txtsearch;
    private SearchAdapter mediaAdapter;
    private int offset;
    private static int limit = 8;
    private int x = 0;
    private String text;
    Unbinder unbinder;
    @BindView(R.id.recyclerview_childrenlaugh)
    ShimmerRecyclerView recyclerviewChildrenlaugh;
    Call<WebResponse<Object>> categoriesCall;
    private ArrayList<MediaModel> arrCategory;
    private MediaPlayer mediaPlayer;
    int fileTypeValue = AppConstants.FILE_TYPE_PUBLIC;
    Call<WebResponse<Object>> favCall, unfavCall;
    private boolean isSearchBarEmpty = true;


    public static SearchFragment newInstance() {

        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_search;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("Search Mantras");
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrCategory = new ArrayList<>();
        mediaAdapter = new SearchAdapter(getContext(), arrCategory, this);

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
        //  getHomeActivity().showBottomBar();
        //arrCategory.clear();

        bindRecyclerView();
      /*  if (onCreated) {
            return;
        }
*/
        txtsearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (txtsearch.getStringTrimmed().equalsIgnoreCase("")) {
                    isSearchBarEmpty = true;
                    btnclear.setVisibility(View.GONE);

                } else {
                    isSearchBarEmpty = false;
                    btnclear.setVisibility(View.VISIBLE);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

                if (txtsearch.getText().length() > 2) {

                    text = String.valueOf(txtsearch.getText());
                    getMedia(limit, 0);
                }

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        if (isSearchBarEmpty) {
            btnclear.setVisibility(View.GONE);
        } else {
            btnclear.setVisibility(View.VISIBLE);
        }


        KeyboardHelper.showSoftKeyboardForcefully(getContext(), txtsearch);
        KeyboardHelper.showSoftKeyboard(getContext(), txtsearch);
    }


    private void getMedia(int limit, int offset) {


        recyclerviewChildrenlaugh.showShimmer();

        Map<String, Object> mquery = new HashMap<>();
        mquery.put("query", text);

        categoriesCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.Q_PARAM_SEARCH, mquery, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                Type type = new TypeToken<ArrayList<MediaModel>>() {
                }.getType();
                ArrayList<MediaModel> arrayList = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type);

                recyclerviewChildrenlaugh.hideShimmer();

                arrCategory.clear();
                arrCategory.addAll(arrayList);
                mediaAdapter.notifyDataSetChanged();


                //onDonePaging();
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

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerviewChildrenlaugh.setLayoutManager(mLayoutManager1);
        ((DefaultItemAnimator) recyclerviewChildrenlaugh.getItemAnimator()).setSupportsChangeAnimations(false);
        PagingDelegate pagingDelegate = new PagingDelegate.Builder(mediaAdapter)
                .attachTo(recyclerviewChildrenlaugh)
                .listenWith(SearchFragment.this)
                .build();
        recyclerviewChildrenlaugh.setAdapter(mediaAdapter);
        recyclerviewChildrenlaugh.setItemViewType((type, position) -> R.layout.shimmer_customlist_listsearch);

        //mediaAdapter.notifyDataSetChanged();

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


    @Override
    public void onItemClick(int position, Object object, View view, String adapterName) {


        boolean isNotNull = UIHelper.testArrayListAndPosition(arrCategory, position);

        if (isNotNull) {
            switch (view.getId()) {
                case R.id.layout1:
                    for (MediaModel mediaModel : arrCategory) {
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            //  selected = false;
                            mediaModel.setMedia1(false);
                            arrCategory.get(position).setMedia1(false);
                            mediaAdapter.notifyDataSetChanged();
                        }
                    }
                    for (MediaModel mediaModel : arrCategory) {
                        mediaModel.setChoice1(false);
                    }
                    arrCategory.get(position).setChoice1(true);
                    mediaAdapter.notifyDataSetChanged();
                    break;
                case R.id.imgbtn_close:
                    for (MediaModel mediaModel : arrCategory) {
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            //  selected = false;
                            mediaModel.setMedia1(false);
                            arrCategory.get(position).setMedia1(false);
                            mediaAdapter.notifyDataSetChanged();
                        }
                    }
                    arrCategory.get(position).setChoice1(false);
                    mediaAdapter.notifyItemChanged(position);
                    break;
                case R.id.detail:
                    MediaModel model2 = (MediaModel) object;
                    getBaseActivity().addDockableFragment(DetailFragment.newInstance(model2), true);
                    break;
                case R.id.reminder:
                    MediaModel mediaReminder = (MediaModel) object;
                    getBaseActivity().addDockableFragment(MantraDetailFragment.newInstance(mediaReminder, FragmentName.SearchFragment, fileTypeValue, mediaReminder.getName(), mediaReminder.getFileAbsoluteUrl(), mediaReminder.getMediaLength()), true);
                    break;
                case R.id.fav:
                    MediaModel media = (MediaModel) object;
                    onFavClick(media.getId());
                    arrCategory.get(position).setIsFavourite(0);
                    mediaAdapter.notifyItemChanged(position);
                    break;
                case R.id.unfav:
                    MediaModel media2 = (MediaModel) object;
                    onUnFavClick(media2.getId());
                    arrCategory.get(position).setIsFavourite(1);
                    mediaAdapter.notifyItemChanged(position);
                    break;
                case R.id.playMedia:
                    try {
                        if ((arrCategory.get(position).isMedia1())) {
                            arrCategory.get(position).setMedia1(false);
                            mediaPlayer.release();
                            mediaAdapter.notifyItemChanged(position);
                        } else {
                            for (MediaModel mediaModel : arrCategory) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                    mediaModel.setMedia1(false);
                                    arrCategory.get(position).setMedia1(false);
                                    mediaAdapter.notifyDataSetChanged();
                                }
                            }
                            arrCategory.get(position).setMedia1(true);
                            mediaAdapter.notifyDataSetChanged();
                            mediaPlayer = new MediaPlayer();

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
                                    arrCategory.get(position).setMedia1(false);
                                    mediaAdapter.notifyItemChanged(position);
                                    mediaPlayer.release();
                                    //selected = false;
                                }
                            });

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

/*
    @Override
    public void onPage(int i) {

        if (offset < i) {

            offset = i;

            x++;
            progressBarActivitymain.setVisibility(View.VISIBLE);

            getMedia(limit, i);
        }
    }

    @Override
    public void onDonePaging() {


        if (progressBarActivitymain != null) {
            progressBarActivitymain.setVisibility(View.GONE);
        }

    }*/

    @OnClick(R.id.btnclear)
    public void onViewClicked() {
        txtsearch.setText("");
        isSearchBarEmpty = true;
        arrCategory.clear();
        btnclear.setVisibility(View.GONE);
        KeyboardHelper.hideSoftKeyboard(getContext(), btnclear);
        mediaAdapter.notifyDataSetChanged();
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
        if (categoriesCall != null) {
            categoriesCall.cancel();
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


    @Override
    public void onPage(int offset) {

    }

    @Override
    public void onDonePaging() {

    }
}
