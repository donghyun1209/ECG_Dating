package kr.ac.dankook.ecg_dating;

import android.annotation.SuppressLint;
import android.content.Intent; // <<<--- 1. Intent 클래스를 import 합니다.
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.dankook.ecg_dating.databinding.ActivityBalanceGameBinding;
import kr.ac.dankook.ecg_dating.databinding.ActivityMainBinding;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Balancegame extends AppCompatActivity {

    private ActivityBalanceGameBinding binding;

    // ... (기존 Question 클래스 및 변수 선언은 그대로)
    private static class Question {
        String title;
        String optA;
        String optB;
        Question(String t, String a, String b) {
            title = t; optA = a; optB = b;
        }
    }

    private final List<Question> questions = new ArrayList<>();
    private int qIndex = 0;

    private final ArrayList<Integer> answersA = new ArrayList<>();
    private final ArrayList<Integer> answersB = new ArrayList<>();

    private Integer bpmA = null;
    private Integer bpmB = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBalanceGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupQuestions();
        showQuestion();

        binding.btnOptionA.setOnClickListener(v -> {
            recordAnswer(0);
            highlightChoice(binding.btnOptionA, binding.btnOptionB);
        });

        binding.btnOptionB.setOnClickListener(v -> {
            recordAnswer(1);
            highlightChoice(binding.btnOptionB, binding.btnOptionA);
        });

        binding.btnNext.setOnClickListener(v -> nextQuestion());

        binding.btnConnect.setOnClickListener(v ->
                Toast.makeText(Balancegame.this, "블루투스 연결 기능을 구현하세요.", Toast.LENGTH_SHORT).show()
        );

        binding.btnDemoBpm.setOnClickListener(v -> simulateDemoBpm());

        // <<<--- 2. '돌아가기' 버튼에 대한 클릭 리스너를 추가합니다.
        binding.btnBack.setOnClickListener(v -> {
            // ConversationActivity로 돌아가는 인텐트(Intent)를 생성합니다.
            Intent intent = new Intent(Balancegame.this, ConversationActivity.class);

            // 인텐트를 실행하여 화면을 전환합니다.
            startActivity(intent);

            // 현재 Balancegame 액티비티를 종료하여 뒤로가기 스택에 남지 않도록 합니다.
            finish();
        });
        // --- 여기까지 추가 --->>>
    }

    // ... (setupQuestions, showQuestion 등 나머지 메서드는 그대로 유지)
    private void setupQuestions() {
        questions.add(new Question("영화 보기 vs 음악 듣기", "영화 보기", "음악 듣기"));
        questions.add(new Question("커피 vs 차", "커피", "차"));
        questions.add(new Question("집에서 쉬기 vs 밖에서 활동", "집에서 쉬기", "밖에서 활동"));
        questions.add(new Question("책 읽기 vs 운동하기", "책 읽기", "운동하기"));
        questions.add(new Question("초콜릿 vs 사탕", "초콜릿", "사탕"));
    }

    private void showQuestion() {
        if (qIndex >= questions.size()) {
            finishGame();
            return;
        }
        Question q = questions.get(qIndex);
        binding.tvQuestion.setText(q.title);
        binding.btnOptionA.setText(q.optA);
        binding.btnOptionB.setText(q.optB);

        int progress = (int) Math.round((qIndex / (double) questions.size()) * 100.0);
        binding.progress.setProgress(progress);
        binding.tvResult.setText("");
        binding.btnOptionA.setAlpha(1f);
        binding.btnOptionB.setAlpha(1f);
    }

    private void recordAnswer(int choice) {
        if (answersA.size() > qIndex) {
            answersA.set(qIndex, choice);
        } else {
            answersA.add(choice);
            // 데모용: 상대는 반대로 선택했다고 가정 (실제 구현 시에는 네트워크 / 블루투스로 받기)
            answersB.add(1 - choice);
        }
    }

    private void highlightChoice(View selected, View other) {
        selected.setAlpha(1f);
        other.setAlpha(0.6f);
    }

    private void nextQuestion() {
        if (answersA.size() <= qIndex) {
            Toast.makeText(this, "옵션을 선택하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        qIndex++;
        if (qIndex < questions.size()) {
            showQuestion();
        } else {
            finishGame();
        }
    }

    @SuppressLint("SetTextI18n")
    private void finishGame() {
        int common = 0;
        int total = Math.min(answersA.size(), answersB.size());
        for (int i = 0; i < total; i++) {
            if (answersA.get(i).intValue() == answersB.get(i).intValue()) common++;
        }
        int matchPercent = total > 0 ? (int) Math.round(common * 100.0 / total) : 0;
        int bpmScore = computeBpmScore();
        int totalScore = (int) Math.round(matchPercent * 0.7 + bpmScore * 0.3);

        binding.tvResult.setText("매칭 점수: " + totalScore + "%  (취향 일치: " + matchPercent + "%, 심박 가중치: " + bpmScore + "%)");
        if (totalScore >= 80) {
            binding.tvMatchIndicator.setText("💖 매칭 성공!");
        } else if (totalScore >= 50) {
            binding.tvMatchIndicator.setText("🙂 관심 있음");
        } else {
            binding.tvMatchIndicator.setText("😅 매칭 낮음");
        }
        binding.progress.setProgress(100);
    }

    private int computeBpmScore() {
        if (bpmA == null || bpmB == null) return 50;
        int diff = Math.abs(bpmA - bpmB);
        if (diff <= 5) return 100;
        if (diff <= 15) return 80;
        if (diff <= 30) return 60;
        if (diff <= 50) return 40;
        return 20;
    }

    private void simulateDemoBpm() {
        Random r = new Random();
        int randomA = r.nextInt(51) + 60; // 60..110
        int randomB = r.nextInt(51) + 60;
        updateBpmForPlayer("A", randomA);
        updateBpmForPlayer("B", randomB);
        Toast.makeText(this, "Demo BPM set: A=" + randomA + ", B=" + randomB, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    private void updateBpmForPlayer(String who, int bpm) {
        if (who == null) who = "A";
        who = who.toUpperCase();
        if (who.equals("A")) {
            bpmA = bpm;
            binding.tvABpm.setText("BPM: " + bpm);
        } else if (who.equals("B")) {
            bpmB = bpm;
            binding.tvBBpm.setText("BPM: " + bpm);
        } else {
            bpmA = bpm;
            binding.tvABpm.setText("BPM: " + bpm);
        }
    }
}
