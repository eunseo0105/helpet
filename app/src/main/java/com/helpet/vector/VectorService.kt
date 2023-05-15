package com.helpet.vector

import android.content.IntentFilter.create
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.net.URI.create

//강아지 눈진단
interface VectorService {
    @Multipart
    @POST("/api/dog/eye")
    fun vectorResult(
        @Part postImg: MultipartBody.Part,
//        @Part("date") date: RequestBody,
//        @Part("petname") petname: RequestBody,
//        @Part("username") username: RequestBody,

    ): Call<ResponseDto>
}
//고양이 눈 진단
interface catVectorService {
    @Multipart
    @POST("/api/cat/eye")
    fun catvectorResult(
        @Part postImg: MultipartBody.Part,
//        @Part("date") date: RequestBody,
//        @Part("petname") petname: RequestBody,
//        @Part("username") username: RequestBody,

    ): Call<ResponseDto>
}
//강아지 피부 진단
interface dogSkinVectService {
    @Multipart
    @POST("/api/dog/skin")
    fun dogskinvectResult(
        @Part postImg: MultipartBody.Part,
//        @Part("date") date: RequestBody,
//        @Part("petname") petname: RequestBody,
//        @Part("username") username: RequestBody,

    ): Call<ResponseDto>
}

//반려동물 등록할때 보내는 요청
interface PetService {
    @FormUrlEncoded
    @POST("/api/pet/register")
    fun PetRegister(
//        @Part petImg : MultipartBody.Part,
//        @Field ("petImg") petImg: String,
        @Field("userId") userId: String,
        @Field("petSpecies") petSpecies : String,
        @Field("petName") petName: String,
        @Field("petAge") petAge: String,
        @Field("petBirth") petBirth: String,
        @Field("petGender") petGender: String
    ):Call<PetResponseDto>
}

//    status: 성공 실패 여부
//    petImg: 반려동물 사진
//    petSpecies: 반려동물 종(강아지/ 고양이)
//    petName : 이름
//    petAge : 나이
//    petBirth : 생일 (YYYYMMDD형식)
//    petGender: 남자 여자

//사용자가 가지고있는 반려동물 리스트 받아오는 요청
interface GetPetService{
    @FormUrlEncoded
//    @POST("/api/:userId")
    @POST("/api/pet/test")

    fun getPetRegister(
    @Field("userId") userId: String
    ):Call<petListResponseDTO>

}
