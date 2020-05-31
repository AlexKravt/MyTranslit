package carservice.p5.mytranslater.services.sqlite;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.io.File;

public class BaseTranslate extends SQLiteOpenHelper {

    public static final String TABLE_TRANSLATE = "my_translate";
    public static final String TABLE_LIST_LANGUAGE = "list_language";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT_IN = "translateIn";
    public static final String COLUMN_TEXT_TO = "translateTo";
    public static final String COLUMN_CODE_IN = "languageCodeIn";
    public static final String COLUMN_CODE_TO = "languageCodeTo";

    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_NAME = "name";


    private final static int DATABASE_VERSION = 1;
    private final static String DB_NAME = "BaseTranslate";

    public BaseTranslate(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }
    public BaseTranslate(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BaseTranslate(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    public boolean databaseExist(Context context)
    {
        String path_db =  context.getDatabasePath(DB_NAME).getPath();
        File dbFile = new File(path_db);
        return dbFile.exists();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

   /*
     NULL	пустое значение
     INTEGER	целочисленное значение
     REAL	значение с плавающей точкой
     TEXT	строки или символы в кодировке UTF-8, UTF-16BE или UTF-16LE
     BLOB	бинарные данные*/

        String tTranslate = TABLE_TRANSLATE+" ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TEXT_IN+" TEXT UNIQUE," +
                COLUMN_TEXT_TO+" TEXT UNIQUE," +
                COLUMN_CODE_IN+" TEXT," +
                COLUMN_CODE_TO+" TEXT" +
                ");";
        execSQL(db,tTranslate);

        String tListLanguage = TABLE_LIST_LANGUAGE+" ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CODE+" TEXT UNIQUE," +
                COLUMN_NAME+" TEXT UNIQUE" +
                ");";
        execSQL(db,tListLanguage);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       //todo добавить если нужно новую таблицу или столбцы

        /*
       if(newVersion == 1)
        {
            String tItemsFishData = " ALTER TABLE "+ TABLE_DATA_DEFECT+" ADD  ax_sort INTEGER";
            db.execSQL(tItemsFishData);
        }*/
    }


    /**
     * Метод создания таблиц в базе данных
     * @param db SQLiteDatabase
     * @param columns String
     */
    private void execSQL(SQLiteDatabase db, String columns)
    {

        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS "+columns);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        /*

        try{
            db.execSQL("CREATE TABLE "+columns);
        }
        catch(Exception e) {
            db.execSQL("CREATE TABLE IF NOT EXISTS "+columns);
        }
*/
    }
}
