package kr.ac.dankook.ecg_dating;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

// DB 관련 클래스 임포트
import kr.ac.dankook.ecg_dating.db.AppDatabase;
import kr.ac.dankook.ecg_dating.db.EcgData;
import kr.ac.dankook.ecg_dating.db.EcgDataDao;

public class ConversationActivity extends AppCompatActivity {

    private Chronometer timerConversation;
    private Button btnQuestion, btnBalanceGame, btnIdealType;
    private Button btnEndDating;

    // 실시간 모니터링을 위한 타이머
    private Timer monitoringTimer;

    // 진동 횟수 카운트
    private int vibCountMale = 0;
    private int vibCountFemale = 0;

    // 초기값
    private double baseHrvMale = 0;
    private double baseHrvFemale = 0;
    private boolean isBaseSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        timerConversation = findViewById(R.id.timer_conversation);
        btnQuestion = findViewById(R.id.btn_question);
        btnBalanceGame = findViewById(R.id.btn_balance_game);
        btnIdealType = findViewById(R.id.btn_ideal_type);
        btnEndDating = findViewById(R.id.btn_end_dating);


        initSession();


        timerConversation.setBase(SystemClock.elapsedRealtime());
        timerConversation.start();


        startHeartRateMonitoring();


        btnQuestion.setOnClickListener(v -> startActivity(new Intent(ConversationActivity.this, QuestionListActivity.class)));
        btnIdealType.setOnClickListener(v -> startActivity(new Intent(ConversationActivity.this, ChoiceActivity.class)));
        btnBalanceGame.setOnClickListener(v -> startActivity(new Intent(ConversationActivity.this, Balancegame.class)));


        btnEndDating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timerConversation.stop();


                if (monitoringTimer != null) {
                    monitoringTimer.cancel();
                    monitoringTimer = null;
                }


                String duration = timerConversation.getText().toString();


                showEyeContactPopup(duration);
            }
        });
    }


    private void initSession() {
        baseHrvMale = 0;
        baseHrvFemale = 0;
        isBaseSet = false;
        vibCountMale = 0;
        vibCountFemale = 0;

        // 기존 DB 데이터 삭제
        new Thread(() -> {
            AppDatabase.getDBInstance(this).ecgDataDao().deleteAll();
            System.out.println(">>> DB 초기화 완료 (새 세션 시작)");
        }).start();
    }

    // 3초마다 데이터를 생성하고 DB에 저장 (Log)
    private void startHeartRateMonitoring() {
        monitoringTimer = new Timer();
        Random random = new Random();

        // DB 인스턴스 미리 가져오기
        AppDatabase db = AppDatabase.getDBInstance(this);

        // 0초 대기 후 시작, 3초(3000ms)마다 반복
        monitoringTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 데이터 생성 (나중에는 블루투스 수신값으로 대체)
                // 60~100 사이의 랜덤 심박수
                double curMale = 60 + random.nextInt(40);
                double curFemale = 60 + random.nextInt(40);
                long now = System.currentTimeMillis();

                // [DB 저장] 남성 데이터 Insert
                EcgData dataMale = new EcgData();
                dataMale.setUserId("user_male");
                dataMale.setHrv(curMale);
                dataMale.setMeasuredAt(now);
                db.ecgDataDao().insert(dataMale);

                // [DB 저장] 여성 데이터 Insert
                EcgData dataFemale = new EcgData();
                dataFemale.setUserId("user_female");
                dataFemale.setHrv(curFemale);
                dataFemale.setMeasuredAt(now);
                db.ecgDataDao().insert(dataFemale);

                // 로그 확인
                System.out.println(">>> 실시간 저장: 남(" + (int)curMale + "), 여(" + (int)curFemale + ")");

                if (!isBaseSet) {
                    // 첫 측정값이면 기준값(평소 심박수)으로 설정
                    baseHrvMale = curMale;
                    baseHrvFemale = curFemale;
                    isBaseSet = true;
                    System.out.println(">>> 기준값 설정됨: 남(" + baseHrvMale + "), 여(" + baseHrvFemale + ")");
                } else {
                    // 기준값보다 20 이상 높으면 '설렘(긴장)'으로 간주 -> 진동
                    if (curMale - baseHrvMale > 20) {
                        vibCountMale++;
                    }
                    if (curFemale - baseHrvFemale > 20) {
                        vibCountFemale++;
                    }
                }
            }
        }, 0, 3000);
    }

    // [핵심 기능 2] 눈 마주치기 팝업 및 카운트다운
    private void showEyeContactPopup(String duration) {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_eye_contact, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);
        builder.setCancelable(false); // 터치로 닫기 방지

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
                // 카운트다운 종료 후 결과 처리 시작
                processResultAndFinish(duration);
            }
        }.start();
    }

    // [핵심 기능 3] DB에서 전체 데이터를 조회하여 결과 산출
    private void processResultAndFinish(String duration) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDBInstance(this);
            EcgDataDao dao = db.ecgDataDao();

            // DB 쿼리: 전체 평균 심박수 가져오기
            double avgMale = dao.getAverageHrv("user_male");
            double avgFemale = dao.getAverageHrv("user_female");

            // DB 쿼리: 가장 처음 저장된 값(기준값) 가져오기
            List<EcgData> maleHistory = dao.getAllDataForUser("user_male");
            List<EcgData> femaleHistory = dao.getAllDataForUser("user_female");

            double firstMale = maleHistory.isEmpty() ? 0 : maleHistory.get(0).getHrv();
            double firstFemale = femaleHistory.isEmpty() ? 0 : femaleHistory.get(0).getHrv();

            // 점수 알고리즘 (임시 구현)
            double scoreMale = (avgMale - firstMale);
            double scoreFemale = (avgFemale - firstFemale);

            if (scoreMale < 0) scoreMale = 0;
            if (scoreFemale < 0) scoreFemale = 0;

            double finalScore = 50 + scoreMale + scoreFemale;
            if (finalScore > 100) finalScore = 100;

            System.out.println(">>> 최종 분석: 남(평균:" + (int)avgMale + " - 초기:" + (int)firstMale + "), 여(평균:" + (int)avgFemale + ")");
            System.out.println(">>> 최종 점수: " + finalScore);

            // 4. 결과 화면으로 이동
            Intent intent = new Intent(ConversationActivity.this, ResultActivity.class);
            intent.putExtra("DURATION", duration);
            intent.putExtra("HRV_SCORE", finalScore);
            intent.putExtra("VIBRATION_MALE", vibCountMale);
            intent.putExtra("VIBRATION_FEMALE", vibCountFemale);

            startActivity(intent);
            finish(); // 현재 액티비티 종료
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 앱이 백그라운드로 가거나 종료될 때 타이머 정리
        if (monitoringTimer != null) {
            monitoringTimer.cancel();
            monitoringTimer = null;
        }
        if (timerConversation != null) {
            timerConversation.stop();
        }
    }
}