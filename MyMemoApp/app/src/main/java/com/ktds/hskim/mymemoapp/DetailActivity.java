package com.ktds.hskim.mymemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ktds.hskim.mymemoapp.db.DBHelper;
import com.ktds.hskim.mymemoapp.vo.Memo;

public class DetailActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    private Button btnSubmit;
    private Button btnDelete;

    private EditText etSubject;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if ( dbHelper == null ) {
            dbHelper = new DBHelper(DetailActivity.this, "MEMO", null, 1);
        }

        Intent intent = getIntent();
        final int id = intent.getIntExtra("id", 0);

        etSubject = (EditText) findViewById(R.id.etSubject);
        etContent = (EditText) findViewById(R.id.etContent);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        if ( id == 0) {
            setTitle("메모 등록");
            btnDelete.setVisibility(View.INVISIBLE);
        }
        else {
            setTitle("메모 수정");
            btnSubmit.setText("수정");

            Memo memo = dbHelper.getOneMemo(id);

            etSubject.setText(memo.getSubject());
            etContent.setText(memo.getContent());
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( id == 0 ) {
                    // Memo 등록 로직
                    // 제목 입력 Validation
                    if ( etSubject.getText().toString().length() < 0 ) {
                        Toast.makeText(DetailActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Memo memo = new Memo();
                    memo.setSubject(etSubject.getText().toString());
                    memo.setContent(etContent.getText().toString());

                    dbHelper.addToMemo(memo);

                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    startActivity(intent);

                }
                else {
                    // Memo 수정 로직
                    // 제목 입력 Validation
                    if ( etSubject.getText().toString().length() < 0 ) {
                        Toast.makeText(DetailActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Memo memo = new Memo();
                    memo.set_id(id);
                    memo.setSubject(etSubject.getText().toString());
                    memo.setContent(etContent.getText().toString());

                    dbHelper.modifyToMemo(memo);

                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteToMemo(id);

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ( id == R.id.action_modify ) {
        }
        else if ( id == R.id.action_delete ) {
        }

        return super.onOptionsItemSelected(item);
    }
}
