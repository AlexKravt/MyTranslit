package carservice.p5.mytranslater.services.rest_service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InterruptedIOException;

import carservice.p5.mytranslater.services.GlobalConstant;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpService extends Api {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;

    public OkHttpService() {
        client = new OkHttpClient();
    }

    @Override
    public Observable<JsonObject> getToken(String url, String params) {
        return Observable.fromCallable(() -> setPost(url, params))
                .filter(js -> !js.equals(""))
                .map(js -> new Gson().fromJson(js, JsonObject.class));
    }

    @Override
    public Observable<JsonObject> getLanguages(String url, String token, String params) {
        return Observable.fromCallable(() -> setPostAuth(url, token, params))
                .filter(js -> !js.equals(""))
                .map(js -> new Gson().fromJson(js, JsonObject.class));
    }

    @Override
    public Observable<JsonObject> getTextTranslate(String url, String token, String params) {
        return Observable.fromCallable(() -> setPostAuth(url, token, params))
                .filter(js -> !js.equals(""))
                .map(js -> new Gson().fromJson(js, JsonObject.class));
    }


    /**
     * Метод для POST запроса без авторизации с передачей заголовка type/json
     *
     * @param url  String
     * @param json String
     * @return String
     * @throws IOException error
     */
    private String setPost(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (InterruptedIOException ex) {
            ex.printStackTrace();
        }

        return GlobalConstant.EMPTY;
    }

    /**
     * Метод для POST запроса c авторизацией и передачей заголовка type/json
     *
     * @param url   String
     * @param token String
     * @param json  String
     * @return String
     * @throws IOException error
     */
    private String setPostAuth(String url, String token, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new TokenAuthInterceptor(token));
        OkHttpClient client = builder.build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (InterruptedIOException ex) {
            ex.printStackTrace();
        }

        return GlobalConstant.EMPTY;
    }

}
