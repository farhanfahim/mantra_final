package com.tekrevol.mantra.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.tekrevol.mantra.libraries.imageloader.ImageLoaderHelper;
import com.tekrevol.mantra.widget.AnyTextView;
import com.jsibbold.zoomage.ZoomageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.tekrevol.mantra.R;

import static com.tekrevol.mantra.constatnts.AppConstants.IMAGE_PREVIEW_TITLE;
import static com.tekrevol.mantra.constatnts.AppConstants.IMAGE_PREVIEW_URL;

public class ImagePreviewActivity extends AppCompatActivity {

    @BindView(R.id.image)
    ZoomageView image;
    @BindView(R.id.txtTitle)
    AnyTextView txtTitle;

    @BindView(R.id.contProfilePicOptions)
    LinearLayout contProfilePicOptions;

    String url;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        ButterKnife.bind(this);
        getData();
    }

    private void getData() {
        url = getIntent().getStringExtra(IMAGE_PREVIEW_URL);
        title = getIntent().getStringExtra(IMAGE_PREVIEW_TITLE);
        contProfilePicOptions.setVisibility(View.VISIBLE);

        txtTitle.setText(title);
        loadImage(url);
    }

    private void loadImage(String url) {
        ImageLoaderHelper.loadImageWithConstantHeaders(this, image, url, true);
    }


    @OnClick({R.id.btnBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                finish();
                break;

        }
    }


}
