package com.example.administrator.dianming;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/11/2 0002.
 */

public class shenhe extends AppCompatActivity {

    private Button bt4;
    private EditText cdT;
    private EditText kkT;
    private EditText qjT;

    private int id;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shenhe);

        kkT= (EditText) findViewById(R.id.editText2);//旷课次数
        cdT= (EditText) findViewById(R.id.editText5);//迟到次数
        qjT= (EditText) findViewById(R.id.editText10);//请假次数

        kkT.setEnabled(false);
        cdT.setEnabled(false);
        qjT.setEnabled(false);

        db=openOrCreateDatabase("student.db",MODE_PRIVATE,null);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        id=bundle.getInt("id");

        Cursor cursor= db.rawQuery("select Nlate,Nabsent,Nleave from studenttb where _id = ?",new String[]{id+""});

        while(cursor.moveToNext()){
            int i=cursor.getInt(cursor.getColumnIndex("Nabsent"))/2;
            if(i==0){
                kkT.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("Nabsent"))));
            }else{
                kkT.setText(String.valueOf(i));
            }
            int m=cursor.getInt(cursor.getColumnIndex("Nlate"))/2;
            if(m==0){
                cdT.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("Nlate"))));
            }else{
                cdT.setText(String.valueOf(m));
            }
            int n=cursor.getInt(cursor.getColumnIndex("Nleave"))/2;
            if(n==0){
                qjT.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("Nleave"))));
            }else{
                qjT.setText(String.valueOf(n));
            }
            //qjT.setText(String.valueOf(n));

        }


        cursor.close();

    }
}
