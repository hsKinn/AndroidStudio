package com.ktds.hskim.mycameraapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnTakePicture;
    private Button btnSendPicture;
    private ImageView ivPicture;

    private String imagePath;
    private String encodedImageString;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
        btnSendPicture = (Button) findViewById(R.id.btnSendPicture);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Camera App이 있는지 조사
                if ( isExistsCameraApplication() ) {
                    // Camera Application을 실행
                    Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // 찍은 사진을 보관할 파일 객체를 만들어서 보냄
                    File picture = savePictureFile();

                    if ( picture != null ) {
                        cameraApp.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
                        startActivityForResult(cameraApp, 10000);
                    }
                }
            }
        });

        btnSendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Image를 Base64 타입으로 암호화
                Base64Utility base64 = new Base64Utility();
                encodedImageString = base64.encodeJPEG(imagePath);

                // 인터넷 접근 권한 얻어오기
                PermissionRequester.Builder requester = new PermissionRequester.Builder(MainActivity.this);
                int result = requester.create().request(Manifest.permission.INTERNET, 30000, new PermissionRequester.OnClickDenyButtonListener() {
                    @Override
                    public void onClick(Activity activity) {
                        Toast.makeText(MainActivity.this, "사진 보내기를 취소했습니다", Toast.LENGTH_SHORT).show();
                    }
                });

                if ( result == PermissionRequester.ALREADY_GRANTED || result == PermissionRequester.NOT_SUPPORT_VERSION ) {
                    // 사진을 보낸다
                    SendPictureTask task = new SendPictureTask();
                    task.execute(encodedImageString);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if ( requestCode == 30000
                && permissions[0].equals(Manifest.permission.INTERNET)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 사진을 보낸다
                SendPictureTask task = new SendPictureTask();
                task.execute(encodedImageString);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ( requestCode == 10000 && resultCode == RESULT_OK ) {
            // 사진을 ImageVIew에 보여줌줌
            BitmapFactory.Options factory = new BitmapFactory.Options();
            factory.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(imagePath);

            factory.inJustDecodeBounds = false;
            factory.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, factory);
            ivPicture.setImageBitmap(bitmap);
       }
    }


    /**
     * Android에 Camera Application이 설치되어 있는지 확인
     * @return
     */
    private boolean isExistsCameraApplication() {

        // Android의 모든 Application을 얻어온다
        PackageManager packageManager = getPackageManager();

        // Camera Application
        Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // MediaStore.ACTION_IMAGE_CAPTURE의 Intent를 처리할 수 있는 Application 정보 가져옴
        List<ResolveInfo> cameraApps = packageManager.queryIntentActivities(cameraApp, PackageManager.MATCH_DEFAULT_ONLY);

        return cameraApps.size() > 0;
    }

    /**
     * Camera에서 찍은 사진 파일을 외부 저장소에 저장
     * @return
     */
    private File savePictureFile() {

        // 외부 저장소 쓰기 권한 받기
        PermissionRequester.Builder requester = new PermissionRequester.Builder(this);

        int result = requester  .create()
                                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, 20000, new PermissionRequester.OnClickDenyButtonListener() {
                                    @Override
                                    public void onClick(Activity activity) {

                                    }
                                });

        // 권한 사용 거부를 누르지 않았을 때
        if ( result == PermissionRequester.ALREADY_GRANTED || result == PermissionRequester.REQUEST_PERMISSION ) {

            // 사진 파일의 이름 설정
            String timestamp = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());
            String fileName = "IMG_" + timestamp;

            // 사진 파일 저장 위치 설정
            File pictureStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MYAPP/");

            // 사진 파일 저장될 폴더가 없다면 생성함
            if ( !pictureStorage.exists() ) {
                pictureStorage.mkdirs();
            }

            try {
                File file = File.createTempFile(fileName, ".jpg", pictureStorage);

                // 사진 파일의 절대 경로를 얻어온다
                imagePath = file.getAbsolutePath();

                // 찍힌 사진을 갤러리에 저장한다다
                Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
                File f = new File( imagePath );
                Uri contentUri = Uri.fromFile( f );
                mediaScanIntent.setData( contentUri );
                this.sendBroadcast( mediaScanIntent );

               return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 암호화된 사진을 보내는 AsyncTask
     */
    private class SendPictureTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... params) {

            HttpClient.Builder http = new HttpClient.Builder("POST", "Web Server URL 입력");

            // Parameter 추가
            http.addOrReplaceParameter("image", params[0]);

            HttpClient post = http.create();
            post.request();

            int statusCode = post.getHttpStatusCode();
            Log.d("RESULT", statusCode + "");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(MainActivity.this, "사진 전송", Toast.LENGTH_SHORT).show();
        }
    }
}
