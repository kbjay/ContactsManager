package contacts.lol.com.contacts.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import contacts.lol.com.contacts.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //测试——跳转详情页
    public void jump_DetailActivity(View view){
        startActivity(new Intent(MainActivity.this, DetailActivity.class));

    }
    public void jump_AddContactActivity(View view){
        startActivity(new Intent(MainActivity.this,AddContactActivity.class));

    }
}
