package com.example.p5_213.ui;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.R;


public final class StartActivity extends AppCompatActivity {
    public void onCreate(Bundle s){ super.onCreate(s);
        setContentView(R.layout.activity_start);
        findViewById(R.id.btnStart)
                .setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));
    }
}
