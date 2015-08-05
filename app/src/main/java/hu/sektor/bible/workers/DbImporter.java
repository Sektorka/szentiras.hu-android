package hu.sektor.bible.workers;

import android.os.AsyncTask;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hu.sektor.bible.callbacks.DbImportCB;
import hu.sektor.bible.database.Book;
import hu.sektor.bible.database.Translation;
import hu.sektor.bible.database.Verse;

public class DbImporter extends AsyncTask<File, Object, Void> {

    private List<DbImportCB> dbImportCBs = null;
    private Exception exception = null;
    private int importedItemsCount = 0;
    protected static Date lastReaded = null;

    protected static final String JSON_LINK = "http://tbot.sektor.hu:12580/";
    protected static final int BUFFER_SIZE = 1024;

    public static Date getLastReaded(){
        return lastReaded;
    }

    public DbImporter(){
        dbImportCBs = new ArrayList<DbImportCB>();
    }

    @Override
    protected void onPreExecute() {
        if(dbImportCBs.size() > 0) {
            for(DbImportCB dbImportCB: dbImportCBs) {
                dbImportCB.onJsonReadStarted();
            }
        }
    }

    @Override
    protected Void doInBackground(File... files) {
        //ArrayList<Message> messages = new ArrayList<Message>();

        for(File file : files){

            if(!file.exists() || !file.canRead()){
                continue;
            }



            try {
                startParsingJsonContent(
                    new JsonReader(
                        new InputStreamReader(
                                new FileInputStream(file), "UTF-8"
                        )
                    )
                );

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        /*if(exception != null){
            for(MessageReaderCB messageReaderCB : messageReaderCBs){
                messageReaderCB.onMessageReaderFinished(null);
                messageReaderCB.onMessageReaderError(exception);
            }
        }
        else{
            if(messages == null){
                for(MessageReaderCB messageReaderCB : messageReaderCBs) {
                    messageReaderCB.onMessageReaderError(new NullPointerException("messages is NULL!"));
                }
            }

            for(MessageReaderCB messageReaderCB : messageReaderCBs){
                messageReaderCB.onMessageReaderFinished(messages);
            }
        }*/
    }

    @Override
    protected void onProgressUpdate(Object... objects) {

        for(Object obj : objects){
            if(obj instanceof Translation){
                for(DbImportCB cb : dbImportCBs){
                    cb.onImportTranslation((Translation) obj);
                }
            }
            else if(obj instanceof Book){
                for(DbImportCB cb : dbImportCBs){
                    cb.onImportBook((Book) obj);
                }
            }
            else if(obj instanceof Verse){
                for(DbImportCB cb : dbImportCBs){
                    cb.onImportVerse((Verse) obj);
                }
            }
        }

        importedItemsCount++;


    }

    @Override
    protected void onCancelled() {
        /*if(exception != null) {
            for (MessageReaderCB messageReaderCB : messageReaderCBs) {
                messageReaderCB.onMessageReaderFinished(null);
            }

            for (MessageReaderCB messageReaderCB : messageReaderCBs) {
                messageReaderCB.onMessageReaderError(exception);
            }
        }*/
    }

    /*@Override
    protected void onCancelled(ArrayList<Message> messages) {
        onCancelled();
    }*/

    protected final HttpURLConnection getConnection() throws IOException {
        HttpURLConnection connection;

        String jsonUrl = JSON_LINK + (lastReaded != null ? "?start_date=" + (lastReaded.getTime() / 1000) : "");

        connection = (HttpURLConnection) new URL(jsonUrl).openConnection();
        connection.setRequestProperty("User-Agent", "The book of truth");
        connection.setRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000); //5s
        connection.setReadTimeout(10000); //10s

        return connection;
    }

    protected final void startParsingJsonContent(JsonReader reader/*, List<Message> messages*/) throws IOException {
        //publishProgress(new ContentProgress(0, countMessages, MessageReaderCB.State.DOWNLOADING_AND_PARSING));

        reader.beginObject();
        Integer totalRows = reader.nextInt();
        reader.endObject();

        reader.beginObject();
        reader.skipValue(); // = translations
        reader.beginObject();

        Translation translation = null;
        Book book = null;
        Verse verse = null;

        String prefix;
        String key;
        int pfxDlm;
        int lastChapterId = -1;

        while(reader.hasNext()){
            //System.out.println("WHILE...");
            if(reader.peek() == JsonToken.END_DOCUMENT){
                reader.close();
                break;
            }

            key = reader.nextName();
            //System.out.println("KEY=" + key);

            pfxDlm = key.indexOf('_'); //prefix delimiter
            prefix = key.substring(0,pfxDlm);

            key = key.substring(pfxDlm+1);

            try {
                int id = Integer.parseInt(key);

                switch (prefix){
                    case "t":
                        translation = new Translation(id);
                        break;
                    case "b":
                        book = new Book(id);
                        break;
                    case "ch":
                        lastChapterId = Integer.parseInt(key);
                        break;
                    case "v":
                        verse = new Verse(id, lastChapterId);
                        break;

                }

                reader.beginObject();

                continue;
            }
            catch(NumberFormatException ex){
                //ex.printStackTrace();
            }

            switch (prefix){
                case "t":
                    switch (key){
                        case "name":
                            translation.setName(reader.nextString());
                            break;
                        case "abbreviation":
                            translation.setAbbreviation(reader.nextString());
                            break;
                        case "denom":
                        case "lang":
                        case "copyright":
                        case "publisher":
                        case "publisherUrl":
                        case "references":
                            reader.skipValue();
                            break;
                        case "books":
                            publishProgress(translation);

                            reader.beginObject();
                            break;
                    }

                    break;
                case "b":
                    switch (key){
                        case "name":
                            book.setName(reader.nextString());
                            break;
                        case "abbreviation":
                            book.setAbbrevation(reader.nextString());
                            break;
                        case "chaptersCount":
                            book.setCountChapters(reader.nextInt());
                            break;
                        case "oldTestament":
                            book.setOldTestament(reader.nextBoolean());
                            break;
                        case "chapters":
                            book.setTranslation(translation);
                            publishProgress(book);

                            reader.beginObject();
                            break;
                    }
                    break;

                case "v":
                    switch (key){
                        case "verseNumber":
                            verse.setVerseNumber(reader.nextInt());
                            break;
                        case "verse":
                            verse.setVerse(reader.nextString());
                            verse.setBook(book);
                            verse.setTranslation(translation);

                            publishProgress(verse);

                            while(reader.peek() == JsonToken.END_OBJECT){
                                reader.endObject();
                            }

                            break;
                    }
                    break;
            }

//publishProgress(new ContentProgress(id, countMessages, MessageReaderCB.State.DOWNLOADING_AND_PARSING));
        }

        //reader.endArray();
    }

    protected final String getContent() throws IOException {
        final StringBuilder content = new StringBuilder();

        HttpURLConnection connection = getConnection();
        InputStream inputStream = connection.getInputStream();

        int contentLength = connection.getHeaderFieldInt("Uncompressed-Content-Length", -1); // connection.getContentLength();

        /*if("gzip".equals(connection.getContentEncoding())){
            inputStream = new GZIPInputStream(inputStream);
        }*/

        InputStreamReader in = new InputStreamReader(inputStream, "UTF-8");

        if(contentLength == -1){
            in.close();
            return "[]";
        }

        //publishProgress(new ContentProgress(0, contentLength, MessageReaderCB.State.DOWNLOADING));

        char[] buffer = new char[BUFFER_SIZE];
        int readed;

        while((readed = in.read(buffer)) != -1){
            content.append(buffer, 0, readed);
            //publishProgress(new ContentProgress(content.length(), contentLength, MessageReaderCB.State.DOWNLOADING));
        }

        in.close();

        return content.toString();
    }

    private void callbackError(final Exception e){
        exception = e;
        cancel(false);
    }


    public final DbImporter addCallback(DbImportCB dbImportCB) {
        if (!dbImportCBs.contains(dbImportCB)) {
            dbImportCBs.add(dbImportCB);
        }

        return this;
    }

    public int getImportedItemsCount() {
        return importedItemsCount;
    }
}
