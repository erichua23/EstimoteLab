package org.newbees.estimotelab.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by erichua on 6/14/15.
 */
@AVClassName("CheckIn")
public class CheckIn extends AVObject{
    private String message;
    private String userName;

    public String getMessageId() {
        return getString("message");
    }

    public void setMessageId(String messageId) {
        put("message", messageId);
    }

    public String getUserName() {
        return getString("userName");
    }

    public void setUserName(String userName) {
        put("userName", userName);
    }
}

