package com.example.orangetrainingcenterandroid.data.training.remote.api



import com.example.orangetrainingcenterandroid.data.training.remote.dto.training.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalDate
import java.util.*

interface TrainingApi {

    @GET("mobile/training/accepted")
    suspend fun fetchAvailableTrainings(
        @Header("Authorization") token: String,
        @Query("searchQuery") searchQuery: String = "",
        @Query("themeQuery") themeQuery: String = "",
        @Query("startDateQuery") startDateQuery: LocalDate? = null,
        @Query("endDateQuery") endDateQuery: LocalDate? = null,
        @Query("cursor") cursor: String? = null
    ): Response<TrainingResponse>


    @GET("android/home/section")
    suspend fun getTrainingHomeSections(@Header("Authorization") token: String): Response<TrainingHomeSectionsResponse>

    @GET("android/detail/training/{id}")
    suspend fun getTrainingDetail(@Path("id") id: String, @Header("Authorization") token: String): Response<TrainingDetailsResponse>

    @GET("current/training")
    suspend fun getCurrentTraining( @Header("Authorization") token: String): Response<TrainingDetailsResponse>


    @GET("verify/participation/{id}")
    suspend fun verifyParticipation(@Path("id") id: String, @Header("Authorization") token: String):Response<ResponseBody>
    @PUT("mobile/participate/{id}")
    suspend fun submitRequestReservation(@Header("Authorization") token: String, @Path("id") id: String) : RequestParticipationResponse

    //theme
    @GET("themes")
    suspend fun getAllThemes(@Header("Authorization") token: String) : Response<ThemeResponse>

    @GET("trainer/profile/{id}")
    suspend fun getTrainerProfile(@Header("Authorization") token: String,@Path("id") id: String) : Response<TrainerProfileResponse>

    @POST("presence/confirm-attendance/{id}")
    suspend fun confirmAttendance(@Path("id") id: String, @Header("Authorization") token: String):Response<PresenceResponse>
    @GET("android/presence/state/{id}")
    suspend fun getPresenceState(@Path("id") id: String, @Header("Authorization") token: String):Response<PresenceStateResponse>

}