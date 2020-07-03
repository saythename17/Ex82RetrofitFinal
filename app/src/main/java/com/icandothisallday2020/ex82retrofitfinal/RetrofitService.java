package com.icandothisallday2020.ex82retrofitfinal;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface RetrofitService {

    //여러개의 data 와 file 동시에 전송
    @Multipart
    @POST ("/Retrofit/insertDB.php")
    Call<String> postDataToBoard(@PartMap Map<String,String> dataPart,
                                 @Part MultipartBody.Part filePart);
                         //                └(file(body) + 식별자 + 파일명)을 가지고 있음

    //서버에서 json 데이터를 읽어와서 GSON 라이브러리를 통해
    //자바객체로 응답결과를 리턴해주는 추상 메소드
    @GET("/Retrofit/loadDB.php")
    Call<ArrayList<BoardItem>> loadDataFromBoard();

    //토글아이콘버튼 업데이트 해주는 추상메소드
    // [ @Body : 전달받은 객체를 서버에 json 으로 변환하여 전달]
    @PUT("/Retrofit/{filename}")
    Call<BoardItem> updateData(@Path("filename") String filename,
                               @Body BoardItem item);
}
