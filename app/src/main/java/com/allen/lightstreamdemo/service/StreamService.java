package com.allen.lightstreamdemo.service;

import com.allen.lightstreamdemo.bean.Session;
import com.allen.lightstreamdemo.bean.UserInfo;
import com.allen.lightstreamdemo.bean.WatchList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by: allen on 16/9/1.
 */

public interface StreamService {
    //@POST(apiURL)
//void methodName(
//        @Header(HeadersContract.HEADER_AUTHONRIZATION) String token,
//        @Header(HeadersContract.HEADER_CLIENT_ID) String token,
//        @Body TypedInput body,
//        Callback<String> callback);
//    @Headers({"Cache-Control: max-age=640000", "User-Agent: My-App-Name"})

    //        @Header(HeadersContract.HEADER_CLIENT_ID) String token
    @POST("gateway/deal/session")
    Call<Session> logging(@Header("VERSION") String vesion, @Header("X-IG-API-KEY") String token, @Body UserInfo userInfo);
    @GET("gateway/deal/watchlists")
    Observable<WatchList> watchlists( @Header("X-IG-API-KEY") String key, @Header("CST") String CST, @Header("X-SECURITY-TOKEN") String token);
    @GET("gateway/deal/accounts")
    Observable<WatchList> accounts( @Header("X-IG-API-KEY") String key, @Header("CST") String CST, @Header("X-SECURITY-TOKEN") String token);
    @GET("gateway/deal/history/activity")
    Observable<WatchList> activity(@Header("VERSION") String vesion, @Header("X-IG-API-KEY") String key, @Header("CST") String CST, @Header("X-SECURITY-TOKEN") String token, @Query("from") String time);
//    http://blog.csdn.net/jdsjlzx/article/details/52415615  RxJava 合并组合两个（或多个）Observable数据源

}
