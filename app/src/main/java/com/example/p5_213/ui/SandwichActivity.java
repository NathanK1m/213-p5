package com.example.p5_213.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.p5_213.R;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.*;

import java.util.ArrayList;
import java.util.List;

public final class SandwichActivity extends AppCompatActivity {

    // UI references
    private Spinner spBread, spProtein, spQty;
    private CheckBox cbLettuce, cbTomato, cbOnion, cbAvocado, cbCheese;
    private TextView tvPrice;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_sandwich);

        // find views
        spBread   = findViewById(R.id.spBread);
        spProtein = findViewById(R.id.spProtein);
        spQty     = findViewById(R.id.spQty);
        cbLettuce = findViewById(R.id.cbLettuce);
        cbTomato  = findViewById(R.id.cbTomato);
        cbOnion   = findViewById(R.id.cbOnion);
        cbAvocado = findViewById(R.id.cbAvocado);
        cbCheese  = findViewById(R.id.cbCheese);
        tvPrice   = findViewById(R.id.tvPrice);

        // populate spinners
        spBread.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Bread.values()));
        spProtein.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Protein.values()));
        spQty.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, List.of(1,2,3,4,5)));

        spQty.setSelection(0);

        // update price whenever a control changes
        View.OnClickListener upd = v -> updatePrice();
        cbLettuce.setOnClickListener(upd);
        cbTomato .setOnClickListener(upd);
        cbOnion  .setOnClickListener(upd);
        cbAvocado.setOnClickListener(upd);
        cbCheese .setOnClickListener(upd);
        spBread  .setOnItemSelectedListener(new SimpleSel(){ public void onItemSelected(){updatePrice();}});
        spProtein.setOnItemSelectedListener(new SimpleSel(){ public void onItemSelected(){updatePrice();}});
        spQty    .setOnItemSelectedListener(new SimpleSel(){ public void onItemSelected(){updatePrice();}});

        // buttons
        findViewById(R.id.btnAdd).setOnClickListener(v -> addToOrder());
        findViewById(R.id.btnMain).setOnClickListener(v -> finish());

        updatePrice();
    }

    private void addToOrder() {
        try {
            Sandwich s = buildSandwich();
            OrderRepository.get().current().addItem(s);
            Toast.makeText(this, R.string.added_to_order, Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException ex) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.incomplete_item)
                    .setMessage(R.string.select_bread_and_protein)
                    .setPositiveButton(android.R.string.ok,null).show();
        }
    }

    private void updatePrice() {
        try {
            tvPrice.setText(String.format("$%.2f", buildSandwich().price()));
        } catch (Exception ignore) {
            tvPrice.setText("$0.00");
        }
    }

    private Sandwich buildSandwich() {
        Bread   bread   = (Bread)   spBread.getSelectedItem();
        Protein protein = (Protein) spProtein.getSelectedItem();
        Integer qty     = (Integer) spQty.getSelectedItem();
        if (bread == null || protein == null || qty == null)
            throw new IllegalStateException();

        ArrayList<AddOns> addOns = new ArrayList<>();
        if (cbLettuce.isChecked()) addOns.add(AddOns.LETTUCE);
        if (cbTomato .isChecked()) addOns.add(AddOns.TOMATOES);
        if (cbOnion  .isChecked()) addOns.add(AddOns.ONIONS);
        if (cbAvocado.isChecked()) addOns.add(AddOns.AVOCADO);
        if (cbCheese .isChecked()) addOns.add(AddOns.CHEESE);

        return new Sandwich(bread, protein, addOns, qty);
    }

    /** helper so we don’t repeat empty methods */
    private abstract static class SimpleSel implements AdapterView.OnItemSelectedListener{
        public void onNothingSelected(AdapterView<?> p) { }
        public abstract void onItemSelected();
        public final void onItemSelected(AdapterView<?> p, View v, int i, long l){onItemSelected();}
    }
}
