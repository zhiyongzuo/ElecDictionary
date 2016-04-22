package com.example.tomsdeath.ordinaryelec;

import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomsdeath.ordinaryelec.Fragment.LocalFragment;
import com.example.tomsdeath.ordinaryelec.Fragment.WebFragment;
import com.example.tomsdeath.ordinaryelec.XmlPullParser.MyXmlPullParserThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String DIRECTORY_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/dictionary";
    String DATABASE_FILENAME = "dictionary.db";
    String DB_PATH = DIRECTORY_PATH + "/" + DATABASE_FILENAME;
    SQLiteDatabase sqLiteDatabase;
    DictionaryAdapter dictionaryAdapter;
    Cursor cursor;
    String wordToSearch;

    @Bind(R.id.search_local_button) Button search_local_button;
    @Bind(R.id.search_web_button) Button search_web_button;
    @Bind(R.id.et_input) AutoCompleteTextView et_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initDictionary();
        initSoftInput();
        showSoftInput();
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        search_web_button.setOnClickListener(this);
        search_local_button.setOnClickListener(this);
    }

    private void initSoftInput() {
        et_input.setInputType(EditorInfo.TYPE_TEXT_VARIATION_URI);
        et_input.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        et_input.addTextChangedListener(textWatcher);
        et_input.setOnEditorActionListener(editorActionListerer);
        et_input.setOnItemClickListener(listener);
    }

    private void showSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(et_input, 0);
    }

    @Override
    public void onClick(View v) {
        wordToSearch = et_input.getText().toString();
        closeSoftInput();
        switch (v.getId()) {
            case R.id.search_web_button:
                translateInWeb();
                break;
            case R.id.search_local_button:
                translateInLocal();
                break;
        }
    }

    private void closeSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
    }

    private void translateInLocal() {
        Cursor cursor = sqLiteDatabase.rawQuery("select chinese from t_words where english=?" , new String[] {
                wordToSearch
        });
        String searchresult;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            searchresult = cursor.getString(cursor.getColumnIndex("chinese")).replace("&amp", "&");
            LocalFragment localFragment = new LocalFragment();
            Bundle bundle = new Bundle();
            bundle.putString("key", searchresult);
            localFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.framlayout, localFragment).commit();
        }
    }

    private void translateInWeb() {
        if (!Utils.isNetWork(MainActivity.this)) {
            Toast.makeText(MainActivity.this, "cannot connect the internet", Toast.LENGTH_SHORT).show();
        } else {
            new Thread() {
                @Override
                public void run() {
                    BufferedReader br = null;
                    try {
                        StringBuilder sb = new StringBuilder();
                        URL url = new URL(Utils.getUrl(wordToSearch, MainActivity.this));
                        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                        httpURLConnection.connect();
                        br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        String s;
                        while((s=br.readLine())!=null) {
                            sb.append(s);
                        }
                        getSharedPreferences("dict", Context.MODE_PRIVATE).edit().putString("dict", sb.toString()).commit();
                        WebFragment webFragment = new WebFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.framlayout, webFragment).commit();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    finally{
                        try {
                            if(br!=null) {
                                br.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }

    private void initDictionary() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdir();
            File fileDB = new File(DB_PATH);
            if(!fileDB.exists()) {
                InputStream inputStream = getResources().openRawResource(R.raw.dictionary);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(DB_PATH);
                    byte[] bytes = new byte[1024];
                    while ((inputStream.read(bytes)) > 0) {
                        fileOutputStream.write(bytes);
                    }
                    inputStream.close();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            cursor = sqLiteDatabase.rawQuery("select english as _id from t_words where english like ?",
                    new String[]{
                            s.toString() + "%"
                    });
            dictionaryAdapter = new DictionaryAdapter(MainActivity.this, cursor);
            et_input.setAdapter(dictionaryAdapter);
        }
    };

    TextView.OnEditorActionListener editorActionListerer = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            wordToSearch = et_input.getText().toString();
            et_input.dismissDropDown();
            closeSoftInput();
            translateInWeb();
            return false;
        }
    };

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            closeSoftInput();
            wordToSearch = et_input.getText().toString();
            if(Utils.isNetWork(MainActivity.this)) {
                translateInWeb();
            } else {
                translateInLocal();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        sqLiteDatabase.close();
    }
}
