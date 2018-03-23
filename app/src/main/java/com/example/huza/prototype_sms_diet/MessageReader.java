package com.example.huza.prototype_sms_diet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 허씨네 on 2018-03-19.
 */

public class MessageReader {

    private Context mContext;
    private ContentResolver contentResolver;
    private Cursor cursor;

    private MessageDatabase db;

    public MessageReader(Context context) {
        mContext = context;
        contentResolver = mContext.getContentResolver();
        db = new MessageDatabase(context);
    }

    public int getALLmsg_cnt() {
        List<Message> msg_list = db.getAllMessage();
        return msg_list.size();
    }

    public List<String> getALLmsg() {
        List<Message> msg_list = db.getAllMessage();
        List<String> msg_string_list = new ArrayList<String>();

        if (msg_list.isEmpty()) {
            msg_string_list.add("msg is empty");
        }

        for (int i = 0; i < msg_list.size(); i++) {
            msg_string_list.add(msg_list.get(i).toString());
        }

        return msg_string_list;
    }

    public void parse_mms(Message msg) {
        String mmsId = "mid = " + msg.getID();
        Cursor cursor_mms =  contentResolver.query(Uri.parse("content://mms/part"), null, mmsId, null, null);

        while(cursor_mms.moveToNext()) {

            String pid = cursor_mms.getString(cursor_mms.getColumnIndex("_id"));
            String type = cursor_mms.getString(cursor_mms.getColumnIndex("ct"));
            if ("text/plain".equals(type)) {
                //msg.setBody(msg.getBody() + c.getString(c.getColumnIndex("text")));
                //Log.d("SMS_TEST", msg.getBody() + cursor_mms.getString(cursor_mms.getColumnIndex("text")));
            } else  {//if (type.contains("image")) {
                //msg.setImg(getMmsImg(pid));
                //Log.d("SMS_TEST", "NOT TEXT : " + type);
            }


        }
        cursor_mms.close();
    }

    public String getMMSaddr(String id) {
        String sel = new String("msg_id=" + id);
        String uriString = MessageFormat.format("content://mms/{0}/addr", id);
        Uri uri = Uri.parse(uriString);
        Cursor c = contentResolver.query(uri, null, sel, null, null);
        String name = "";
        while (c.moveToNext()) {
/*          String[] col = c.getColumnNames();
            String str = "";
            for(int i = 0; i < col.length; i++) {
                str = str + col[i] + ": " + c.getString(i) + ", ";
            }
            System.out.println(str);*/
            String t = c.getString(c.getColumnIndex("address"));
            if(!(t.contains("insert")))
                name = name + t + " ";
        }
        c.close();
        return name;
    }

    public void read_message() {
        cursor = contentResolver.query(Uri.parse("content://sms"), null, null, null, "date DESC");

        int cnt = 0;

        Log.d("SMS_TEST", String.valueOf(cursor.getCount()));
        Toast.makeText(mContext, String.valueOf(cursor.getCount()), Toast.LENGTH_SHORT).show();
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
//                String msgData = "";
//                for(int idx=0;idx<cursor.getColumnCount();idx++)
//                {
//                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
//                }
                // use msgData
                if (cnt < 100) {
                    Message msg = new Message(cursor.getString(cursor.getColumnIndex("_id")));
                    msg.setDate(cursor.getString(cursor.getColumnIndex("date")));
                    msg.setAddr(cursor.getString(cursor.getColumnIndex("Address")));
                    msg.setBody(cursor.getString(cursor.getColumnIndex("body")));
                    msg.setDirection(cursor.getString(cursor.getColumnIndex("type")));
                    msg.setContact(cursor.getString(cursor.getColumnIndex("person")));

                    db.add_message(msg);
                    //Log.d("SMS_TEST", msg.toString());

//                    for (int i = 0; i < cursor.getColumnCount(); i++) {
//                        Log.d("SMS_TEST", cursor.getColumnName(i) + " : " + cursor.getString(i));
//                    }
                }

                cnt++;
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }

        cursor = contentResolver.query(Uri.parse("content://mms"), null, null, null, "date DESC");

        cnt = 0;

        Log.d("SMS_TEST", String.valueOf(cursor.getCount()));
        Toast.makeText(mContext, String.valueOf(cursor.getCount()), Toast.LENGTH_SHORT).show();
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                if (cnt < 30) {
                    Message msg = new Message(cursor.getString(cursor.getColumnIndex("_id")));
                    msg.setThread(cursor.getString(cursor.getColumnIndex("thread_id")));
                    msg.setDate(cursor.getString(cursor.getColumnIndex("date")));
                    msg.setAddr(getMMSaddr(msg.getID()));

                    parse_mms(msg);

                    //Log.d("SMS_TEST", msg.toString());

                    cnt++;
                }
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
    }
}
