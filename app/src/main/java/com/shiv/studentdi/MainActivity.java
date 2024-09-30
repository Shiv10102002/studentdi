package com.shiv.studentdi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Timber.d("MainActivity started");
        TextView textView =  findViewById(R.id.hellowtext);
        textView.postDelayed(()->{
                    Intent intent = new Intent(MainActivity.this,HomeScreenActivity.class);
                    startActivity(intent);
                    Timber.d("navigated from mainActivity to homScreenActivity");

                    finish();
                },2000
        );
    }
}