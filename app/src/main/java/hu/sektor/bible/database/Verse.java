package hu.sektor.bible.database;

public class Verse {
    private int id, chapter, verseNumber;
    private Translation translation;
    private Book book;
    private String verse;

    public static final String TABLE_NAME = "verses";

    public static final String COL_ID = "id";
    public static final String COL_TRANSLATION_ID = "translation_id";
    public static final String COL_BOOK_ID = "book_id";
    public static final String COL_CHAPTER = "chapter";
    public static final String COL_VERSE_NUMBER = "verse_number";
    public static final String COL_VERSE = "verse";

    public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS \"" + TABLE_NAME + "\" (\n" +
            "\"" + COL_ID + "\"  INTEGER PRIMARY KEY NOT NULL,\n" +
            "\"" + COL_TRANSLATION_ID + "\"  INTEGER NOT NULL,\n" +
            "\"" + COL_BOOK_ID + "\"  INTEGER NOT NULL,\n" +
            "\"" + COL_CHAPTER + "\"  INTEGER NOT NULL," +
            "\"" + COL_VERSE_NUMBER + "\"  INTEGER NOT NULL,\n" +
            "\"" + COL_VERSE + "\"  TEXT DEFAULT NULL\n" +
            ");\n";
    public static final String CREATE_INDEX = "CREATE INDEX \"" + TABLE_NAME + "_" + COL_TRANSLATION_ID + "_idx\" ON \"" + TABLE_NAME + "\" (\""+COL_TRANSLATION_ID+"\");";

    public Verse(int id, int chapter){
        this.id = id;
        this.chapter = chapter;
    }

    public Verse(int id, Translation translation, Book book, int chapter, int verseNumber, String verse) {
        this.id = id;
        this.translation = translation;
        this.book = book;
        this.chapter = chapter;
        this.verseNumber = verseNumber;
        this.verse = verse;
    }

    public int getId() {
        return id;
    }

    public Translation getTranslation() {
        return translation;
    }

    public Book getBook() {
        return book;
    }

    public int getChapter() {
        return chapter;
    }

    public int getVerseNumber() {
        return verseNumber;
    }

    public String getVerse() {
        return verse;
    }

    public void setVerseNumber(int verseNumber) {
        this.verseNumber = verseNumber;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }
}
