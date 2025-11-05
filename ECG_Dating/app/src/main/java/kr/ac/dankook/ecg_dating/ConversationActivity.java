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

public class ConversationActivity extends AppCompatActivity {

    private Chronometer timerConversation;
    private Button btnQuestion, btnBalanceGame, btnIdealType;
    private Button btnEndDating;
    //버튼 4개

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        timerConversation = findViewById(R.id.timer_conversation);
        btnQuestion = findViewById(R.id.btn_question);
        btnBalanceGame = findViewById(R.id.btn_balance_game);
        btnIdealType = findViewById(R.id.btn_ideal_type);
        btnEndDating = findViewById(R.id.btn_end_dating);
        //버튼 연결 부분


        timerConversation.setBase(SystemClock.elapsedRealtime());
        timerConversation.start();
        // 타이머 시작

        // 여기에 각 버튼 눌렀을 때 효과들 만들면 됨.

        // 종료 버튼 설정
        btnEndDating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerConversation.stop();
                // 메인 타이머 정지
                String duration = timerConversation.getText().toString();
                // 현재까지의 시간을 문자열로 가져옴
                showEyeContactPopup(duration);
                //눈 마주치기 팝업 띄우기
            }
        });
    }

    // 눈 마주치기 팝업 + 10초 타이머
    private void showEyeContactPopup(String duration) {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_eye_contact, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);
        builder.setCancelable(false);
        //밖 눌러도 안꺼짐 (강제 눈맞춤ㅋㅋ)

        TextView tvCountdown = popupView.findViewById(R.id.tv_countdown);
        AlertDialog dialog = builder.create();
        dialog.show();

        // 10초 카운트다운 타이머
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                tvCountdown.setText(String.valueOf(secondsLeft + 1));
                // 남은 시간을 초 단위로 계산
                // 10..9..1
            }

            @Override
            public void onFinish() {
                // 10초가 끝나면
                dialog.dismiss();
                // 팝업 닫기

                // 결과 화면으로 이동
                Intent intent = new Intent(ConversationActivity.this, ResultActivity.class);

                // 소개팅 시간 (문자열) 전달
                intent.putExtra("DURATION", duration);

                // 진동 횟수 (지금은 임시값 전달)
                // 진동 울린 횟수 전달하는거 구현
                intent.putExtra("VIBRATION_MALE", 0);
                intent.putExtra("VIBRATION_FEMALE", 0);

                startActivity(intent);
                finish();
            }
        }.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timerConversation.stop();
    }
}