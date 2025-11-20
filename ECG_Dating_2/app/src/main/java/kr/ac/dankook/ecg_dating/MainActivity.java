package kr.ac.dankook.ecg_dating;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private Button btnShowPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowPopup = findViewById(R.id.btn_show_popup);
        
        btnShowPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConnectPopup();
                //팝업 컨텐츠 보여주는 코드임.
            }
        });
    }

    // 팝업 창을 생성 + 보여주는 메서드
    private void showConnectPopup() {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_connect, null);
        // 팝업 창 레이아웃을 가져오기.(popup_connect.xml)
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);

        Button btnConnectDevice = popupView.findViewById(R.id.btn_connect_device);
        Button btnStartDating = popupView.findViewById(R.id.btn_start_dating);
        // 팝업 창 내부의 버튼들을 찾아 놓기
        
        AlertDialog dialog = builder.create();
        // 팝업 창 객체를 생성
        btnConnectDevice.setOnClickListener(new View.OnClickListener() {
            // 'ECG 장치 연결하기' 버튼 클릭 
            @Override
            public void onClick(View v) {
                // 여기에 블루투스 연결 코드 넣으면 된다.
                Toast.makeText(MainActivity.this, "ECG 장치 연결 중...", Toast.LENGTH_SHORT).show();

                // 연결이 성공하면 '소개팅 시작!' 버튼을 활성화
                btnStartDating.setEnabled(true);
                //지금은 무조건 통과로 구현된 상태
                Toast.makeText(MainActivity.this, "연결 성공!", Toast.LENGTH_SHORT).show();
            }
        });

        btnStartDating.setOnClickListener(new View.OnClickListener() {
            // '소개팅 시작!' 버튼 클릭
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "소개팅을 시작합니다!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ConversationActivity.class);
                startActivity(intent);
                // 팝업 창을 닫습니다.
                dialog.dismiss();
            }
        });
        
        dialog.show();
        //팝업창 보여주기
    }
}