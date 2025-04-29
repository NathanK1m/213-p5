package com.example.p5_213.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.R;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.*;

import java.util.ArrayList;

public final class SandwichActivity extends AppCompatActivity {

    private RadioGroup rgBread;
    private RadioGroup rgProtein;
    private Spinner spQty;
    private CheckBox cbLet;
    private CheckBox cbTom;
    private CheckBox cbOnion;
    private CheckBox cbAvo;
    private CheckBox cbChe;
    private TextView tvPrice;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_sandwich);

        rgProtein = findViewById(R.id.rgProtein);
        rgBread = findViewById(R.id.rgBread);
        spQty = findViewById(R.id.spQty);
        cbLet = findViewById(R.id.cbLettuce);
        cbTom = findViewById(R.id.cbTomato);
        cbOnion = findViewById(R.id.cbOnion);
        cbAvo = findViewById(R.id.cbAvocado);
        cbChe = findViewById(R.id.cbCheese);
        tvPrice = findViewById(R.id.tvTot);

        spQty.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new Integer[]{1,2,3,4,5}));

        View.OnClickListener u = v -> updatePrice();
        cbLet.setOnClickListener(u); cbTom.setOnClickListener(u);
        cbOnion.setOnClickListener(u); cbAvo.setOnClickListener(u); cbChe.setOnClickListener(u);
        rgBread.setOnCheckedChangeListener((g,i)->updatePrice());
        rgProtein.setOnCheckedChangeListener((g,i)->updatePrice());
        spQty.setOnItemSelectedListener(new SimpleSel(){public void onItemSelected(){updatePrice();}});

        findViewById(R.id.btnPlace).setOnClickListener(v -> addToOrder());
        findViewById(R.id.btnCombo).setOnClickListener(v -> {
            try { startActivity(ComboActivity.intent(this, buildSandwich())); }
            catch (IllegalStateException ex){ warn("Choose bread/protein."); }
        });
        findViewById(R.id.btnMain).setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));

        updatePrice();
    }

    private void addToOrder(){
        try{
            OrderRepository.get().current().addItem(buildSandwich());
            Toast.makeText(this,R.string.added_to_order,Toast.LENGTH_SHORT).show();
        }catch (IllegalStateException ex){ warn("Choose bread/protein.");}
    }

    private void updatePrice(){
        try{ tvPrice.setText(String.format("$%.2f", buildSandwich().price())); }
        catch (Exception ignore){ tvPrice.setText("$0.00"); }
    }

    private Sandwich buildSandwich(){
        int breadId = rgBread.getCheckedRadioButtonId();
        int proteinId = rgProtein.getCheckedRadioButtonId();
        if(breadId==-1 || proteinId==-1) throw new IllegalStateException();

        Bread bread = Bread.valueOf(((RadioButton)findViewById(breadId))
                .getText().toString().toUpperCase());
        Protein protein = switch(((RadioButton)findViewById(proteinId))
                .getText().toString()){
            case "Roast Beef" -> Protein.ROAST_BEEF;
            case "Salmon" -> Protein.SALMON;
            default -> Protein.CHICKEN;
        };

        ArrayList<AddOns> add = new ArrayList<>();
        if(cbLet.isChecked()) add.add(AddOns.LETTUCE);
        if(cbTom.isChecked()) add.add(AddOns.TOMATOES);
        if(cbOnion.isChecked())add.add(AddOns.ONIONS);
        if(cbAvo.isChecked()) add.add(AddOns.AVOCADO);
        if(cbChe.isChecked()) add.add(AddOns.CHEESE);

        int q = Integer.parseInt(spQty.getSelectedItem().toString());
        return new Sandwich(bread, protein, add, q);
    }

    private void warn(String m){
        new AlertDialog.Builder(this).setMessage(m).setPositiveButton(android.R.string.ok,null).show();
    }

    private abstract static class SimpleSel implements AdapterView.OnItemSelectedListener{
        public void onNothingSelected(AdapterView<?> p){} public abstract void onItemSelected();
        public final void onItemSelected(AdapterView<?> p, View v,int i,long l){onItemSelected();}
    }
}
