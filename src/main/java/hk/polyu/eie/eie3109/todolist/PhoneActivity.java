package hk.polyu.eie.eie3109.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class PhoneActivity extends AppCompatActivity {

    private ListView myPhoneList;
    private SimpleCursorAdapter myCursorAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        Button BNBack = findViewById(R.id.BNBack);

        BNBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        myPhoneList = findViewById(R.id.LVPhoneList);

        myPhoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final ContentResolver cr = getContentResolver();
                String phoneNumber = "No Phone Number";

                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER}, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    for (int j = 0; j < i; j++) {
                        cursor.moveToNext();

                    }

                    int hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    if(hasPhone > 0) {
                        Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        if (cp != null && cp.moveToFirst()) {
                            phoneNumber = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            while(cp.moveToNext()) {
                                phoneNumber += "\n";
                                phoneNumber += cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            }
                            cp.close();
                        }

                    }

                    Toast.makeText(getApplicationContext(), phoneNumber , Toast.LENGTH_SHORT).show();
                    cursor.close();
                }
            }
        });
        showContacts();
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
        } else {
            final ContentResolver cr = getContentResolver();
            Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);

            myCursorAdaptor = new SimpleCursorAdapter(this,R.layout.list_item,c,new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                    new int[] {R.id.TVRow},0);

            myPhoneList.setAdapter(myCursorAdaptor);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
