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

public final class SandwichActivity extends AppCompatActivity {

    private Spinner spBread, spProtein, spQty;
    private CheckBox cbLet, cbTom, cbOnion, cbAvo, cbChe;
    private TextView tvPrice;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_sandwich);

        spBread   = findViewById(R.id.spBread);
        spProtein = findViewById(R.id.spProtein);
        spQty     = findViewById(R.id.spQty);

        cbLet   = findViewById(R.id.cbLettuce);
        cbTom   = findViewById(R.id.cbTomato);
        cbOnion = findViewById(R.id.cbOnion);
        cbAvo   = findViewById(R.id.cbAvocado);
        cbChe   = findViewById(R.id.cbCheese);

        tvPrice  = findViewById(R.id.tvPrice);

        spBread  .setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Bread.values()));
        spProtein.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Protein.values()));
        spQty.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new Integer[]{1,2,3,4,5}));

        View.OnClickListener upd = v -> updatePrice();
        cbLet.setOnClickListener(upd); cbTom.setOnClickListener(upd);
        cbOnion.setOnClickListener(upd); cbAvo.setOnClickListener(upd); cbChe.setOnClickListener(upd);

        AdapterView.OnItemSelectedListener l = new SimpleSel(){ public void onItemSelected(){updatePrice();}};
        spBread.setOnItemSelectedListener(l); spProtein.setOnItemSelectedListener(l); spQty.setOnItemSelectedListener(l);

        findViewById(R.id.btnAdd).setOnClickListener(v -> addToOrder());
        findViewById(R.id.btnMain).setOnClickListener(v -> finish());

        updatePrice();
    }

    private void addToOrder() {
        try {
            OrderRepository.get().current().addItem(buildSandwich());
            Toast.makeText(this, R.string.added_to_order, Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException ex) {
            warn("Choose bread/protein");
        }
    }

    private void updatePrice() {
        try { tvPrice.setText(String.format("$%.2f", buildSandwich().price())); }
        catch (Exception ignore){ tvPrice.setText("$0.00"); }
    }

    private Sandwich buildSandwich() {
        Bread   bread   = (Bread)   spBread  .getSelectedItem();
        Protein protein = (Protein) spProtein.getSelectedItem();
        Integer qty     = (Integer) spQty    .getSelectedItem();
        if (bread==null || protein==null || qty==null) throw new IllegalStateException();

        ArrayList<AddOns> addOns = new ArrayList<>();
        if (cbLet.isChecked()) addOns.add(AddOns.LETTUCE);
        if (cbTom.isChecked()) addOns.add(AddOns.TOMATOES);
        if (cbOnion.isChecked()) addOns.add(AddOns.ONIONS);
        if (cbAvo.isChecked()) addOns.add(AddOns.AVOCADO);
        if (cbChe.isChecked()) addOns.add(AddOns.CHEESE);

        return new Sandwich(bread, protein, addOns, qty);
    }

    private void warn(String m){ new AlertDialog.Builder(this)
            .setMessage(m).setPositiveButton(android.R.string.ok,null).show(); }

    private abstract static class SimpleSel implements AdapterView.OnItemSelectedListener{
        public void onNothingSelected(AdapterView<?> p){} public abstract void onItemSelected();
        public final void onItemSelected(AdapterView<?> p, View v, int i, long l){onItemSelected();}
    }
}
