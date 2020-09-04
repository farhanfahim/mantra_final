package com.tekrevol.mantra.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tekrevol.mantra.R;
import com.tekrevol.mantra.adapters.recyleradapters.HomeChildLaughAdapter;
import com.tekrevol.mantra.adapters.recyleradapters.HomeDailyMantraAdapter;
import com.tekrevol.mantra.adapters.recyleradapters.MovieLineAdapter;
import com.tekrevol.mantra.callbacks.OnItemClickListener;
import com.tekrevol.mantra.constatnts.AppConstants;
import com.tekrevol.mantra.constatnts.WebServiceConstants;
import com.tekrevol.mantra.enums.FileType;
import com.tekrevol.mantra.enums.FragmentName;
import com.tekrevol.mantra.fragments.abstracts.BaseFragment;
import com.tekrevol.mantra.helperclasses.ui.helper.UIHelper;
import com.tekrevol.mantra.libraries.imageloader.ImageLoaderHelper;
import com.tekrevol.mantra.managers.retrofit.WebServices;
import com.tekrevol.mantra.managers.retrofit.entities.MultiFileModel;
import com.tekrevol.mantra.models.UserDetails;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.models.receiving_model.UserModel;
import com.tekrevol.mantra.models.sending_model.EditProfileSendingModel;
import com.tekrevol.mantra.models.wrappers.WebResponse;
import com.tekrevol.mantra.widget.AnyEditTextView;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment implements OnItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.img_pic)
    CircleImageView imgPic;
    @BindView(R.id.txt_seeall_mantra)
    TextView txtSeeallMantra;
    @BindView(R.id.txt_seeall_movieline)
    TextView txtSeeallMovieline;
    @BindView(R.id.recyclerview_movie_lines)
    ShimmerRecyclerView recyclerviewMovieLines;
    @BindView(R.id.camera_icon)
    ImageView cameraIcon;
    @BindView(R.id.txt_username)
    AnyTextView txtUsername;
    @BindView(R.id.txt_email)
    AnyTextView txtEmail;
    @BindView(R.id.txtmantracount)
    AnyTextView txtmantracount;
    @BindView(R.id.txtsharedmantracount)
    AnyTextView txtsharedmantracount;
    @BindView(R.id.about)
    AnyTextView about;
    @BindView(R.id.txtAbout)
    AnyTextView txtAbout;
    @BindView(R.id.edttxt_email)
    AnyEditTextView edttxtEmail;
    @BindView(R.id.edttxtAbout)
    AnyEditTextView edttxtAbout;
    @BindView(R.id.sharedmantra)
    LinearLayout sharedmantra;
    @BindView(R.id.sheduledmantra)
    LinearLayout sheduledmantra;
    @BindView(R.id.favmantra)
    LinearLayout favmantra;
    @BindView(R.id.draftmantra)
    LinearLayout draftmantra;
    Call<WebResponse<Object>> webCall;
    private boolean isTitlebar = false;


    private File fileTemporaryProfilePicture;
    private MovieLineAdapter homeMovieLinesAdapter;
    private ArrayList<MediaModel> arrListMantras, arrShareMantra, arrSheduledMantra, arrFavMantra, arrDraftMantra;

    public static ProfileFragment newInstance() {

        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_UNLOCKED;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle("Profile");
        titleBar.showSidebar(getBaseActivity());
        if (isTitlebar == false) {
            titleBar.setVisibilityButton(true);
            titleBar.showEditProfile(getHomeActivity(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //  isTitlebar = true;
                    cameraIcon.setVisibility(View.VISIBLE);
                    txtAbout.setVisibility(View.GONE);
                    edttxtAbout.setVisibility(View.VISIBLE);
                    txtUsername.setVisibility(View.GONE);
                    edttxtEmail.setVisibility(View.VISIBLE);
                    titleBar.setVisibilityButton(false);
                    about.setVisibility(View.VISIBLE);

                }
            });
        } else {
            titleBar.setVisibilityButton(false);
        }
        titleBar.showSaveProfile(getHomeActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editProfileAPI();
                if (isTitlebar == true) {
                    isTitlebar = false;
                    cameraIcon.setVisibility(View.GONE);
                    edttxtAbout.setVisibility(View.GONE);
                    txtAbout.setVisibility(View.VISIBLE);
                    edttxtEmail.setVisibility(View.GONE);
                    txtUsername.setVisibility(View.VISIBLE);
                    titleBar.setVisibilityButton(true);
                }


            }
        });

        titleBar.showBackButton(getBaseActivity());

    }

    private void editProfileAPI() {


        if (edttxtEmail.testValidity()) {

            EditProfileSendingModel editProfileSendingModel = new EditProfileSendingModel();
            ArrayList<MultiFileModel> arrMultiFileModel = new ArrayList<>();

            // Adding Images
            if (fileTemporaryProfilePicture != null) {
                arrMultiFileModel.add(new MultiFileModel(fileTemporaryProfilePicture, FileType.IMAGE, "image"));
            }
            isTitlebar = true;

            editProfileSendingModel.setAbout(edttxtAbout.getStringTrimmed());
            editProfileSendingModel.setName(edttxtEmail.getStringTrimmed());
            txtAbout.setText(edttxtAbout.getStringTrimmed());
            txtUsername.setText(edttxtEmail.getStringTrimmed());

            getBaseWebServices(true).postMultipartAPI(WebServiceConstants.PATH_PROFILE, arrMultiFileModel, editProfileSendingModel.toString(), new WebServices.IRequestWebResponseAnyObjectCallBack() {
                @Override
                public void requestDataResponse(WebResponse<Object> webResponse) {

                    UIHelper.showToast(getContext(), webResponse.message);
                    UserDetails userDetails = getGson().fromJson(getGson().toJson(webResponse.result), UserDetails.class);
                    UserModel currentUser = sharedPreferenceManager.getCurrentUser();
                    currentUser.setName(userDetails.getFullName().trim());
                    currentUser.setUserDetails(userDetails);
                    sharedPreferenceManager.putObject(AppConstants.KEY_CURRENT_USER_MODEL, currentUser);
                    getBaseActivity().leftSideMenuFragment.bindData();
                    getBaseActivity().popBackStack();
                }

                @Override
                public void onError(Object object) {

                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //arrDailyMantra = new ArrayList<>();
        //  arrChildrenLaugh = new ArrayList<>();
       /* arrShareMantra = new ArrayList<>();
        arrSheduledMantra = new ArrayList<>();
        arrFavMantra = new ArrayList<>();
        arrDraftMantra = new ArrayList<>();*/
        //arrListMantras = new ArrayList<>();
        //  homeDailyMantraAdapter = new HomeDailyMantraAdapter(getContext(), arrDailyMantra, this);
        //homeChildLaughAdapter = new HomeChildLaughAdapter(getContext(), arrChildrenLaugh, this);
        // homeMovieLinesAdapter = new MovieLineAdapter(getContext(), arrListMantras, this);
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
        //  bindRecyclerView();

        if (fileTemporaryProfilePicture == null) {
            ImageLoaderHelper.loadImageWithAnimations(imgPic, getCurrentUser().getUserDetails().getImageUrl(), true);
        } else {
            ImageLoaderHelper.loadImageWithAnimations(imgPic, fileTemporaryProfilePicture.getAbsolutePath(), true);
        }

        //getData();

    }

    private void bindData() {

        txtEmail.setText(getCurrentUser().getEmail());
        edttxtEmail.setText(getCurrentUser().getUserDetails().getFullName());
        txtUsername.setText(getCurrentUser().getUserDetails().getFullName());

        if (getCurrentUser().getUserDetails().getAbout() != null) {
            about.setVisibility(View.VISIBLE);
            txtAbout.setVisibility(View.VISIBLE);
            txtAbout.setText(getCurrentUser().getUserDetails().getAbout());
            edttxtAbout.setText(getCurrentUser().getUserDetails().getAbout());

        }
        webCall = getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_ME, "", new WebServices.IRequestWebResponseAnyObjectCallBack() {
            @Override
            public void requestDataResponse(WebResponse<Object> webResponse) {

                UserModel userModel = getGson().fromJson(getGson().toJson(webResponse.result), UserModel.class);
                UserModel currentUser = sharedPreferenceManager.getCurrentUser();
                currentUser.setUserDetails(userModel.getUserDetails());
                sharedPreferenceManager.putObject(AppConstants.KEY_CURRENT_USER_MODEL, currentUser);
                txtsharedmantracount.setText(String.valueOf(currentUser.getUserDetails().getShareMantraCount()));
                txtmantracount.setText(String.valueOf(currentUser.getUserDetails().getMyMantraCount()));

            }

            @Override
            public void onError(Object object) {

            }
        });
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
    public void onDestroyView() {
        if (webCall != null) {
            webCall.cancel();
        }

        unbinder.unbind();
        super.onDestroyView();

    }

    @Override
    public void onItemClick(int position, Object object, View view, String adapterName) {
        if (HomeDailyMantraAdapter.class.getSimpleName().equals(adapterName)) {

            MediaModel model = (MediaModel) object;
            getBaseActivity().addDockableFragment(DetailFragment.newInstance(model), true);

        } else if (HomeChildLaughAdapter.class.getSimpleName().equals(adapterName)) {
            // getBaseActivity().addDockableFragment(CategoryViewAllFragment.newInstance(model.getId()), true);

        }
    }

    @OnClick({R.id.txt_seeall_mantra, R.id.txt_seeall_movieline, R.id.camera_icon, R.id.sheduledmantra, R.id.sharedmantra, R.id.favmantra, R.id.draftmantra})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_seeall_mantra:
                //   getBaseActivity().addDockableFragment(ProfileSeeAllFragment.newInstance(TAG), true);
                break;
            case R.id.txt_seeall_movieline:
                //      getBaseActivity().addDockableFragment(MovieLinesSeeAllFragment.newInstance(), true);
                break;

            case R.id.camera_icon:
                isTitlebar = true;
                UIHelper.cropImagePicker(getContext(), this);
                break;
            case R.id.sheduledmantra:
                isTitlebar = false;
                getBaseActivity().addDockableFragment(ScheduleMantraFragment.newInstance(FragmentName.ScheduledMantra), true);

                //    populateMantra(arrSheduledMantra);
                break;
            case R.id.sharedmantra:
                isTitlebar = false;
                getBaseActivity().addDockableFragment(ProfileSeeAllFragment.newInstance(FragmentName.SharedMantra), true);

                //    populateMantra(arrShareMantra);
                break;
            case R.id.favmantra:
                isTitlebar = false;
                getBaseActivity().addDockableFragment(ProfileSeeAllFragment.newInstance(FragmentName.FavouriteMantra), true);

//                populateMantra(arrFavMantra);
                break;
            case R.id.draftmantra:
                isTitlebar = false;
                getBaseActivity().addDockableFragment(ProfileSeeAllFragment.newInstance(FragmentName.DraftMantra), true);

//                populateMantra(arrDraftMantra);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                fileTemporaryProfilePicture = new File(result.getUri().getPath());
//                uploadImageFile(fileTemporaryProfilePicture.getPath(), result.getUri().toString());
                setImageAfterResult(result.getUri().toString());


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }

    private void setImageAfterResult(final String uploadFilePath) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ImageLoader.getInstance().displayImage(uploadFilePath, imgPic);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
