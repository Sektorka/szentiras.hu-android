package hu.sektor.bible.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DbManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "bible.db";
    private static final int DB_VERSION = 1;

    private List<Translation> translations;
    private List<Book> books;
    private List<Verse> verses;
    private SQLiteDatabase db;

    public static Context context;
    private static DbManager instance;

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

    public static DbManager getInstance(){
        if(instance == null){
            instance = new DbManager(context);
        }

        return instance;
    }

    private DbManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.context = context;
        db = getWritableDatabase();
    }

    public synchronized SQLiteDatabase getDb() {
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Translation.CREATE_TABLE_QUERY);
        db.execSQL(Book.CREATE_TABLE_QUERY);
        db.execSQL(Verse.CREATE_TABLE_QUERY);
        db.execSQL(Verse.CREATE_INDEX);

        Toast.makeText(context, "onCreate", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "onUpgrade", Toast.LENGTH_LONG).show();
        /*if (oldVersion < 4) {
            db.execSQL(DBMessagesFavourited.DB_CREATE_TABLE_MESSAGES_FAVOURITED);
        }

        db.execSQL(DBMessages.DB_DROP_TABLE_MESSAGES);
        */
    }

    /*@Override
    public synchronized void close() {
        super.close();
        db.close();
        db = null;
        instance = null;
    }
*/
    //get translations

    public synchronized List<Translation> getTranslations(){
        return getTranslations(false);
    }

    public synchronized List<Translation> getTranslations(boolean reload){
        if(translations == null || reload) {
            translations = new ArrayList<Translation>();

            Cursor c = db.query(Translation.TABLE_NAME,
                    new String[]{
                            Translation.COL_ID,
                            Translation.COL_NAME,
                            Translation.COL_ABBREV
                    },
                    null, null, null, null,
                    Book.COL_ID + " ASC");

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

            //close();
        }

        return translations;
    }

    //get books
/*
    public List<Book> getBooks(){
        return getBooks(false);
    }

    public List<Book> getBooks(boolean reload){
        if(books == null) {
            books = new ArrayList<Book>();

            Cursor c = db.query(Book.TABLE_NAME,
                    new String[]{
                            Book.COL_ID,
                            Book.COL_TRANSLATION_ID,
                            Book.COL_NAME,
                            Book.COL_ABBREV,
                            Book.COL_OLD_TESTAMENT
                    },
                    null, null, null, null,
                    Book.COL_ID + " ASC");

            if (c.moveToFirst()) {
                do {
                    books.add(
                            new Book(
                                    c.getInt(c.getColumnIndex(Book.COL_ID)),
                                    getTranslationById(c.getInt(c.getColumnIndex(Book.COL_TRANSLATION_ID))),
                                    c.getString(c.getColumnIndex(Book.COL_NAME)),
                                    c.getString(c.getColumnIndex(Book.COL_ABBREV)),
                                    c.getInt(c.getColumnIndex(Book.COL_COUNT_CHAPTERS)),
                                    c.getInt(c.getColumnIndex(Book.COL_OLD_TESTAMENT)) == 1 ? true : false
                            )
                    );
                }
                while (c.moveToNext());
            }

            //close();
        }

        return books;
    }

    //get verses

    public List<Verse> getVerses(){
        return getVerses(false);
    }

    public List<Verse> getVerses(boolean reload){
        if(verses == null) {
            verses = new ArrayList<Verse>();

            Cursor c = db.query(Book.TABLE_NAME,
                    new String[]{
                            Verse.COL_ID,
                            Verse.COL_TRANSLATION_ID,
                            Verse.COL_BOOK_ID,
                            Verse.COL_CHAPTER,
                            Verse.COL_VERSE_NUMBER,
                            Verse.COL_VERSE
                    },
                    null, null, null, null,
                    Translation.COL_ID + " ASC");

            if (c.moveToFirst()) {
                do {
                    verses.add(
                            new Verse(
                                    c.getInt(c.getColumnIndex(Verse.COL_ID)),
                                    getTranslationById(c.getInt(c.getColumnIndex(Verse.COL_TRANSLATION_ID))),
                                    getBookById(c.getInt(c.getColumnIndex(Verse.COL_BOOK_ID))),
                                    c.getInt(c.getColumnIndex(Verse.COL_CHAPTER)),
                                    c.getInt(c.getColumnIndex(Verse.COL_VERSE_NUMBER)),
                                    c.getString(c.getColumnIndex(Verse.COL_VERSE))
                            )
                    );
                }
                while (c.moveToNext());
            }

            //close();
        }

        return verses;
    }
*/
    //insert
    public synchronized void insertTranslation(Translation translation){
        if(translation == null){
            throw new NullPointerException(translation.getClass().getName());
        }

        ContentValues values = new ContentValues();

        values.put(Translation.COL_ID, translation.getId());
        values.put(Translation.COL_NAME, translation.getName());
        values.put(Translation.COL_ABBREV, translation.getAbbreviation());

        db.insertWithOnConflict(Translation.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        //close();
    }

    public void insertBook(Book book){
        if(book == null){
            throw new NullPointerException(book.getClass().getName());
        }

        ContentValues values = new ContentValues();

        values.put(Book.COL_ID, book.getId());
        values.put(Book.COL_TRANSLATION_ID, book.getTranslation().getId());
        values.put(Book.COL_NAME, book.getName());
        values.put(Book.COL_ABBREV, book.getAbbrevation());
        values.put(Book.COL_OLD_TESTAMENT, book.isOldTestament());

        db.insertWithOnConflict(Book.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        //close();
    }

    public void insertVerse(Verse verse){
        if(verse == null){
            throw new NullPointerException(verse.getClass().getName());
        }

        ContentValues values = new ContentValues();

        values.put(Verse.COL_ID, verse.getId());
        values.put(Verse.COL_TRANSLATION_ID, verse.getTranslation().getId());
        values.put(Verse.COL_BOOK_ID, verse.getBook().getId());
        values.put(Verse.COL_CHAPTER, verse.getChapter());
        values.put(Verse.COL_VERSE_NUMBER, verse.getVerseNumber());
        values.put(Verse.COL_VERSE, verse.getVerse());

        db.insertWithOnConflict(Verse.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        //close();
    }

    //find

    private synchronized Translation getTranslationById(int translationId) {
        if(translations != null && translations.size() > 0){
            for(Translation translation : translations){
                if(translation.getId() == translationId){
                    return translation;
                }
            }
        }

        return null;
    }

    private Book getBookById(int bookId){
        if(books != null && books.size() > 0){
            for(Book book : books){
                if(book.getId() == bookId){
                    return book;
                }
            }
        }

        return null;
    }
}
