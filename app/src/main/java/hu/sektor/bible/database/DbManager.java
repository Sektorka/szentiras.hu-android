package hu.sektor.bible.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "bible.db";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase db;
    private boolean opened;

    private List<Translation> translations;

    /*public static class DBMessagesReaded {
        public static final String TABLE_NAME = "messages_readed";

        public static final String COL_ID = "id";
        public static final String COL_READED = "readed";

        private static final String DB_CREATE_TABLE_MESSAGES_READED = "CREATE TABLE IF NOT EXISTS \"" + TABLE_NAME + "\" (\n" +
                "\"" + COL_ID + "\"  INTEGER PRIMARY KEY NOT NULL,\n" +
                "\"" + COL_READED + "\"  INTEGER DEFAULT 0);";

        private static final String DB_DROP_TABLE_MESSAGES_READED = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static class DBMessagesFavourited {
        public static final String TABLE_NAME = "messages_favourited";

        public static final String COL_ID = "id";
        public static final String COL_FAVOURITED = "favourited";

        private static final String DB_CREATE_TABLE_MESSAGES_FAVOURITED = "CREATE TABLE IF NOT EXISTS \"" + TABLE_NAME + "\" (\n" +
                "\"" + COL_ID + "\"  INTEGER PRIMARY KEY NOT NULL,\n" +
                "\"" + COL_FAVOURITED + "\"  INTEGER DEFAULT 0);";

        private static final String DB_DROP_TABLE_MESSAGES_FAVOURITE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }*/

    public DbManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void open(){
        if(opened){
            return;
        }

        db = getWritableDatabase();
        opened = true;
    }

    @Override
    public synchronized void close() {
        super.close();
        db.close();
        opened = false;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Translation.CREATE_TABLE_QUERY);

        /*db.execSQL(DBMessages.DB_CREATE_MESSAGES_DATE_INDEX);
        db.execSQL(DBMessagesReaded.DB_CREATE_TABLE_MESSAGES_READED);
        db.execSQL(DBMessagesFavourited.DB_CREATE_TABLE_MESSAGES_FAVOURITED);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*if (oldVersion < 4) {
            db.execSQL(DBMessagesFavourited.DB_CREATE_TABLE_MESSAGES_FAVOURITED);
        }*/
        /*db.execSQL(DBMessages.DB_DROP_TABLE_MESSAGES);
        db.execSQL(DBMessages.DB_CREATE_TABLE_MESSAGES);
        db.execSQL(DBMessages.DB_CREATE_MESSAGES_DATE_INDEX);

        db.execSQL(DBMessagesReaded.DB_DROP_TABLE_MESSAGES_READED);
        db.execSQL(DBMessagesReaded.DB_CREATE_TABLE_MESSAGES_READED);*/
    }

    public List<Translation> getTranslations(){
        return getTranslations(false);
    }

    public List<Translation> getTranslations(boolean reload){
        if(translations == null) {
            translations = new ArrayList<Translation>();

            open();

            Cursor c = db.query(Translation.TABLE_NAME,
                    new String[]{
                            Translation.COL_ID,
                            Translation.COL_NAME,
                            Translation.COL_ABBREV
                    },
                    null, null, null, null,
                    Translation.COL_ID + " ASC");

            if (c.moveToFirst()) {
                do {
                    translations.add(
                            new Translation(
                                    c.getInt(c.getColumnIndex(Translation.COL_ID)),
                                    c.getString(c.getColumnIndex(Translation.COL_NAME)),
                                    c.getString(c.getColumnIndex(Translation.COL_ABBREV))
                            )
                    );
                }
                while (c.moveToNext());
            }

            close();
        }

        return translations;
    }

    public void insertTranslation(Translation translation){
        open();

        ContentValues values = new ContentValues();

        values.put(Translation.COL_ID, translation.getId());
        values.put(Translation.COL_NAME, translation.getName());
        values.put(Translation.COL_ABBREV, translation.getAbbreviation());

        db.insertWithOnConflict(Translation.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        close();
    }
}
