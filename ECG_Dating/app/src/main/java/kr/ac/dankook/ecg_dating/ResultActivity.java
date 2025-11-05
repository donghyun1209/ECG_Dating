package kr.ac.dankook.ecg_dating;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView tvScore, tvDuration, tvVibrationDetail;
    private Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvScore = findViewById(R.id.tv_score);
        tvDuration = findViewById(R.id.tv_duration);
        tvVibrationDetail = findViewById(R.id.tv_vibration_detail);
        btnBackToMain = findViewById(R.id.btn_back_to_main);

        // ConversationActivity에서 보낸 데이터 받기
        Intent intent = getIntent();
        String duration = intent.getStringExtra("DURATION");
        int vibMale = intent.getIntExtra("VIBRATION_MALE", 0);
        int vibFemale = intent.getIntExtra("VIBRATION_FEMALE", 0);

        // 점수 알고리즘 여기에 구현
        int score = 85; // (임시 점수)

        tvScore.setText("점수: " + score + "점");
        tvDuration.setText("소개팅 시간: " + duration);
        tvVibrationDetail.setText("남(" + vibMale + "회) 여(" + vibFemale + "회)");
        // 받은 데이터로 화면 텍스트 설정

        // 메인으로 돌아가기 버튼
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 메인 화면으로 이동
                Intent mainIntent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
    }
}
