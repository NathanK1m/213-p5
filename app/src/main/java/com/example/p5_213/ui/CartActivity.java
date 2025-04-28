package com.example.p5_213.ui;
import android.app.AlertDialog; import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.data.OrderRepository;
import com.example.p5_213.model.*;
import com.example.p5_213.R;

public final class CartActivity extends AppCompatActivity {
    private ListView lv; private TextView sub,tax,tot;
    public void onCreate(Bundle s){ super.onCreate(s);
        setContentView(R.layout.activity_cart);
        lv=findViewById(R.id.lvCart); sub=findViewById(R.id.tvSub); tax=findViewById(R.id.tvTax); tot=findViewById(R.id.tvTot);
        lv.setOnItemLongClickListener((a,v,i,id)->{ OrderRepository.get().current().removeItem(i); refresh(); return true;});
        findViewById(R.id.btnPlace).setOnClickListener(v->place()); findViewById(R.id.btnMain).setOnClickListener(v->finish());
        refresh();
    }
    private void refresh(){
        Order o=OrderRepository.get().current();
        lv.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,
                o.getItems().stream().map(MenuItem::toString).toList()));
        sub.setText(String.format("$%.2f",o.getSubtotal()));
        tax.setText(String.format("$%.2f",o.getTax()));
        tot.setText(String.format("$%.2f",o.getTotal()));
    }
    private void place(){
        if(OrderRepository.get().current().getItems().isEmpty()){ Toast.makeText(this,"Cart empty",Toast.LENGTH_SHORT).show(); return;}
        OrderRepository.get().place();
        new AlertDialog.Builder(this).setMessage("Order placed").setPositiveButton("OK",(d,i)->finish()).show();
    }
}
