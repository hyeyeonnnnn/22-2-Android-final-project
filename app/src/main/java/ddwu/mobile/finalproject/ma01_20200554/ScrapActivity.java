package ddwu.mobile.finalproject.ma01_20200554;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ScrapActivity extends AppCompatActivity {

    ScrapDBHelper helper;
    Cursor cursor;
    ListView list;
    SimpleCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap);
        list = (ListView) findViewById(R.id.scrapList);
        helper = new ScrapDBHelper(this);

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, new String[] {"name", "address"},
                new int[] {android.R.id.text1, android.R.id.text2}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScrapActivity.this);
                builder.setTitle("즐겨찾기 삭제")
                        .setMessage("즐겨찾기를 정말 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SQLiteDatabase db = helper.getWritableDatabase();
                                String whereClause = helper.ID +"=?";
                                String[] whereArgs = new String[] {String.valueOf(l)};
                                db.delete(ScrapDBHelper.TABLE, whereClause, whereArgs);

                                adapter.notifyDataSetChanged();
                                helper.close();
                                Toast.makeText(ScrapActivity.this, "삭제되었습니다!", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent scrap = new Intent(ScrapActivity.this, ScrapActivity.class);
                                startActivity(scrap);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();

                return true;
            }
        });

    }
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + ScrapDBHelper.TABLE, null);
        adapter.changeCursor(cursor);
        helper.close();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null)
            cursor.close();
    }
}