package ddwu.mobile.finalproject.ma01_20200554;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ReviewListActivity extends AppCompatActivity {


    Cursor cursor;
    ReviewDBHelper helper;
    ListView review;
    ReviewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        helper = new ReviewDBHelper(this);
        review = (ListView)findViewById(R.id.list);
        adapter = new ReviewAdapter(this, R.layout.reviewadapter, null);
        review.setAdapter(adapter);

        review.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ReviewListActivity.this, ReviewDetailActivity.class);
                intent.putExtra("id", l);
                startActivity(intent);
            }
        });


        review.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int item, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReviewListActivity.this);
                builder.setTitle("리뷰 삭제")
                        .setMessage("리뷰를 정말 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SQLiteDatabase db = helper.getWritableDatabase();
                                String whereClause = helper.ID +"=?";
                                String[] whereArgs = new String[] {String.valueOf(l)};
                                db.delete(ReviewDBHelper.TABLE, whereClause, whereArgs);

                                adapter.notifyDataSetChanged();
                                helper.close();
                                Toast.makeText(ReviewListActivity.this, "삭제되었습니다!", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent lists = new Intent(ReviewListActivity.this, ReviewListActivity.class);
                                startActivity(lists);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();

                return true;
            }
        });

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_review:
                Intent intent = new Intent(this, ReviewAddActivity.class);
                startActivity(intent);

                break;
        }
    }
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + ReviewDBHelper.TABLE, null);
        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();
        helper.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null)
            cursor.close();
    }

}