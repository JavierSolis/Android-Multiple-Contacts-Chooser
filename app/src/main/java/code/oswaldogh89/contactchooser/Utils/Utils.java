package code.oswaldogh89.contactchooser.Utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oswaldogh89 on 12/06/16.
 */
public class Utils {


    public static List<String> getContactList(Activity act) {
        List<String> list = new ArrayList<>();
        ArrayList<String> number = new ArrayList<>();
        ContentResolver cr = act.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    assert pCur != null;
                    while (pCur.moveToNext()) {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        if (!number.contains(contactNumber)) {
                            list.add(contactName);
                        }
                        number.add(contactNumber);
                        break;
                    }
                    pCur.close();
                }
            } while (cursor.moveToNext());
        }

        return list;
    }

    public static String getNumberbyName(String name, Activity act) {
        String id_name = null;
        Uri resultUri = ContactsContract.Contacts.CONTENT_URI;
        Cursor cont = act.getContentResolver().query(resultUri, null, null, null, null);
        String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME + " = ?";
        String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, name};
        Cursor nameCur = act.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
        assert nameCur != null;
        while (nameCur.moveToNext()) {
            id_name = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID));
        }
        nameCur.close();
        assert cont != null;
        cont.close();
        nameCur.close();
        return searchNumber(id_name, act);
    }

    public static String searchNumber(String id, Activity act) {
        String phone = null;
        Uri resultUri = ContactsContract.Contacts.CONTENT_URI;
        Cursor cont = act.getContentResolver().query(resultUri, null, null, null, null);
        String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";

        String[] whereNameParams2 = new String[]{ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, id};
        Cursor nameCur2 = act.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams2, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
        assert nameCur2 != null;
        while (nameCur2.moveToNext()) {
            phone = nameCur2.getString(nameCur2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        nameCur2.close();
        assert cont != null;
        cont.close();
        nameCur2.close();

        return phone;
    }
}
