package com.example.p5_213.ui;
import android.content.Intent; import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p5_213.R;

public final class MenuActivity extends AppCompatActivity {
    public void onCreate(Bundle s){ super.onCreate(s);
        setContentView(R.layout.menu_page);
        link(R.id.btnBurger,   BurgerActivity.class);
        link(R.id.btnSandwich, SandwichActivity.class);
        link(R.id.btnSide,     SideActivity.class);
        link(R.id.btnDrink,    BeverageActivity.class);
        link(R.id.btnCart,     CartActivity.class);
        link(R.id.btnHistory,  ArchiveActivity.class);

        findViewById(R.id.btnStart).setOnClickListener(view -> startActivity(new Intent(this, StartActivity.class)));
    }
    private void link(int id, Class<?> c){
        findViewById(id).setOnClickListener(v -> startActivity(new Intent(this,c)));
    }
}
