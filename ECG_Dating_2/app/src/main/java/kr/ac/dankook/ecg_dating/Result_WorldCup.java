package kr.ac.dankook.ecg_dating;

// ResultActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Result_WorldCup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worldcup_result);

        ImageView ivWinner = findViewById(R.id.iv_winner_image);
        TextView tvWinner = findViewById(R.id.tv_winner_name);
        Button btnBack = findViewById(R.id.btn_back_to_choice);
        Button btnBackToConversation = findViewById(R.id.btn_back_to_conversation);

        // WorldCupActivity에서 보낸 Intent를 받습니다.
        Intent intent = getIntent();
        String winnerName = intent.getStringExtra("WINNER_NAME");
        int winnerImageId = intent.getIntExtra("WINNER_IMAGE_ID", 0);

        // 받은 정보로 화면을 설정합니다.
        tvWinner.setText(winnerName);
        if (winnerImageId != 0) {
            ivWinner.setImageResource(winnerImageId);
        }

        // '돌아가기' 버튼 클릭 시 ChoiceActivity로 이동합니다.
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(Result_WorldCup.this, ChoiceActivity.class);
            // 이전에 쌓인 액티비티들을 모두 지우고 새로운 ChoiceActivity를 시작합니다.
            backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backIntent);
            finish(); // 현재 결과 액티비티는 종료합니다.
        });

        btnBackToConversation.setOnClickListener(v -> {
            // ConversationActivity로 이동합니다.
            Intent mainIntent = new Intent(Result_WorldCup.this, ConversationActivity.class);
            // 월드컵 관련 액티비티 스택을 모두 지우고 ConversationActivity로 돌아갑니다.
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish(); // 현재 결과 액티비티는 종료합니다.
        });
    }
}


