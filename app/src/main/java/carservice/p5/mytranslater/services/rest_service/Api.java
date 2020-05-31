package carservice.p5.mytranslater.services.rest_service;

import com.google.gson.JsonObject;

import io.reactivex.Observable;

/**
 * Абстрактный клас для реализации методов
 */
public abstract class Api {

    /**
     * Метод получения токена для доступа к API
     * @param url String
     * @param params String
     * @return  Observable<JsonObject>
     */
    public abstract Observable<JsonObject> getToken(String url,String params);

    /**
     * Метод получения списка поддерживаемых языков
     * @param url String
     * @param token String
     * @param params String
     * @return bservable<JsonObject>
     */
    public abstract Observable<JsonObject> getLanguages(String url, String token,String params);

    /**
     * Метод получения перевода текста
     * @param url String
     * @param token String
     * @param params String
     * @return Observable<JsonObject>
     */
    public abstract Observable<JsonObject> getTextTranslate(String url, String token,String params);
}
