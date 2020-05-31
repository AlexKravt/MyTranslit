package carservice.p5.mytranslater.services.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import carservice.p5.mytranslater.mvp.Models.TranslateModel;


public class TableBase extends DataBase {

    private SQLiteDatabase db;
    private Context context;

    public TableBase(Context c) {
        super(c);
        this.context = c;
        db = SQLdb();
    }

    public void createTables() {
        this.getBase().onCreate(db); //Создаём таблицы в базе данных
    }

    /**
     * Заносим данные в таблицу
     *
     * @param translateModel TranslateModel
     */
    public void setData(TranslateModel translateModel) {
        ContentValues data = new ContentValues();
        data.put("translateIn", translateModel.getTextIn());
        data.put("translateTo", translateModel.getTextTo());
        data.put("languageCodeIn", translateModel.getLangCodeIn());
        data.put("languageCodeTo", translateModel.getLangCodeTo());

        String WHERE = " translateIn='" + translateModel.getTextIn() +
                "' AND translateTo='" + translateModel.getTextTo() +
                "' AND languageCodeIn='" + translateModel.getLangCodeIn() +
                "' AND languageCodeTo='" + translateModel.getLangCodeTo() + "'";

        long id = update(BaseTranslate.TABLE_TRANSLATE, data, WHERE);

        if (id == 0) {
            id = insert(BaseTranslate.TABLE_TRANSLATE, data);
        }

        Log.d("IndexInsert", BaseTranslate.TABLE_TRANSLATE + " - (" + id + ")");

    }

    /**
     * Заносим список языков в таблицу
     *
     * @param code String
     * @param name String
     */
    public void setLanguages(String code, String name) {
        ContentValues data = new ContentValues();
        data.put("code", code);
        data.put("name", name);
        String WHERE = " code='" + code + "' AND name='" + name + "'";

        long id = update(BaseTranslate.TABLE_LIST_LANGUAGE, data, WHERE);

        if (id == 0) {
            id = insert(BaseTranslate.TABLE_LIST_LANGUAGE, data);
        }

        Log.d("IndexInsert", BaseTranslate.TABLE_LIST_LANGUAGE + " - (" + id + ")");

    }


    public Cursor getLanguages() {

        String[] param = {"code", "name"};
        try {
            db = SQLdb();
            Cursor c = db.query(BaseTranslate.TABLE_LIST_LANGUAGE, param, null, null, null, null, null);
            if (c != null && c.moveToFirst()) {
                return c;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cursor getHistoryTranslate(String text) {

        String WHERE = " translateIn LIKE '%" + text + "%' OR translateTo LIKE '%" + text + "%'";

        if (text == null || text.isEmpty())
            WHERE = null;
        try {
            db = SQLdb();
            Cursor c = db.query(BaseTranslate.TABLE_TRANSLATE, null, WHERE, null, null, null, null);
            if (c != null && c.moveToFirst()) {
                return c;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cursor getHistoryById(int id) {

        String WHERE = BaseTranslate.COLUMN_ID + "=" + id;

        try {
            db = SQLdb();
            Cursor c = db.query(BaseTranslate.TABLE_TRANSLATE, null, WHERE, null, null, null, null);
            if (c != null && c.moveToFirst()) {
                return c;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void dropHistory() {
        try {
            db = SQLdb();
            String WHERE = " name='" + BaseTranslate.TABLE_TRANSLATE + "'";
            db.delete(BaseTranslate.TABLE_TRANSLATE, null, null);
            db.delete("SQLITE_SEQUENCE", WHERE, null);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


}
