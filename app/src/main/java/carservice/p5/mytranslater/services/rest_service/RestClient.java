package carservice.p5.mytranslater.services.rest_service;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

public class RestClient {
    private  OkHttpClient.Builder builder;
    public RestClient() {
        builder = new OkHttpClient.Builder();

    }

    public RestClient setToken(String token){
        builder.addInterceptor(new TokenAuthInterceptor(token));
        return this;
    }

    public RestClient headerTypeJson(){
        builder.addInterceptor(new HeaderJsonType());
        builder.addInterceptor(new RecievedCookiesInterceptor());
        return this;
    }

    public RestClient setConnectTimeout(int timeout) {
        builder.connectTimeout(timeout, TimeUnit.SECONDS);
        return this;
    }

    public RestClient setWriteTimeout(int timeout) {
        builder.writeTimeout(timeout, TimeUnit.SECONDS);
        return this;
    }

    public RestClient setReadTimeout(int timeout) {
        builder.readTimeout(timeout, TimeUnit.SECONDS);
        return this;
    }

    public OkHttpClient getClient() {
        return  builder.build();
    }
}
