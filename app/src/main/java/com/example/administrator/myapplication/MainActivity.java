package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // 필요 변수 선언
    //스레드
    EditText editText1;
    EditText editText2;
    TextView textViewResult;
    int text1, text2;

    int mainValue = 0;
    int backValue = 0;
    TextView mainText;
    TextView backText;
    TabHost tabHost;

    //DB
    myDB my;
    EditText edittext11, edittext22, edittext3;
    TextView hakbun, name, address;
    Button btn1, btn2, btn3;
    SQLiteDatabase sql;

//    // map
//    EditText getAdd;
//    Button   getLocation;
//    MapView  map;
//    MapController controller;
//
//    String strAddress;
//    List<Address> listAddress;
//    Geocoder geocoder;
//    Address AddrAddress;
//    GeoPoint location;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Initialize(); //xml파일에 정의된 입력창, 버튼, 지도등 변수들 연결
//        getLocation.setOnClickListener(this);


        // Tab부분
        setTitle("216230086 김주희");
        tabHost = (TabHost) findViewById(R.id.tabhost1);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("A").setContent(R.id.tab1).setIndicator("Thread");
        tabHost.addTab(tab1);//1번탭 생성

        TabHost.TabSpec tab2 = tabHost.newTabSpec("B").setContent(R.id.tab2).setIndicator("DB");
        tabHost.addTab(tab2);//2번탭 생성

        TabHost.TabSpec tab3 = tabHost.newTabSpec("C").setContent(R.id.tab3).setIndicator("url");
        tabHost.addTab(tab3);//3번탭 생성

//        TabHost.TabSpec tab4 = tabHost.newTabSpec("D").setContent(R.id.tab4).setIndicator("map");
//        tabHost.addTab(tab4);//3번탭 생성




//////////////////////////////////////////DB부분
        edittext11 = (EditText) findViewById(R.id.editText11);
        edittext22 = (EditText) findViewById(R.id.editText22);
        edittext3 = (EditText) findViewById(R.id.editText3);

        hakbun = (TextView) findViewById(R.id.hakbun);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);

        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);

        my = new myDB(this);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sql = my.getWritableDatabase();
                my.onUpgrade(sql, 1, 2);
                sql.close();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sql = my.getReadableDatabase();
                Cursor cursor;
                cursor = sql.rawQuery("SELECT * FROM MEMBER;", null);
                String hakbun2 = "학번" + "\r\n";
                String name2 = "이름" + "\r\n";
                String address2 = "주소" + "\r\n";

                while (cursor.moveToNext()) {
                    hakbun2 += cursor.getString(0) + "\r\n";
                    name2 += cursor.getString(1) + "\r\n";
                    address2 += cursor.getString(2) + "\r\n";
                }
                hakbun.setText(hakbun2);
                name.setText(name2);
                address.setText(address2);
                cursor.close();
                sql.close();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sql = my.getWritableDatabase();
                sql.execSQL("INSERT INTO member VALUES(" + edittext11.getText().toString() + ",'"
                        + edittext22.getText().toString() + "','"
                        + edittext3.getText().toString() + "');");
                sql.close();
                Toast.makeText(getApplicationContext(), "정보가 저장되었습니다.", Toast.LENGTH_LONG).show();
            }
        });


        /////////////////////////////////////////// tab1 스레드
        mainText = (TextView) findViewById(R.id.mainvalue);
        backText = (TextView) findViewById(R.id.backvalue);

//        // 스레즈 생성하고 시작
//        BackThread thread = new BackThread(mHandler,text1,text2,"+");
//        thread.setDaemon(true);
//        thread.start();


        // UI 위젯 객체 변수 할당
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        textViewResult = (TextView) findViewById(R.id.textViewResult);

        // + 버튼
        Button buttonPlus = (Button) findViewById(R.id.buttonPlus);
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1 = Integer.parseInt(editText1.getText().toString());
                text2 = Integer.parseInt(editText2.getText().toString());
                // 스레즈 생성하고 시작
                BackThread bThread = new BackThread(mHandler, text1, text2, "+");
                bThread.setDaemon(true);
                bThread.start();
            }
        });

        // - 버튼
        Button buttonMinus = (Button) findViewById(R.id.buttonMinus);
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1 = Integer.parseInt(editText1.getText().toString());
                text2 = Integer.parseInt(editText2.getText().toString());
                BackThread bThread = new BackThread(mHandler, text1, text2, "-");
                bThread.setDaemon(true);
                bThread.start();
            }
        });

        // * 버튼
        Button buttonMul = (Button) findViewById(R.id.buttonMul);
        buttonMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1 = Integer.parseInt(editText1.getText().toString());
                text2 = Integer.parseInt(editText2.getText().toString());
                BackThread bThread = new BackThread(mHandler, text1, text2, "*");
                bThread.setDaemon(true);
                bThread.start();
            }
        });

        // / 버튼
        Button buttonDiv = (Button) findViewById(R.id.buttonDiv);
        buttonDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1 = Integer.parseInt(editText1.getText().toString());
                text2 = Integer.parseInt(editText2.getText().toString());
                BackThread bThread = new BackThread(mHandler, text1, text2, "/");
                bThread.setDaemon(true);
                bThread.start();
            }
        });
    }


    /////////////////////////////////////////////////////url
    public void goURL(View view) {
        TextView tvURL = (TextView) findViewById(R.id.txtURL);
        String url = tvURL.getText().toString();
        Log.i("URL", "Opening URL with WebView :" + url);

        final long startTime = System.currentTimeMillis();
        WebView webView = (WebView) findViewById(R.id.webView);

        // 하드웨어 가속
        // 캐쉬 끄기
        //webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                long elapsedTime = System.currentTimeMillis() - startTime;
                TextView tvSec = (TextView) findViewById(R.id.tvSec);
                tvSec.setText(String.valueOf(elapsedTime));
            }
        });
        webView.loadUrl(url);

    }


    ///////////////////////////////////////////////////////////////스레드
    // 핸들러
    Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (m.what == 0) {
                textViewResult.setText(Integer.toString(m.arg1));
            }
        }
    };

    // '메인스레드' 에서 Handler 객체를 생성한다.
    // Handler 객체를 생성한 스레드 만이 다른 스레드가 전송하는 Message나 Runnable 객체를
    // 수신할수 있다.
    // 아래 생성된 Handler 객체는 handlerMessage() 를 오버라이딩 하여
    // Message 를 수진합니다.
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {   // Message id 가 0 이면
                backText.setText("BackValue:" + backValue); // 메인스레드의 UI 내용 변경
            }
        }
    };


    // 버튼을 누르면 mainValue 증가
    public void mOnClick(View v) {
        mainValue++;
        mainText.setText("MainValue:" + mainValue);
    }

    class BackThread extends Thread {
        // 필요 변수 선언
        int text1;
        int text2;
        int result;
        Handler mHandler;
        String flag;
        // 생성자

        public BackThread(Handler handler, int text1, int text2, String flag) {
            this.text1 = text1;
            this.text2 = text2;
            this.mHandler = handler;
            this.flag = flag;
        }

        // 연산자별 계산
        @Override
        public void run() {
            if (flag.equals("+")) {
                result = text1 + text2;
                Message m = new Message();
                m.what = 0;
                m.arg1 = result;
                mHandler.sendMessage(m);
            } else if (flag.equals("-")) {
                result = text1 - text2;
                Message m = new Message();
                m.what = 0;
                m.arg1 = result;
                mHandler.sendMessage(m);
            } else if (flag.equals("*")) {
                result = text1 * text2;
                Message m = new Message();
                m.what = 0;
                m.arg1 = result;
                mHandler.sendMessage(m);
            } else if (flag.equals("/")) {
                result = text1 / text2;
                Message m = new Message();
                m.what = 0;
                m.arg1 = result;
                mHandler.sendMessage(m);
            }

            while (true) {
                backValue++;
                // 메인에서 생성된 Handler 객체의 sendEmpryMessage 를 통해 Message 전달
                handler.sendEmptyMessage(0);

                try {
                    Thread.sleep(1000);
                    System.out.println("스레드 동작");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("스레드 종료");
                    // 이 부분에서 Thread가 종료 된 후에 실행된 명령어를 적어줄 수 있다.
                }// interrupt()호출시점에 sleep()메소드가 걸리면 InterruptedException 발생
            } // end while
        } // end run()
//    } // end class BackThread
    } // end class(oncreate)

    ////////////////////////////////////////////////////////////////////////DB
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public class myDB extends SQLiteOpenHelper {
        public myDB(Context context) {
            super(context, "human", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table member " +
                    "(hakbun Integer primary key, name char(10), address char(50))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS member");
            onCreate(db);
        }
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


//    //map
//    private void Initialize() {
//        // TODO Auto-generated method stub
//        getAdd = (EditText) findViewById(R.id.etGetAddress);
//        getLocation = (Button) findViewById(R.id.bGetLocation);
//        map = (MapView) findViewById(R.id.mvGetLocation);
//    }
//
//    @Override
//    protected boolean isRouteDisplayed() {
//        // TODO Auto-generated method stub
//        return false;
//    }

//    public void onClick(View v) {
//        // TODO Auto-generated method stub
//        switch (v.getId()) { // 본 예제의 다음 활용을 위해 switch문으로 작성됨
//            case R.id.bGetLocation:
//                strAddress = getAdd.getText().toString();
//                // EditText를 통해 입력받은 주소를 getText()로 가져와 toString()으로 String전환 후 String 변수인 strAddress에 저장
//                geocoder = new Geocoder(this);
//                // Geocoder는 주소를 통해 위도와 경도의 값을 연산한다
//                try {
//                    listAddress = geocoder.getFromLocationName(strAddress, 5);
//                    // Geocoder의 getFromLocationName 메쏘드를 통해 주소를 List변수에 저장한다.
//                    // strAddress는 String 주소(텍스트)이고, listAddress는 String을 통해 구글 맵이
//                    //이해할 수 있는 주소로 변환한 값, 함수내의 숫자는 전달하는 주소값의 크기값 (1~5)
//                    if (listAddress.size() > 0) {// 주소값이 존재 하면
//                        AddrAddress = listAddress.get(0); // Address형태로 List<Address>를 전달
//                        location = new GeoPoint(
//                                (int) (AddrAddress.getLatitude() * 1E6),
//                                (int) (AddrAddress.getLongitude() * 1E6));
//                        // GeoPoint 메쏘드를 통해 Address값에서 위도와 경도를 받아 GeoPoint에 전달
//                        // GeoPoint는 맵에위도와 경도값을 넘겨준다.
//                        //구글 맵에서 사용하는 위도와 경도의 값으로 맞춰주기 위해 좌표값을 얻은 후 *1E6 해줘야 함
//                        controller = map.getController(); // MapController 선언
//                        controller.animateTo(location); // MapController에 위도, 경도값 을 전달해 지도를 해당 위치로 이동
//                        controller.setZoom(14);
//                    } // 지도에 해당위치를 뿌릴때 zoom 정도. 숫자가 클수록 확되된 화면
//                } catch (IOException e) { // EditText를 통해 값을 입력 받을 때 생길 수 있는 오류를 catch한다
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }
}
