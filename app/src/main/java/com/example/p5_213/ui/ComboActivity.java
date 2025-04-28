package com.example.p5_213.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.*;
import com.example.p5_213.R;

public final class ComboActivity extends AppCompatActivity {
    private static final String EXTRA="BASE";
    public static Intent intent(Context c,Sandwich base){
        return new Intent(c,ComboActivity.class).putExtra(EXTRA,base);
    }
    private Sandwich base; private Spinner side,drink,qty; private TextView price; private ImageView imgSide,imgDrink;
    public void onCreate(Bundle s){ super.onCreate(s);
        setContentView(R.layout.activity_combo);
        base=(Sandwich)getIntent().getSerializableExtra(EXTRA);
        ((TextView)findViewById(R.id.tvBase)).setText(base.toString());
        side=findViewById(R.id.spnSide); drink=findViewById(R.id.spnDrink); qty=findViewById(R.id.spnQty);
        price=findViewById(R.id.tvPrice); imgSide=findViewById(R.id.imgSide); imgDrink=findViewById(R.id.imgDrink);
        AdapterView.OnItemSelectedListener l=new Sel(); side.setOnItemSelectedListener(l); drink.setOnItemSelectedListener(l); qty.setOnItemSelectedListener(l); update();
        findViewById(R.id.btnAdd).setOnClickListener(v->{ if(check()){ OrderRepository.get().current().addItem(build()); Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show();}});
        findViewById(R.id.btnMain).setOnClickListener(v->finish());
    }
    private boolean check(){ if(side.getSelectedItemPosition()<0||drink.getSelectedItemPosition()<0){Toast.makeText(this,"Pick side & drink",Toast.LENGTH_SHORT).show();return false;}return true;}
    private Combo build(){
        Side s=new Side(SideType.values()[side.getSelectedItemPosition()],Size.SMALL,1);
        Beverage b=new Beverage(Size.MEDIUM,Flavor.values()[drink.getSelectedItemPosition()],1);
        int q=Integer.parseInt(qty.getSelectedItem().toString());
        return new Combo(base,b,s,q);
    }
    private void update(){
        if(side.getSelectedItemPosition()>=0){
            String img="side_"+SideType.values()[side.getSelectedItemPosition()].name().toLowerCase();
            imgSide.setImageResource(getRes(img));}
        if(drink.getSelectedItemPosition()>=0){
            String img="flavor_"+Flavor.values()[drink.getSelectedItemPosition()].name().toLowerCase();
            imgDrink.setImageResource(getRes(img));}
        price.setText(String.format("$%.2f",build().price()));
    }
    private int getRes(String n){ return getResources().getIdentifier(n,"drawable",getPackageName());}
    private final class Sel implements AdapterView.OnItemSelectedListener{
        public void onNothingSelected(AdapterView<?> p){} public void onItemSelected(AdapterView<?> a,View v,int i,long id){update();}}
}
