package carservice.p5.mytranslater.services.rest_service;

import com.google.gson.JsonObject;

import carservice.p5.mytranslater.BuildConfig;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService extends Api{

    private final int CONNECT_TIMEOUT = 240;
    private final int WRITE_TIMEOUT = 240;
    private final int READ_TIMEOUT = 240;
    private String token;

    public RetrofitService(){}

    public Retrofit getServices(String url){
        OkHttpClient client =  new RestClient().getClient();
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit  getTokenService(String url){

        OkHttpClient client =  new RestClient()
                .headerTypeJson()
                .getClient();

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Retrofit  getRXServices(String url, int connectTimeout, int writeTimeout, int readTimeout){

        OkHttpClient client =  new RestClient()
                .setToken(token)
                .setConnectTimeout(connectTimeout)
                .setWriteTimeout(writeTimeout)
                .setReadTimeout(readTimeout)
                .getClient();

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }



    public Retrofit  getRXServices(String url){
        return getRXServices(url,CONNECT_TIMEOUT,WRITE_TIMEOUT,READ_TIMEOUT);
    }

    private void setToken(String token){
        this.token = token;
    }


    @Override
    public Observable<JsonObject> getToken(String url, String params) {
        ApiRest.AuthService restApi =  getTokenService(BuildConfig.URL_GET_TOKEN).create(ApiRest.AuthService.class);
        return restApi.getToken(params);
    }

    @Override
    public Observable<JsonObject> getLanguages(String url, String token, String params) {
        setToken(token);
        ApiRest.Languages restApi =  getRXServices(url).create(ApiRest.Languages.class);
        return restApi.getLanguages(params);
    }

    @Override
    public Observable<JsonObject> getTextTranslate(String url, String token, String params) {
        setToken(token);
        ApiRest.Languages restApi =  getRXServices(url).create(ApiRest.Languages.class);
        return restApi.getTranslate(params);
    }
}
