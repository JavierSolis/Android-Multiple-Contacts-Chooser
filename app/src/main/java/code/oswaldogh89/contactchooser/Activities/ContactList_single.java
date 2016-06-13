package code.oswaldogh89.contactchooser.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import code.oswaldogh89.contactchooser.Objects.Contact;
import code.oswaldogh89.contactchooser.R;
import code.oswaldogh89.contactchooser.Utils.Utils;
import code.oswaldogh89.contactchooser.adapters.AdptSelectedContacts;

public class ContactList_single extends AppCompatActivity {

    private final int PICK_CONTACT = 200;
    private MaterialSearchView searchView;
    private AdptSelectedContacts adapterContacts;
    private Contact contactObj;
    private ArrayList<String> repeatedNumbers;
    private ArrayList<Contact> listContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_contacts_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setBackIcon(null);
        searchView.setEllipsize(true);


        List<String> list = Utils.getContactList(ContactList_single.this);
        String[] stringArray = list.toArray(new String[list.size()]);
        searchView.setSuggestions(stringArray);


        repeatedNumbers = new ArrayList<>();
        listContacts = new ArrayList<>();

        adapterContacts = new AdptSelectedContacts(ContactList_single.this, listContacts);
        mRecyclerView.setAdapter(adapterContacts);


        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RelativeLayout rootRelativeLayout = (RelativeLayout) view;
                for (int i = 0; i < rootRelativeLayout.getChildCount(); i++) {
                    View child = rootRelativeLayout.getChildAt(i);
                    if (child instanceof AppCompatTextView) {
                        AppCompatTextView text = (AppCompatTextView) child;
                        String name = text.getText().toString();
                        String number = Utils.getNumberbyName(text.getText().toString(), ContactList_single.this);
                        if (!repeatedNumbers.contains(number)) {
                            contactObj = new Contact();
                            contactObj.setName(name);
                            contactObj.setPhoneNumber(number);
                            listContacts.add(contactObj);
                        }
                        repeatedNumbers.add(number);
                    }
                }
                searchView.closeSearch();
                adapterContacts.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT && null != data) {
            Bundle res = data.getExtras();
            try {
                JSONArray json = new JSONArray(res.getString("contactList"));
                Contact contacto;
                for (int i = 0; i < json.length(); i++) {
                    JSONObject o = new JSONObject(json.get(i).toString());
                    String number = o.getString("phoneNumber");
                    String name = o.getString("name");
                    contacto = new Contact();
                    contacto.setName(name);
                    contacto.setPhoneNumber(number);
                    listContacts.add(contacto);
                }
                adapterContacts.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.v("contacts_added", "e :: " + e.getMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionMultiple:
                Intent iM = new Intent(ContactList_single.this, ContactList_all.class);
                startActivityForResult(iM, PICK_CONTACT);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }


}
