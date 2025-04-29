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
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.p5_213.R;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.*;

public final class ComboActivity extends AppCompatActivity {

    private static final String EXTRA = "BASE";

    public static Intent intent(Context c, MenuItem base) {
        return new Intent(c, ComboActivity.class).putExtra(EXTRA, base);
    }

    private MenuItem base;
    private Spinner spnSide, spnQty;
    private TextView tvPrice;
    private FlavorAdapter adapter;

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


    private boolean check() {
        if (spnSide.getSelectedItemPosition() < 0 || adapter.selected() == null) {
            Toast.makeText(this, "Pick side/drink", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Combo build() {
        Side side = new Side(SideType.values()[spnSide.getSelectedItemPosition()], Size.SMALL, 1);
        Beverage drink = new Beverage(Size.MEDIUM, adapter.selected(), 1);
        int quantity = (Integer) spnQty.getSelectedItem();
        return new Combo(base, drink, side, quantity);
    }

    private void update() {
        try {
            tvPrice.setText(String.format("$%.2f", build().price()));
        } catch (Exception ignore) {
            tvPrice.setText("$0.00");
        }
    }

    private final class Sel implements AdapterView.OnItemSelectedListener {
        public void onNothingSelected(AdapterView<?> p) {}
        public void onItemSelected(AdapterView<?> p, android.view.View v, int i, long l) { update(); }
    }
}
