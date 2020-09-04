package com.tekrevol.mantra.fragments.abstracts;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tekrevol.mantra.R;
import com.tekrevol.mantra.widget.AnyTextView;
import com.tekrevol.mantra.widget.TitleBar;

import butterknife.BindView;


/**
 * Created by khanhamza on 21-Feb-17.
 */

public class GenericContentFragment extends BaseFragment {

    @BindView(R.id.txtViewContent_generic_content)
    AnyTextView txtViewContentGenericContent;
    @BindView(R.id.webView)
    WebView webView;
    private String content = "";
    private String title = "";
    private boolean isShowWebView;


    public static GenericContentFragment newInstance(String title, String content, boolean isShowWebView) {
        Bundle args = new Bundle();
        GenericContentFragment fragment = new GenericContentFragment();
        fragment.title = title;
        fragment.content = content;
        fragment.isShowWebView = isShowWebView;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_generic_content;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews(view);
    }

    private void bindViews(View view) {

        webView.setBackgroundColor(Color.TRANSPARENT);
        if (isShowWebView) {
            txtViewContentGenericContent.setVisibility(View.GONE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
        } else {
            webView.setVisibility(View.GONE);
            txtViewContentGenericContent.setText(Html.fromHtml(content), TextView.BufferType.SPANNABLE);
        }
/*

        AnyTextView txtViewContent = view.findViewById(R.id.txtViewContent_generic_content);

        txtViewContent.setText(Html.fromHtml(content), TextView.BufferType.SPANNABLE);
*/

//        txtViewContent.setText(content);
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle(title);
        titleBar.showBackButton(getBaseActivity());
        titleBar.showRightInivisible();
    }


    @Override
    public void setListeners() {

    }

    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
    }


//    private void bindData(String key) {
//
//        showProgress();
//
//        WebServiceFactory.getInstance("0").getStaticPageData(0, key)
//                .enqueue(new Callback<WebResponse<Content>>() {
//                    @Override
//                    public void onResponse(Call<WebResponse<Content>> call, Response<WebResponse<Content>> responseCode) {
//
//                        if (responseCode.body().isSuccess()) {
//                            txtViewContent.setText(Html.fromHtml(responseCode.body().result.content));
//                            dismissProgress();
//                        } else {
//                            UIHelper.showToast(getContext(), responseCode.body().responseMessage);
//                            dismissProgress();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<WebResponse<Content>> call, Throwable t) {
//                        t.printStackTrace();
//                        dismissProgress();
//                    }
//                });
//    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
