package org.newbees.estimotelab.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by erichua on 6/13/15.
 */
@AVClassName("Goods")
public class BeaconMessage extends AVObject {
    private String msgTitle;
    private String msgDetail;
    private String msgUrl;

    private String msgType;

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
}
