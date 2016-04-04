package com.yzq.android.notebook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/4/22 0022.
 */
public class NoteAdapter extends ArrayAdapter<Note> {

    public static final String TAG = "NoteLab";
    private int resourceId;
    public NoteAdapter(Context context, int textRsID, ArrayList<Note> notes) {
        super(context, textRsID, notes);
        resourceId = textRsID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note n = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        }
        else {
            view = convertView;
        }


        /*if (view == null) {
            Log.d(TAG, "view null");
        }*/
        TextView companyTextView = (TextView)view.findViewById(R.id.company_name);
        TextView dateTextView = (TextView)view.findViewById(R.id.date);

        companyTextView.setText(n.getCompany());
        if (n.getDate() != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            String mDateFormat = simpleDateFormat.format(n.getDate());
            dateTextView.setText(mDateFormat);
        }

        return view;
    }
}
