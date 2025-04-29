/**
 * Handles the combo scene.
 * Allows the user to combine a sandwich or burger with a medium drink and a small apple or chips into a single combo item.
 * A text field showing the base sandwich/burger information
 * Combo boxes to select a side and drink
 * Drop down menu for quantity
 * Image previews for selected side and drink
 * Beverages uses Recycler View
 * @author Ryan Bae
 */
package com.example.p5_213.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.p5_213.R;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.Beverage;
import com.example.p5_213.model.Combo;
import com.example.p5_213.model.MenuItem;
import com.example.p5_213.model.Side;
import com.example.p5_213.model.SideType;
import com.example.p5_213.model.Size;
public final class ComboActivity extends AppCompatActivity {

    private static final String EXTRA = "BASE"; //Passes base MenuItem object between activities.

    /**
     * Creates an Intent to launch ComboActivity with the given base MenuItem.
     * @param c the context where the intent is launched
     * @param base the MenuItem to be included in the combo
     * @return an Intent to start ComboActivity
     */
    public static Intent intent(Context c, MenuItem base) {
        return new Intent(c, ComboActivity.class).putExtra(EXTRA, base);
    }

    private MenuItem base;
    private Spinner spnSide, spnQty;
    private TextView tvPrice;
    private FlavorAdapter adapter;

    /**
     * Initializes combo activity and sets up listeners.
     * @param s the saved instance state.
     */
    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_combo);

        base = (MenuItem) getIntent().getSerializableExtra(EXTRA);

        spnSide = findViewById(R.id.spnSide);
        spnSide.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SideType.values()));

        spnQty = findViewById(R.id.spQty);
        spnQty.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new Integer[]{1,2,3,4,5}));

        tvPrice = findViewById(R.id.tvTot);

        RecyclerView rvFlavor = findViewById(R.id.rvFlavor);
        adapter = new FlavorAdapter(f -> update());
        rvFlavor.setLayoutManager(new GridLayoutManager(this, 1));
        rvFlavor.setAdapter(adapter);

        findViewById(R.id.btnPlace).setOnClickListener(v -> {
            if (check()) {
                OrderRepository.get().current().addItem(build());
                Toast.makeText(this, "Added to order", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnMain).setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));

        AdapterView.OnItemSelectedListener listener = new Sel();
        spnSide.setOnItemSelectedListener(listener);
        spnQty.setOnItemSelectedListener(listener);
        update();
    }

    /**
     * Checks whether side and drink are selected.
     * @return true if selections are valid, false otherwise
     */
    private boolean check() {
        if (spnSide.getSelectedItemPosition() < 0 || adapter.selected() == null) {
            Toast.makeText(this, "Pick side/drink", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Builds a Combo object based on current user's selections.
     * @return new Combo object with selected side, drink, and quantity.
     */
    private Combo build() {
        Side side = new Side(SideType.values()[spnSide.getSelectedItemPosition()], Size.SMALL, 1);
        Beverage drink = new Beverage(Size.MEDIUM, adapter.selected(), 1);
        int quantity = (Integer) spnQty.getSelectedItem();
        return new Combo(base, drink, side, quantity);
    }

    /**
     * Updates the displayed total price for the selected combo.
     */
    private void update() {
        try {
            tvPrice.setText(String.format("$%.2f", build().price()));
        } catch (Exception ignore) {
            tvPrice.setText("$0.00");
        }
    }

    /**
     * Listener class for handling spinner selections to update combo price.
     */
    private final class Sel implements AdapterView.OnItemSelectedListener {
        /**
         * Called when nothing is selected.
         * @param p the AdapterView.
         */
        public void onNothingSelected(AdapterView<?> p) {}

        /**
         * Called when an item is selected.
         * @param p the AdapterView.
         * @param v the view that was clicked.
         * @param i the position of the selected item.
         * @param l the id of the selected item.
         */
        public void onItemSelected(AdapterView<?> p, android.view.View v, int i, long l) { update(); }
    }
}
