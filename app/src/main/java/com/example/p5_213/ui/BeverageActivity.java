/**
 * Handles the Beverage Scene.
 * Allows users to choose the beverage type, size, and quantity.
 * Using Recycler View to go through beverages.
 * Allows users to add the beverage to the current order.
 * ComboBoxes for selecting flavor, size, and quantity.
 * TextField for showing computed price.
 * Button actions for adding to order and returning to the main menu scene.
 * @author Ryan Bae
 */


package com.example.p5_213.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.p5_213.R;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.Beverage;
import com.example.p5_213.model.Flavor;
import com.example.p5_213.model.Size;
public final class BeverageActivity extends AppCompatActivity {
    private RecyclerView rvFlavor;
    private Spinner spSize, spQty;
    private TextView tvPrice;
    private FlavorAdapter adapter;

    /**
     * Initialize beverage activity and set up listeners.
     * @param s the saved instance state .
     */
    @Override protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_beverage);

        rvFlavor = findViewById(R.id.rvFlavor);
        spSize = findViewById(R.id.spSize);
        spQty = findViewById(R.id.spQty);
        tvPrice = findViewById(R.id.tvTot);

        /* RecyclerView: 3-column grid with pictures */
        adapter = new FlavorAdapter(f -> updatePrice());
        rvFlavor.setLayoutManager(new GridLayoutManager(this,1));
        rvFlavor.setAdapter(adapter);

        spSize.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Size.values()));
        spQty.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new Integer[]{1,2,3,4,5}));

        AdapterView.OnItemSelectedListener l = new SimpleSel(){public void onItemSelected(){updatePrice();}};
        spSize.setOnItemSelectedListener(l); spQty.setOnItemSelectedListener(l);

        findViewById(R.id.btnPlace).setOnClickListener(v -> addToOrder());
        findViewById(R.id.btnMain).setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));

        updatePrice();
    }

    /**
     * Adds the beverage to the cart.
     */
    private void addToOrder(){
        try{
            OrderRepository.get().current().addItem(buildBeverage());
            Toast.makeText(this,R.string.added_to_order,Toast.LENGTH_SHORT).show();
        }catch (IllegalStateException ex){ warn("Pick flavor / size."); }
    }

    /**
     * Updates the displayed price based on size and quantity.
     */
    private void updatePrice(){
        try{ tvPrice.setText(String.format("$%.2f", buildBeverage().price())); }
        catch (Exception ignore){ tvPrice.setText("$0.00"); }
    }

    /**
     * Builds a Beverage object based on the selection.
     * @return the Beverage object with selected size, flavor, and quantity.
     */
    private Beverage buildBeverage(){
        Flavor flavor = adapter.selected();
        Size size = (Size) spSize.getSelectedItem();
        Integer qty = (Integer)spQty .getSelectedItem();
        if(flavor==null || size==null || qty==null) throw new IllegalStateException();
        return new Beverage(size, flavor, qty);
    }

    /**
     * Shows warning message.
     * @param m the message.
     */
    private void warn(String m){ new AlertDialog.Builder(this).setMessage(m).setPositiveButton(android.R.string.ok,null).show(); }

    /**
     * Listener for item selected actions for spinners.
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
         * @param v the view that is clicked.
         * @param i the position of the selected item.
         * @param l the id of the selected item.
         */
        public final void onItemSelected(AdapterView<?> p, android.view.View v,int i,long l){onItemSelected();}
    }
}
