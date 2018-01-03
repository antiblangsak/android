package com.antiblangsak.antiblangsak;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;


public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        PhotoView fullScreenImageView = (PhotoView) findViewById(R.id.fullScreenImageView);

        Intent callingActivityIntent = getIntent();
        if(callingActivityIntent != null) {
            Bitmap bitmap = ImageUtil.convert(getIntent().getStringExtra(AppConstant.KEY_IMAGE_BASE64));
            if(bitmap != null && fullScreenImageView != null) {
                AppHelper.showImageWithGlide(this, bitmap, fullScreenImageView);
            }
        }
    }
}
