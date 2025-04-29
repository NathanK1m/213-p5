/**
 * A RecyclerView Adapter that displays a list of available beverage flavors.
 * Each row consists of:
 * - A RadioButton displaying the flavor name
 * - An ImageView displaying a representative image for the flavor
 * The adapter handles selection logic so that only one flavor can be selected at a time.
 * When a flavor is selected, a callback Consumer is triggered.
 *
 * @author Ryan Bae
 */
package com.example.p5_213.ui;

import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import com.example.p5_213.model.*;
import java.util.function.Consumer;
import com.example.p5_213.R;

/**
 * FlavorAdapter binds a list of Flavors to a RecyclerView for user selection.
 */
public final class FlavorAdapter extends RecyclerView.Adapter<FlavorAdapter.Holder> {

    private final Flavor[] data = Flavor.values();
    private Flavor sel;
    private final Consumer<Flavor> cb;

    /**
     * Constructor for the FlavorAdapter.
     *
     * @param c a Consumer callback to notify when a flavor is selected
     */
    FlavorAdapter(Consumer<Flavor> c) {
        this.cb = c;
    }

    /**
     * Holder class that represents a single row in the RecyclerView.
     * Each row contains a RadioButton and an ImageView.
     */
    static final class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        RadioButton radio;  // ✨ NOW using RadioButton

        Holder(View v) {
            super(v);
            img = v.findViewById(R.id.cellImg);
            radio = v.findViewById(R.id.radioButton); // ✨ Updated to match your XML id
        }
    }

    /**
     * Inflates the cell_flavor layout and creates a new Holder.
     *
     * @param parent the parent ViewGroup
     * @param viewType the view type (unused here)
     * @return a new Holder instance
     */
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_flavor, parent, false));
    }

    /**
     * Binds a Flavor to a Holder, updating the RadioButton text and ImageView.
     *
     * @param h the Holder to bind data to
     * @param i the position in the dataset
     */
    @Override
    public void onBindViewHolder(Holder h, int i) {
        Flavor f = data[i];
        h.radio.setText(f.name()); // ✨ Set text on the RadioButton

        int id = h.itemView.getContext().getResources().getIdentifier(
                f.name().toLowerCase(), "drawable", h.itemView.getContext().getPackageName());
        h.img.setImageResource(id);

        h.radio.setChecked(f == sel); // ✨ Correctly show selected item

        // Click handlers
        h.itemView.setOnClickListener(v -> {
            sel = f;
            notifyDataSetChanged();
            cb.accept(f);
        });

        h.radio.setOnClickListener(v -> {
            sel = f;
            notifyDataSetChanged();
            cb.accept(f);
        });
    }

    /**
     * Returns the total number of flavors.
     *
     * @return the number of flavors
     */
    @Override
    public int getItemCount() {
        return data.length;
    }

    /**
     * Returns the currently selected Flavor.
     *
     * @return the selected flavor
     */
    Flavor selected() {
        return sel;
    }
}
