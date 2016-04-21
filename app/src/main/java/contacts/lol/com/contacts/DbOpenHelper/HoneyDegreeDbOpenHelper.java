package contacts.lol.com.contacts.DbOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.List;

import contacts.lol.com.contacts.Bean.ContactListItemInfo;
import contacts.lol.com.contacts.Dao.ContactsDao;


/**
 * Created by Administrator on 2016/4/21.
 */
public class HoneyDegreeDbOpenHelper extends SQLiteOpenHelper {

    Context context;


    public HoneyDegreeDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "create table honeyDegree (_id integer primary key autoincrement, contact_id varchar(4),name varchar(20)," +
                "phone_number varchar(20),count integer , score integer);";
        sqLiteDatabase.execSQL(sql);

        initTable(sqLiteDatabase);
    }

    private void initTable(SQLiteDatabase db) {

        ContactsDao contactsDb = new ContactsDao(db);
        List<ContactListItemInfo> allContacts = contactsDb.getAllContacts(context);
        for (ContactListItemInfo info:
                allContacts
             ) {
            String contact_id = info.getmContact_id();
            String rawContact_id = info.getmRawContact_id();
            String name = info.getmName();
            String count = info.getmContact_count();
            String phone=getPhoneById(context,rawContact_id);
            int score=getScore(Integer.valueOf(count));

            ContentValues values = new ContentValues();
            values.put("contact_id",contact_id);
            values.put("name",name);
            values.put("phone_number",phone);
            values.put("count", Integer.valueOf(count));
            values.put("score",score);
            db.insert("honeyDegree",null,values);

        }

       /* ContentResolver contentResolver = context.getContentResolver();
        String[] projection = {ContactsContract.Contacts._ID, ContactsContract.Contacts.TIMES_CONTACTED};

        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String contactId = cursor.getString(0);
                int time_count = cursor.getInt(1);

                int score = getScore(time_count);

                ContentValues values = new ContentValues();

                values.put("contact_id", contactId);
                values.put("score", score);


                db.insert("honeyDegree", null, values);

            }
            cursor.close();
        }*/
    }

    private String getPhoneById(Context context, String rawContact_id) {
        String phone=null;
        Cursor query = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data.DATA1},
                ContactsContract.Data.MIMETYPE + "=?", new String[]{"vnd.android.cursor.item/phone_v2"}, null);
        if(query.moveToNext()){
            phone  = query.getString(0);
        }
        return phone;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public int getScore(int number) {
        int re = -1;
        if (number == 0) {
            re = 20;
        } else {
            re = 20 + (int) (80 - 80 / number);
        }
        return re;
        //return score;
    }
    //add de shihou ,xu yao gen ju qinmidu de zhi xiu gai biao
    //delete de shi hou ,ye yao xiugai
    //update de shihou ye yao
    //check de shihou ye xuyao
    //jiu xuyao ba db chuanhuoqu ,zheyao suoyou de douxuyao xiugai !!!!!!
}
