package com.ktds.hskim.mymemoapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.ktds.hskim.mymemoapp.vo.Memo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 206-006 on 2016-06-20.
 */
public class DBHelper extends SQLiteOpenHelper {

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
        sb.append( " CREATE TABLE MEMO_TABLE ( ");
        sb.append( " _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append( " SUBJECT TEXT, " );
        sb.append( " CONTENT TEXT, ");
        sb.append( " DATE DATE); ");

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
        Toast.makeText(context, "Version 올라감", Toast.LENGTH_SHORT).show();
    }

    public void memoDB() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public List<Memo> getAllMemoList() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, SUBJECT, CONTENT, DATE FROM MEMO_TABLE ");

        // 일기 전용 DB 객체
        SQLiteDatabase db = getReadableDatabase();

        // SELECT 실행
        Cursor cursor = db.rawQuery(sb.toString(), null);

        List<Memo> memoList = new ArrayList<Memo>();

        Memo memo = null;

        while ( cursor.moveToNext() ) {
            memo = new Memo();
            memo.set_id(cursor.getInt(0));
            memo.setSubject(cursor.getString(1));
            memo.setContent(cursor.getString(2));
            memo.setDate(cursor.getString(3));

            memoList.add(memo);
        }

        return memoList;
    }

    public void addToMemo(Memo memo) {

        // 사용 가능한 DB 객체 가져오기
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();

        // 시간값 받아오기
        sb.append(" SELECT datetime('now') ");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        if ( cursor.moveToNext() ) {
            memo.setDate(cursor.getString(0));
            sb = new StringBuffer();
        }

        // Memo Data Insert
        sb.append(" INSERT INTO MEMO_TABLE ( ");
        sb.append(" SUBJECT, CONTENT, DATE ) ");
        sb.append(" VALUES ( ?, ?, ? ) ");

        db.execSQL(sb.toString(), new Object[]{ memo.getSubject(), memo.getContent(), memo.getDate() });

        Toast.makeText(context, "메모 입력 완료", Toast.LENGTH_SHORT).show();
    }

    public Memo getOneMemo (int id) {

        Memo memo = new Memo();

        // 일기 전용 DB 객체
        SQLiteDatabase db = getReadableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, SUBJECT, CONTENT, DATE FROM MEMO_TABLE WHERE _ID = ? ");

        // SELECT 실행
        Cursor cursor = db.rawQuery(sb.toString(), new String[]{String.valueOf(id)});

        if ( cursor.moveToNext() ) {
            memo.set_id(cursor.getInt(0));
            memo.setSubject(cursor.getString(1));
            memo.setContent(cursor.getString(2));
            memo.setDate(cursor.getString(3));
        }
        return memo;
    }

    public void modifyToMemo (Memo memo) {
        // 사용 가능한 DB 객체 가져오기
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();

        // 시간값 받아오기
        sb.append(" SELECT datetime('now', 'localtime') ");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        if ( cursor.moveToNext() ) {
            memo.setDate(cursor.getString(0));
            sb = new StringBuffer();
        }

        // Memo Data Insert
        sb.append(" UPDATE MEMO_TABLE ");
        sb.append(" SET SUBJECT = ?, CONTENT = ?, DATE = ? ");
        sb.append(" WHERE _ID = ? ");

        db.execSQL(sb.toString(), new Object[]{ memo.getSubject(), memo.getContent(), memo.getDate(), memo.get_id() });

        Toast.makeText(context, "메모 수정 완료", Toast.LENGTH_SHORT).show();
    }

    public void deleteToMemo(int id) {
        // 사용 가능한 DB 객체 가져오기
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();

        sb.append(" DELETE FROM MEMO_TABLE ");
        sb.append(" WHERE _ID = ? ");

        db.execSQL(sb.toString(), new Object[]{ id });

        Toast.makeText(context, "메모 삭제 완료", Toast.LENGTH_SHORT).show();
    }
}
