package hk.polyu.eie.eie3109.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String PREFERENCE_PACKAGE="hk.polyu.eie.eie3109.helloworld";
    public static final String PREFERENCE_NAME = "MyProfile";
    public static int MODE = Context.MODE_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button BNPhone = findViewById(R.id.BNPhone);
        if(BNPhone!=null){
            BNPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,PhoneActivity.class));
                }
            });
        }
        Context c = null;
        try {
            c = this.createPackageContext(PREFERENCE_PACKAGE, CONTEXT_IGNORE_SECURITY);
            SharedPreferences sharedPreferences = c.getSharedPreferences(PREFERENCE_NAME,MODE);
            String name = sharedPreferences.getString("Name","Default Name");

        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

    }
}
