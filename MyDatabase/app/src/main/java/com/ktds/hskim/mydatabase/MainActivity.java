package com.ktds.hskim.mydatabase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ktds.hskim.mydatabase.db.DBHelper;
import com.ktds.hskim.mydatabase.vo.Person;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateDatabase;
    private Button btnInsertData;
    private Button btnSelectAllDatas;

    private ListView lvPersons;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateDatabase = (Button) findViewById(R.id.btnCreateDatabase);
        btnCreateDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lvPersons.setVisibility(View.INVISIBLE);

                final EditText etDBName = new EditText(MainActivity.this);
                etDBName.setHint("DB명을 입력하세요");

                // Dialog로 database의 이름을 입력 받음
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog  .setTitle("Database 이름 입력")
                        .setMessage("Database 이름 입력")
                        .setView(etDBName)
                        .setPositiveButton("생성", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if ( etDBName.getText().toString().length() > 0 ) {
                                    dbHelper = new DBHelper(MainActivity.this ,
                                                etDBName.getText().toString(),
                                                null,
                                                DBHelper.DB_VERSION);
                                    dbHelper.testDB();
                                }
                            }
                        })
                        .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }
        });

        btnInsertData = (Button) findViewById(R.id.btnInsertData);
        btnInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lvPersons.setVisibility(View.INVISIBLE);

                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText etName = new EditText(MainActivity.this);
                etName.setHint("이름 입력");
                final EditText etAge = new EditText(MainActivity.this);
                etAge.setHint("나이 입력");
                final EditText etPhone = new EditText(MainActivity.this);
                etPhone.setHint("핸드폰 번호 입력");
                final EditText etAddress = new EditText(MainActivity.this);
                etAddress.setHint("주소 입력");

                layout.addView(etName);
                layout.addView(etAge);
                layout.addView(etPhone);
                layout.addView(etAddress);

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog  .setTitle("정보 입력")
                        .setView(layout)
                        .setPositiveButton("등록", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = etName.getText().toString();
                                String age = etAge.getText().toString();
                                String phone = etPhone.getText().toString();
                                String address = etAddress.getText().toString();

                                if ( dbHelper == null ) {
                                    dbHelper = new DBHelper(MainActivity.this, "TEST", null, DBHelper.DB_VERSION);
                                }

                                Person person = new Person();
                                person.setName(etName.getText().toString());
                                person.setAge(etAge.getText().toString());
                                person.setPhone(etPhone.getText().toString());
                                person.setAddress(etAddress.getText().toString());

                                dbHelper.addPerson(person);
                            }
                        })
                        .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
            }
        });

        lvPersons = (ListView) findViewById(R.id.lvPersons);
        btnSelectAllDatas = (Button) findViewById(R.id.btnSelectAllDatas);
        btnSelectAllDatas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ListView를 보여준다
                lvPersons.setVisibility(View.VISIBLE);

                // Person 데이터를 모두 가져온다
                if ( dbHelper == null ) {
                    dbHelper = new DBHelper(MainActivity.this, "TEST", null, DBHelper.DB_VERSION);
                }
                List<Person> persons =  dbHelper.getAllPersons();

                // ListView에 Person 데이터를 보여준다
                lvPersons.setAdapter(new PersonListAdapter(persons, MainActivity.this));
            }
        });

    }

    private class PersonListAdapter extends BaseAdapter {

        private List<Person> persons;
        private Context context;

        public PersonListAdapter(List<Person> persons, Context context) {
            this.persons = persons;
            this.context = context;
        }
        @Override
        public int getCount() {
            return this.persons.size();
        }
        @Override
        public Object getItem(int position) {
            return this.persons.get(position);
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

                TextView tvId = new TextView(context);
                tvId.setPadding(10,10,10,10);
                TextView tvName = new TextView(context);
                tvName.setPadding(10,10,10,10);
//                TextView tvAge = new TextView(context);
//                tvAge.setPadding(10,10,10,10);
//                TextView tvPhone = new TextView(context);
//                tvPhone.setPadding(10,10,10,10);
//                TextView tvAddress = new TextView(context);
//                tvAddress.setPadding(10,10,10,10);

                ((LinearLayout) convertView).addView(tvId);
                ((LinearLayout) convertView).addView(tvName);
//                ((LinearLayout) convertView).addView(tvAge);
//                ((LinearLayout) convertView).addView(tvPhone);
//                ((LinearLayout) convertView).addView(tvAddress);

                holder = new Holder();
                holder.tvId = tvId;
                holder.tvName = tvName;
//                holder.tvAge = tvAge;
//                holder.tvPhone = tvPhone;
//                holder.tvAddress = tvAddress;

                convertView.setTag(holder);
            }
            else {
                holder = (Holder) convertView.getTag();
            }

            Person person = (Person) getItem(position);
            holder.tvId.setText(person.get_id() + "");
            holder.tvName.setText(person.getName());
//            holder.tvAge.setText(person.getAge());
//            holder.tvPhone.setText(person.getPhone());
//            holder.tvAddress.setText(person.getAddress());

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
        public TextView tvId;
        public TextView tvName;
//        public TextView tvAge;
//        public TextView tvPhone;
//        public TextView tvAddress;
    }

}
