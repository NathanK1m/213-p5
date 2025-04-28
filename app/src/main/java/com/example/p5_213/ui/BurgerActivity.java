package com.example.p5_213.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.R;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.*;

import java.util.ArrayList;

public final class BurgerActivity extends AppCompatActivity {

    private RadioGroup rgBread, rgPatty;
    private Spinner spQty;
    private CheckBox cbLet, cbTom, cbOnion, cbAvo, cbChe;
    private TextView tvPrice;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_burger);

        rgBread = findViewById(R.id.rgBread);
        rgPatty = findViewById(R.id.rgPatty);
        spQty   = findViewById(R.id.spQty);

        cbLet   = findViewById(R.id.cbLettuce);
        cbTom   = findViewById(R.id.cbTomato);
        cbOnion = findViewById(R.id.cbOnion);
        cbAvo   = findViewById(R.id.cbAvocado);
        cbChe   = findViewById(R.id.cbCheese);

        tvPrice = findViewById(R.id.tvPrice);

        // qty 1-5
        spQty.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new Integer[]{1,2,3,4,5}));

        View.OnClickListener upd = v -> updatePrice();
        cbLet.setOnClickListener(upd); cbTom.setOnClickListener(upd);
        cbOnion.setOnClickListener(upd); cbAvo.setOnClickListener(upd); cbChe.setOnClickListener(upd);
        rgBread.setOnCheckedChangeListener((g,i)->updatePrice());
        rgPatty.setOnCheckedChangeListener((g,i)->updatePrice());
        spQty  .setOnItemSelectedListener(new SimpleSel(){ public void onItemSelected(){updatePrice();}});

        findViewById(R.id.btnAdd).setOnClickListener(v -> addToCart());
        findViewById(R.id.btnCombo).setOnClickListener(v -> {
            try { startActivity(ComboActivity.intent(this, buildBurger())); }
            catch (IllegalStateException ex){ warn("You must have a complete burger"); }
        });
        findViewById(R.id.btnMain).setOnClickListener(v->finish());

        updatePrice();
    }

    private void addToCart() {
        try {
            OrderRepository.get().current().addItem(buildBurger());
            Toast.makeText(this, R.string.added_to_order, Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException ex) {
            warn("You must have a complete burger");
        }
    }

    private void updatePrice() {
        try { tvPrice.setText(String.format("$%.2f", buildBurger().price())); }
        catch (Exception ignore) { tvPrice.setText("$0.00"); }
    }

    private Burger buildBurger() {
        int breadId = rgBread.getCheckedRadioButtonId();
        int pattyId = rgPatty.getCheckedRadioButtonId();
        if (breadId == -1 || pattyId == -1) throw new IllegalStateException();

        Bread bread = Bread.valueOf(((RadioButton)findViewById(breadId))
                .getText().toString().toUpperCase());
        boolean isDouble = ((RadioButton)findViewById(pattyId))
                .getText().toString().equalsIgnoreCase("Double");

        ArrayList<AddOns> adds = new ArrayList<>();
        if (cbLet.isChecked())   adds.add(AddOns.LETTUCE);
        if (cbTom.isChecked())   adds.add(AddOns.TOMATOES);
        if (cbOnion.isChecked()) adds.add(AddOns.ONIONS);
        if (cbAvo.isChecked())   adds.add(AddOns.AVOCADO);
        if (cbChe.isChecked())   adds.add(AddOns.CHEESE);

        int qty = Integer.parseInt(spQty.getSelectedItem().toString());
        return new Burger(bread, isDouble, adds, qty);
    }

    private void warn(String msg){ new AlertDialog.Builder(this)
            .setMessage(msg).setPositiveButton(android.R.string.ok,null).show(); }

    /** helper */
    private abstract static class SimpleSel implements AdapterView.OnItemSelectedListener{
        public void onNothingSelected(AdapterView<?> p) {}
        public abstract void onItemSelected();
        public final void onItemSelected(AdapterView<?> p, View v, int i, long l){ onItemSelected(); }
    }
}
