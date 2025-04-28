package com.example.p5_213.ui;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import com.example.p5_213.model.*;
import java.util.function.Consumer;
import com.example.p5_213.R;
public final class FlavorAdapter extends RecyclerView.Adapter<FlavorAdapter.Holder>{
    private final Flavor[] data=Flavor.values(); private Flavor sel; private final Consumer<Flavor> cb;
    FlavorAdapter(Consumer<Flavor> c){this.cb=c;}
    static final class Holder extends RecyclerView.ViewHolder{
        ImageView img; TextView txt; Holder(View v){super(v); img=v.findViewById(R.id.cellImg); txt=v.findViewById(R.id.cellTxt);}
    }
    public Holder onCreateViewHolder(ViewGroup p,int vType){
        return new Holder(LayoutInflater.from(p.getContext()).inflate(R.layout.cell_flavor,p,false));}
    public void onBindViewHolder(Holder h,int i){
        Flavor f=data[i]; h.txt.setText(f.name());
        int id=h.itemView.getContext().getResources().getIdentifier("flavor_"+f.name().toLowerCase(),"drawable",h.itemView.getContext().getPackageName());
        h.img.setImageResource(id); h.itemView.setSelected(f==sel);
        h.itemView.setOnClickListener(v->{sel=f;notifyDataSetChanged();cb.accept(f);});
    }
    public int getItemCount(){return data.length;} Flavor selected(){return sel;}
}
