package kr.ac.dankook.ecg_dating;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // 로그 확인용
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List; // 리스트 사용

// ▼▼▼ [DB 관련 임포트 추가] ▼▼▼
import kr.ac.dankook.ecg_dating.db.AppDatabase;
import kr.ac.dankook.ecg_dating.db.EcgData;

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

        // [추가됨] 계산된 점수 받기 (기본값 50.0)
        double rawScore = intent.getDoubleExtra("HRV_SCORE", 50.0);

        // 점수 표시 (소수점 버리고 정수로 변환)
        int displayScore = (int) rawScore;

        // 화면 텍스트 설정
        tvScore.setText("점수: " + displayScore + "점");
        tvDuration.setText("소개팅 시간: " + duration);
        tvVibrationDetail.setText("남(" + vibMale + "회) 여(" + vibFemale + "회)");

        //  [확인용] DB에 잘 저장됐는지 로그로 찍어보기 (백그라운드 스레드)
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDBInstance(this);
            List<EcgData> history = db.ecgDataDao().getAllEcgData();

            // Logcat에서 'DB_CHECK' 태그로 검색하면 볼 수 있음.
            Log.d("DB_CHECK", "=== 저장된 기록 개수: " + history.size() + " ===");
            if (!history.isEmpty()) {
                EcgData latest = history.get(0);
                Log.d("DB_CHECK", "최근 기록 - ID: " + latest.getId() + ", 점수: " + latest.getHrv());
            }
        }).start();

        // 메인으로 돌아가기 버튼
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ResultActivity.this, MainActivity.class);

                // 스택 비우기
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(mainIntent);
                finish(); // 현재 액티비티 종료
            }
        });
    }
}