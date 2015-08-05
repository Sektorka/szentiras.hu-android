package hu.sektor.bible.database;

public class Book {
    private boolean oldTestament;
    private int id, countChapters;
    private String name, abbrevation;
    private Translation translation;

    public static final String TABLE_NAME = "books";

    public static final String COL_ID = "id";
    public static final String COL_TRANSLATION_ID = "translation_id";
    public static final String COL_NAME = "name";
    public static final String COL_ABBREV = "abbrevation";
    public static final String COL_COUNT_CHAPTERS = "count_chapters";
    public static final String COL_OLD_TESTAMENT = "old_testament";

    public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS \"" + TABLE_NAME + "\" (\n" +
            "\"" + COL_ID + "\"  INTEGER PRIMARY KEY NOT NULL,\n" +
            "\"" + COL_TRANSLATION_ID + "\"  INTEGER NOT NULL,\n" +
            "\"" + COL_NAME + "\"  TEXT DEFAULT NULL,\n" +
            "\"" + COL_ABBREV + "\"  TEXT DEFAULT NULL,\n" +
            "\"" + COL_COUNT_CHAPTERS + "\"  INTEGER DEFAULT 0,\n" +
            "\"" + COL_OLD_TESTAMENT + "\"  INTEGER DEFAULT 0\n" +
            ");";

    public Book(int id){
        this.id = id;
    }

    public Book(int id, Translation translation, String name, String abbrevation, int countChapters, boolean oldTestament) {
        this.id = id;
        this.translation = translation;
        this.name = name;
        this.abbrevation = abbrevation;
        this.countChapters = countChapters;
        this.oldTestament = oldTestament;
    }

    public int getId() {
        return id;
    }

    public Translation getTranslation() {
        return translation;
    }

    public String getName() {
        return name;
    }

    public String getAbbrevation() {
        return abbrevation;
    }

    public int getCountChapters() {
        return countChapters;
    }

    public boolean isOldTestament() {
        return oldTestament;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountChapters(int countChapters) {
        this.countChapters = countChapters;
    }

    public void setAbbrevation(String abbrevation) {
        this.abbrevation = abbrevation;
    }

    public void setOldTestament(boolean oldTestament) {
        this.oldTestament = oldTestament;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }
}
