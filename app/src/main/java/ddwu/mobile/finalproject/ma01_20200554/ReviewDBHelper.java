package ddwu.mobile.finalproject.ma01_20200554;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class ReviewDBHelper extends SQLiteOpenHelper {

    private final static String TAG = "ReviewDBHelper";
    private final static String DB ="review_db";
    public final static String TABLE = "review_table";
    public final static String ID = "_id";
    public final static String TITLE = "title";
    public final static String DATE = "date";
    public final static String PHOTO = "photo";
    public final static String REVIEW = "review";

    public ReviewDBHelper(@Nullable Context context) {
        super(context, DB, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "create table " + TABLE + " (" + ID + " integer primary key autoincrement, "
                + TITLE + " text, " + DATE + " text, " + PHOTO + " text, " + REVIEW + " text);";

        Log.d(TAG, sql);
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table " + TABLE);
        onCreate(sqLiteDatabase);
    }
}
