package kr.ac.dankook.ecg_dating;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.text.SimpleDateFormat; // 시간 변환용
import java.util.Date;
import java.util.Locale;

// DB 관련 임포트
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

        Intent intent = getIntent();
        String duration = intent.getStringExtra("DURATION");

        int vibMale = intent.getIntExtra("VIBRATION_MALE", 0);
        int vibFemale = intent.getIntExtra("VIBRATION_FEMALE", 0);

        double rawScore = intent.getDoubleExtra("HRV_SCORE", 50.0);
        int displayScore = (int) rawScore; // 소수점 제거

        tvScore.setText("점수: " + displayScore + "점");
        tvDuration.setText("소개팅 시간: " + duration);
        tvVibrationDetail.setText("남(" + vibMale + "회) 여(" + vibFemale + "회)");

        // [DB 확인용] 저장된 모든 데이터를 로그로 출력
        checkDatabaseLog();

        // 메인으로 돌아가기 버튼
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ResultActivity.this, MainActivity.class);

                // 스택 비우기 (중요!)
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(mainIntent);
                finish(); // 현재 액티비티 종료
            }
        });
    }

    // DB 로그 확인 함수
    private void checkDatabaseLog() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDBInstance(this);

            // 모든 데이터 가져오기
            List<EcgData> history = db.ecgDataDao().getAllEcgData();

            Log.d("DB_CHECK", "========================================");
            Log.d("DB_CHECK", "=== [최종 저장된 데이터 총 개수: " + history.size() + "] ===");

            // 저장된 데이터를 하나씩 로그에 출력 (최대 10개만 출력해서 확인)
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());

            int count = 0;
            for (EcgData data : history) {
                String timeStr = sdf.format(new Date(data.getMeasuredAt()));
                Log.d("DB_CHECK", "ID:" + data.getId() +
                        " | 유저:" + data.getUserId() +
                        " | 심박수:" + (int)data.getHrv() +
                        " | 시간:" + timeStr);

                count++;
                if (count >= 10) break; // 너무 많으면 10개까지만 보여줌
            }
            Log.d("DB_CHECK", "========================================");

        }).start();
    }
}