package hk.polyu.eie.eie3109.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ListView myToDoList;
    private ArrayList<String> myStringList;
    private ArrayAdapter<String> myAdapater;
    protected void LoadListFromPreference(){
        Context con = null;
        try {
            con = this.createPackageContext(MainActivity.PREFERENCE_PACKAGE, CONTEXT_IGNORE_SECURITY);
            SharedPreferences sharedPreferences = con.getSharedPreferences(MainActivity.PREFERENCE_NAME,MainActivity.MODE);
            int LengthOfArray = sharedPreferences.getInt("Length",0);
            if(LengthOfArray!=0) {
                for (int i = 0; i < LengthOfArray; i++) {
                    String item = sharedPreferences.getString("item_" + i, null);
                    myStringList.add(item);
                }
            }
            else
            {
                for (int i = 0; i < 10; i++) {
                    myStringList.add("Empty "+i);

                }
                myStringList.set(0,"Returns Books to Library");
                myStringList.set(1,"Meeting with Advisor");
            }

        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    protected  void SaveListToPreference(){
        Context con = null;
        try {
            con = this.createPackageContext(MainActivity.PREFERENCE_PACKAGE, CONTEXT_IGNORE_SECURITY);
            SharedPreferences.Editor editor = con.getSharedPreferences(MainActivity.PREFERENCE_NAME,MainActivity.MODE).edit();
            editor.putInt("Length", myStringList.size());
            for (int i=0;i<myStringList.size();i++){
                editor.putString("item_"+i,myStringList.get(i));
            }
            editor.commit();
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        myStringList = new ArrayList<String>();

        Button BNBack = findViewById(R.id.BNBack);
        if (BNBack != null){
            BNBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        TextView TVMessage = findViewById(R.id.TVMessage);
        Context c = null;
        try {
            c = this.createPackageContext(MainActivity.PREFERENCE_PACKAGE, CONTEXT_IGNORE_SECURITY);
            SharedPreferences sharedPreferences = c.getSharedPreferences(MainActivity.PREFERENCE_NAME,MainActivity.MODE);
            //load name
            String name = sharedPreferences.getString("Name","Default Name");
            if(!name.equals("Default Name")){
                TVMessage.setText("Hi "+name+" !");
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        LoadListFromPreference();
        myToDoList = findViewById(R.id.LVList);
        myAdapater = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myStringList);
        myToDoList.setAdapter(myAdapater);

        myToDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                ListView options = new ListView((ListActivity.this));
                options.setAdapter(new ArrayAdapter<String>(ListActivity.this, android.R.layout.simple_list_item_1, new String[]{"Add", "Edit", "Remove"}));
                builder.setView(options);

                final Dialog dialog = builder.create();
                dialog.show();

                final int item_loc = i;

                options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                        String Msg = "item "+ item_loc + " " + parent.getAdapter().getItem(i).toString();
                        Toast.makeText(getApplicationContext(), Msg, Toast.LENGTH_SHORT).show();
                        final Dialog dialogForm = new Dialog((ListActivity.this));
                        dialogForm.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogForm.setContentView(R.layout.form_operation);
                        TextView TVTitle = dialogForm.findViewById(R.id.TVTitle);
                        final EditText ETText = dialogForm.findViewById(R.id.ETText);
                        Button BNSubmit = dialogForm.findViewById(R.id.BNSubmit);
                        if(i==0) { //add
                            if (TVTitle != null) {
                                TVTitle.setText("Add");
                            }
                            if (ETText != null) {
                                ETText.setText((myStringList.get(item_loc)));
                            }
                            if (BNSubmit != null) {
                                BNSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myStringList.add(ETText.getText().toString());
                                        SaveListToPreference();
                                        myAdapater.notifyDataSetChanged();
                                        dialogForm.dismiss();
                                    }
                                });
                            } dialogForm.show();
                        }
                        if(i==1) // edit button
                        {
                            if(TVTitle != null){
                                TVTitle.setText("Edit");
                            }
                            if(ETText !=  null){
                                ETText.setText((myStringList.get(item_loc)));
                            }
                            if(BNSubmit != null){
                                BNSubmit.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        myStringList.set(item_loc,ETText.getText().toString());
                                        SaveListToPreference();
                                        myAdapater.notifyDataSetChanged();
                                        dialogForm.dismiss();
                                    }
                                });
                            }
                            dialogForm.show();
                        }
                        if(i==2) // edit button
                        {
                            myStringList.remove(item_loc);
                            SaveListToPreference();
                            myAdapater.notifyDataSetChanged();
                            dialogForm.dismiss();
                        }

                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
