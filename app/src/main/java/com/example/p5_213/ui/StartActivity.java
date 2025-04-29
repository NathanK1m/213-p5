/**
 * Represents the starting screen of the application.
 * Displays a "Start" button that, when clicked, navigates the user to the main menu.
 * @author Ryan Bae
 */
package com.example.p5_213.ui;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.R;

/**
 * StartActivity class handles the initial splash or welcome screen of the app.
 */
public final class StartActivity extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     * Sets up the layout and button listener to move to the MenuActivity.
     *
     * @param s savedInstanceState if re-creating the activity from a previous state.
     */
    public void onCreate(Bundle s){ super.onCreate(s);
        setContentView(R.layout.activity_start);
        findViewById(R.id.btnStart).setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));
    }
}
