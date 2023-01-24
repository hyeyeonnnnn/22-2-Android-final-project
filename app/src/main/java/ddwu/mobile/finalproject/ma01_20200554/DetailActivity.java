package ddwu.mobile.finalproject.ma01_20200554;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    EditText etName;
    EditText etPhone;
    EditText etAddress;
    EditText etRating;
    Button call;
    String name, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etRating = findViewById(R.id.etRating);

        Intent intent = getIntent();
        name =intent.getStringExtra("name");
        String phone =intent.getStringExtra("phone");
        address =intent.getStringExtra("address");
        String rating = Double.toString(intent.getDoubleExtra("rating",0));
        etName.setText(name);
        etPhone.setText(phone);
        etAddress.setText(address);
        etRating.setText(rating);


        call = (Button)findViewById(R.id.btnCall);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+etPhone.getText().toString()));
                    try {
                        startActivity(callIntent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScrap:
                ScrapDBHelper helper = new ScrapDBHelper(DetailActivity.this);
                SQLiteDatabase db =  helper.getWritableDatabase();

                ContentValues rows = new ContentValues();
                rows.put(ScrapDBHelper.NAME, name);
                rows.put(ScrapDBHelper.ADDRESS, address);
                db.insert(ScrapDBHelper.TABLE, null, rows);
                Toast.makeText(DetailActivity.this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DetailActivity.this, ScrapActivity.class);
                startActivity(i);

                break;
            case R.id.btnReview:
                Intent intent = new Intent(DetailActivity.this, ReviewAddActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("address",address);
                startActivity(intent);
                break;
            case R.id.btnAlarm:
                Intent intent2 = new Intent(this, AlarmActivity.class);
                intent2.putExtra("name",name);
                startActivity(intent2);
                break;

            case R.id.btnClose:
                finish();
                break;
        }
    }
}
