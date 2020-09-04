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
import com.tekrevol.mantra.adapters.recyleradapters.MediaAdapter;
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

public class CategoryViewAllFragment extends BaseFragment implements OnItemClickListener, PagingDelegate.OnPageListener {


    Call<WebResponse<Object>> favCall, unfavCall;
    Unbinder unbinder;
    @BindView(R.id.recyclerview_childrenlaugh)
    ShimmerRecyclerView recyclerviewChildrenlaugh;
    @BindView(R.id.progressBar_activitymain)
    ProgressBar progressBarActivitymain;
    private MediaAdapter mediaAdapter;
    private ArrayList<MediaModel> arrCategory;
    private int categoryid;
    private String name;
    private int offset;
    private static int limit = 11;
    private Boolean selected = false;
    private MediaPlayer mediaPlayer;
    int fileTypeValue = AppConstants.FILE_TYPE_PUBLIC;

    Call<WebResponse<Object>> categoriesCall;

    public static CategoryViewAllFragment newInstance(int id, String name) {

        Bundle args = new Bundle();
        CategoryViewAllFragment fragment = new CategoryViewAllFragment();
        fragment.categoryid = id;
        fragment.name = name;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_categories_view_all;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle(name);
        titleBar.showResideMenu(getHomeActivity());
        titleBar.showBackButton(getBaseActivity());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrCategory = new ArrayList<>();
        mediaAdapter = new MediaAdapter(getContext(), arrCategory, this);

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


        arrCategory.clear();
        bindRecyclerView();
        getMedia(limit, 0);

    }

    private void getMedia(int limit, int offset) {


        if (offset == 0) {
            recyclerviewChildrenlaugh.showShimmer();

        }

        Map<String, Object> mquery = new HashMap<>();
        mquery.put(WebServiceConstants.Q_PARAM_CATEGORIESID, categoryid);
        mquery.put(WebServiceConstants.Q_PARAM_LIMIT, limit);
        mquery.put(WebServiceConstants.Q_PARAM_OFFSET, offset);
        mquery.put(WebServiceConstants.Q_PARAM_ORDERBY, AppConstants.ORDER_BY_ID);
        mquery.put(WebServiceConstants.Q_PARAM_SORTED, AppConstants.SORTED_BY);


        categoriesCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_MEDIA, mquery, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                Type type = new TypeToken<ArrayList<MediaModel>>() {
                }.getType();
                ArrayList<MediaModel> arrayList = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type);


                // arrCategory.clear();

                if (offset == 0) {
                    recyclerviewChildrenlaugh.hideShimmer();

                }
                arrCategory.addAll(arrayList);
                mediaAdapter.notifyDataSetChanged();


                onDonePaging();
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
                .listenWith(CategoryViewAllFragment.this)
                .build();
        recyclerviewChildrenlaugh.setAdapter(mediaAdapter);
        recyclerviewChildrenlaugh.setItemViewType((type, position) -> R.layout.shimmer_customlist_listsearch);

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
    public void onDestroyView() {
        if (categoriesCall != null) {
            categoriesCall.cancel();
        }
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
                case R.id.reminder:
                    MediaModel mediaReminder = (MediaModel) object;
                    getBaseActivity().addDockableFragment(MantraDetailFragment.newInstance(mediaReminder, FragmentName.CategoryViewAllFragment, fileTypeValue, mediaReminder.getName(), mediaReminder.getFileAbsoluteUrl(), mediaReminder.getMediaLength()), true);
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
                case R.id.detail:
                    MediaModel model2 = (MediaModel) object;
                    getBaseActivity().addDockableFragment(DetailFragment.newInstance(model2), true);
                    break;
                case R.id.playMedia:
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
                                    arrCategory.get(position).setMedia1(false);
                                    mediaAdapter.notifyItemChanged(position);
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

    @Override
    public void onPage(int i) {

        if (offset < i) {

            offset = i;

            progressBarActivitymain.setVisibility(View.VISIBLE);

            getMedia(limit, i);
        }
    }

    @Override
    public void onDonePaging() {

        if (progressBarActivitymain != null) {
            progressBarActivitymain.setVisibility(View.GONE);
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
}
