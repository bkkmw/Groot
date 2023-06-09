package com.chocobi.groot.view.pot.model

import com.chocobi.groot.data.BasicResponse
import com.chocobi.groot.data.MsgResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PotService {
    @GET("/api/pots/{potId}")
    fun getPotDetail(
        @Path("potId") potId: Int
    ): Call<PotResponse>

    @GET("/api/pots")
    fun getPotList(
    ): Call<PotListResponse>

    @GET("/api/pots/archive")
    fun getPotArchive(
    ): Call<PotListResponse>

    @GET("/api/diaries/weekly")
    fun getDateDiary(
        @Query("date") date: String
    ): Call<DateDiaryResponse>

    @GET("/api/diaries/{potId}")
    fun requestPotDiary(
        @Path("potId") potId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Call<DiaryListResponse>

    @DELETE("/api/pots/{potId}")
    fun deletePot(
        @Path("potId") potId: Int
    ): Call<MsgResponse>

    @PUT("/api/pots/{potId}/status")
    fun gonePot(
        @Path("potId") potId: Int,
        @Body params: PotStatusRequest
    ): Call<MsgResponse>

    @Multipart
    @PUT("/api/pots/{potId}")
    fun changePotImg(
        @Path("potId") potId: Int,
        @Part("pot") metaData: PotNameRequest?,
        @Part filePart: MultipartBody.Part?
    ): Call<PotImgResponse>

    @Multipart
    @POST("/api/diaries")
    fun requestPostDiary(
        @Part("postData") metaData: DiaryRequest,
        @Part filePart: MultipartBody.Part?
    ): Call<BasicResponse>

    @Multipart
    @PUT("/api/diaries")
    fun requestEditDiary(
        @Part("postData") metaData: EditDiaryRequest,
        @Part filePart: MultipartBody.Part?
    ): Call<BasicResponse>


    @DELETE("/api/diaries/{diaryId}/{userPK}")
    fun requestDeleteDiary(
        @Path("diaryId") diaryId: Int?,
        @Path("userPK") userPK: Int,
        @Query("planId") planId: Int?
    ): Call<BasicResponse>

    @GET("/api/diaries/check/{potId}")
    fun requestDiaryCheckState(
        @Path("potId") potId: Int
    ): Call<DiaryCheckStatusResponse>
}

class PotNameRequest internal constructor(
    val potName: String?,
)

class PotStatusRequest internal constructor(
    val status: String
)