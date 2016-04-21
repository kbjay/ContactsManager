package contacts.lol.com.contacts.Dao;


import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;

import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.CallLog;
import android.provider.ContactsContract;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import contacts.lol.com.contacts.Bean.CallRecordInfo;
import contacts.lol.com.contacts.Bean.ContactDetailInfo;
import contacts.lol.com.contacts.Bean.ContactListItemInfo;
import contacts.lol.com.contacts.R;


/**
 * 提供的接口如下：
 * 1：查看系统中所有的联系人的简要信息：getAllContacts(Context context),return：List<ContactListItemInfo> list；
 *          耗时操作，调用的时候要在子线程中使用。
 * 2: 根据contact_id获取联系人其他信息；getContactMessage（Context context，String contact_id）； return Contact c；
 * 这里需要
 * <p/>
 * 3：删除联系人，deleteContact(Context context,String contact_id); return int  ;
 * 4: 增加联系人，addContact(Context context ,ContactDetailInfo contactInfo); return int ;
 * 5: 修改联系人：update（Context context, ContactDetailInfo ）
 *
 * @author wzq
 *         created at 2016/4/19 14:18
 */
public class ContactsDao {
    //create table honeyDegree (_id integer primary key autoincrement, contact_id varchar(4),score integer);";
    public static final String HONEY_TABLE="honeyDegree";
    public static final String HA_COLUMN_SCORE="score";
    public static final String HA_COLUMN_CONTACTID="contact_id";

    public static final String MIMETYPE_NAME = "vnd.android.cursor.item/name";
    public static final String MIMETYPE_EMAIL = "vnd.android.cursor.item/email_v2";
    public static final String MIMETYPE_ADDRESS = "vnd.android.cursor.item/postal-address_v2";
    public static final String MIMETYPE_PHONENUMBER = "vnd.android.cursor.item/phone_v2";
    public static final String MIMETYPE_PHOTO = "vnd.android.cursor.item/photo";

    private SQLiteDatabase mHoneyDegreeDb;

    public ContactsDao(SQLiteDatabase db){
        mHoneyDegreeDb=db;
    }

    /**
    *功能：获取所有的通话记录
    *@author wzq
    *created at 2016/4/21 13:35
    */
    public List<CallRecordInfo> getCallLogMessage(Context context) {

        Uri uri = android.provider.CallLog.Calls.CONTENT_URI;

        ArrayList<CallRecordInfo> list_callLog = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();

        String[] projection = {
                CallLog.Calls.DATE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.TYPE,
                CallLog.Calls._ID,

                CallLog.Calls.DURATION,
                CallLog.Calls.CACHED_PHOTO_ID
        };

        Cursor query = contentResolver.query(uri, projection, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        query.moveToFirst();

        while (query.moveToNext()) {
            System.out.println("jinru 循环");
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
            CallRecordInfo callRecordInfo = new CallRecordInfo();
            Date date_ = new Date(query.getLong(query.getColumnIndex(CallLog.Calls.DATE)));
            String date = sdf.format(date_);
            callRecordInfo.setmDate(date);
            String number = query.getString(query.getColumnIndex(CallLog.Calls.NUMBER));
            callRecordInfo.setmNumber(number);
            String name = query.getString(query.getColumnIndex(CallLog.Calls.CACHED_NAME));
            if (name == null || name!="") {
                callRecordInfo.setmName(number);
            } else {
                callRecordInfo.setmName(name);
            }
            int type = query.getInt(query.getColumnIndex(CallLog.Calls.TYPE));
            callRecordInfo.setmType(type);
            int id = query.getInt(query.getColumnIndex(CallLog.Calls._ID));
            callRecordInfo.setmId(id);

            String duration = query.getString(query.getColumnIndex(CallLog.Calls.DURATION));
            callRecordInfo.setmTimeDuration(duration);

            SimpleDateFormat sdf_detail=new SimpleDateFormat("hh:mm");
            String date_detail = sdf_detail.format(date_);
            callRecordInfo.setmDetailDate(date_detail);

            String photo_id = query.getString(query.getColumnIndex(CallLog.Calls.CACHED_PHOTO_ID));
            callRecordInfo.setmPhotoId(photo_id);

            Bitmap bitmap =null;
            if(photo_id.equals("0")){
                //给默认的
                bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.default_logo);
            }else{
                bitmap= getBitmapByPhotoId(context,photo_id);
            }
            callRecordInfo.setmContactIcon(bitmap);

            list_callLog.add(callRecordInfo);
        }
        return list_callLog;
    }
    /**
    *功能：根据photoid获取photo
    *@author wzq
    *created at 2016/4/21 14:08
    */
    private Bitmap getBitmapByPhotoId(Context context, String id) {
        Bitmap bitmap=null;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts.PHOTO_URI},
                ContactsContract.Contacts.PHOTO_ID + "=?", new String[]{id}, null
        );
        if(query.moveToNext()){
            String photo_uri = query.getString(0);
            try {
                AssetFileDescriptor afd = contentResolver.openAssetFileDescriptor(Uri.parse(photo_uri), "r");
                FileInputStream inputStream = afd.createInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=1;
                bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return  bitmap;
    }

    /**
     * 功能：往系统联系人中加入新的联系人
     *
     * @param info 系统联系人的bean：这里不需要考虑id，只填充自己有的东西就行
     * @author wzq
     * created at 2016/4/20 16:39
     */
    public  void insert(Context context, ContactDetailInfo info) {
        String name = info.getmDisplay_name();
        String email = info.getmEmail();
        String address = info.getmAddress();
        String phone_number = info.getmPhone_number();
        Bitmap image_icon = info.getmContact_icon();
        int score = info.getmScore();

        //insert score
        ContentValues values = new ContentValues();
        values.put(HA_COLUMN_SCORE,score);
        mHoneyDegreeDb.insert(HONEY_TABLE,null,values);

        ContentResolver contentResolver = context.getContentResolver();

        //需要根据rawContactId去data中添加数据
        //id,type,value
        ArrayList<ContentProviderOperation> opt = new ArrayList<>();
        //往rawContact中插入一个，后面利用插入返回的rawContentId
        opt.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        );
        //name
        opt.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, MIMETYPE_NAME)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build()
        );
        //email
        opt.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, MIMETYPE_EMAIL)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                .build()
        );
        //address
        opt.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, MIMETYPE_ADDRESS)
                .withValue(ContactsContract.CommonDataKinds.SipAddress.DATA, address)
                .build()
        );
        //phone_number
        opt.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, MIMETYPE_PHONENUMBER)
                .withValue(ContactsContract.CommonDataKinds.Phone.DATA, phone_number)
                .build()
        );
        //bitmap
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image_icon.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        opt.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, MIMETYPE_PHOTO)
                .withValue(ContactsContract.Contacts.Photo.PHOTO, bytes)
                .withValue(ContactsContract.Data.IS_PRIMARY, "1")
                .withValue(ContactsContract.Data.IS_SUPER_PRIMARY, "1")
                .withValue(ContactsContract.Data.DATA14, "1")
                .build()
        );

        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, opt);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：修改内容，
     *
     * @param info:这里跟insert不同，实例化的时候必须要setContactId和setRawContactId
     * @author wzq
     * created at 2016/4/19 21:57
     */
    public  void updateContact(Context context, ContactDetailInfo info) {
        String contact_id = info.getmContact_id();
        String rawContact_id = info.getmRawContact_id();
        String name = info.getmDisplay_name();
        String phoneNumber = info.getmPhone_number();
        String email = info.getmEmail();
        String address = info.getmAddress();
        Bitmap bitmap = info.getmContact_icon();
        int score = info.getmScore();

        //update score
        ContentValues values = new ContentValues();
        values.put(HA_COLUMN_SCORE,score);
        mHoneyDegreeDb.update(HONEY_TABLE,values,HA_COLUMN_CONTACTID+"=?",new String[]{contact_id});

        ContentResolver contentResolver = context.getContentResolver();
        //这里的opt是数据据的batch操作
        ArrayList<ContentProviderOperation> opt = new ArrayList<>();

        //name
        if (name != null) {
            opt.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?",
                            new String[]{rawContact_id, MIMETYPE_NAME})
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                    .build());
        }
        //email
        if (email != null) {
            opt.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?",
                            new String[]{rawContact_id, MIMETYPE_EMAIL})
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                    .build());
        }
        //address
        if (address != null) {
            opt.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?",
                            new String[]{rawContact_id, MIMETYPE_ADDRESS})
                    .withValue(ContactsContract.CommonDataKinds.SipAddress.DATA, address)
                    .withValue(ContactsContract.CommonDataKinds.SipAddress.TYPE, ContactsContract.CommonDataKinds.SipAddress.TYPE_HOME)
                    .build());
        }
        //phone
        if (phoneNumber != null) {
            opt.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?",
                            new String[]{rawContact_id, MIMETYPE_PHONENUMBER})
                    .withValue(ContactsContract.CommonDataKinds.Phone.DATA, phoneNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }
        if (bitmap != null) {
            //将bitmap转化为字节
            //System.out.println("进入bitmap=======");

            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            // 将Bitmap压缩成PNG编码，质量为100%存储
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            byte[] image_byte = os.toByteArray();

            //System.out.println(image_byte.toString() + "-------------------------");

            opt.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?",
                            new String[]{rawContact_id, MIMETYPE_PHOTO})
                    .withValue(ContactsContract.Contacts.Photo.PHOTO, image_byte)
                    .build());
        }
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, opt);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：删除联系人
     *
     * @param contact_id:
     * @param rawContact_id 这两参数必须给
     * @author wzq
     * created at 2016/4/19 21:16
     */
    public  void  deleteContact(Context context, String contact_id, String rawContact_id) {
        //在rawContaxt中根据contact_id删除联系人
        ContentResolver contentResolver = context.getContentResolver();
        int delete = contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID + "=?",
                new String[]{contact_id});
        contentResolver.delete(ContactsContract.Data.CONTENT_URI, ContactsContract.Data.RAW_CONTACT_ID + "=?",
                new String[]{rawContact_id});

        mHoneyDegreeDb.delete(HONEY_TABLE,HA_COLUMN_CONTACTID+"=?",new String[]{contact_id});
    }

    /**
     * 功能：获取详细的信息
     * 根据rawContact_id在data中获取该联系人的详细信息
     * 根据contact_id获取头像
     *
     * @author wzq
     * created at 2016/4/19 17:59
     */
    public  ContactDetailInfo getContactMessage(Context context, String contact_id, String rawContact_id) {
        ContactDetailInfo contactInfo = new ContactDetailInfo();
        ContentResolver contentResolver = context.getContentResolver();

        String[] projection = {ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1};
        Cursor cs_data = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, ContactsContract.Data.RAW_CONTACT_ID + "=?", new String[]{rawContact_id}, null);
        while (cs_data.moveToNext()) {
            String mimetype = cs_data.getString(0);
            //System.out.println(mimetype + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            switch (mimetype) {
                case MIMETYPE_EMAIL:
                    String email = cs_data.getString(1);
                    contactInfo.setmEmail(email);
                    break;
                case MIMETYPE_NAME://name
                    String name = cs_data.getString(1);
                    contactInfo.setmDisplay_name(name);
                    break;
                case MIMETYPE_PHONENUMBER://phone
                    String phoneNum = cs_data.getString(1);
                    contactInfo.setmPhone_number(phoneNum);
                    break;
                case MIMETYPE_ADDRESS:
                    String address = cs_data.getString(1);
                    contactInfo.setmAddress(address);
                    break;
            }
        }
        cs_data.close();

        Bitmap bitmap = getBitmap(Integer.valueOf(contact_id), context);
        contactInfo.setmContact_icon(bitmap);

        contactInfo.setmContact_id(contact_id);
        contactInfo.setmRawContact_id(rawContact_id);

        return contactInfo;
    }

    /**
     * 功能：获取填充list需要的所有信息
     * 需要获取  name，contactId，phonebookLabel  根据sort_key
     * 这里需要在子线程中调用，getBitmap是个耗时操作。
     *
     * @author wzq
     * created at 2016/4/19 15:32
     */
    public List<ContactListItemInfo> getAllContacts(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        ArrayList<ContactListItemInfo> contactListInfos = new ArrayList<>();
        //1：在rawContact表中获取contact_id(根据这个获取icon)，rawContact_id+name+phone_label，根据sort_key
        String[] projection = {"_id", "contact_id", "display_name", "phonebook_label"};
        Cursor cs_raw = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI, projection, "deleted=?", new String[]{"0"}, "sort_key");

        while (cs_raw.moveToNext()) {
            ContactListItemInfo itemInfo = new ContactListItemInfo();
            String raw_id = cs_raw.getString(0);
            String contact_id = cs_raw.getString(1);
            String display_name = cs_raw.getString(2);
            String phonebook_label = cs_raw.getString(3);
            itemInfo.setmContact_id(contact_id);
            itemInfo.setmRawContact_id(raw_id);
            itemInfo.setmName(display_name);
            itemInfo.setmPhoneBookLabel(phonebook_label);

            Bitmap bitmap = getBitmap(Integer.valueOf(contact_id), context);
            itemInfo.setmContact_icon(bitmap);

            //设置亲密度
            Cursor query = mHoneyDegreeDb.query(HONEY_TABLE, new String[]{HA_COLUMN_SCORE}, HA_COLUMN_CONTACTID + "=?",
                    new String[]{contact_id}, null, null, null);
            if(query.moveToNext()){
                itemInfo.setmScore(query.getInt(0));
            }
            //设置通话次数
            String count =getContactCountById(context,contact_id);
            itemInfo.setmContact_count(count);
            //设置最后一次通话时间
            String last_time=getLastTimeById(context,contact_id);
            itemInfo.setmLast_time_contact(last_time);
            contactListInfos.add(itemInfo);
        }
        //2：根据rawContactId在data中获取信息
        return contactListInfos;
    }

    private String getLastTimeById(Context context, String contact_id) {
        String last_time=null;
        Cursor query = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts.LAST_TIME_CONTACTED},
                ContactsContract.Contacts._ID + "=?", new String[]{contact_id}, null);
        if(query.moveToNext()){
            last_time=query.getString(0);
        }
        return last_time;
    }

    private String getContactCountById(Context context, String contact_id) {
        String count=null;
        Cursor query = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts.TIMES_CONTACTED},
                ContactsContract.Contacts._ID + "=?", new String[]{contact_id}, null);
        if(query.moveToNext()){
            count=query.getString(0);
        }
        return count;
    }

    /**
     * 功能：根据contactId获取头像的bitmap，耗时操作
     *
     * @author wzq
     * created at 2016/4/20 16:47
     */
    public Bitmap getBitmap(long contactId, Context context) {
        Bitmap bitmap = null;

        String[] strings = {ContactsContract.Contacts.PHOTO_URI};
        Cursor query = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, strings, ContactsContract.Contacts._ID + "=?",
                new String[]{String.valueOf(contactId)}, null);
        if (query.moveToNext()) {
            String photo_uri = query.getString(0);
            //  System.out.println(photo_uri+"头像uri+++++"+contactId);
            if (photo_uri != null) {
                AssetFileDescriptor fd = null;
                try {
                    fd = context.getContentResolver().openAssetFileDescriptor(Uri.parse(photo_uri), "r");

                    FileInputStream inputStream = fd.createInputStream();
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inSampleSize = 1;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, opt);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_logo);
            }
        }
        return bitmap;
    }
}
