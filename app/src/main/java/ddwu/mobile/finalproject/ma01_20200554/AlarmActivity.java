package ddwu.mobile.finalproject.ma01_20200554;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.Calendar;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private AlarmManager alarmManager;
    private int hour, minute;
    EditText alarmName, alarmText;
    CheckBox sun, mon, tue, wed, thu, fri, sat;

    static String TAG="AlarmActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);



        timePicker=findViewById(R.id.tp_timepicker);
        alarmManager= (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmName =  (EditText)findViewById(R.id.alarmName);
        alarmText =  (EditText)findViewById(R.id.alarmText);
        sun=findViewById(R.id.cb_sun);
        mon=findViewById(R.id.cb_mon);
        tue=findViewById(R.id.cb_thu);
        wed=findViewById(R.id.cb_wed);
        thu=findViewById(R.id.cb_thu);
        fri=findViewById(R.id.cb_fri);
        sat=findViewById(R.id.cb_sat);
        createNotificationChannel();
    }

    public void regist(View view) {

        boolean[] week = { false, sun.isChecked(), mon.isChecked(), tue.isChecked(), wed.isChecked(),
                thu.isChecked(), fri.isChecked(), sat.isChecked() }; // cbSun을 1번부터 사용하기 위해 배열 0번은 false로 고정

        if(!sun.isChecked() &&  !mon.isChecked() &&  !tue.isChecked() && !wed.isChecked() &&  !thu.isChecked() && !fri.isChecked() && !sat.isChecked()){
            Toast.makeText(this, "요일을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour=timePicker.getHour();
            minute=timePicker.getMinute();
        }else{
            Toast.makeText(this, "버전을 확인해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = alarmName.getText().toString();
        String text = alarmText.getText().toString();

        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        intent.putExtra("weekday", week);
        intent.putExtra("alarmName", name);
        intent.putExtra("alarmText", text);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0,intent, 0); //PendingIntent.FLAG_UPDATE_CURRENT

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date today = new Date();
        long intervalDay = 24 * 60 * 60 * 1000;// 24시간

        long selectTime=calendar.getTimeInMillis();
        long currenTime=System.currentTimeMillis();

        //만약 설정한 시간이, 현재 시간보다 작다면 알람이 부정확하게 울리기 때문에 다음날 울리게 설정
        if(currenTime>selectTime){
            selectTime += intervalDay;
        }

        Log.e(TAG,"등록 버튼을 누른 시간 : "+today+"  설정한 시간 : "+calendar.getTime());
        Log.d(TAG,"calendar.getTimeInMillis()  : "+calendar.getTimeInMillis());

        // 지정한 시간에 매일 알림
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, selectTime,  intervalDay, pIntent);
        Toast.makeText(this, "알람이 등록 됐습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }// regist()..

    public void unregist(View view) {
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pIntent);
        Toast.makeText(this, "알람이 취소 됐습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }// unregist()..

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test";       // strings.xml 에 채널명 기록
            String description = "알람";       // strings.xml에 채널 설명 기록
            int importance = NotificationManager.IMPORTANCE_DEFAULT;    // 알림의 우선순위 지정
            NotificationChannel channel = new NotificationChannel("test", name, importance);    // CHANNEL_ID 지정
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);  // 채널 생성
            notificationManager.createNotificationChannel(channel);
        }
    }
}