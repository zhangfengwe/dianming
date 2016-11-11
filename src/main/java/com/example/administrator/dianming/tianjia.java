package com.example.administrator.dianming;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.transition.SidePropagation;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/11/2 0002.
 */

public class tianjia extends AppCompatActivity implements View.OnClickListener{

    private Button bt5;
    private Button bt4;
    private Button bt6;

    private EditText sidT2;
    private EditText nameT2;
    private EditText classT2;

    private CheckBox cb4;
    private CheckBox cb5;
    private int cflag4=0,cflag5=0;
    private String sex;

    private SQLiteDatabase db;
    public  String filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tianjia);

        bt5= (Button) findViewById(R.id.button5);//确定
        bt4= (Button) findViewById(R.id.button4);//选择图片
        bt6= (Button) findViewById(R.id.button6);//手机拍摄

        cb4= (CheckBox) findViewById(R.id.checkBox4);//男
        cb5= (CheckBox) findViewById(R.id.checkBox5);//女

        sidT2= (EditText) findViewById(R.id.editText6);//学号
        nameT2= (EditText) findViewById(R.id.editText7);//姓名
        classT2= (EditText) findViewById(R.id.editText9);//班级

        db=openOrCreateDatabase("student.db",MODE_PRIVATE,null);

        filePath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/a.png";
        bt4.setOnClickListener(this);
        bt6.setOnClickListener(this);

        /*
         *确定按钮事件
         */
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=0;
                ContentValues values=new ContentValues();
                while(flag==0){
                    //if(sidT2.getText()!=null&&nameT2.getText()!=null&&classT2.getText()!=null&&(cb4.isChecked()==false&&cb5.isChecked()==false)){
                        values.put("Sid",sidT2.getText().toString());
                        values.put("Sname",nameT2.getText().toString());
                        values.put("class",classT2.getText().toString());
                        values.put("Ssex",sex);
                        values.put("picture",filePath);
                        try{
                            db.insert("studenttb",null,values);
                            flag=1;
                        }catch (Exception e){
                            Toast.makeText(tianjia.this,"学号输入有误，请重新输入",Toast.LENGTH_LONG).show();
                            values.clear();
                            sidT2.setText("");
                            nameT2.setText("");
                            classT2.setText("");
                            cb4.setChecked(false);
                            cb5.setChecked(false);
                        }
                    //}else{
                     //   Toast.makeText(tianjia.this,"不能为空",Toast.LENGTH_LONG).show();
                    //}
                }
                values.clear();
                sidT2.setText("");
                nameT2.setText("");
                classT2.setText("");
                cb4.setChecked(false);
                cb5.setChecked(false);
            }
        });

        /*
         *checkbox事件
         */

        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cb5.isChecked()==false){
                    cflag4=1;
                    sex="男";
                }
            }
        });

        cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb4.isChecked()==false){
                    sex="女";
                }
            }
        });
        //db.close();
    }
    /*
     *选择图片事件
     */
    @Override
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.button4:
                intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,0x1);
                break;
            case R.id.button6:
                intent=new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0x3);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==0x1){
            if(data!=null){
                Uri uri=data.getData();
                getImg(uri);
                Bundle bundle=data.getExtras();
                Bitmap bitmap=bundle.getParcelable("data");
                saveImg(bitmap);
            }else {
                return;
            }
        }
        if(requestCode==0x2){
            if(data!=null){
                Bundle bundle=data.getExtras();
                Bitmap bitmap=bundle.getParcelable("data");
                saveImg(bitmap);
                //img.setImageBitmap(bitmap);
            }else{
                return;
            }
        }
        if(requestCode==0x3){
            if(data!=null){
                Bundle bundle=data.getExtras();
                Bitmap bitmap=bundle.getParcelable("data");
                saveImg(bitmap);
                //img.setImageBitmap(bitmap);
            }else {
                return;
            }
        }
    }
    private Bitmap readImg(){
        File mfile=new File(filePath);
        Bitmap bm=null;
        if(mfile.exists()){
            bm= BitmapFactory.decodeFile(filePath);
        }
        return bm;
    }

    private void saveImg(Bitmap mBitmap){
        File f=new File(filePath);
        try{
            if(!f.exists()){
                f.createNewFile();
            }
            filePath=f.getAbsolutePath().toString();
            FileOutputStream out=new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG,100,out);
            out.flush();
            out.close();
            Toast.makeText(this,f.getAbsolutePath().toString(),Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getImg(Uri uri){
        try{
            InputStream inputStream=getContentResolver().openInputStream(uri);
            cutImg(uri);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cutImg(Uri uri){
        if(uri!=null){
            Intent intent=new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri,"image/*");
            intent.putExtra("crop","true");
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
            intent.putExtra("outputX",300);
            intent.putExtra("outputY",300);
            intent.putExtra("return-data",true);
            intent.putExtra("output",uri);
            intent.putExtra("scale",true);
            startActivityForResult(intent,0x2);
        }else{
            return;
        }
    }
}
