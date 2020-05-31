package carservice.p5.mytranslater.services.rest_service;

import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public class ApiRest {

    public interface YandexApi{
        @GET("1.x/?format=json")
        Observable<JsonObject> getCoordinates(@Query("geocode") String address, @Query("results") int count);
    }

    public interface AuthService{
        @FormUrlEncoded
        @POST("tokens")
        Observable<JsonObject> getToken(@FieldMap String fields);

    }


    public interface Languages{
        @FormUrlEncoded
        @POST("languages")
        Observable<JsonObject> getLanguages(@FieldMap String fields);

        @FormUrlEncoded
        @POST("translate")
        Observable<JsonObject> getTranslate(@FieldMap String fields);

    }
}
