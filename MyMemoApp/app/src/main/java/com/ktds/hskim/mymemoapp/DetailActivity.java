package com.ktds.hskim.mymemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ktds.hskim.mymemoapp.db.DBHelper;
import com.ktds.hskim.mymemoapp.vo.Memo;

public class DetailActivity extends ActionBarActivity {

    private DBHelper dbHelper;

    private EditText etSubject;
    private EditText etContent;

    private final int[] id = { 0 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (dbHelper == null) {
            dbHelper = new DBHelper(DetailActivity.this, "MEMO", null, 1);
        }

        Intent intent = getIntent();
        id[0] = intent.getIntExtra("id", 0);

        etSubject = (EditText) findViewById(R.id.etSubject);
        etContent = (EditText) findViewById(R.id.etContent);

        if (id[0] == 0) {
            setTitle("메모 등록");
        } else {
            setTitle("메모 수정");

            Memo memo = dbHelper.getOneMemo(id[0]);

            etSubject.setText(memo.getSubject());
            etContent.setText(memo.getContent());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_detail, menu);

        MenuItem submit = menu.findItem(R.id.action_submit);
        MenuItem modify = menu.findItem(R.id.action_modify);
        MenuItem delete = menu.findItem(R.id.action_delete);

        // 등록 시 수정 삭제 버튼 감추기
        if ( id[0] == 0 ) {
            modify.setVisible(false);
            delete.setVisible(false);
        }
        else {
            submit.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if ( itemId == R.id.action_submit ) {
            // Memo 등록 로직
            // 제목 입력 Validation
            if ( etSubject.getText().toString().length() < 1 ) {
                Toast.makeText(DetailActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                return false;
            }

            Memo memo = new Memo();
            memo.setSubject(etSubject.getText().toString());
            memo.setContent(etContent.getText().toString());

            dbHelper.addToMemo(memo);

            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if ( itemId == R.id.action_modify ) {
            // Memo 수정 로직
            // 제목 입력 Validation
            if ( etSubject.getText().toString().length() < 1 ) {
                Toast.makeText(DetailActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                return false;
            }

            Memo memo = new Memo();
            memo.set_id(id[0]);
            memo.setSubject(etSubject.getText().toString());
            memo.setContent(etContent.getText().toString());

            dbHelper.modifyToMemo(memo);

            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if ( itemId == R.id.action_delete ) {
            // Memo 삭제 로직
            dbHelper.deleteToMemo(id[0]);

            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
