package carservice.p5.mytranslater.services.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DataBase {

    private BaseTranslate base;

    public DataBase(Context context) {
        base = new BaseTranslate(context);
    }

    public BaseTranslate getBase() {
        return base;
    }

    public long insert(String name_tab, ContentValues Valus) {
        long idInsert = 0;
        try {
            SQLiteDatabase db = SQLdb();
            idInsert = db.insert(name_tab, null, Valus);
            db.close();
            base.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return idInsert;
    }

    public long update(String name_tab, ContentValues Valus, String WHERE) {

        SQLiteDatabase db = SQLdb();
        long idUpdate = db.update(name_tab, Valus, WHERE, null);
        db.close();
        base.close();
        return idUpdate;
    }

    public long update(String name_tab, ContentValues Valus, String whereClause, String[] whereArgs) {

        SQLiteDatabase db = SQLdb();
        long idUpdate = db.update(name_tab, Valus, whereClause, whereArgs);
        db.close();
        base.close();
        return idUpdate;
    }

    public long delete(String name_tab, String WHERE, String[] whereArgs) {

        SQLiteDatabase db = SQLdb();
        long idDelete = db.delete(name_tab, WHERE, whereArgs);
        db.close();
        base.close();
        return idDelete;
    }

    public SQLiteDatabase SQLdb() {
        SQLiteDatabase db;
        try {
            db = base.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = base.getReadableDatabase();
        }
        return db;
    }
}
