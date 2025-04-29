/**
 * Handles the Side Scene.
 * Allows users to choose the side type, size, and quantity.
 * Allows users to add the side to the current order.
 * ComboBoxes for selecting side type, size, and quantity.
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.R;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.Side;
import com.example.p5_213.model.SideType;
import com.example.p5_213.model.Size;

public final class SideActivity extends AppCompatActivity {

    /**
     * SideActivity class manages the UI and logic for customizing and ordering a side item.
     */
    private Spinner spType, spSize, spQty;
    private TextView tvPrice;

    /**
     * Called when the activity is starting.
     * Sets up the side selection screen with spinners and event listeners.
     *
     * @param s savedInstanceState if re-creating the activity from a previous state.
     */
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

    /**
     * Adds the currently selected Side item to the current order.
     * Displays a warning if any required selections are missing.
     */
    private void addToOrder() {
        try {
            OrderRepository.get().current().addItem(buildSide());
            Toast.makeText(this, R.string.added_to_order, Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException ex) {
            warn("Choose side type/size");
        }
    }

    /**
     * Updates the displayed price based on the current selections.
     * Displays $0.00 if selections are incomplete.
     */
    private void updatePrice() {
        try { tvPrice.setText(String.format("$%.2f", buildSide().price())); }
        catch (Exception ignore){ tvPrice.setText("$0.00"); }
    }

    /**
     * Constructs and returns a Side object based on user selections.
     *
     * @return a fully configured Side
     * @throws IllegalStateException if required selections are missing
     */
    private Side buildSide() {
        SideType type = (SideType) spType.getSelectedItem();
        Size size = (Size)spSize.getSelectedItem();
        Integer qty = (Integer) spQty .getSelectedItem();
        if (type==null || size==null || qty==null) throw new IllegalStateException();
        return new Side(type, size, qty);
    }

    /**
     * Displays a simple alert dialog with a warning message.
     *
     * @param m the message to display
     */
    private void warn(String m){ new AlertDialog.Builder(this).setMessage(m).setPositiveButton(android.R.string.ok,null).show(); }

    /**
     * SimpleSel is a helper abstract class for simplified OnItemSelectedListener.
     */
    private abstract static class SimpleSel implements AdapterView.OnItemSelectedListener{
        public void onNothingSelected(AdapterView<?> p){} public abstract void onItemSelected();
        public final void onItemSelected(AdapterView<?> p, View v, int i, long l){onItemSelected();}
    }
}
