/**
 * Handles the Sandwich scene.
 * Allows users to choose the bread type, protein, quantity, and optional addons.
 * Allows users to add the sandwich to the current order.
 * Allows users to make the sandwich into a combo.
 * ComboBoxes for selecting bread type, protein, quantity, and optional addons.
 * TextField for showing computed price.
 * Button actions for adding to order and returning to the main menu scene.
 * @author Ryan Bae
 */

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

/**
 * SandwichActivity class manages the UI and logic for customizing a sandwich order.
 */
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

    /**
     * Called when the activity is starting.
     * Sets up UI components and event listeners for user interaction.
     *
     * @param s savedInstanceState if re-creating the activity from a previous state.
     */
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

    /**
     * Adds the currently customized Sandwich to the current order.
     * Shows a warning if bread or protein is not selected.
     */
    private void addToOrder(){
        try{
            OrderRepository.get().current().addItem(buildSandwich());
            Toast.makeText(this,R.string.added_to_order,Toast.LENGTH_SHORT).show();
        }catch (IllegalStateException ex){ warn("Choose bread/protein.");}
    }

    /**
     * Updates the displayed price based on the current sandwich selections.
     * Shows $0.00 if an incomplete sandwich is selected.
     */
    private void updatePrice(){
        try{ tvPrice.setText(String.format("$%.2f", buildSandwich().price())); }
        catch (Exception ignore){ tvPrice.setText("$0.00"); }
    }

    /**
     * Builds and returns a Sandwich object based on current selections.
     *
     * @return a fully configured Sandwich
     * @throws IllegalStateException if required selections are missing
     */
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

    /**
     * Shows a simple alert dialog with a message.
     *
     * @param m the message to display
     */
    private void warn(String m){
        new AlertDialog.Builder(this).setMessage(m).setPositiveButton(android.R.string.ok,null).show();
    }

    /**
     * A simple OnItemSelectedListener that only overrides onItemSelected().
     */
    private abstract static class SimpleSel implements AdapterView.OnItemSelectedListener{
        public void onNothingSelected(AdapterView<?> p){} public abstract void onItemSelected();
        public final void onItemSelected(AdapterView<?> p, View v,int i,long l){onItemSelected();}
    }
}
