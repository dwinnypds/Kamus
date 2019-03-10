package dwinny.kamus.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static dwinny.kamus.DB.DB_Contract.KolomKata.ARTI;
import static dwinny.kamus.DB.DB_Contract.KolomKata.KATA;
import static dwinny.kamus.DB.DB_Contract.TABLE_EN;
import static dwinny.kamus.DB.DB_Contract.TABLE_ID;

public class Kamus_Helper {
    private Context context;
    private DB_Helper db_helper;
    private SQLiteDatabase database;

    public Kamus_Helper(Context context) {
        this.context = context;
    }

    public Kamus_Helper open() throws SQLException {
        db_helper = new DB_Helper(context);
        database = db_helper.getWritableDatabase();
        return this;
    }

    public void close() {
        db_helper.close();
    }

    public Cursor getKata(String query, boolean en) {
        String DATABASE_TABLE = en ? TABLE_EN : TABLE_ID;
        Log.e("getKata", query);
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KATA + " LIKE '%" + query + "%'", null);
    }


    public Cursor queryData(boolean en) {
        String DATABASE_TABLE = en ? TABLE_EN : TABLE_ID;
        Log.e("TABLE",DATABASE_TABLE);
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " ORDER BY " + _ID + " ASC", null);
    }


    public ArrayList<Kata> getDataByKata(String kata, boolean en) {
        Kata kataModel;
        ArrayList<Kata> arrayList = new ArrayList<>();
        Cursor cursor = getKata(kata, en);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            Log.e("cursor", String.valueOf(cursor.getCount()));
            do {
                kataModel = new Kata();
                kataModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                kataModel.setKata(cursor.getString(cursor.getColumnIndexOrThrow(KATA)));
                kataModel.setArti(cursor.getString(cursor.getColumnIndexOrThrow(ARTI)));
                arrayList.add(kataModel);

                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;

    }

    public String getData(String search, boolean en) {
        String res = "";
        Cursor cursor = getKata(search, en);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            res = cursor.getString(2);
            for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                res = cursor.getString(2);
            }

        }
        cursor.close();
        return res;

    }

    public ArrayList<Kata> getAllData(boolean en) {
        Kata kataModel;
        ArrayList<Kata> arrayList = new ArrayList<>();
        Cursor cursor = queryData(en);
        Log.e("Data", ""+cursor.getCount());
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                kataModel = new Kata();
                kataModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                kataModel.setKata(cursor.getString(cursor.getColumnIndexOrThrow(KATA)));
                kataModel.setArti(cursor.getString(cursor.getColumnIndexOrThrow(ARTI)));
                arrayList.add(kataModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());

        }
        cursor.close();
        return arrayList;
    }

    public void insertTransaction(ArrayList<Kata> kataModel, boolean en) {
        String TABLE = en ? TABLE_EN : TABLE_ID;
        String sql = "INSERT INTO " +TABLE + " (" +
                KATA + ", " +
                ARTI + ") VALUES (?, ?)";
        database.beginTransaction();
        SQLiteStatement statement = database.compileStatement(sql);
        for (int i = 0; i < kataModel.size(); i++) {
            statement.bindString(1, kataModel.get(i).getKata());
            statement.bindString(2, kataModel.get(i).getArti());
            statement.execute();
            statement.clearBindings();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public void update(Kata kataModel, boolean en) {
        String TABLE = en ? TABLE_EN : TABLE_ID;
        ContentValues values = new ContentValues();
        values.put(KATA, kataModel.getKata());
        values.put(ARTI, kataModel.getArti());
        database.update(TABLE, values, _ID + " = '" + kataModel.getId() + "'", null);


    }

    public void delete(int id, boolean en){
        String TABLE = en ? TABLE_EN: TABLE_ID;
        database.delete(TABLE, _ID + " = '" + id + "'",null);
    }
}
