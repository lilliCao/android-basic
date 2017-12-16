package com.example.android.booksearch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>>, Observer {
    private static final String BOOK_LIST = "book_list";
    private static final String SORT_FRAGMENT = "sort_fragment";
    private static final String ABOUT_FRAGMENT = "about_fragment";
    private static final String CONTACT_FRAGMENT = "contact_fragment";
    private static final String NO_SORT = "No sort or filter";
    private static final String EMPTY_VIEW_VISIBILITY = "empty_view_visibility";
    private static final String EMPTY_VIEW_TEXT = "empty_view_text";
    private static final String EMPTY_VIEW_IMAGE = "empty_view_image";
    private static final String URL_SAVE = "url";
    private static BookAdapter bookAdapter;
    private LoaderManager loaderManager;
    private ConnectivityManager connectivityManager;
    private TextView emptyTextView;
    private ImageView emptyImageView;
    private RelativeLayout empty_view;
    private ProgressBar loading;
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private EditText editText;
    private Button search;
    private int maxResult = 20;
    private String url;
    private String searchVolume;
    private static BookVariableWrapper sortMethod;
    private final int LOADER_ID = 1;
    public final static String FILTER_BY_EBOOK = "Filter by ebook";
    public final static String FILTER_BY_PDF = "Filter by pdf";
    public final static String SORT_BY_DATE = "Sort by early publish date";
    public final static String FILTER_BY_LANGUAGE = "Filter by language";
    private static String[] sortMethods = {FILTER_BY_EBOOK, FILTER_BY_PDF, SORT_BY_DATE, FILTER_BY_LANGUAGE};
    private ListView bookList;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BOOK_LIST, getListFromAdapter());
        outState.putInt(EMPTY_VIEW_VISIBILITY, empty_view.getVisibility());
        outState.putString(EMPTY_VIEW_TEXT, (String) emptyTextView.getText());
        if (emptyImageView.getTag() != null) {
            outState.putInt(EMPTY_VIEW_IMAGE, (Integer) emptyImageView.getTag());
        } else {
            outState.putInt(EMPTY_VIEW_IMAGE, -1);
        }
        outState.putString(URL_SAVE, url);
    }

    private static ArrayList<Book> getListFromAdapter() {
        ArrayList<Book> list = new ArrayList<>();
        for (int i = 0; i < bookAdapter.getCount(); i++) {
            list.add(bookAdapter.getItem(i));
        }
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set change observer and get default sort
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this,R.xml.preferences, false);
        String sort = sharedPreferences.getString(getString(R.string.setting_sort), "");
        String max = sharedPreferences.getString(getString(R.string.setting_number_of_result), "");
        maxResult = Integer.parseInt(max);
        sortMethod = new BookVariableWrapper(sort);
        sortMethod.addObserver(this);

        //Get booklist
        bookList = findViewById(R.id.list);
        loaderManager = getLoaderManager();

        //get other views
        emptyTextView = findViewById(R.id.text);
        emptyImageView = findViewById(R.id.image);
        empty_view = findViewById(R.id.empty_view);
        loading = findViewById(R.id.loading);
        editText = findViewById(R.id.search);
        search = findViewById(R.id.search_button);

        if (savedInstanceState != null) {
            ArrayList<Book> list = savedInstanceState.getParcelableArrayList(BOOK_LIST);
            bookAdapter = new BookAdapter(this, list);
            int visual = savedInstanceState.getInt(EMPTY_VIEW_VISIBILITY);
            String text = savedInstanceState.getString(EMPTY_VIEW_TEXT);
            int image = savedInstanceState.getInt(EMPTY_VIEW_IMAGE);
            empty_view.setVisibility(visual);
            if (image == R.drawable.ic_cloud_off_black_24dp) {
                emptyImageView.setImageResource(image);
                emptyImageView.setTag(image);
            }
            emptyTextView.setText(text);
            url = savedInstanceState.getString(URL_SAVE);
        } else {
            bookAdapter = new BookAdapter(this, new ArrayList<Book>());
        }
        bookList.setAdapter(bookAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get current Setting
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String sort = sharedPreferences.getString(getString(R.string.setting_sort), "");
                String max = sharedPreferences.getString(getString(R.string.setting_number_of_result), "");
                maxResult = Integer.parseInt(max);
                //Do search book
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                Editable searchKey = editText.getText();
                if (searchKey.toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter keyword to search", Toast.LENGTH_LONG).show();
                } else {
                    searchVolume = searchKey.toString();
                    searchVolume = searchVolume.trim().replaceAll("\\s+", "+");
                    url = BASE_URL + searchVolume + "&" + "maxResults=" + maxResult;
                    //check network and call request
                    sortMethod.setSorthMethod(sort);
                }
            }
        });

        //Create toolbar set up
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void callInBackGround() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            setOnInternet();
            if (loaderManager.getLoader(LOADER_ID) == null) {
                //init
                loaderManager.initLoader(LOADER_ID, null, MainActivity.this);
            } else {
                //restart
                loaderManager.restartLoader(LOADER_ID, null, MainActivity.this);
            }
        } else {
            bookAdapter.clear();
            setNoInternet();
        }
    }

    /**
     * inflate xml menu to real menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view = findViewById(R.id.setting_button);
        switch (item.getItemId()) {
            case R.id.setting_button:
                //create pop up menu
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_items, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.sort:
                                DialogFragment dialogFragment = new SortDialogFragment();
                                dialogFragment.show(getSupportFragmentManager(), SORT_FRAGMENT);
                                break;
                            case R.id.about:
                                DialogFragment dialogFragment1 = new AboutDialogFragment();
                                dialogFragment1.show(getSupportFragmentManager(), ABOUT_FRAGMENT);
                                break;
                            case R.id.setting:
                                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.feedback:
                                DialogFragment dialogFragment2 = new ContactDialogFragment();
                                dialogFragment2.show(getSupportFragmentManager(), CONTACT_FRAGMENT);
                                break;
                            case R.id.saved:
                                //Save for next lession
                                break;
                            default:
                                //Get current setting for refresh
                                if(!editText.getText().toString().isEmpty()) {
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                    String sort = sharedPreferences.getString(getString(R.string.setting_sort), "");
                                    String max = sharedPreferences.getString(getString(R.string.setting_number_of_result), "");
                                    maxResult = Integer.parseInt(max);
                                    url = BASE_URL + searchVolume + "&" + "maxResults=" + maxResult;
                                    sortMethod.setSorthMethod(NO_SORT);
                                }else{
                                    Toast.makeText(MainActivity.this,getString(R.string.no_search),Toast.LENGTH_SHORT).show();
                                }
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setNoInternet() {
        loading.setVisibility(GONE);
        empty_view.setVisibility(View.VISIBLE);
        emptyTextView.setText("There is no internet connection");
        emptyImageView.setImageResource(R.drawable.ic_cloud_off_black_24dp);
        emptyImageView.setTag(R.drawable.ic_cloud_off_black_24dp);
    }

    private void setOnInternet() {
        empty_view.setVisibility(GONE);
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int i, Bundle bundle) {
        loading.setVisibility(View.VISIBLE);
        return new BookLoader(MainActivity.this, url, sortMethod.getSorthMethod());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        loading.setVisibility(View.GONE);
        if ((books == null) || books.isEmpty()) {
            emptyTextView.setText("There is no book in the searching.");
            empty_view.setVisibility(View.VISIBLE);
            bookAdapter.clear();
            return;
        }
        bookAdapter.clear();
        bookAdapter.addAll(books);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        bookAdapter.clear();
    }

    @Override
    public void update(Observable observable, Object o) {
        callInBackGround();
        //set scroll back to first item in book list
        bookList.setSelection(0);
    }

    //class Dialog Fragment
    public static class SortDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(sortMethods, new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sortMethod.setSorthMethod(sortMethods[i]);
                    dialogInterface.cancel();
                }
            });
            return builder.create();
        }
    }

    public static class AboutDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.about_text))
                    .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            return builder.create();
        }
    }

    public static class ContactDialogFragment extends DialogFragment {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.contact, null);
            EditText user = view.findViewById(R.id.user);
            EditText feed = view.findViewById(R.id.feed);
            builder.setTitle("Feedback")
                    .setView(view)
                    .setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("*/*");
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"caothivananh98@gmail.com"});
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for BookSearch App from " + user.getText().toString());
                            intent.putExtra(Intent.EXTRA_TEXT, feed.getText().toString());
                            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                                startActivity(intent);
                            }
                            dialogInterface.cancel();
                        }
                    });
            return builder.create();
        }
    }

}

