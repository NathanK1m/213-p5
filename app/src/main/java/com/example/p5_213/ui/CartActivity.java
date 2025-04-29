package com.example.p5_213.ui;
/**
 * Handles the checkout scene.
 * Displays the user's current order.
 * Allows removal of items.
 * Handles placing the final order.
 * ListView to show all menu items currently in the cart.
 * TextFields showing subtotal, tax, and total cost.
 * Buttons to remove items, place the order, or return to the main menu.
 * @author Nathan Kim
 */

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
    private ListView lv;
    private TextView tvSub;
    private TextView tvTax;
    private TextView tvTot;
    private int selected = -1; // –1 means nothing is selected

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_cart);

        lv = findViewById(R.id.lvCart);
        tvSub = findViewById(R.id.tvSub);
        tvTax = findViewById(R.id.tvTax);
        tvTot = findViewById(R.id.tvTot);

        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener((p, v, pos, id) -> selected = pos);

        lv.setOnItemLongClickListener((a,v,i,id) -> { remove(i); return true; });

        findViewById(R.id.btnCancel).setOnClickListener(v -> remove(selected));

        findViewById(R.id.btnPlace).setOnClickListener(v -> place());

        findViewById(R.id.btnMain).setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));

        refresh();
    }

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

    private void remove(int idx) {
        if (idx < 0) { Toast.makeText(this, "Select an item first", Toast.LENGTH_SHORT).show(); return; }
        OrderRepository.get().current().removeItem(idx);
        selected = -1;
        refresh();
    }

    private void place() {
        OrderRepository repo = OrderRepository.get();
        if (repo.current().getItems().isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        repo.place();
        new AlertDialog.Builder(this).setMessage("Order placed").setPositiveButton("OK", (d,i) -> finish()).show();
    }
}
