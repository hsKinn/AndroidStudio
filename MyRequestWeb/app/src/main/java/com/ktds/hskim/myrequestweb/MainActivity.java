package com.ktds.hskim.myrequestweb;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends ActionBarActivity {

    private Button btnSearch;
    private EditText etURL;
    private TextView tvResult;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        etURL = (EditText) findViewById(R.id.etURL);
        tvResult = (TextView) findViewById(R.id.tvResult);

        handler = new Handler();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // HTTP로 요청 보냄 Thread 작업 필요
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 작업 시작
                        String url = etURL.getText().toString();
                        try {
                            // 요청을 보낼 URL 정보
                            URL httpURL = new URL(url);

                            // 요청을 보내기 위한 준비 (요청 전)
                            HttpURLConnection conn = (HttpURLConnection) httpURL.openConnection();
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            // 최대 요청 지연 시간 설정 5초 이상 걸릴 경우 요청 취소
                            conn.setConnectTimeout(5000);
                            // Get 요청 POST 요청 원할 경우 "POST" 작성
                            conn.setRequestMethod("GET");

                            // 요청 보내기 + 응답 받기
                            int responseCode = conn.getResponseCode();
                            // 요청과 응답이 제대로 이뤄졌는지 검사
                            // HTTP_OK : 응답이 200 OK라는 의미
                            if ( responseCode == HttpURLConnection.HTTP_OK ) {
                                // 응답 본문 전체를 담는다
                                final StringBuffer responseBody = new StringBuffer();
                                // 응답 본문의 한 줄 한 줄씩 얻어온다
                                String line = null;
                                // 응답 본문을 가진 InputStream을 받아옴
                                // BufferedReader는 InputStream을 한 줄씩 얻어올 수 있는 객체
                                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                                // 응답 본문 종료 시 까지 반복
                                while ( (line = reader.readLine()) != null ) {
                                    responseBody.append(line + "\n");
                                }

                                reader.close();
                                conn.disconnect();

                                // 독립된 Thread에서 Android의 Main Thread로 접근할 수 있는 Handler 생성 후 UI View 컨트롤
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvResult.setText(responseBody.toString());
                                    }
                                });
                            }
                        } catch (MalformedURLException e) {
                            return;
                        } catch (IOException e) {
                            return;
                        }
                    }
                }).start();
            }
        });
    }
}
