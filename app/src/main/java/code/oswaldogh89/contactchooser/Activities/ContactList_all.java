package code.oswaldogh89.contactchooser.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import code.oswaldogh89.contactchooser.Objects.Contact;
import code.oswaldogh89.contactchooser.R;
import code.oswaldogh89.contactchooser.adapters.AdptSelectContacts;

public class ContactList_all extends AppCompatActivity {

    AdptSelectContacts adapter;
    private ArrayList<Contact> allContacts;
    private ArrayList<Contact> listNumbers;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_contacts_all);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        listNumbers = new ArrayList<>();

        ArrayList<String> number = new ArrayList<>();


        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            allContacts = new ArrayList<>();
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    assert pCur != null;
                    while (pCur.moveToNext()) {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));


                        Contact c = new Contact();
                        c.setPhoneNumber(contactNumber);
                        c.setName(contactName);

                        if (!number.contains(contactNumber)) {
                            allContacts.add(c);
                        }
                        number.add(contactNumber);


                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext());
        }

        //ordena los contactos
        Collections.sort(allContacts, new Comparator<Contact>() {
            public int compare(Contact v1, Contact v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });

        adapter = new AdptSelectContacts(ContactList_all.this, allContacts);
        mRecyclerView.setAdapter(adapter);

        EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                adapter.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add("addPeople").setIcon(R.mipmap.ic_check).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add("check").setIcon(R.drawable.ic_check).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().equals("check")) {

            Contact obj;

            AdptSelectContacts adpt = (AdptSelectContacts) mRecyclerView.getAdapter();
            for (int i = 0; i < adpt.getItemCount(); i++) {
                if (adpt.getItem(i).isSelected()) {
                    obj = new Contact();
                    obj.setPhoneNumber(adpt.getItem(i).getPhoneNumber());
                    obj.setName(adpt.getItem(i).getName());
                    listNumbers.add(obj);
                }
            }


            String json = new Gson().toJson(listNumbers);

            if (listNumbers.size() <= 0) {
                json = null;
            }

            int resultCode = 200;
            Intent resultIntent = new Intent();
            resultIntent.putExtra("contactSize", listNumbers.size());
            resultIntent.putExtra("contactList", json);
            setResult(resultCode, resultIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}