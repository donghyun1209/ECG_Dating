package kr.ac.dankook.ecg_dating;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QuestionListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        RecyclerView questionsRecyclerView = findViewById(R.id.questionsRecyclerView);
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<String> questions = new ArrayList<>();
        questions.add("1. 어떤 음식을 좋아하세요?");
        questions.add("2. 취미가 무엇인가요?");
        questions.add("3. 주말에 보통 무얼 하면서 보내세요?");
        questions.add("4. 최근에 재미있게 본 영화나 드라마가 있나요?");
        questions.add("5. 어떤 음악을 즐겨 들으세요?");
        questions.add("6. 여행을 좋아하시나요? 가보고 싶은 곳이 있으세요?");
        questions.add("7. 성격의 장단점을 하나씩 말해본다면?");
        questions.add("8. 스트레스는 어떻게 푸는 편이세요?");
        questions.add("9. 이상형이 어떻게 되시나요?");
        questions.add("10. 10년 뒤 자신의 모습은 어떨 것 같으세요?");

        QuestionAdapter adapter = new QuestionAdapter(questions);
        questionsRecyclerView.setAdapter(adapter);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionsRecyclerView);

        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}