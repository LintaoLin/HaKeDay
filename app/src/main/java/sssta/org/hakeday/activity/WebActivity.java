package sssta.org.hakeday.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sssta.org.hakeday.R;

public class WebActivity extends AppCompatActivity {

    @BindView(R.id.web_view)
    WebView webView;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        init();
    }

    @SuppressLint("JavascriptInterface")
    private void init() {
        webView.getSettings()
                .setJavaScriptEnabled(true);
        webView.getSettings()
                .setDomStorageEnabled(true);
        webView.addJavascriptInterface(this, "swt");
        webView.loadUrl("file:///android_asset/Hackday/new.html");
    }

    public void showMessage(String message) {
        dialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .show();
    }


    public void track(String title, String content) {

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
