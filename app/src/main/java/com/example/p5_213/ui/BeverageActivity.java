package com.example.p5_213.ui;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity; import androidx.recyclerview.widget.RecyclerView;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.*;
import com.example.p5_213.R;
public final class BeverageActivity extends AppCompatActivity {
    private Spinner size,qnt; private TextView price; private FlavorAdapter ad;
    public void onCreate(Bundle s){ super.onCreate(s);
        setContentView(R.layout.activity_beverage);
        size=findViewById(R.id.spnSize); qnt=findViewById(R.id.spnQty); price=findViewById(R.id.tvPrice);
        RecyclerView rv=findViewById(R.id.rvFlavors); ad=new FlavorAdapter(f->update()); rv.setAdapter(ad);
        AdapterView.OnItemSelectedListener sel=new Sel(); size.setOnItemSelectedListener(sel); qnt.setOnItemSelectedListener(sel); update();
        findViewById(R.id.btnAdd).setOnClickListener(v->{ if(ad.selected()==null){Toast.makeText(this,"Pick flavour",Toast.LENGTH_SHORT).show();return;}
            OrderRepository.get().current().addItem(build()); Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show();});
        findViewById(R.id.btnMain).setOnClickListener(v->finish());
    }
    private Beverage build(){
        Size sz=Size.values()[size.getSelectedItemPosition()];
        int q=Integer.parseInt(qnt.getSelectedItem().toString());
        return new Beverage(sz,ad.selected(),q);
    }
    private void update(){ if(ad.selected()!=null) price.setText(String.format("$%.2f",build().price())); else price.setText("$0.00"); }
    private final class Sel implements AdapterView.OnItemSelectedListener{
        public void onNothingSelected(AdapterView<?> p){} public void onItemSelected(AdapterView<?> a,View v,int i,long id){update();}}
}
