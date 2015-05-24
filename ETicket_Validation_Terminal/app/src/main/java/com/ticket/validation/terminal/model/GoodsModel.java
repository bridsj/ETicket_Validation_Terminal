package com.ticket.validation.terminal.model;

/**
 * Created by dengshengjin on 15/5/24.
 */
public class GoodsModel extends BaseModel {
    public String mGoodsName;//商品名称
    public String mEndTime;//有效期
    public String mActTime;
    public String mStartTime;
    public String mPiececode;
    public String mInfo;
    public String mStorageStatus;
    public String mUserName;//姓名
    public String mIdCard;//身份证
    public String mOrderCommments;//备注
    public String mSoldGoodsId;
    public String mExchageFunc;
    public int mStatus;
    public boolean mIsSelected;
    public int mCount;

    @Override
    public String toString() {
        return "GoodsModel{" +
                "mGoodsName='" + mGoodsName + '\'' +
                ", mEndTime='" + mEndTime + '\'' +
                ", mActTime='" + mActTime + '\'' +
                ", mStartTime='" + mStartTime + '\'' +
                ", mPiececode='" + mPiececode + '\'' +
                ", mInfo='" + mInfo + '\'' +
                ", mStorageStatus='" + mStorageStatus + '\'' +
                ", mUserName='" + mUserName + '\'' +
                ", mIdCard='" + mIdCard + '\'' +
                ", mOrderCommments='" + mOrderCommments + '\'' +
                ", mSoldGoodsId='" + mSoldGoodsId + '\'' +
                ", mExchageFunc='" + mExchageFunc + '\'' +
                ", mStatus=" + mStatus +
                ", mIsSelected=" + mIsSelected +
                '}';
    }



}
