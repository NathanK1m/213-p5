/**
 * Handles the Burger scene.
 * Allows users to choose the bread type, patty, quantity, and optional addons.
 * Allows users to add the burger to the current order.
 * Allows users to make the burger into a combo.
 * ComboBoxes for selecting bread type, patty, quantity, and optional addons.
 * TextField for showing computed price.
 * Button actions for adding to order and returning to the main menu scene.
 * @author Nathan Kim
 */

package com.example.p5_213.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.R;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.AddOns;
import com.example.p5_213.model.Bread;
import com.example.p5_213.model.Burger;

import java.util.ArrayList;

public final class BurgerActivity extends AppCompatActivity {

    private RadioGroup rgBread;
    private RadioGroup rgPatty;
    private Spinner spQty;
    private CheckBox cbLet;
    private CheckBox cbTom;
    private CheckBox cbOni;
    private CheckBox cbAvo;
    private CheckBox cbChe;
    private TextView tvPrice;

    /**
     * Initializes burger activity and sets up listeners.
     * @param s the saved instance state.
     */
    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_burger);

        rgBread = findViewById(R.id.rgBread);
        rgPatty = findViewById(R.id.rgPatty);
        spQty = findViewById(R.id.spQty);

        cbLet = findViewById(R.id.cbLettuce);
        cbTom = findViewById(R.id.cbTomato);
        cbOni = findViewById(R.id.cbOnion);
        cbAvo = findViewById(R.id.cbAvocado);
        cbChe = findViewById(R.id.cbCheese);
        tvPrice = findViewById(R.id.tvTot);

        spQty.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new Integer[]{1,2,3,4,5}));

        View.OnClickListener u = v -> updatePrice();
        cbLet.setOnClickListener(u); cbTom.setOnClickListener(u); cbOni.setOnClickListener(u);
        cbAvo.setOnClickListener(u); cbChe.setOnClickListener(u);

        rgBread.setOnCheckedChangeListener((g,i)->updatePrice());
        rgPatty.setOnCheckedChangeListener((g,i)->updatePrice());
        spQty.setOnItemSelectedListener(new SimpleSel(){ public void onItemSelected(){ updatePrice(); }});

        findViewById(R.id.btnPlace).setOnClickListener(v -> addToCart());
        findViewById(R.id.btnCombo).setOnClickListener(v -> {
            try { startActivity(ComboActivity.intent(this, buildBurger())); }
            catch (IllegalStateException ex){ warn("Choose bread/patty."); }
        });
        findViewById(R.id.btnMain).setOnClickListener(v -> finish());

        updatePrice();
    }

    /**
     * Adds the selected burger to the cart.
     */
    private void addToCart() {
        try {
            OrderRepository.get().current().addItem(buildBurger());
            Toast.makeText(this, R.string.added_to_order, Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException ex) {
            warn("Choose bread/patty.");
        }
    }

    /**
     * Updates the displayed total price based on the user's selections.
     */
    private void updatePrice() {
        try { tvPrice.setText(String.format("$%.2f", buildBurger().price())); }
        catch (Exception ignore) { tvPrice.setText("$0.00"); }
    }

    /**
     * Builds a Burger object based on user's selections.
     * @return a new Burger object with selected bread, patty, add-ons, and quantity.
     */
    private Burger buildBurger() {
        int breadId = rgBread.getCheckedRadioButtonId();
        int pattyId = rgPatty.getCheckedRadioButtonId();
        if (breadId == -1 || pattyId == -1) throw new IllegalStateException();

        Bread bread = Bread.valueOf(((RadioButton)findViewById(breadId)).getText().toString().toUpperCase());
        boolean dbl = ((RadioButton)findViewById(pattyId)).getText().toString().equalsIgnoreCase("Double");

        ArrayList<AddOns> add = new ArrayList<>();
        if (cbLet.isChecked()) add.add(AddOns.LETTUCE);
        if (cbTom.isChecked()) add.add(AddOns.TOMATOES);
        if (cbOni.isChecked()) add.add(AddOns.ONIONS);
        if (cbAvo.isChecked()) add.add(AddOns.AVOCADO);
        if (cbChe.isChecked()) add.add(AddOns.CHEESE);

        int q = Integer.parseInt(spQty.getSelectedItem().toString());
        return new Burger(bread, dbl, add, q);
    }

    /**
     * Shows a warning message.
     * @param m the message.
     */
    private void warn(String m){ new AlertDialog.Builder(this).setMessage(m).setPositiveButton(android.R.string.ok,null).show(); }

    /**
     * Listener for spinner selected actions for spinners.
     */
    private abstract static class SimpleSel implements AdapterView.OnItemSelectedListener{
        /**
         * Called when nothing is selected.
         * @param p the AdapterView.
         */
        public void onNothingSelected(AdapterView<?> p){} public abstract void onItemSelected();

        /**
         * Called when an item is selected.
         * @param p the AdapterView.
         * @param v the view in the AdapterView that was clicked.
         * @param i the position of the selected item.
         * @param l the id of the selected item.
         */
        public final void onItemSelected(AdapterView<?> p, View v, int i, long l){ onItemSelected(); }
    }
}
