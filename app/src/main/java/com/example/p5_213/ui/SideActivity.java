package com.example.p5_213.ui;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.*;
import com.example.p5_213.R;

public final class SideActivity extends AppCompatActivity {
    private Spinner type,size,qty; private TextView price;
    public void onCreate(Bundle s){ super.onCreate(s);
        setContentView(R.layout.activity_side);
        type=findViewById(R.id.spnType); size=findViewById(R.id.spnSize); qty=findViewById(R.id.spnQty); price=findViewById(R.id.tvPrice);
        AdapterView.OnItemSelectedListener l=new Sel(); type.setOnItemSelectedListener(l); size.setOnItemSelectedListener(l); qty.setOnItemSelectedListener(l); update();
        findViewById(R.id.btnAdd).setOnClickListener(v->add()); findViewById(R.id.btnMain).setOnClickListener(v->finish());
    }
    private Side build(){
        SideType t=SideType.values()[type.getSelectedItemPosition()];
        Size sz   =Size.values()[size.getSelectedItemPosition()];
        int q     =Integer.parseInt(qty.getSelectedItem().toString());
        return new Side(t,sz,q);
    }
    private void update(){ price.setText(String.format("$%.2f", build().price())); }
    private void add(){ OrderRepository.get().current().addItem(build()); Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show(); }
    private final class Sel implements AdapterView.OnItemSelectedListener{
        public void onNothingSelected(AdapterView<?> p){} public void onItemSelected(AdapterView<?> a,View v,int i,long id){update();}}
}
