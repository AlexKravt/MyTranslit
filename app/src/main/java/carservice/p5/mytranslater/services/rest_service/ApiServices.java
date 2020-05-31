package carservice.p5.mytranslater.services.rest_service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import carservice.p5.mytranslater.BuildConfig;
import io.reactivex.Observable;

public class ApiServices extends RetrofitService{

    public ApiServices(){}

    /**
     * Метод получения токена для доступа к API
     * @return Observable<JsonObject>
     */
    public Observable<JsonObject> getToken() {
        JsonObject jo = new JsonObject();
        jo.addProperty("yandexPassportOauthToken", BuildConfig.KEY_API);
        String url = BuildConfig.URL_GET_TOKEN;
        return getToken(url,jo.toString());
    }


    /**
     * Метод получения списка поддерживаемых языков
     * @return Observable<JsonObject>
     */
    public Observable<JsonObject> getLanguages(String token) {

        JsonObject jo = new JsonObject();
        jo.addProperty("folderId", BuildConfig.FOLDER_ID);
        String url = BuildConfig.URL_API;
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
        String url = BuildConfig.URL_API;

        return getTextTranslate(url,token,jo.toString());
    }



}
