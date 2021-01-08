package com.example.socialmacropad.activities.introAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.socialmacropad.R;

public class LearnPDFActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_p_d_f);

        WebView webview = (WebView)findViewById(R.id.webview);
        ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressbar);
        webview.getSettings().setJavaScriptEnabled(true);
        String filename ="https://raw.githubusercontent.com/memoriasIT/memoriasIT.github.io/master/library/manual.pdf";
        webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + filename);

        webview.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                progressbar.setVisibility(View.GONE);
            }
        });
    }
}