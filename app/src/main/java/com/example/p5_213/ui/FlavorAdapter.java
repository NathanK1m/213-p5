package com.example.p5_213.ui;

import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import com.example.p5_213.model.*;
import java.util.function.Consumer;
import com.example.p5_213.R;

public final class FlavorAdapter extends RecyclerView.Adapter<FlavorAdapter.Holder> {
    private final Flavor[] data = Flavor.values();
    private Flavor sel;
    private final Consumer<Flavor> cb;

    FlavorAdapter(Consumer<Flavor> c) {
        this.cb = c;
    }

    static final class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        RadioButton radio;  // ✨ NOW using RadioButton

        Holder(View v) {
            super(v);
            img = v.findViewById(R.id.cellImg);
            radio = v.findViewById(R.id.radioButton); // ✨ Updated to match your XML id
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_flavor, parent, false));
    }

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

    @Override
    public int getItemCount() {
        return data.length;
    }

    Flavor selected() {
        return sel;
    }
}
