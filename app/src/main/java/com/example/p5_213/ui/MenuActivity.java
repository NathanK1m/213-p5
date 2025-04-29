/**
 * Handles the main menu scene.
 * Handles navigation between different burger, sandwich, beverage, side, checkout, and orders placed scenes.
 *  * Buttons:
 *  * - Burger → BurgerActivity
 *  * - Sandwich → SandwichActivity
 *  * - Side → SideActivity
 *  * - Drink → BeverageActivity
 *  * - Cart → CartActivity
 *  * - History → ArchiveActivity
 *  * - Start Over → StartActivity
 *  *
 *  * Each button navigates to its corresponding activity using intents.
 *  *
 * @author Ryan Bae
 */

package com.example.p5_213.ui;
import android.content.Intent; import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.R;

/**
 * MenuActivity class represents the main menu screen where users can navigate
 * to different parts of the application.
 */
public final class MenuActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting.
     * Sets up the menu layout and links all navigation buttons to their respective activities.
     *
     * @param s savedInstanceState if re-creating the activity from a previous state.
     */
    public void onCreate(Bundle s){ super.onCreate(s);
        setContentView(R.layout.menu_page);
        link(R.id.btnBurger, BurgerActivity.class);
        link(R.id.btnSandwich, SandwichActivity.class);
        link(R.id.btnSide, SideActivity.class);
        link(R.id.btnDrink, BeverageActivity.class);
        link(R.id.btnCart, CartActivity.class);
        link(R.id.btnHistory, ArchiveActivity.class);

        findViewById(R.id.btnStart).setOnClickListener(view -> startActivity(new Intent(this, StartActivity.class)));
    }

    /**
     * Helper method to simplify button linking.
     * Associates a button ID with an intent to start a given activity when clicked.
     *
     * @param id the resource ID of the button to link.
     * @param c  the target activity class to launch.
     */
    private void link(int id, Class<?> c){
        findViewById(id).setOnClickListener(v -> startActivity(new Intent(this,c)));
    }
}
