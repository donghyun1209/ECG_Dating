package kr.ac.dankook.ecg_dating;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import kr.ac.dankook.ecg_dating.db.AppDatabase;
import kr.ac.dankook.ecg_dating.db.EcgData;

public class ConversationActivity extends AppCompatActivity {

    private Chronometer timerConversation;
    private Button btnQuestion, btnBalanceGame, btnIdealType;
    private Button btnEndDating;

    private Timer monitoringTimer;

    // 초기 기준 심박수 (Baseline)
    private double baseHrvMale = 0;
    private double baseHrvFemale = 0;

    // 점수 계산을 위한 누적 데이터
    private double totalExcitementMale = 0;
    private double totalExcitementFemale = 0;
    private int measureCount = 0;

    // 진동 울린 횟수 카운트
    private int vibCountMale = 0;
    private int vibCountFemale = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        // UI 연결
        timerConversation = findViewById(R.id.timer_conversation);
        btnQuestion = findViewById(R.id.btn_question);
        btnBalanceGame = findViewById(R.id.btn_balance_game);
        btnIdealType = findViewById(R.id.btn_ideal_type);
        btnEndDating = findViewById(R.id.btn_end_dating);

        initSession();

        // 타이머 시작
        timerConversation.setBase(SystemClock.elapsedRealtime());
        timerConversation.start();

        startHeartRateMonitoring();

        btnQuestion.setOnClickListener(v -> startActivity(new Intent(ConversationActivity.this, QuestionListActivity.class)));
        btnIdealType.setOnClickListener(v -> startActivity(new Intent(ConversationActivity.this, ChoiceActivity.class)));
        btnBalanceGame.setOnClickListener(v -> startActivity(new Intent(ConversationActivity.this, Balancegame.class)));

        // 종료 버튼
        btnEndDating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerConversation.stop();
                // 모니터링 종료 (이제 더 이상 측정 X)
                if (monitoringTimer != null) {
                    monitoringTimer.cancel();
                }

                String duration = timerConversation.getText().toString();
                showEyeContactPopup(duration);
            }
        });
    }

    private void initSession() {
        // 기준값 초기화
        baseHrvMale = 0;
        baseHrvFemale = 0;

        // 누적 데이터 초기화
        totalExcitementMale = 0;
        totalExcitementFemale = 0;

        // 카운트 초기화
        measureCount = 0;
        vibCountMale = 0;
        vibCountFemale = 0;

        // 혹시 돌아가고 있는 타이머가 있다면 확실히 종료
        if (monitoringTimer != null) {
            monitoringTimer.cancel();
            monitoringTimer = null;
        }

        // 로그 확인용
        System.out.println(">>> 세션 초기화 완료! 새로운 측정을 준비합니다.");
    }
    private void startHeartRateMonitoring() {
        monitoringTimer = new Timer();
        Random random = new Random();

        // 0초 뒤 시작, 3초마다 반복
        monitoringTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 현재 가짜 심박수 생성 (60 ~ 100 사이)
                double curMale = 60 + random.nextInt(40);
                double curFemale = 60 + random.nextInt(40);

                // [초기값 설정] 첫 측정이라면 기준점으로 삼음
                if (measureCount == 0) {
                    baseHrvMale = curMale;
                    baseHrvFemale = curFemale;
                    System.out.println(">>> 초기값 설정 완료! 남: " + baseHrvMale + ", 여: " + baseHrvFemale);
                }

                // [변화량 계산] 현재값 - 기준값
                double diffMale = curMale - baseHrvMale;
                double diffFemale = curFemale - baseHrvFemale;

                // 흥분도 누적 (심박수가 떨어지면 0으로 처리하거나, 그냥 더할 수도 있음. 여기선 양수만 더함)
                if (diffMale > 0) totalExcitementMale += diffMale;
                if (diffFemale > 0) totalExcitementFemale += diffFemale;

                measureCount++; // 측정 횟수 증가

                // 기준값보다 20 이상 높으면 진동 조건 충족
                if (diffMale > 20) {
                    vibCountMale++;
                }
                if (diffFemale > 20) {
                    vibCountFemale++;
                }
            }
        }, 0, 3000);
    }

    // 눈 마주치기 팝업
    private void showEyeContactPopup(String duration) {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_eye_contact, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);
        builder.setCancelable(false);
        TextView tvCountdown = popupView.findViewById(R.id.tv_countdown);
        AlertDialog dialog = builder.create();
        dialog.show();

        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountdown.setText(String.valueOf(millisUntilFinished / 1000 + 1));
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                // 눈 마주치기 끝난 후 최종 저장 및 결과 이동
                saveDataToDB(duration);
            }
        }.start();
    }

    // DB 저장 및 결과 화면 이동
    private void saveDataToDB(String duration) {
        new Thread(() -> {
            // 최종 점수 계산
            double avgExcitementMale = (measureCount > 0) ? (totalExcitementMale / measureCount) : 0;
            double avgExcitementFemale = (measureCount > 0) ? (totalExcitementFemale / measureCount) : 0;

            // 최종 점수
            double finalScoreDouble = 50 + (avgExcitementMale + avgExcitementFemale);
            if (finalScoreDouble > 100) finalScoreDouble = 100; // 100점 만점 제한

            // DB 저장
            AppDatabase db = AppDatabase.getDBInstance(ConversationActivity.this);
            EcgData data = new EcgData();
            data.setUserId("couple_log");
            data.setHrv(finalScoreDouble); // 점수를 HRV 필드에 저장 (임시)
            data.setMeasuredAt(System.currentTimeMillis());
            db.ecgDataDao().insert(data);

            // 결과 화면으로 데이터 전달
            Intent intent = new Intent(ConversationActivity.this, ResultActivity.class);
            intent.putExtra("DURATION", duration);

            // 계산된 실제 점수 전달
            intent.putExtra("HRV_SCORE", finalScoreDouble);

            // 누적된 진동 횟수 전달
            intent.putExtra("VIBRATION_MALE", vibCountMale);
            intent.putExtra("VIBRATION_FEMALE", vibCountFemale);

            startActivity(intent);
            finish();
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (monitoringTimer != null) {
            monitoringTimer.cancel();
        }
    }
}