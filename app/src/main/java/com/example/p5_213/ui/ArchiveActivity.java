package com.example.p5_213.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.R;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.MenuItem;
import com.example.p5_213.model.Order;

public final class ArchiveActivity extends AppCompatActivity {

    private Spinner   sel;
    private ListView  lv;
    private TextView  tot;

    @Override public void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_archive);

        sel = findViewById(R.id.spnOrders);
        lv  = findViewById(R.id.lvDetails);
        tot = findViewById(R.id.tvTotal);

        sel.setOnItemSelectedListener(new Sel());
        findViewById(R.id.btnCancel).setOnClickListener(v -> cancel());
        findViewById(R.id.btnMain).setOnClickListener(v -> finish());

        reload();
    }
    
    private void reload() {
        var h = OrderRepository.get().history();
        sel.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                h.stream().map(o -> "Order #"+o.getNumber()).toList()));
        if (!h.isEmpty()) sel.setSelection(0);
        display();
    }

    private void display() {
        int idx = sel.getSelectedItemPosition();       // nothing chosen → skip
        if (idx < 0) return;
        Order o = OrderRepository.get().history().get(idx);
        lv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                o.getItems().stream().map(MenuItem::toString).toList()));
        tot.setText(String.format("$%.2f", o.getTotal()));
    }

    private void cancel() {
        int i = sel.getSelectedItemPosition();
        if (i >= 0) {
            OrderRepository.get().history().remove(i);
            reload();
        }
    }

    private final class Sel implements AdapterView.OnItemSelectedListener {
        public void onNothingSelected(AdapterView<?> p) {}
        public void onItemSelected(AdapterView<?> a, View v, int i, long id) { display(); }
    }
}
