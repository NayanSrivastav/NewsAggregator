package screen;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import datalicious.com.news.R;
import datalicious.com.news.utils.Constants;

/**
 * Created by nayan on 11/7/16.
 */
public class WebViewFragment extends DataliciousFragment {

    public static Fragment getInstance(String url) {
        Fragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(Constants.URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview_fragment, container, false);
        WebView webView = (WebView) view.findViewById(R.id.web_view);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.show();
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress>70)
                {
                    progressDialog.dismiss();
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(getArguments().getString(Constants.URL));
        return view;
    }
}
