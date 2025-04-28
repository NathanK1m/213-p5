package com.example.p5_213.ui;
import android.app.AlertDialog; import android.os.Bundle; import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.*;
import com.example.p5_213.R;
import java.util.ArrayList;

public final class BurgerActivity extends AppCompatActivity {
    private RadioGroup breadGrp, pattyGrp; private Spinner qty; private TextView price;
    private CheckBox let,tom,on,avo,che;
    public void onCreate(Bundle s){ super.onCreate(s);
        setContentView(R.layout.activity_burger);
        breadGrp = findViewById(R.id.grpBread);
        pattyGrp = findViewById(R.id.grpPatty);
        qty      = findViewById(R.id.spnQty);
        price    = findViewById(R.id.tvPrice);
        let=findViewById(R.id.cbLet); tom=findViewById(R.id.cbTom);
        on=findViewById(R.id.cbOn);   avo=findViewById(R.id.cbAvo); che=findViewById(R.id.cbChe);

        View.OnClickListener u = v->update(); breadGrp.setOnCheckedChangeListener((g,i)->update());
        pattyGrp.setOnCheckedChangeListener((g,i)->update());
        let.setOnClickListener(u); tom.setOnClickListener(u); on.setOnClickListener(u);
        avo.setOnClickListener(u); che.setOnClickListener(u);
        qty.setOnItemSelectedListener(sel); update();

        findViewById(R.id.btnAdd).setOnClickListener(v->add());
        findViewById(R.id.btnMain).setOnClickListener(v->finish());
        findViewById(R.id.btnCombo).setOnClickListener(
                v -> startActivity(ComboActivity.intent(this, build())));
    }
    private void update(){ try{ price.setText(String.format("$%.2f", build().price())); }
    catch(Exception e){ price.setText("$0.00");}}
    private Burger build(){
        RadioButton b = (RadioButton)breadGrp.getSelectedToggle();
        RadioButton p = (RadioButton)pattyGrp.getSelectedToggle();
        if(b==null||p==null) throw new IllegalStateException();
        Bread bread = Bread.valueOf(b.getText().toString().toUpperCase());
        boolean dbl = p.getText().toString().equalsIgnoreCase("Double");
        ArrayList<AddOns> adds=new ArrayList<>();
        if(let.isChecked())adds.add(AddOns.LETTUCE);
        if(tom.isChecked())adds.add(AddOns.TOMATOES);
        if(on.isChecked()) adds.add(AddOns.ONIONS);
        if(avo.isChecked())adds.add(AddOns.AVOCADO);
        if(che.isChecked())adds.add(AddOns.CHEESE);
        int q=Integer.parseInt(qty.getSelectedItem().toString());
        return new Burger(bread,dbl,adds,q);
    }
    private void add(){
        try{ OrderRepository.get().current().addItem(build());
            Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show();
        }catch(Exception e){ warn("Choose bread & patty");}
    }
    private void warn(String m){ new AlertDialog.Builder(this).setMessage(m).setPositiveButton("OK",null).show();}
    private final AdapterView.OnItemSelectedListener sel=new AdapterView.OnItemSelectedListener(){
        public void onNothingSelected(AdapterView<?> p){} public void onItemSelected(AdapterView<?> a,View v,int i,long id){update();}};
}
