package hu.sektor.bible;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import hu.sektor.bible.database.DbManager;
import hu.sektor.bible.database.Translation;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private DbManager dbManager;
    private static int REQUEST_LOAD_DATABASE = 0x100;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DbManager.context = this;

        //initDb();



        //dbManager.getDb().execSQL("DROP TABLE \"" + Translation.TABLE_NAME + "\"" );

        /*if(dbManager.getTranslations(true).size() == 0) {
            dbManager.insertTranslation(new Translation(1, "Szent István Társulati Biblia", "SZIT"));
            dbManager.insertTranslation(new Translation(2, "Magyar Bibliatársulat újfordítású Bibliája", "UF"));
            dbManager.insertTranslation(new Translation(3, "Káldi-Neovulgáta", "KNB"));
            dbManager.insertTranslation(new Translation(4, "Károli Gáspár revideált fordítása", "KG"));
            dbManager.insertTranslation(new Translation(5, "Békés-Dalos Újszövetségi Szentírás", "BD"));
            //dbManager.close();
        }*/

        setContentView(R.layout.activity_main);






        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //if(dbManager.getTranslations(true).size() == 0){
            startActivityForResult(new Intent(this, LoadDatabaseActivity.class),REQUEST_LOAD_DATABASE);
        //}

        /*Translation translation = new Translation(1, "Szent István Társulati Biblia", "SZIT");

        dbManager = new DbManager(this);

        //System.out.println("DBINFO: " + dbManager.getWritableDatabase().hashCode() + ", " + dbManager.getReadableDatabase().hashCode() + ", " + dbManager.getWritableDatabase().hashCode());
        System.out.println("RW_EQUAL: " + (dbManager.getWritableDatabase() == dbManager.getReadableDatabase()));

        SQLiteDatabase db = dbManager.getWritableDatabase();
        db.beginTransaction();

        //dbManager.onCreate(db);
        ContentValues values = new ContentValues();

        values.put(Translation.COL_ID, translation.getId());
        values.put(Translation.COL_NAME, translation.getName());
        values.put(Translation.COL_ABBREV, translation.getAbbreviation());

        db.insert(Translation.TABLE_NAME, null, values);

        db.setTransactionSuccessful();

        db.endTransaction();

        dbManager.getWritableDatabase().setLockingEnabled(false);
        dbManager.getWritableDatabase().close();

        ///2

        dbManager = new DbManager(this);

        db = dbManager.getWritableDatabase();
        db.beginTransaction();

        //dbManager.onCreate(db);
        values = new ContentValues();

        values.put(Translation.COL_ID, translation.getId());
        values.put(Translation.COL_NAME, translation.getName());
        values.put(Translation.COL_ABBREV, translation.getAbbreviation());

        db.insert(Translation.TABLE_NAME, null, values);

        db.setTransactionSuccessful();

        db.endTransaction();

        dbManager.close();*/
    }

    /*private void initDb(){
        if(dbManager == null){
            dbManager = DbManager.getInstance(this);
        }
    }*/

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        /*List<Translation> translations = dbManager.getTranslations();

        if(translations.size() > 0) {
            mTitle = translations.get(number - 1).getName();
        }
        else{
            mTitle = "No translation (Line: 93)";
        }*/

        mTitle = "No translation (Line: 105)";
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public DbManager getDbManager() {
        //initDb();
        return dbManager;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            rootView.findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("TDB: " + ((MainActivity) getActivity()).dbManager.getTranslations(true).size());
                }
            });
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }



}
