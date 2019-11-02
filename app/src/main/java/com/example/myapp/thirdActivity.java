package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class thirdActivity extends AppCompatActivity implements Runnable{

    private final String TAG = "Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;
    private String updateDate = "";

    EditText rmb;
    TextView show;
    Handler handler;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        rmb = findViewById(R.id.rmb);
        show = findViewById(R.id.show_switch);

        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);

        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);
        updateDate = sharedPreferences.getString("update_date","");

        Date today =Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Thread t =new Thread(this);
        t.start();
         handler = new Handler(){
            public void handlerMessage(Message msg){
                if(msg.what==5){
                    Bundle bd1 = (Bundle) msg.obj;
                    dollarRate = bd1.getFloat("dollar-rate");
                    euroRate = bd1.getFloat("euro-rate");
                    wonRate = bd1.getFloat("won-rate");
                    Toast.makeText(thirdActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
    }

    public void onClick(View btn) {
        Log.i(TAG,"onClick:");
        String str = rmb.getText().toString();
        Log.i(TAG,"onClick: get str="+ str);
        float r = 0;
        if (str.length() > 0) {
            r = Float.parseFloat(str);
        }else{
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
        }

        if(btn.getId()==R.id.dollar_rate){
            show.setText(String.format("%.2f",r*dollarRate));
        }else if(btn.getId()==R.id.euro){
            show.setText(String.format("%.2f",r*euroRate));
        }else {
            show.setText(String.format("%.2f",r*wonRate));
        }
    }
    public void openOne(View btn){
        openConfig();
    }
    public  void openConfig(){
        Intent config = new Intent(this,configActivity.class);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);
        startActivityForResult(config,1);
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1 && resultCode==2){
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);
            SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
    public void run() {
        for (int i = 1; i < 3; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Bundle bundle = new Bundle();
        Message msg = handler.obtainMessage();
        //msg.what = 5;
        //msg.obj = "Hello for run()";
        msg.obj = bundle;
        handler.sendMessage(msg);

        /*URL url = null;
        try {
             url = new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection htttp = (HttpURLConnection)url.openConnection();
            InputStream in = htttp.getInputStream();
            String html = inputStream2String(in);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }*/


        bundle =getFromBOC();
    }

    private Bundle getFromBOC() {
        Bundle bundle = new Bundle();
        Document doc = null;
        try{
            doc= Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            Elements tables = doc.getElementsByTag("table");
            /*for(Element table : tables){

            }
            /*Elements newsHeadlines = doc.select("#mp-itn b a");
            for (
                    Element headline : newsHeadlines){

            }*/

            Element tables6 = tables.get(5);
            Elements tds = tables6.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=8){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                String str1 = td1.text();
                String val = td2.text();
                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else if("韩元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return bundle;
    }

    private Bundle getFromUsdCny() {
        Bundle bundle = new Bundle();
        Document doc = null;
        try{
            doc= Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Elements tables = doc.getElementsByTag("table");
            /*for(Element table : tables){

            }
            /*Elements newsHeadlines = doc.select("#mp-itn b a");
            for (
                    Element headline : newsHeadlines){

            }*/

            Element tables6 = tables.get(5);
            Elements tds = tables6.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=8){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                String str1 = td1.text();
                String val = td2.text();
                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else if("韩元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return bundle;
    }

    private String inputStream2String(InputStream inputStream) throws IOException{
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"gb2312");
        for(; ;){
            int rsz = in.read(buffer,0,buffer.length);
            if(rsz<0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();

    }

}
