package hu.sektor.bible.callbacks;

import java.util.List;

import hu.sektor.bible.database.Book;
import hu.sektor.bible.database.Translation;
import hu.sektor.bible.database.Verse;

/**
 * Created by GyKrisztian on 2015.07.09..
 */
public interface DbImportCB {
    public void onJsonReadStarted();
    public void onImportTranslation(Translation translation);
    public void onImportBook(Book book);
    public void onImportVerse(Verse verse);
    public void onImportFinished();
}
