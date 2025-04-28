package com.example.p5_213.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.p5_213.R;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.*;

public final class BeverageActivity extends AppCompatActivity {

    private RecyclerView rvFlavor;
    private Spinner      spSize, spQty;
    private TextView     tvPrice;
    private FlavorAdapter adapter;

    @Override protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_beverage);

        rvFlavor = findViewById(R.id.rvFlavor);
        spSize   = findViewById(R.id.spSize);
        spQty    = findViewById(R.id.spQty);
        tvPrice  = findViewById(R.id.tvTot);

        /* RecyclerView: 3-column grid with pictures */
        adapter = new FlavorAdapter(f -> updatePrice());
        rvFlavor.setLayoutManager(new GridLayoutManager(this,3));
        rvFlavor.setAdapter(adapter);

        spSize.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Size.values()));
        spQty.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new Integer[]{1,2,3,4,5}));

        AdapterView.OnItemSelectedListener l = new SimpleSel(){public void onItemSelected(){updatePrice();}};
        spSize.setOnItemSelectedListener(l); spQty.setOnItemSelectedListener(l);

        findViewById(R.id.btnPlace).setOnClickListener(v -> addToOrder());
        findViewById(R.id.btnMain).setOnClickListener(
                v -> startActivity(new Intent(this, MenuActivity.class)));

        updatePrice();
    }

    /* helpers -------------------------------------------------------------- */

    private void addToOrder(){
        try{
            OrderRepository.get().current().addItem(buildBeverage());
            Toast.makeText(this,R.string.added_to_order,Toast.LENGTH_SHORT).show();
        }catch (IllegalStateException ex){ warn("Pick flavor / size."); }
    }

    private void updatePrice(){
        try{ tvPrice.setText(String.format("$%.2f", buildBeverage().price())); }
        catch (Exception ignore){ tvPrice.setText("$0.00"); }
    }

    private Beverage buildBeverage(){
        Flavor  flavor = adapter.selected();
        Size    size   = (Size)   spSize.getSelectedItem();
        Integer qty    = (Integer)spQty .getSelectedItem();
        if(flavor==null || size==null || qty==null) throw new IllegalStateException();
        return new Beverage(size, flavor, qty);
    }

    private void warn(String m){ new AlertDialog.Builder(this)
            .setMessage(m).setPositiveButton(android.R.string.ok,null).show(); }

    private abstract static class SimpleSel implements AdapterView.OnItemSelectedListener{
        public void onNothingSelected(AdapterView<?> p){} public abstract void onItemSelected();
        public final void onItemSelected(AdapterView<?> p, android.view.View v,int i,long l){onItemSelected();}
    }
}
