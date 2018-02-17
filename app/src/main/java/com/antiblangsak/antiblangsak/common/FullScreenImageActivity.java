package com.antiblangsak.antiblangsak.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.antiblangsak.antiblangsak.utils.ImageUtil;
import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;
import com.antiblangsak.antiblangsak.app.AppHelper;
import com.github.chrisbanes.photoview.PhotoView;


public class FullScreenImageActivity extends AppCompatActivity {

    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        sharedPrefManager = new SharedPrefManager(this);

        PhotoView fullScreenImageView = (PhotoView) findViewById(R.id.fullScreenImageView);

        Intent callingActivityIntent = getIntent();
        if(callingActivityIntent != null) {
            Bitmap bitmap = ImageUtil.convert(sharedPrefManager.getPhoto(callingActivityIntent.getStringExtra("TYPE")));
            if(bitmap != null && fullScreenImageView != null) {
                AppHelper.showImageWithGlide(this, bitmap, fullScreenImageView);
            }
        }
    }
}
