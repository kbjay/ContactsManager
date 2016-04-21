package contacts.lol.com.contacts.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import contacts.lol.com.contacts.DbOpenHelper.HoneyDegreeDbOpenHelper;
import contacts.lol.com.contacts.R;

public class SflashActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sflash);
        initHoneyTable();
        //做一个viewPager。。。
    }
    private void initHoneyTable() {
        HoneyDegreeDbOpenHelper honeyDegreeDb = new HoneyDegreeDbOpenHelper(SflashActivity.this, "honeyDegreeDb.db", null, 1);
        SQLiteDatabase db = honeyDegreeDb.getWritableDatabase();
    }
    public void jump(View view){
        Intent intent = new Intent(SflashActivity.this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
