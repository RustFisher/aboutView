package com.rust.aboutview.contactview;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.rust.aboutview.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * get data from Contacts provider
 */
public class ContactPeopleActivity extends Activity {
    final String CONTACT_ITEM_NAME = "contact_list_item_name";
    final String CONTACT_ITEM_PHONE = "contact_list_item_phone";
    final String PHONE_BOOK_NAME = ContactsContract.PhoneLookup.DISPLAY_NAME;
    final String PHONE_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

    static final int LOAD_CONTACTS_DATA = 100;
    ListView contactListView;
    static SimpleAdapter contactsSimpleAdapter;
    Thread getContactsThread = new Thread(new LoadContacts());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_people);
        contactListView = (ListView) findViewById(R.id.contact_list);
        getContactsThread.start();
    }

    ContactsDataHandler handler = new ContactsDataHandler(this);

    static class ContactsDataHandler extends android.os.Handler {
        ContactPeopleActivity activity;

        public ContactsDataHandler(ContactPeopleActivity a) {
            activity = a;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LOAD_CONTACTS_DATA) {
                activity.contactListView.setAdapter(contactsSimpleAdapter);
            }
        }
    }

    class LoadContacts implements Runnable {
        @Override
        public void run() {
            getAllContacts();
            Message message = new Message();
            message.what = LOAD_CONTACTS_DATA;
            handler.sendEmptyMessage(message.what);
        }
    }

    /**
     * get all the Contacts and load them
     */
    private void getAllContacts() {
        Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(contactsUri, null, null, null, null);
        Map<String, String> contactItemMap;
        List<Map<String, String>> contactsList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int nameIndex = cursor.getColumnIndex(PHONE_BOOK_NAME);
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        PHONE_ID + " =? ", new String[]{id}, null);
                String name = cursor.getString(nameIndex);

                contactItemMap = new HashMap<>();
                if (phoneCursor != null) {
                    // an user may have more than one phone
                    String phone = "";
                    while (phoneCursor.moveToNext()) {
                        String phoneTemp = phoneCursor.getString(
                                phoneCursor.getColumnIndex(PHONE_NUMBER));
                        phone = phone + phoneTemp;
                        contactItemMap.put(CONTACT_ITEM_PHONE, phone);
                        phone += "\n";
                    }
                    contactItemMap.put(CONTACT_ITEM_NAME, name);
                    contactsList.add(contactItemMap);
                    phoneCursor.close();
                }
            }
            cursor.close();
        }
        contactsSimpleAdapter = new SimpleAdapter(getApplicationContext(), contactsList,
                R.layout.contacts_list_item,
                new String[]{CONTACT_ITEM_NAME, CONTACT_ITEM_PHONE},
                new int[]{R.id.contact_list_item_name, R.id.contact_list_item_phone});
    }
}
