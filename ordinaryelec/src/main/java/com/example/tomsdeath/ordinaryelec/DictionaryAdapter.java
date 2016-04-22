package com.example.tomsdeath.ordinaryelec;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by tomsdeath on 2016/1/22.
 */
public class DictionaryAdapter extends CursorAdapter {
    public DictionaryAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {
        return cursor == null ? "" : cursor.getString(cursor.getColumnIndex("_id"));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.list_item, null);
        TextView textView = (TextView)view;
        textView.setText(cursor.getString(cursor.getColumnIndex("_id")));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView)view;
        textView.setText(cursor.getString(cursor.getColumnIndex("_id")));
    }
}
