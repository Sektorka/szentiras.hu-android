package hu.sektor.bible;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import hu.sektor.bible.callbacks.DbImportCB;
import hu.sektor.bible.database.Book;
import hu.sektor.bible.database.DbManager;
import hu.sektor.bible.database.Translation;
import hu.sektor.bible.database.Verse;
import hu.sektor.bible.workers.DbImporter;

import static android.content.DialogInterface.*;

public class LoadDatabaseActivity extends ActionBarActivity implements ActionBar.TabListener, DbImportCB {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ProgressDialog progressDialog;
    private DbImporter dbImporter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_database);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_load_database, menu);
        return true;
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onJsonReadStarted() {
        //DbManager.getInstance().getDb().beginTransaction();
        progressDialog = ProgressDialog.show(this, "DB betoltes", "Betoltes fajlbol",true, false);

    }

    @Override
    public void onImportTranslation(Translation translation) {
        DbManager.getInstance().insertTranslation(translation);

        progressDialog.setMessage(dbImporter.getImportedItemsCount() + "db");
    }

    @Override
    public void onImportBook(Book book) {
        DbManager.getInstance().insertBook(book);

        progressDialog.setMessage(dbImporter.getImportedItemsCount() + "db");
    }

    @Override
    public void onImportVerse(Verse verse) {
        DbManager.getInstance().insertVerse(verse);

        progressDialog.setMessage(dbImporter.getImportedItemsCount() + "db");
    }

    @Override
    public void onImportFinished() {
        DbManager.getInstance().getDb().endTransaction();

        progressDialog.dismiss();
        new AlertDialog.Builder(this).setTitle("Kesz").setMessage("Vege").setPositiveButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.tbLoadFromServer).toUpperCase(l);
                case 1:
                    return getString(R.string.tbLoadFromStorage).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
        private FileList<File> folders;
        private ArrayList<String> parentFolders;
        private ListView listDirs;
        private File currentFolder;
        private Spinner spFolders;

        public static int FOLDER_SELECT_REQUEST = 0x101;
        public static String FOLDER_CURRENT_FOLDER_INDEX = "currentFolder";
        public static String FOLDER_SELECTED_FOLDER_INDEX = "selectedFolder";

        private static FileFilter dirFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && getFileExtension(file).equals(".json") || file.isDirectory())/* && file.canRead()*/;
            }
        };



        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnCancel:
                    getActivity().finish();
                    break;
            }
        }

        //dir or file selection
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if(position == 0){
                if(currentFolder.getParentFile() != null){
                    currentFolder = currentFolder.getParentFile();
                }
            }
            else{
                currentFolder = (File) adapterView.getAdapter().getItem(position);
            }

            if(currentFolder.isDirectory()) {
                if(currentFolder.canRead()) {
                    setParentFolders();
                    setFolders();
                }
                else{
                    currentFolder = currentFolder.getParentFile();
                }
            }
            else{
                new AlertDialog.Builder(getActivity())
                        .setTitle("Biztos?")
                        .setCancelable(false)
                        .setNegativeButton("Nem", null)
                        .setPositiveButton("Igen", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((LoadDatabaseActivity)getActivity()).dbImporter = new DbImporter().addCallback((LoadDatabaseActivity)getActivity());
                                ((LoadDatabaseActivity)getActivity()).dbImporter.execute(currentFolder);
                            }
                        })
                        .create().show();

            }
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            currentFolder = new File((String) adapterView.getAdapter().getItem(position));

            setParentFolders();
            setFolders();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        private class FileList<T extends File> extends ArrayList<T> {

            private static final long serialVersionUID = 3285248730931136234L;

            public void updateList(T[] array){
                if(array != null){
                    removeAll(this);

                    add((T)new File(".."));

                    for(T item : array){
                        this.add(item);
                    }

                    Collections.sort(this, new Comparator<T>() {
                        @Override
                        public int compare(T f1, T f2) {
                            return f1.getName().compareToIgnoreCase(f2.getName());
                        }
                    });


                }
            }
        }

        private void setFolders() {
            if(currentFolder == null){
                return;
            }

            if(!currentFolder.exists()){
                currentFolder = Environment.getExternalStorageDirectory();;
            }

            folders.updateList(currentFolder.listFiles(dirFilter));
            ((FolderSelectorAdapter)listDirs.getAdapter()).notifyDataSetChanged();
        }

        private void setParentFolders(){
            if(!currentFolder.canRead() && parentFolders.size() > 0 || !currentFolder.isDirectory()){
                return;
            }

            parentFolders.removeAll(parentFolders);
            setParentFolders(currentFolder);

            if(spFolders.getAdapter() != null){
                ((ArrayAdapter<String>)spFolders.getAdapter()).notifyDataSetChanged();
                spFolders.setSelection(0);
            }
        }

        private void setParentFolders(File file){
            if(file != null){
                parentFolders.add(file.getAbsolutePath());
                setParentFolders(file.getParentFile());
            }
        }

        private static String getFileExtension(File file){
            String fileName = file.getAbsolutePath();
            int pos = -1;

            if((pos = fileName.lastIndexOf(".")) != -1){
                return fileName.substring(pos).toLowerCase();
            }
            else{
                return "";
            }
        }

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
            View w = inflater.inflate(R.layout.fragment_load_database_storage, container, false);

            //((TextView) findViewById(R.id.lblTitle)).setText(getString(R.string.app_name) + " :: " + getString(R.string.folderSelectorTitle));

            listDirs = (ListView) w.findViewById(R.id.listDirs);
            spFolders = (Spinner) w.findViewById(R.id.spFolders);

            w.findViewById(R.id.btnCancel).setOnClickListener(this);

            currentFolder = Environment.getExternalStorageDirectory();

            System.out.println("File: " + currentFolder.getAbsolutePath() + ", " + currentFolder.exists() + ", " + currentFolder.getParentFile().getAbsolutePath() + ", " + currentFolder.getParentFile().exists());

            folders = new FileList<File>();
            parentFolders = new ArrayList<String>();


            spFolders.setAdapter(new ArrayAdapter<String>(
                    getActivity(), R.layout.parent_folder_list, R.id.lblFolderName, parentFolders
            ));
            spFolders.setOnItemSelectedListener(this);

            listDirs.setAdapter(new FolderSelectorAdapter<File>(getActivity(),R.layout.folder_selector_item,R.id.lblFolderName,folders));
            listDirs.setOnItemClickListener(this);

            setFolders();
            setParentFolders();

            return w;
        }
    }

}
