package com.example.movieticketbooking.movie_page;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.example.movieticketbooking.R;

public class YouTubeDialogHelper {

    public static void showYouTubeVideoDialog(Context context, String videoUrl) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mp_trailer_dialog);

        WebView webView = dialog.findViewById(R.id.mp_youtube_video_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Close button click listener
        ImageButton closeButton = dialog.findViewById(R.id.mp_close_button);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        // Play video when the dialog is shown
        dialog.setOnShowListener(dialogInterface -> {
            webView.loadData(videoUrl, "text/html", "utf-8");
            webView.setWebChromeClient(new WebChromeClient());
        });

        // Stop video when the dialog is dismissed
        dialog.setOnDismissListener(dialogInterface -> {
            webView.stopLoading();
            webView.clearCache(true);
            webView.clearHistory();
            webView.loadUrl("about:blank");
        });

        // Full-screen dialog
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
    }
}
