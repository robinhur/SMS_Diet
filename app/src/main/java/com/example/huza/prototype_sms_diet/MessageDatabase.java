package com.example.huza.prototype_sms_diet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huza on 2018. 3. 24..
 */

public class MessageDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "messages";


    public MessageDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_QUERY = "create table IF NOT EXISTS messages ( " +
                "                   id TEXT PRIMARY KEY," +
                "                   fromto TEXT," +
                "                   addr TEXT," +
                "                   content TEXT," +
                "                   time TEXT);";
        sqLiteDatabase.execSQL(CREATE_QUERY);


        //create_db();
    }

    public void create_db() {
        String CREATE_QUERY = "create table IF NOT EXISTS messages ( " +
                "                   id TEXT PRIMARY KEY," +
                "                   fromto TEXT," +
                "                   addr TEXT," +
                "                   content TEXT," +
                "                   time TEXT);";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_QUERY);
        db.close();
    }

    public void delete_db() {
        String CREATE_QUERY = "drop table IF EXISTS messages";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_QUERY);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void add_message(Message msg) {
        String INSERT_QUERY = "insert OR IGNORE into messages(id, fromto, addr, content, time)" +
                "                   values ('" +  msg.getID() +"', " +
                "                           '" +  msg.getDispDate() +"', " +
                "                           '" +  msg.getThread() +"', " +
                "                           '" +  msg.getBody().replace("\'", "\'\'") +"', " +
                "                           '" +  msg.getDate() +"');";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(INSERT_QUERY);
        db.close();
    }

    public List<Message> getAllMessage(){
        List<Message> messageList = new ArrayList<>();

        String SELECT_ALL_QUERY = "SELECT * FROM messages";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Message tmp_m = new Message( cursor.getString(0) );

                tmp_m.setBody( cursor.getString(3) );
                tmp_m.setDate( cursor.getString(4) );
                tmp_m.setThread( cursor.getString(2) );
                tmp_m.setAddr( cursor.getString(1) );

                messageList.add(tmp_m);
            } while (cursor.moveToNext());
        }

        db.close();
        return messageList;
    }
}
