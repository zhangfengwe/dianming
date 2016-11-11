package com.example.administrator.dianming;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private Button bt1;
    private Button bt2;
    private Button bt3;

    private EditText idT;
    private EditText nameT;
    private EditText sidT;
    private EditText classT;

    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;

    private ImageView img;

    public int id=0;
    private SQLiteDatabase db=null;
    public  String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        bt1= (Button) findViewById(R.id.button);//下一位
        bt2= (Button) findViewById(R.id.button2);//添加
        bt3= (Button) findViewById(R.id.button3);//统计

        idT= (EditText) findViewById(R.id.editText);//编号
        sidT= (EditText) findViewById(R.id.editText3);//学号
        nameT= (EditText) findViewById(R.id.editText4);//姓名
        classT= (EditText) findViewById(R.id.editText8);//班级

        img= (ImageView) findViewById(R.id.imageView);

        idT.setEnabled(false);
        sidT.setEnabled(false);
        nameT.setEnabled(false);
        classT.setEnabled(false);

        cb1= (CheckBox) findViewById(R.id.checkBox);//迟到
        cb2= (CheckBox) findViewById(R.id.checkBox2);//旷课
        cb3= (CheckBox) findViewById(R.id.checkBox3);//请假

        //filePath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/a.png";

        db=openOrCreateDatabase("student.db",MODE_PRIVATE,null);

        db.execSQL("create table if not exists studenttb (_id integer primary key autoincrement," +
                "Sid text not null unique," +
                "Sname text not null," +
                "Ssex text not null," +
                "class text not null,"+
                "picture text,"+
                "Nlate integer,Nabsent integer,Nleave integer)");

        /*
         *下一位按钮事件
         */
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id++;
                try{
                    Cursor cursor= db.rawQuery("select _id,Sid,Sname,class,picture from studenttb where _id = ?",new String[]{id+""});
                    while(cursor.moveToNext()){
                        idT.setText(cursor.getInt(cursor.getColumnIndex("_id"))+"");
                        sidT.setText(cursor.getString(cursor.getColumnIndex("Sid"))+"");
                        nameT.setText(cursor.getString(cursor.getColumnIndex("Sname"))+"");
                        classT.setText(cursor.getString(cursor.getColumnIndex("class"))+"");
                        filePath= cursor.getString(cursor.getColumnIndex("picture"));
                        Bitmap bitmap1=readImg(filePath);
                        if(bitmap1!=null){
                            img.setImageBitmap(bitmap1);
                        }else{
                            return;
                        }
                    }
                    cursor.close();
                }catch (Exception e){
                }
                cb1.setChecked(false);
                cb2.setChecked(false);
                cb3.setChecked(false);
            }
        });

        /*
         *添加按钮事件
         */
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,tianjia.class);
                startActivity(intent);
            }
        });

        /*
         *统计按钮事件
         */
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,shenhe.class);
                Bundle bundle=new Bundle();
                bundle.putInt("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
                cb1.setChecked(false);
                cb2.setChecked(false);
                cb3.setChecked(false);
            }
        });

        /*
         *checkbox事件
         */
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cb2.isChecked()==false&&cb3.isChecked()==false){
                    Cursor cursor=db.rawQuery("select Nlate from studenttb where _id = ?",new String[]{id+""});
                    while(cursor.moveToNext()){
                        if(cursor.getString(cursor.getColumnIndex("Nlate"))==null){
                            db.execSQL("update studenttb set Nlate ="+1+" where _id = "+id+"");
                            //break;
                        }else{
                            int i=Integer.parseInt(cursor.getString(cursor.getColumnIndex("Nlate")).trim())+1;
                            db.execSQL("update studenttb set Nlate ="+i+" where _id = "+id+"");
                        }
                    }
                    cursor.close();
                }
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cb1.isChecked()==false&&cb3.isChecked()==false){
                    Cursor cursor=db.rawQuery("select Nabsent from studenttb where _id = ?",new String[]{id+""});
                    while(cursor.moveToNext()){
                        if(cursor.getString(cursor.getColumnIndex("Nabsent"))==null){
                            db.execSQL("update studenttb set Nabsent ="+1+" where _id = "+id+"");
                        }else {
                            int i=Integer.parseInt(cursor.getString(cursor.getColumnIndex("Nabsent")).trim())+1;
                            db.execSQL("update studenttb set Nabsent ="+i+" where _id = "+id+"");
                        }
                    }
                    cursor.close();
                }
            }
        });
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cb1.isChecked()==false&&cb2.isChecked()==false){
                    Cursor cursor=db.rawQuery("select Nleave from studenttb where _id = ?",new String[]{id+""});
                    while (cursor.moveToNext()){
                        if(cursor.getString(cursor.getColumnIndex("Nleave"))==null){
                            db.execSQL("update studenttb set Nleave ="+1+" where _id = "+id+"");
                        }else{
                            int i=Integer.parseInt(cursor.getString(cursor.getColumnIndex("Nleave")).trim())+1;
                            db.execSQL("update studenttb set Nleave ="+i+" where _id = "+id+"");
                        }
                    }
                    cursor.close();
                }
            }
        });
    }

    public Bitmap readImg(String filepath){
        File mfile=new File(filePath);
        Bitmap bm=null;
        if(mfile.exists()){
            bm= BitmapFactory.decodeFile(filePath);
        }
        return bm;
    }

    @Override
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
    }
}
