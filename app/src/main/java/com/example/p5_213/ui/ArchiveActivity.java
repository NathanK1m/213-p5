/**
 * Handles the orders placed scene
 * ComboBox to view previously placed orders.
 * Displays the details of a selected order in a ListView.
 * Delete a selected order.
 * @author Nathan Kim
 */
package com.example.p5_213.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.R;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.MenuItem;
import com.example.p5_213.model.Order;
public final class ArchiveActivity extends AppCompatActivity {

    private Spinner sel;
    private ListView lv;
    private TextView tot;

    /**
     * Initializes archived activity and sets up listeners.
     * @param s the saved instance state.
     */
    @Override public void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_archive);

        sel = findViewById(R.id.spnOrders);
        lv = findViewById(R.id.lvDetails);
        tot = findViewById(R.id.tvTotal);

        sel.setOnItemSelectedListener(new Sel());
        findViewById(R.id.btnCancel).setOnClickListener(v -> cancel());
        findViewById(R.id.btnMain).setOnClickListener(v -> finish());

        reload();
    }

    /**
     * Reloads the archived orders list.
     */
    private void reload() {
        var h = OrderRepository.get().history();
        sel.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, h.stream().map(o -> "Order #"+o.getNumber()).toList()));
        if (!h.isEmpty()) sel.setSelection(0);
        display();
    }

    /**
     * Displays the selected archived order items.
     */
    private void display() {
        int idx = sel.getSelectedItemPosition();
        if (idx < 0) return;
        Order o = OrderRepository.get().history().get(idx);
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, o.getItems().stream().map(MenuItem::toString).toList()));
        tot.setText(String.format("$%.2f", o.getTotal()));
    }

    /**
     * Deletes the selected archived order.
     */
    private void cancel() {
        int i = sel.getSelectedItemPosition();
        if (i >= 0) {
            OrderRepository.get().history().remove(i);
            reload();
        }
    }

    /**
     * Listener for when a different archived order is selected.
     */
    private final class Sel implements AdapterView.OnItemSelectedListener {
        /**
         * Called when nothing is selected.
         * @param p the AdapterView.
         */
        public void onNothingSelected(AdapterView<?> p) {}

        /**
         * Called when an archived order is selected.
         * @param a the AdapterView.
         * @param v the View that is clicked.
         * @param i the position of the view.
         * @param id the id of the item that is selected.
         */
        public void onItemSelected(AdapterView<?> a, View v, int i, long id) { display(); }
    }
}
