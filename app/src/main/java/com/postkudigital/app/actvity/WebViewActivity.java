package com.postkudigital.app.actvity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.postkudigital.app.R;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar bar;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Bundle p = getIntent().getExtras();
        String alamat =p.getString("url");
        Log.e("URL", alamat);
        // Toast.makeText(this, alamat, Toast.LENGTH_SHORT).show();
        webView = (WebView) findViewById(R.id.webView);
        bar=(ProgressBar) findViewById(R.id.progressBar4);
        backButton = findViewById(R.id.back_btn);

        bar.setVisibility(View.VISIBLE);

        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.setWebViewClient(new myWebclient());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.clearCache(true);
                webView.clearHistory();
                webView.loadUrl(alamat);
            }
        }, 3000);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class myWebclient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            bar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            bar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Log.e("URL", "error");
            super.onReceivedError(view, request, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode==KeyEvent.KEYCODE_BACK) && webView.canGoBack()){
            webView.goBack();
            this.finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}