package com.tekrevol.mantra.fragments;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
import retrofit2.Call;

import static com.tekrevol.mantra.constatnts.WebServiceConstants.PATH_ACTIONS;
import static com.tekrevol.mantra.constatnts.WebServiceConstants.PATH_MEDIA;

public class PrivateFragment extends BaseFragment implements OnItemClickListener, PagingDelegate.OnPageListener {
    @BindView(R.id.recyclerview_movie_lines)
    ShimmerRecyclerView recyclerviewMovieLines;
    @BindView(R.id.progressBar_activitymain)
    ProgressBar progressBarActivitymain;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    private ProfileMediaAdapter adapter;
    private ArrayList<MediaModel> arrayList;
    Call<WebResponse<Object>> mediaCall;
    private int offset;
    private static int limit = 11;
    private int x = 0;
    private MediaPlayer mediaPlayer;
    private Boolean selected = false;
    Call<WebResponse<Object>> favCall, unfavCall, deleteCall;
    private Boolean delete = false;
    int fileTypeValue = AppConstants.FILE_TYPE_PUBLIC;


    @Override
    public int getDrawerLockMode() {
        return 0;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_mymantra;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {

    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList = new ArrayList<>();
        adapter = new ProfileMediaAdapter(getContext(), arrayList, FragmentName.DraftMantra, this);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arrayList.clear();
        bindRecyclerView();
        /*if (onCreated) {
            return;
        }*/
    }

    private void getMantras(int limit, int offset) {

        Map<String, Object> mquery = new HashMap<>();
        if (x == 0) {
            recyclerviewMovieLines.showShimmer();
        }
        mquery.put(WebServiceConstants.Q_DRAFT, 1);
        mquery.put(WebServiceConstants.Q_PARAM_LIMIT, limit);
        mquery.put(WebServiceConstants.Q_PARAM_OFFSET, offset);
        mquery.put(WebServiceConstants.Q_PARAM_ORDERBY, AppConstants.ORDER_BY_ID);
        mquery.put(WebServiceConstants.Q_PARAM_SORTED, AppConstants.SORTED_BY);
        mediaCall = getBaseWebServices(false).getAPIAnyObject(PATH_MEDIA, mquery, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {
                Type type = new TypeToken<ArrayList<MediaModel>>() {
                }.getType();
                ArrayList<MediaModel> arrayList1 = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type);


                if (x == 0) {
                    recyclerviewMovieLines.hideShimmer();
                }
           /*     if (arrayList1.size() > 0) {
                    recyclerviewMovieLines.setVisibility(View.VISIBLE);
                    txtTitle.setVisibility(View.GONE);
                }else {
                    recyclerviewMovieLines.setVisibility(View.GONE);
                    txtTitle.setText("No Private Mantra");
                    txtTitle.setVisibility(View.VISIBLE);
                }*/
                arrayList.addAll(arrayList1);
                adapter.notifyDataSetChanged();
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

    private void bindRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerviewMovieLines.setLayoutManager(mLayoutManager1);
        ((DefaultItemAnimator) recyclerviewMovieLines.getItemAnimator()).setSupportsChangeAnimations(false);
        PagingDelegate pagingDelegate = new PagingDelegate.Builder(adapter)
                .attachTo(recyclerviewMovieLines)
                .listenWith(PrivateFragment.this)
                .build();
        recyclerviewMovieLines.setAdapter(adapter);
        recyclerviewMovieLines.setItemViewType((type, position) -> R.layout.shimmer_customlist_listsearch);
        getMantras(limit, 0);

    }

    @Override
    public void onItemClick(int position, Object object, View view, String adapterName) {

        boolean isNotNull = UIHelper.testArrayListAndPosition(arrayList, position);

        if (isNotNull) {

            switch (view.getId()) {
                case R.id.layout1:
                    for (MediaModel mediaModel : arrayList) {
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            //  selected = false;
                            mediaModel.setMedia1(false);
                            arrayList.get(position).setMedia1(false);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    for (MediaModel mediaModel : arrayList) {
                        mediaModel.setChoice1(false);
                    }
                    arrayList.get(position).setChoice1(true);
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.imgbtn_close:
                    for (MediaModel mediaModel : arrayList) {
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            //  selected = false;
                            mediaModel.setMedia1(false);
                            arrayList.get(position).setMedia1(false);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    arrayList.get(position).setChoice1(false);
                    adapter.notifyItemChanged(position);
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
                    onFavClick(media.getId());
                    arrayList.get(position).setIsFavourite(0);
                    adapter.notifyItemChanged(position);
                    break;
                case R.id.unfav:
                    MediaModel media2 = (MediaModel) object;
                    onUnFavClick(media2.getId());
                    arrayList.get(position).setIsFavourite(1);
                    adapter.notifyItemChanged(position);
                    break;
                case R.id.imgbtn_delete:
                    UIHelper.showAlertDialog("Are you sure you want to delete Mantra?", "Delete Mantra", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("sgd", "tww");
                            MediaModel mediadelete = (MediaModel) object;
                            deleteApi(mediadelete.getId(), position);
                        }
                    }, getContext());

                    break;
                case R.id.playMedia:
                    if ((arrayList.get(position).isMedia1())) {
                        arrayList.get(position).setMedia1(false);
                        mediaPlayer.release();
                        adapter.notifyItemChanged(position);
                    } else {
                        for (MediaModel mediaModel : arrayList) {
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                //  selected = false;
                                mediaModel.setMedia1(false);
                                arrayList.get(position).setMedia1(false);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        arrayList.get(position).setMedia1(true);
                        adapter.notifyDataSetChanged();
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
                                    arrayList.get(position).setMedia1(false);
                                    adapter.notifyItemChanged(position);
                                    mediaPlayer.release();
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
                        for (MediaModel mediaModel : arrayList) {
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                //  selected = false;
                                mediaModel.setMedia1(false);
                                arrayList.get(position).setMedia1(false);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                arrayList.remove(position);
                adapter.notifyItemRemoved(position);
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

    @Override
    public void onPage(int i) {
        if (offset < i) {

            offset = i;

            x++;
            progressBarActivitymain.setVisibility(View.VISIBLE);

            getMantras(limit, i);
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

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
