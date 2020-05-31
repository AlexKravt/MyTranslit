package carservice.p5.mytranslater.services.rest_service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import carservice.p5.mytranslater.BuildConfig;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ApiServiceNew extends OkHttpService{

    public ApiServiceNew() {}

    /**
     * Метод получения токена для доступа к API
     * @return Observable<JsonObject>
     */
    public Observable<JsonObject> getToken() {
        JsonObject jo = new JsonObject();
        jo.addProperty("yandexPassportOauthToken", BuildConfig.KEY_API);
        String url = BuildConfig.URL_GET_TOKEN + "tokens";
        return getToken(url,jo.toString());
    }


    /**
     * Метод получения списка поддерживаемых языков
     * @return Observable<JsonObject>
     */
    public Observable<JsonObject> getLanguages(String token) {

        JsonObject jo = new JsonObject();
        jo.addProperty("folderId", BuildConfig.FOLDER_ID);
        String url = BuildConfig.URL_API + "languages";
        return getLanguages(url,token,jo.toString());
    }

    /**
     * Метод получения перевода текста
     * @param token String
     * @param text String
     * @param targetLanguage String
     * @return Observable<JsonObject>
     */
    public Observable<JsonObject> getTranslateText(String token, String text, String targetLanguage) {

        JsonArray jaText = new JsonArray();
        jaText.add(text);
        JsonObject jo = new JsonObject();
        jo.addProperty("folderId", BuildConfig.FOLDER_ID);
        jo.add("texts", jaText);
        jo.addProperty("targetLanguageCode", targetLanguage);
        String url = BuildConfig.URL_API + "translate";

        return getTextTranslate(url,token,jo.toString());
    }



}
