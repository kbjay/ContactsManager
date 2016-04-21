package contacts.lol.com.contacts.Bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/4/20.
 */
public class CallRecordInfo {
    private int mId;
    private String mName; // 名称
    private String mNumber; // 号码
    private String mDate; // 日期
    int mType;
    String mDetailDate;
    String mTimeDuration;
    String mPhotoId;
    Bitmap mContactIcon;

    public Bitmap getmContactIcon() {
        return mContactIcon;
    }

    public void setmContactIcon(Bitmap mContactIcon) {
        this.mContactIcon = mContactIcon;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    @Override
    public String toString() {
        return "CallRecordInfo{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mNumber='" + mNumber + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mType=" + mType +
                ", mDetailDate='" + mDetailDate + '\'' +
                ", mTimeDuration='" + mTimeDuration + '\'' +
                ", mPhotoId='" + mPhotoId + '\'' +
                ", mContactIcon=" + mContactIcon +
                '}';
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmNumber() {
        return mNumber;
    }

    public void setmNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmDetailDate() {
        return mDetailDate;
    }

    public void setmDetailDate(String mDetailDate) {
        this.mDetailDate = mDetailDate;
    }

    public String getmTimeDuration() {
        return mTimeDuration;
    }

    public void setmTimeDuration(String mTimeDuration) {
        this.mTimeDuration = mTimeDuration;
    }

    public String getmPhotoId() {
        return mPhotoId;
    }

    public void setmPhotoId(String mPhotoId) {
        this.mPhotoId = mPhotoId;
    }

    public CallRecordInfo() {

    }


}

