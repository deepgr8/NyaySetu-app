package com.example.nyaysetu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomSpinnerAdapter extends BaseAdapter {
    Context context;
    String[] languages ;

    public CustomSpinnerAdapter(Context context, String[] languages) {
        this.context = context;
        this.languages = languages;
    }

    @Override
    public int getCount() {
        return languages.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.custom_spinner,viewGroup,false);
        TextView select_lang = view.findViewById(R.id.types_of_lang);
        select_lang.setText(languages[i]);
        return view;
    }
}
