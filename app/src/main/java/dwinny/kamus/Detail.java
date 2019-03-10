package dwinny.kamus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Detail extends AppCompatActivity {

    TextView detail_Kata, detail_Arti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detail_Kata = findViewById(R.id.detail_kata);
        detail_Arti= findViewById(R.id.detail_arti);
        detail_Kata.setText(getIntent().getStringExtra("kata"));
        detail_Arti.setText(getIntent().getStringExtra("arti"));

    }
}
