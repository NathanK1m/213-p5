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
        Button btn; // ✨ Now using Button instead of TextView

        Holder(View v) {
            super(v);
            img = v.findViewById(R.id.cellImg);
            btn = v.findViewById(R.id.button); // ✨ Bind the Button here
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_flavor, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder h, int i) {
        Flavor f = data[i];
        h.btn.setText(f.name()); // ✨ Set Button text
        int id = h.itemView.getContext().getResources().getIdentifier(
                "flavor_" + f.name().toLowerCase(), "drawable", h.itemView.getContext().getPackageName());
        h.img.setImageResource(id);

        h.itemView.setSelected(f == sel);

        h.itemView.setOnClickListener(v -> {
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
