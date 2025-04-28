package com.example.p5_213.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.R;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.*;

public final class SideActivity extends AppCompatActivity {

    private Spinner spType, spSize, spQty;
    private TextView tvPrice;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_side);

        spType = findViewById(R.id.spType);
        spSize = findViewById(R.id.spSize);
        spQty  = findViewById(R.id.spQty);
        tvPrice= findViewById(R.id.tvTot);

        spType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SideType.values()));
        spSize.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Size.values()));
        spQty .setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new Integer[]{1,2,3,4,5}));

        AdapterView.OnItemSelectedListener l = new SimpleSel(){ public void onItemSelected(){updatePrice();}};
        spType.setOnItemSelectedListener(l); spSize.setOnItemSelectedListener(l); spQty.setOnItemSelectedListener(l);

        findViewById(R.id.btnPlace).setOnClickListener(v -> addToOrder());
        findViewById(R.id.btnMain).setOnClickListener(v -> finish());

        updatePrice();
    }

    private void addToOrder() {
        try {
            OrderRepository.get().current().addItem(buildSide());
            Toast.makeText(this, R.string.added_to_order, Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException ex) {
            warn("Choose side type/size");
        }
    }

    private void updatePrice() {
        try { tvPrice.setText(String.format("$%.2f", buildSide().price())); }
        catch (Exception ignore){ tvPrice.setText("$0.00"); }
    }

    private Side buildSide() {
        SideType type = (SideType) spType.getSelectedItem();
        Size     size = (Size)     spSize.getSelectedItem();
        Integer  qty  = (Integer)  spQty .getSelectedItem();
        if (type==null || size==null || qty==null) throw new IllegalStateException();
        return new Side(type, size, qty);
    }

    private void warn(String m){ new AlertDialog.Builder(this)
            .setMessage(m).setPositiveButton(android.R.string.ok,null).show(); }

    private abstract static class SimpleSel implements AdapterView.OnItemSelectedListener{
        public void onNothingSelected(AdapterView<?> p){} public abstract void onItemSelected();
        public final void onItemSelected(AdapterView<?> p, View v, int i, long l){onItemSelected();}
    }
}
