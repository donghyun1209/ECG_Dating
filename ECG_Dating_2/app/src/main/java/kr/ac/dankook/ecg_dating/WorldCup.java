package kr.ac.dankook.ecg_dating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldCup extends AppCompatActivity {

    private TextView tvRoundTitle;
    private ImageView ivLeftItem, ivRightItem;
    private TextView tvLeftItemName, tvRightItemName;

    private List<WorldCupItem> itemList; // 전체 아이템 리스트
    private List<WorldCupItem> currentRoundList; // 현재 라운드 대결 리스트
    private List<WorldCupItem> nextRoundList; // 다음 라운드 진출 리스트
    private int currentMatchIndex; // 현재 대결 인덱스

    private View leftLayout, rightLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worldcup);

        // UI 요소 초기화
        tvRoundTitle = findViewById(R.id.tv_round_title);
        ivLeftItem = findViewById(R.id.iv_left_item);
        tvLeftItemName = findViewById(R.id.tv_left_item_name);
        ivRightItem = findViewById(R.id.iv_right_item);
        tvRightItemName = findViewById(R.id.tv_right_item_name);
        leftLayout = findViewById(R.id.layout_left_item);
        rightLayout = findViewById(R.id.layout_right_item);
        String topic = getIntent().getStringExtra("TOPIC");


        // 게임 시작
        startGame(topic);

        // 이미지 클릭 리스너 설정
        leftLayout.setOnClickListener(v -> selectItem(true));
        rightLayout.setOnClickListener(v -> selectItem(false));
    }

    private void startGame(String topic) {
        itemList = new ArrayList<>();

        // ChoiceActivity에서 넘겨준 topic에 따라 다른 리스트를 로드합니다.
        if ("FOOD".equals(topic)) {
            setTitle("음식 월드컵"); // 액티비티 제목 변경
            // 음식 월드컵 아이템 추가
            itemList.add(new WorldCupItem("치킨", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("피자", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("짜장면", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("짬뽕", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("떡볶이", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("라면", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("김치찌개", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("된장찌개", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("제육볶음", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("탕수육", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("된장찌개", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("된장찌개", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("된장찌개", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("된장찌개", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("된장찌개", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("된장찌개", R.drawable.food_jajang));


        } else if ("DATE".equals(topic)) {
            setTitle("데이트 월드컵"); // 액티비티 제목 변경
            // 데이트 월드컵 아이템 추가
            itemList.add(new WorldCupItem("영화관 가기", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("공원 산책", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("맛집 탐방", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("쇼핑몰 데이트", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("쇼핑몰 데이트", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("쇼핑몰 데이트", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("쇼핑몰 데이트", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("쇼핑몰 데이트", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("쇼핑몰 데이트", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("쇼핑몰 데이트", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("쇼핑몰 데이트", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("쇼핑몰 데이트", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("쇼핑몰 데이트", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("쇼핑몰 데이트", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("쇼핑몰 데이트", R.drawable.food_jajang));
            itemList.add(new WorldCupItem("쇼핑몰 데이트", R.drawable.food_jajang));


        } else {
            // 혹시 모를 오류 상황 처리
            Toast.makeText(this, "주제를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            finish(); // 액티비티를 종료하여 앱이 멈추지 않도록 함
            return;
        }

        // 아이템 리스트를 섞어줍니다.
        Collections.shuffle(itemList);

        // 16강으로 시작하도록 리스트 조절
        if (itemList.size() > 16) {
            currentRoundList = new ArrayList<>(itemList.subList(0, 16));
        } else {
            currentRoundList = new ArrayList<>(itemList);
        }

        nextRoundList = new ArrayList<>();
        currentMatchIndex = 0;

        showNextMatch();
    }


    private void startNextRound() {
        // 1. 최종 우승자 결정
        if (nextRoundList.size() == 1) {
            WorldCupItem winner = nextRoundList.get(0);

            // ResultActivity로 우승자 정보 전달
            Intent intent = new Intent(this, Result_WorldCup.class);
            intent.putExtra("WINNER_NAME", winner.getName());
            intent.putExtra("WINNER_IMAGE_ID", winner.getImageId());
            startActivity(intent);
            finish(); // 월드컵 액티비티 종료
            return;
        }

        // 2. 다음 라운드 준비
        // 이번 라운드의 승자 리스트(nextRoundList)를 다음 라운드의 대결 리스트(currentRoundList)로 설정
        currentRoundList = new ArrayList<>(nextRoundList);
        // 다음 라운드의 승자를 담을 리스트는 새로 비워줌
        nextRoundList.clear();
        // 대결 인덱스 초기화
        currentMatchIndex = 0;

        // 3. 다음 라운드 시작
        showNextMatch();
    }

    private void showNextMatch() {
        if (currentMatchIndex >= currentRoundList.size()) {
            // 현재 라운드 종료, 다음 라운드로 이동
            startNextRound();
            return;
        }

        // 라운드 타이틀 업데이트
        int round = currentRoundList.size();
        if (round == 2) {
            tvRoundTitle.setText("결승");
        } else {
            tvRoundTitle.setText(round + "강");
        }

        // 현재 대결할 두 아이템을 가져옴
        WorldCupItem leftItem = currentRoundList.get(currentMatchIndex);
        WorldCupItem rightItem = currentRoundList.get(currentMatchIndex + 1);

        // UI에 아이템 정보 표시
        ivLeftItem.setImageResource(leftItem.getImageId());
        tvLeftItemName.setText(leftItem.getName());
        ivRightItem.setImageResource(rightItem.getImageId());
        tvRightItemName.setText(rightItem.getName());
    }

    // 이 코드로 selectItem 메소드를 전체 교체하세요.
    // 이 코드로 기존의 selectItem 메소드를 완전히 교체하세요.

    private void selectItem(boolean isLeftSelected) {
        // 0. 즉시 중복 클릭 방지
        leftLayout.setClickable(false);
        rightLayout.setClickable(false);

        // 1. 승자/패자 결정 및 다음 라운드 리스트에 추가 (게임 로직)
        WorldCupItem selectedItem = isLeftSelected ?
                currentRoundList.get(currentMatchIndex) :
                currentRoundList.get(currentMatchIndex + 1);
        nextRoundList.add(selectedItem);

        final View winnerView = isLeftSelected ? leftLayout : rightLayout;
        final View loserView = isLeftSelected ? rightLayout : leftLayout;

        // 2. '선택받은' 시각적 애니메이션 실행 (복잡한 리스너 없음!)
        // 승자: 강조 효과
        winnerView.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(400)
                .start();

        // 패자: 사라지는 효과
        loserView.animate()
                .alpha(0f)
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(400)
                .start();

        // 3. 일정 시간(0.9초) 후, 다음 대결을 준비하는 '단일' 로직 실행
        // (패자 애니메이션 400ms + 승자 강조 시간 500ms)
        new android.os.Handler().postDelayed(() -> {
            // 3-1. 다음 대결을 위해 모든 뷰의 상태를 깨끗하게 초기화
            leftLayout.setScaleX(1.0f);
            leftLayout.setScaleY(1.0f);
            leftLayout.setAlpha(1.0f); // 투명도 복원
            rightLayout.setScaleX(1.0f);
            rightLayout.setScaleY(1.0f);
            rightLayout.setAlpha(1.0f); // 투명도 복원

            // 3-2. 다음 매치로 인덱스 이동
            currentMatchIndex += 2;

            // 3-3. 다음 대결 보여주기 (라운드가 끝나면 startNextRound가 내부적으로 호출됨)
            showNextMatch();

            // 3-4. 모든 준비가 끝나면 다시 클릭 가능하도록 설정
            leftLayout.setClickable(true);
            rightLayout.setClickable(true);

        }, 900); // 0.9초 후에 실행
    }

}
