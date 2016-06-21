package com.ktds.hskim.mymemoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ktds.hskim.mymemoapp.db.DBHelper;
import com.ktds.hskim.mymemoapp.vo.Memo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    private ListView lvMemoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvMemoList = (ListView) findViewById(R.id.lvMemoList);

        // Title 설정
        setTitle("메모 App hskim");

        // DB 없다면 생성
        if ( dbHelper == null ) {
            dbHelper = new DBHelper(MainActivity.this, "MEMO", null, 1);
        }

        // MemoList 가져오기
        List<Memo> memoList = dbHelper.getAllMemoList();

        // MemoLIst 출력
        lvMemoList.setAdapter(new MemoListAdapter(memoList, MainActivity.this));

        // Floating 버튼 Memo 추가
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private class MemoListAdapter extends BaseAdapter{

        private List<Memo> memoList;
        private Context context;

        public MemoListAdapter(List<Memo> memoLIst, Context context) {
            this.memoList = memoLIst;
            this.context = context;
        }

        @Override
        public int getCount() {
            return this.memoList.size();
        }

        @Override
        public Object getItem(int position) {
            return this.memoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Holder holder = null;

            if ( convertView == null ) {
                convertView = new LinearLayout(context);
                ((LinearLayout) convertView).setOrientation(LinearLayout.HORIZONTAL);

                TextView tvSubject = new TextView(context);
                tvSubject.setPadding(10,10,10,10);
                tvSubject.setTextSize(20);
                TextView tvDate = new TextView(context);
                tvDate.setPadding(10,10,10,10);
                TextView tvId = new TextView(context);
                tvId.setVisibility(View.INVISIBLE);

                ((LinearLayout) convertView).addView(tvSubject);
                ((LinearLayout) convertView).addView(tvDate);

                holder = new Holder();
                holder.tvSubject = tvSubject;
                holder.tvDate = tvDate;
                holder.tvId = tvId;

                convertView.setTag(holder);
            }
            else {
                holder = (Holder) convertView.getTag();
            }

            Memo memo = (Memo) getItem(position);
            holder.tvSubject.setText(memo.getSubject());
            holder.tvDate.setText(memo.getDate());
            holder.tvId.setText(memo.get_id() + "");

            final Holder finalHolder = holder;

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = Integer.parseInt(finalHolder.tvId.getText().toString());

                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

    private class Holder {
        public TextView tvSubject;
        public TextView tvDate;
        public TextView tvId;
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
