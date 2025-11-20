package kr.ac.dankook.ecg_dating;

// ChoiceActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        Button btnFood = findViewById(R.id.btn_food_worldcup);
        Button btnDate = findViewById(R.id.btn_date_worldcup);

        btnFood.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceActivity.this, WorldCup.class);
            intent.putExtra("TOPIC", "FOOD"); // "음식" 주제 전달
            startActivity(intent);
        });

        btnDate.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceActivity.this, WorldCup.class);
            intent.putExtra("TOPIC", "DATE"); // "데이트" 주제 전달
            startActivity(intent);
        });
    }
}

