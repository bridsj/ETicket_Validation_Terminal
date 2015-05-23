package com.ticket.validation.terminal.model;

/**
 * Created by dengshengjin on 15/5/23.
 */
public class UserModel extends BaseModel {
    public String mUrl;
    public String mUser;
    public String mPassword;

    @Override
    public String toString() {
        return "UserModel{" +
                "mUrl='" + mUrl + '\'' +
                ", mUser='" + mUser + '\'' +
                ", mPassword='" + mPassword + '\'' +
                '}';
    }
}
