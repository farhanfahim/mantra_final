package com.tekrevol.mantra.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.activities.MainActivity;
import com.tekrevol.mantra.broadcast.ExampleService;
import com.tekrevol.mantra.callbacks.OnNewPacketReceivedListener;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.fragments.abstracts.GenericContentFragment;
import com.tekrevol.mantra.libraries.imageloader.ImageLoaderHelper;
import com.tekrevol.mantra.managers.ObjectBoxManager;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.models.receiving_model.Slug;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

/**
 * Created by khanhamza on 09-May-17.
 */

public class LeftSideMenuFragment extends BaseFragment implements OnNewPacketReceivedListener {

    Unbinder unbinder;
    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.profile)
    AnyTextView profile;
    @BindView(R.id.about)
    AnyTextView about;
    @BindView(R.id.libraryOfPositivity)
    AnyTextView libraryOfPositivity;
    @BindView(R.id.privacy)
    AnyTextView privacy;
    @BindView(R.id.logout)
    AnyTextView logout;
    @BindView(R.id.cancel)
    ImageButton cancel;
    @BindView(R.id.scrollView1)
    ScrollView scrollView1;
    @BindView(R.id.changepass)
    AnyTextView changepass;
    @BindView(R.id.image)
    CircleImageView image;
    @BindView(R.id.txt_username)
    AnyTextView txtUsername;
    @BindView(R.id.txt_email)
    AnyTextView txtEmail;
    Call<WebResponse<Object>> aboutCall;


    public static LeftSideMenuFragment newInstance() {

        Bundle args = new Bundle();
        LeftSideMenuFragment fragment = new LeftSideMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_sidebar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindData();
        ////        scrollToTop();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void bindData() {

        if (getCurrentUser() != null) {
            ImageLoaderHelper.loadImageWithAnimations(image, getCurrentUser().getUserDetails().getImageUrl(), true);
            txtEmail.setText(getCurrentUser().getEmail());
            txtUsername.setText(getCurrentUser().getUserDetails().getFullName());
        }
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {

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
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_UNDEFINED;
    }


    @OnClick({R.id.btnBack, R.id.profile, R.id.about, R.id.libraryOfPositivity,R.id.privacy, R.id.logout, R.id.cancel, R.id.changepass, R.id.feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
            case R.id.cancel:
                getBaseActivity().onBackPressed();
                break;
            case R.id.profile:
                getBaseActivity().addDockableFragment(ProfileFragment.newInstance(), true);
                break;
            case R.id.about:
                aboutAPI(AppConstants.KEY_ABOUT);
                break;
            case R.id.libraryOfPositivity:
                libraryOfPositivityAPI(AppConstants.KEY_LIBRARY_OF_POSITIVITY);
                break;
            case R.id.privacy:
                aboutAPI(AppConstants.KEY_PRIVACY);
                break;
            case R.id.logout:
                if (isMyServiceRunning(ExampleService.class)){
                    stopService();
                }
                //ObjectBoxManager.INSTANCE.removeAllDB();
                sharedPreferenceManager.clearDB();
                getBaseActivity().clearAllActivitiesExceptThis(MainActivity.class);

                break;
            case R.id.feedback:
                getBaseActivity().addDockableFragment(FeedbackFragment.newInstance(), true);
                break;
            case R.id.changepass:
                getBaseActivity().addDockableFragment(ChangePasswordFragment.newInstance(), true);
                break;
        }
    }

    public void stopService() {
        AppConstants.IS_LOGOUT = true;
        Intent serviceIntent = new Intent(getContext(), ExampleService.class);
        getContext().stopService(serviceIntent);

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void aboutAPI(String slugId) {

        Map<String, Object> queryMap = new HashMap<>();

        aboutCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_PAGES + "/" + slugId, queryMap, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {


                Slug pagesModel = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , Slug.class);

                getBaseActivity().addDockableFragment(GenericContentFragment.newInstance(pagesModel.getTitle(), pagesModel.getContent(), true), false);

            }

            @Override
            public void onError(Object object) {

            }
        });

    }

    private void libraryOfPositivityAPI(String slugId) {

        Map<String, Object> queryMap = new HashMap<>();

        aboutCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_PAGES + "/" + slugId, queryMap, new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {


                Slug pagesModel = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , Slug.class);

                getBaseActivity().addDockableFragment(GenericContentFragment.newInstance(pagesModel.getTitle(), pagesModel.getContent(), true), false);

            }

            @Override
            public void onError(Object object) {

            }
        });

    }


    @Override
    public void onDestroyView() {

        if (aboutCall != null) {
            aboutCall.cancel();
        }
        unbinder.unbind();


        super.onDestroyView();

    }


}
