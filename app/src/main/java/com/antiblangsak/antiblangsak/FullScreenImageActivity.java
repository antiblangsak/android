package com.antiblangsak.antiblangsak;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;


public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        PhotoView fullScreenImageView = (PhotoView) findViewById(R.id.fullScreenImageView);

        Intent callingActivityIntent = getIntent();
        if(callingActivityIntent != null) {
            Uri imageUri = callingActivityIntent.getData();
            if(imageUri != null && fullScreenImageView != null) {
                fullScreenImageView.setImageURI(imageUri);
            }
        }
    }
}
