package ddwu.mobile.finalproject.ma01_20200554;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ScrapDBHelper extends SQLiteOpenHelper {


    private final static String DB ="scrap_db";
    public final static String TABLE = "scrap_table";
    public final static String ID = "_id";
    public final static String NAME = "name";
    public final static String ADDRESS = "address";


    public ScrapDBHelper(@Nullable Context context) {
        super(context, DB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + TABLE + " (" + ID + " integer primary key autoincrement, "
                + NAME + " text, " + ADDRESS + " text);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int s) {
        sqLiteDatabase.execSQL("drop table " + TABLE);
        onCreate(sqLiteDatabase);
    }
}
