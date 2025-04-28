package com.example.p5_213.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.R;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.MenuItem;
import com.example.p5_213.model.Order;

/** Shows the current order, lets the user remove a selected line-item or place the order. */
public final class CartActivity extends AppCompatActivity {

    /* ------------------------------------------------------------------ */
    /*  Views & state                                                     */
    /* ------------------------------------------------------------------ */
    private ListView  lv;
    private TextView  tvSub, tvTax, tvTot;
    private int       selected = -1;              // –1  → nothing selected

    /* ------------------------------------------------------------------ */
    /*  Lifecycle                                                         */
    /* ------------------------------------------------------------------ */
    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_cart);   // make sure your XML IDs match the ones below

        lv     = findViewById(R.id.lvCart);
        tvSub  = findViewById(R.id.tvSub);
        tvTax  = findViewById(R.id.tvTax);
        tvTot  = findViewById(R.id.tvTot);

        /* ---------- list behaviour ------------------------------------ */
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener((p, v, pos, id) -> selected = pos);

        /* long-press instantly deletes, as before */
        lv.setOnItemLongClickListener((a,v,i,id) -> { remove(i); return true; });

        /* ---------- buttons ------------------------------------------- */
        findViewById(R.id.btnRemove)
                .setOnClickListener(v -> remove(selected));

        findViewById(R.id.btnPlace)
                .setOnClickListener(v -> place());

        findViewById(R.id.btnMain)
                .setOnClickListener(v ->
                        startActivity(new Intent(this, MenuActivity.class)));

        refresh();
    }

    /* ------------------------------------------------------------------ */
    /*  Helpers                                                           */
    /* ------------------------------------------------------------------ */
    private void refresh() {
        Order o = OrderRepository.get().current();
        lv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                o.getItems().stream().map(MenuItem::toString).toList()));

        /* re-apply selection if still valid */
        if (selected >= 0 && selected < o.getItems().size()) {
            lv.setItemChecked(selected, true);
        } else {
            selected = -1;
        }

        tvSub.setText(String.format("$%.2f", o.getSubtotal()));
        tvTax.setText(String.format("$%.2f", o.getTax()));
        tvTot.setText(String.format("$%.2f", o.getTotal()));
    }

    /** Remove line *idx*; if idx<0 nothing is removed. */
    private void remove(int idx) {
        if (idx < 0) { Toast.makeText(this, "Select an item first", Toast.LENGTH_SHORT).show(); return; }
        OrderRepository.get().current().removeItem(idx);
        selected = -1;
        refresh();
    }

    /** Places the order and archives it. */
    private void place() {
        OrderRepository repo = OrderRepository.get();
        if (repo.current().getItems().isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        repo.place();
        new AlertDialog.Builder(this)
                .setMessage("Order placed")
                .setPositiveButton("OK", (d,i) -> finish())
                .show();
    }
}
