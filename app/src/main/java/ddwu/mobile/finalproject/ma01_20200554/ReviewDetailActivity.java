package ddwu.mobile.finalproject.ma01_20200554;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.database.sqlite.SQLiteDatabaseKt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ReviewDetailActivity extends AppCompatActivity {

    ReviewDBHelper helper;
    Cursor cursor;
    EditText date;
    EditText title;
    EditText review;
    ImageView photo;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);


        date = findViewById(R.id.detailDate);
        title = findViewById(R.id.detailTtile);
        review = findViewById(R.id.detailReview);
        photo = findViewById(R.id.detailPhoto);

        Intent intent = getIntent();
        int id = (int) intent.getLongExtra("id", 0);

        if(helper == null){
            helper = new ReviewDBHelper(this);
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + ReviewDBHelper.TABLE + " WHERE _id  = '" + id + "';", null);

        while(cursor.moveToNext()) {
            title.setText(cursor.getString(1));
            date.setText(cursor.getString(2));
            path = cursor.getString(3);
            review.setText(cursor.getString(4));
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        photo.setImageBitmap(bitmap);

        cursor.close();
        helper.close();
    }

    public void onClick(View v) {
        String snsContents = null;
        switch (v.getId()) {
            case R.id.reviewOk:
                finish();
                break;
            case R.id.sns:
                try {
                    snsContents = String.format("http://twitter.com/intent/tweet?text=%s",
                            URLEncoder.encode(review.getText().toString(), "utf-8"));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(snsContents));
                startActivity(intent);
                break;

        }
        }
}