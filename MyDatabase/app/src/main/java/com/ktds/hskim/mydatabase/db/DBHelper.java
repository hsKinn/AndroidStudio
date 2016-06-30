package com.ktds.hskim.mydatabase.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.ktds.hskim.mydatabase.vo.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 206-006 on 2016-06-20.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 2;

    private Context context;


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    /**
     * DB가 존재하지 않을 때, 딱 한번 실행된다
     * DB를 생성하는 역할
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuffer sb = new StringBuffer();
        sb.append( " CREATE TABLE TEST_TABLE ( ");
        sb.append( " _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append( " NAME TEXT, " );
        sb.append( " AGE INTEGER, ");
        sb.append( " PHONE TEXT); ");
        // SQL 실행
        db.execSQL(sb.toString());

        Toast.makeText(context, "DB 생성 완료", Toast.LENGTH_SHORT).show();
    }

    /**
     * Application의 버전이 올라가 Table 구조가 변경되었을 때 실행
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if ( oldVersion == 1 && newVersion == 2 ) {
            StringBuffer sb = new StringBuffer();
            sb.append(" ALTER TABLE TEST_TABLE ADD ADDRESS TEXT ");

            db.execSQL(sb.toString());
        }

        Toast.makeText(context, "Version 올라감", Toast.LENGTH_SHORT).show();
    }

    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public void addPerson(Person person) {
        // 사용 가능한 DB 객체 가져오기
        SQLiteDatabase db = getWritableDatabase();

        // Person Data를 Insert
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO TEST_TABLE ( ");
        sb.append(" NAME, AGE, PHONE, ADDRESS) ");
        sb.append(" VALUES ( ?, ?, ?, ? ) ");

        db.execSQL(sb.toString(), new Object[]{ person.getName(), Integer.parseInt(person.getAge()), person.getPhone(), person.getAddress() });
//        sb.append(" VALUES ( #NAME#, #AGE#, #PHONE# )");
//
//        String query = sb.toString();
//        query = query.replace("#NAME#", "'" + person.getName() + "'");
//        query = query.replace("#AGE#", person.getAge());
//        query = query.replace("#PHONE#", "'" + person.getPhone() + "'");

//        db.execSQL(query);

        Toast.makeText(context, "Insert 완료", Toast.LENGTH_SHORT).show();
    }

    public List<Person> getAllPersons() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, NAME, AGE, PHONE, ADDRESS FROM TEST_TABLE ");

        // 일기 전용 DB 객체
        SQLiteDatabase db = getReadableDatabase();

        // SELECT 실행
        Cursor cursor = db.rawQuery(sb.toString(), null);

        List<Person> persons = new ArrayList<Person>();

        Person person = null;

        while ( cursor.moveToNext() ) {
            person = new Person();
            person.set_id(cursor.getInt(0));
            person.setName(cursor.getString(1));
            person.setAge(cursor.getString(2));
            person.setPhone(cursor.getString(3));
            person.setAddress(cursor.getString(4));

            persons.add(person);
        }

        return persons;
    }

    public Person getOnePerson(int id) {

        Person person = new Person();

        // 일기 전용 DB 객체
        SQLiteDatabase db = getReadableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, NAME, AGE, PHONE, ADDRESS FROM TEST_TABLE WHERE _ID = ? ");

        // SELECT 실행
        Cursor cursor = db.rawQuery(sb.toString(), new String[]{String.valueOf(id)});

        if ( cursor.moveToNext() ) {
            person.set_id(cursor.getInt(0));
            person.setName(cursor.getString(1));
            person.setAge(cursor.getString(2));
            person.setPhone(cursor.getString(3));
            person.setAddress(cursor.getString(4));
        }

        return person;
    }
}
