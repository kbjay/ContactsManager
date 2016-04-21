package contacts.lol.com.contacts.Bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/4/19.
 */
public class ContactDetailInfo {
    String mContact_id; //contact
    String mRawContact_id;  //contact

    String mDisplay_name ; //rawContact  display_name
    String mPhone_number;// data
    String mEmail;  //data
    String mAddress; //data
    String mGroup;  //data
    Bitmap mContact_icon;//null
    int mScore;

    @Override
    public String toString() {
        return "ContactDetailInfo{" +
                "mContact_id='" + mContact_id + '\'' +
                ", mRawContact_id='" + mRawContact_id + '\'' +

                ", mDisplay_name='" + mDisplay_name + '\'' +
                ", mPhone_number='" + mPhone_number + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mGroup='" + mGroup + '\'' +
                ", mContact_icon=" + mContact_icon +
                ", mScore=" + mScore +
                '}';
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



    public String getmDisplay_name() {
        return mDisplay_name;
    }

    public void setmDisplay_name(String mDisplay_name) {
        this.mDisplay_name = mDisplay_name;
    }

    public String getmPhone_number() {
        return mPhone_number;
    }

    public void setmPhone_number(String mPhone_number) {
        this.mPhone_number = mPhone_number;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmGroup() {
        return mGroup;
    }

    public void setmGroup(String mGroup) {
        this.mGroup = mGroup;
    }

    public Bitmap getmContact_icon() {
        return mContact_icon;
    }

    public void setmContact_icon(Bitmap mContact_icon) {
        this.mContact_icon = mContact_icon;
    }

    public int getmScore() {
        return mScore;
    }

    public void setmScore(int mScore) {
        this.mScore = mScore;
    }

    public ContactDetailInfo() {

    }

    public ContactDetailInfo(String mContact_id, String mRawContact_id, String mDisplay_name, String mPhone_number, String mEmail, String mAddress, String mGroup, Bitmap mContact_icon, int mScore) {

        this.mContact_id = mContact_id;
        this.mRawContact_id = mRawContact_id;

        this.mDisplay_name = mDisplay_name;
        this.mPhone_number = mPhone_number;
        this.mEmail = mEmail;
        this.mAddress = mAddress;
        this.mGroup = mGroup;
        this.mContact_icon = mContact_icon;
        this.mScore = mScore;
    }
}
