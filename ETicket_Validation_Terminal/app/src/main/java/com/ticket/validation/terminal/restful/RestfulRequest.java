package com.ticket.validation.terminal.restful;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface RestfulRequest {

    //登录系统
    @GET("/index.php/admin/index/loginCheck")
    void login(@Query("user") String user, @Query("password") String password, Callback<JSONObject> callback);

    //保活
    @GET("/")
    void activate(@Query("sessionid") String sessionid, Callback<JSONObject> callback);

    //获取用户信息
    @GET("/index.php/mobilesite/mobile/UpdateConfigJson")
    void updateConfigJson(@Query("sessionid") String sessionid, Callback<JSONObject> callback);

    //通过id或二维码 查询订单详情
    @GET("/index.php/mobilesite/mobile/CaptchaJsonV2")
    void queryOrderInfo(@Query("credenceno") String credenceno, @Query("sessionid") String sessionid, Callback<JSONObject> callback);

    //核销协议
    @GET("/index.php/mobilesite/mobile/{exchangefunc}")
    void exchangefunc(@Path("exchangefunc") String exchangefunc,
                      @Query("outid") String outid,
                      @Query("soldgoodsID") String soldgoodsID,
                      @Query("usedcount") int usedcount,
                      @Query("sessionid") String sessionid,
                      @Query("retry") int retry,
                      Callback<JSONObject> callback);

    //重新打印协议
    @GET("/index.php/mobilesite/mobile/reprintinfoJsonV2")
    void reprint(@Query("sessionid") String sessionid, Callback<JSONObject> callback);

    //统计报表协议
    @GET("/index.php/mobilesite/mobile/dailyreportJson")
    void dailyreportJson(@Query("sessionid") String sessionid, Callback<JSONObject> callback);
}