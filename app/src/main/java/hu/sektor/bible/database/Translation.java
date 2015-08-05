package hu.sektor.bible.database;

public class Translation {
    private String name, abbreviation;
    private int id;

    public static final String TABLE_NAME = "translations";

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_ABBREV = "abbreviation";
    public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS \"" + TABLE_NAME + "\" (\n" +
            "\"" + COL_ID + "\"  INTEGER PRIMARY KEY NOT NULL,\n" +
            "\"" + COL_NAME + "\"  TEXT DEFAULT NULL,\n" +
            "\"" + COL_ABBREV + "\"  TEXT DEFAULT NULL" +
            ");";

    public Translation(int id){
        this.id = id;
    }

    public Translation(int id, String name, String abbreviation) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getName();
    }
}
