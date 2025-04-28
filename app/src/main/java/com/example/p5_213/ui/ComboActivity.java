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

    public static Intent intent(Context c, Sandwich base) {
        return new Intent(c, ComboActivity.class).putExtra(EXTRA, base);
    }

    private Sandwich base;
    private Spinner spnSide, spnQty;
    private TextView tvPrice;
    private FlavorAdapter adapter;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_combo);

        base = (Sandwich) getIntent().getSerializableExtra(EXTRA);

        // Setup Side Spinner
        spnSide = findViewById(R.id.spnSide);
        spnSide.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, SideType.values()));

        // Setup Quantity Spinner
        spnQty = findViewById(R.id.spQty);
        spnQty.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new Integer[]{1, 2, 3, 4, 5}));

        // Setup Price TextView
        tvPrice = findViewById(R.id.tvTot);

        // Setup RecyclerView for selecting Drink (Flavor)
        RecyclerView rvFlavor = findViewById(R.id.rvFlavor);
        adapter = new FlavorAdapter(f -> update()); // callback to update price when drink selected
        rvFlavor.setLayoutManager(new GridLayoutManager(this, 3));
        rvFlavor.setAdapter(adapter);

        // Setup Buttons
        findViewById(R.id.btnPlace).setOnClickListener(v -> {
            if (check()) {
                OrderRepository.get().current().addItem(build());
                Toast.makeText(this, "Added to order", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnMain).setOnClickListener(v -> finish());

        // Setup selection listeners to update price when side or qty changes
        AdapterView.OnItemSelectedListener listener = new Sel();
        spnSide.setOnItemSelectedListener(listener);
        spnQty.setOnItemSelectedListener(listener);

        update();
    }

    // Helper methods -----------------------------------------------------------------

    private boolean check() {
        if (spnSide.getSelectedItemPosition() < 0 || adapter.selected() == null) {
            Toast.makeText(this, "Pick side & drink", Toast.LENGTH_SHORT).show();
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
        public void onNothingSelected(AdapterView<?> p) { }
        public void onItemSelected(AdapterView<?> p, android.view.View v, int i, long l) { update(); }
    }
}