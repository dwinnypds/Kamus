package dwinny.kamus.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static dwinny.kamus.DB.DB_Contract.KolomKata.ARTI;
import static dwinny.kamus.DB.DB_Contract.KolomKata.KATA;
import static dwinny.kamus.DB.DB_Contract.TABLE_EN;
import static dwinny.kamus.DB.DB_Contract.TABLE_ID;

public class DB_Helper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "dbkamus";

    private static final int DATABASE_VERSION = 1;

    public static String CREATE_TABLE_ID = "create table "+TABLE_ID+
            " ("+_ID+" integer primary key autoincrement, " +
            KATA+" text not null, " +
            ARTI+" text not null);";
    public static String CREATE_TABLE_EN = "create table "+TABLE_EN+
            " ("+_ID+" integer primary key autoincrement, " +
            KATA+" text not null, " +
            ARTI+" text not null);";


    public DB_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ID);
        db.execSQL(CREATE_TABLE_EN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ID);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_EN);
        onCreate(db);
    }}
