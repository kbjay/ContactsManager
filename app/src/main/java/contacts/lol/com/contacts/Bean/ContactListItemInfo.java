package contacts.lol.com.contacts.Bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/4/19.
 */
public class ContactListItemInfo {
    String mName;
    String mContact_id;
    String mRawContact_id;
    String mPhoneBookLabel;//首字母
    Bitmap mContact_icon;//头像
    String mContact_count;
    String mLast_time_contact;

    public String getmLast_time_contact() {
        return mLast_time_contact;
    }

    public void setmLast_time_contact(String mLast_time_contact) {
        this.mLast_time_contact = mLast_time_contact;
    }

    int mScore;//亲密度

    @Override
    public String toString() {
        return "ContactListItemInfo{" +
                "mName='" + mName + '\'' +
                ", mContact_id='" + mContact_id + '\'' +
                ", mRawContact_id='" + mRawContact_id + '\'' +
                ", mPhoneBookLabel='" + mPhoneBookLabel + '\'' +
                ", mContact_icon=" + mContact_icon +
                ", mContact_count='" + mContact_count + '\'' +
                ", mLast_time_contact='" + mLast_time_contact + '\'' +
                ", mScore=" + mScore +
                '}';
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmContact_id() {
        return mContact_id;
    }

    public void setmContact_id(String mContact_id) {
        this.mContact_id = mContact_id;
    }

    public String getmRawContact_id() {
        return mRawContact_id;
    }

    public void setmRawContact_id(String mRawContact_id) {
        this.mRawContact_id = mRawContact_id;
    }

    public String getmPhoneBookLabel() {
        return mPhoneBookLabel;
    }

    public void setmPhoneBookLabel(String mPhoneBookLabel) {
        this.mPhoneBookLabel = mPhoneBookLabel;
    }

    public Bitmap getmContact_icon() {
        return mContact_icon;
    }

    public void setmContact_icon(Bitmap mContact_icon) {
        this.mContact_icon = mContact_icon;
    }

    public String getmContact_count() {
        return mContact_count;
    }

    public void setmContact_count(String mContact_count) {
        this.mContact_count = mContact_count;
    }

    public int getmScore() {
        return mScore;
    }

    public void setmScore(int mScore) {
        this.mScore = mScore;
    }

    public ContactListItemInfo() {

    }

    public ContactListItemInfo(String mName, String mContact_id, String mRawContact_id, String mPhoneBookLabel, Bitmap mContact_icon, String mContact_count, String mLast_time_contact, int mScore) {
        this.mName = mName;
        this.mContact_id = mContact_id;
        this.mRawContact_id = mRawContact_id;
        this.mPhoneBookLabel = mPhoneBookLabel;
        this.mContact_icon = mContact_icon;
        this.mContact_count = mContact_count;
        this.mLast_time_contact = mLast_time_contact;
        this.mScore = mScore;
    }
}
