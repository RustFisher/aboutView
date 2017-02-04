package com.rust.aboutview.contactview;

import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.rust.aboutview.R;
import com.rust.aboutview.database.RustDatabaseHelper;
import com.rust.aboutview.widget.ConfirmDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataListView extends ListFragment {

    final String DATABASE_NAME = RustDatabaseHelper.DATABASE_NAME;
    final String CONTACT_TABLE = RustDatabaseHelper.Tables.CONTACT_TABLE;
    final String CONTACT_ID = RustDatabaseHelper.Contact._ID;
    final String CONTACT_NAME = RustDatabaseHelper.Contact.NAME;
    final String CONTACT_EMAIL = RustDatabaseHelper.Contact.EMAIL;
    final String CONTACT_PHONE = RustDatabaseHelper.Contact.PHONE;

    RustDatabaseHelper databaseHelper;
    SimpleAdapter simpleAdapter;

    ConfirmDialog confirmDialog;
    String itemID = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_list_view, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new RustDatabaseHelper(getActivity(), DATABASE_NAME, null, 1);

        confirmDialog = new ConfirmDialog(getActivity());
        confirmDialog.setClickListener(new ConfirmDialog.OnClickListenerInterface() {
            @Override
            public void doConfirm() {
                SQLiteDatabase dbR = databaseHelper.getWritableDatabase();// use to Delete item
                dbR.delete(CONTACT_TABLE, CONTACT_ID + "=?", new String[]{itemID});
                dbR.close();
                confirmDialog.dismiss();
                onResume();
            }

            @Override
            public void doCancel() {
                confirmDialog.dismiss();
            }
        });

    }

    private List<Map<String, String>> loadData() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        HashMap<String, String> map;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        // get all the items
        Cursor cursor = db.query(CONTACT_TABLE, null, null, null, null, null, null);
        int IdIndex = cursor.getColumnIndex(CONTACT_ID);
        int nameIndex = cursor.getColumnIndex(CONTACT_NAME);
        int phoneIndex = cursor.getColumnIndex(CONTACT_PHONE);
        int emailIndex = cursor.getColumnIndex(CONTACT_EMAIL);

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            map = new HashMap<String, String>();
            map.put(CONTACT_ID, String.valueOf(cursor.getInt(IdIndex)));// put id as string
            map.put(CONTACT_NAME, cursor.getString(nameIndex));
            map.put(CONTACT_PHONE, cursor.getString(phoneIndex));
            map.put(CONTACT_EMAIL, cursor.getString(emailIndex));

            list.add(map);
        }

        cursor.close();
        return list;
    }

    @Override
    public void onResume() {
        // load data
        final String[] from = new String[]{CONTACT_ID, CONTACT_NAME, CONTACT_PHONE, CONTACT_EMAIL};
        final int[] to = new int[]{R.id.list_item_id,/* load id, but never show */
                R.id.list_item_name, R.id.list_item_phone, R.id.list_item_email};
        simpleAdapter = new SimpleAdapter(getActivity(), loadData(),
                R.layout.data_list_item_view, from, to);
        this.setListAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();

        // the view is ready here, so I can get ListView
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idTextView = (TextView) view.findViewById(R.id.list_item_id);
                itemID = (String) idTextView.getText();// get the chosen item ID
                confirmDialog.show();
                return true;// avoid conflict with short click
            }
        });
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent();
        i.putExtra(AddDataActivity.ITEM_OPERATION_TYPE, AddDataActivity.EDIT_ITEM);// purpose: edit
        TextView idTextView = (TextView) v.findViewById(R.id.list_item_id);
        String shortClickItemId = (String) idTextView.getText();
        i.putExtra(AddDataActivity.ITEM_ID, shortClickItemId);// send the item ID
        i.setClass(getActivity(), AddDataActivity.class);
        startActivity(i);// jump to AddDataActivity to edit item

        super.onListItemClick(l, v, position, id);
    }

}
