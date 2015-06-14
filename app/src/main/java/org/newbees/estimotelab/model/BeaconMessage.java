package org.newbees.estimotelab.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erichua on 6/13/15.
 */
@AVClassName("BeaconMessage")
public class BeaconMessage extends AVObject implements Parcelable {
    private String msgTitle;
    private String msgDetail;
    private String msgUrl;
    private String msgType;

    public BeaconMessage() {
        super();
    }

    public BeaconMessage(Parcel in) {
        setObjectId(in.readString());
        setMsgTitle(in.readString());
        setMsgDetail(in.readString());
        setMsgUrl(in.readString());
        setMsgType(in.readString());
    }

    public String getMsgTitle() {
        return getString("msgTitle");
    }

    public void setMsgTitle(String msgTitle) {
        put("msgTitle", msgTitle);
    }

    public String getMsgDetail() {
        return getString("msgDetail");
    }

    public void setMsgDetail(String msgDetail) {
        put("msgDetail", msgDetail);
    }

    public String getMsgUrl() {
        return getString("msgUrl");
    }

    public void setMsgUrl(String msgUrl) {
        put("msgUrl", msgUrl);
    }

    public String getMsgType() {
        return getString("msgType");
    }

    public void setMsgType(String msgType) {
        put("msgType", msgType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getObjectId());
        dest.writeString(this.getMsgTitle());
        dest.writeString(this.getMsgDetail());
        dest.writeString(this.getMsgUrl());
        dest.writeString(this.getMsgType());
    }

    public static final Parcelable.Creator<BeaconMessage> CREATOR
            = new Parcelable.Creator<BeaconMessage>() {
        public BeaconMessage createFromParcel(Parcel in) {
            return new BeaconMessage(in);
        }

        public BeaconMessage[] newArray(int size) {
            return new BeaconMessage[size];
        }
    };
}
