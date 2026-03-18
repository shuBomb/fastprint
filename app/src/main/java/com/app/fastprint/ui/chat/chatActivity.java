package com.app.fastprint.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.fastprint.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class chatActivity extends AppCompatActivity {




    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        WebView webView = (WebView) findViewById(R.id.webviewChat);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://static.zdassets.com/web_widget/latest/liveChat.html?v=10#key=fastprint.zendesk.com");
    }

    public void onBackButtonClicked(View view) {

                finish();

    }

}