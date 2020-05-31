package carservice.p5.mytranslater.services.observables;

import android.database.Cursor;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import carservice.p5.mytranslater.mvp.Models.TranslateModel;
import carservice.p5.mytranslater.services.rest_service.ApiServiceNew;
import carservice.p5.mytranslater.services.rest_service.ApiServices;
import carservice.p5.mytranslater.services.sqlite.BaseTranslate;
import carservice.p5.mytranslater.services.sqlite.TableBase;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ActionsSynchronisation {

    private TableBase tableBase;

    public ActionsSynchronisation() {
    }

    public ActionsSynchronisation(TableBase tableBase) {
        this.tableBase = tableBase;
    }


    public Observable<List<TranslateModel>> getHistoryTranslate(String textSearch) {
        Cursor cursor = tableBase.getHistoryTranslate(textSearch);
        if (cursor == null) {
            return Completable.complete().toObservable();
        }

        return Observable.just(cursor)
                .flatMap(this::getHistoryList);

    }


    public Observable<JsonArray> getHistoryTranslateJson(String textSearch) {
        Cursor cursor = tableBase.getHistoryTranslate(textSearch);
        if (cursor == null) {
            return Completable.complete().toObservable();
        }

        return Observable.just(cursor)
                .flatMap(this::getHistoryListJson);

    }

    public Observable<TranslateModel> getHistoryById(int id) {
        Cursor cursor = tableBase.getHistoryById(id);
        if (cursor == null) {
            return Completable.complete().toObservable();
        }

        return Observable.just(cursor)
                .flatMap(this::getHistoryItem);

    }

    public Observable<JsonArray> getLanguagesDB() {
        Cursor cursor = tableBase.getLanguages();
        if (cursor == null) {
            return Completable.complete().toObservable();
        }

        return Observable.just(cursor)
                .flatMap(c ->
                        Observable.create(emitter ->
                        {
                            JsonArray ja = new JsonArray();
                            for (int i = 0; i < c.getCount(); i++) {
                                int indexCode = c.getColumnIndex(BaseTranslate.COLUMN_CODE);
                                int indexName = c.getColumnIndex(BaseTranslate.COLUMN_NAME);

                                JsonObject jo = new JsonObject();
                                jo.addProperty(c.getColumnName(indexCode), c.getString(indexCode));
                                jo.addProperty(c.getColumnName(indexName), c.getString(indexName));
                                ja.add(jo);

                                if (!c.isLast())
                                    c.moveToNext();
                            }
                            c.close();

                            emitter.onNext(ja);
                            emitter.onComplete();
                        }));

    }

    public Observable<JsonArray> getLanguagesNetwork() {

        return new ApiServiceNew()
                .getToken()
                .onErrorResumeNext(throwable ->{return Observable.empty(); })
                .flatMap(this::getTokenString)
                .filter(token -> !token.equals(""))
                .flatMap(this::getLanguagesNetwork)
                .filter(jo -> jo.has("languages"))
                .map(jo -> jo.get("languages").getAsJsonArray())
                .flatMap(ja -> Observable.create(emitter -> {
                            emitter.onNext(setDataLanguagesInDB(ja));
                            emitter.onComplete();
                        }));
    }


    public Single<JsonArray> getLanguages() {

        return Observable.concat(getLanguagesDB(),getLanguagesNetwork())
                .first(new JsonArray())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<JsonArray> getTranslateText(String text, String targetLanguage) {
        return new ApiServiceNew()
                .getToken()
                .onErrorResumeNext(throwable ->{return Observable.empty();})
                .flatMap(this::getTokenString)
                .filter(token -> !token.equals(""))
                .flatMap(token -> getTranslateTextNetwork(token, text, targetLanguage))
                .filter(jo -> jo.has("translations"))
                .map(jo -> jo.get("translations").getAsJsonArray());
    }

    private Observable<String> getTokenString(JsonObject data) {
        return Observable.create(emitter ->
        {
            if (data.has("iamToken")) {
                emitter.onNext(data.get("iamToken").getAsString());
            } else {
                emitter.onNext("");
            }
        });
    }

    private JsonArray setDataLanguagesInDB(@NonNull JsonArray ja) {
        JsonArray ja0 = new JsonArray();
        for (int i = 0; i < ja.size(); i++) {
            JsonObject jo = ja.get(i).getAsJsonObject();
            JsonElement je0 = jo.get(BaseTranslate.COLUMN_CODE);
            JsonElement je1 = jo.get(BaseTranslate.COLUMN_NAME);
            if (je0 != null && je1 != null) {
                String code = je0.getAsString();
                String name = je1.getAsString();
                tableBase.setLanguages(code, name);

                ja0.add(jo);
            }

        }
        return ja0;
    }

    private Observable<JsonArray> getHistoryListJson(Cursor c) {
        return Observable.create(emitter ->
        {
            JsonArray ja = new JsonArray();
            for (int i = 0; i < c.getCount(); i++) {
                int indexID = c.getColumnIndex(BaseTranslate.COLUMN_ID);
                int indexTIN = c.getColumnIndex(BaseTranslate.COLUMN_TEXT_IN);
                int indexTTO = c.getColumnIndex(BaseTranslate.COLUMN_TEXT_TO);
                int indexCIN = c.getColumnIndex(BaseTranslate.COLUMN_CODE_IN);
                int indexCTO = c.getColumnIndex(BaseTranslate.COLUMN_CODE_TO);

                JsonObject jo = new JsonObject();
                jo.addProperty(c.getColumnName(indexID), c.getInt(indexID));
                jo.addProperty(c.getColumnName(indexTIN), c.getString(indexTIN));
                jo.addProperty(c.getColumnName(indexTTO), c.getString(indexTTO));
                jo.addProperty(c.getColumnName(indexCIN), c.getString(indexCIN));
                jo.addProperty(c.getColumnName(indexCTO), c.getString(indexCTO));
                ja.add(jo);

                if (!c.isLast())
                    c.moveToNext();
            }
            c.close();

            emitter.onNext(ja);
            emitter.onComplete();
        });
    }

    private Observable<List<TranslateModel>> getHistoryList(Cursor c) {
        return Observable.create(emitter ->
        {
            List<TranslateModel> models = new ArrayList<>();
            for (int i = 0; i < c.getCount(); i++) {
                int indexID = c.getColumnIndex(BaseTranslate.COLUMN_ID);
                int indexTIN = c.getColumnIndex(BaseTranslate.COLUMN_TEXT_IN);
                int indexTTO = c.getColumnIndex(BaseTranslate.COLUMN_TEXT_TO);
                int indexCIN = c.getColumnIndex(BaseTranslate.COLUMN_CODE_IN);
                int indexCTO = c.getColumnIndex(BaseTranslate.COLUMN_CODE_TO);
                models.add(new TranslateModel(
                        c.getInt(indexID),
                        c.getString(indexTIN),
                        c.getString(indexTTO),
                        c.getString(indexCIN),
                        c.getString(indexCTO)));

                if (!c.isLast())
                    c.moveToNext();
            }
            c.close();

            emitter.onNext(models);
            emitter.onComplete();
        });
    }


    private Observable<TranslateModel> getHistoryItem(Cursor c) {
        return Observable.create(emitter ->
        {
            int indexID = c.getColumnIndex(BaseTranslate.COLUMN_ID);
            int indexTIN = c.getColumnIndex(BaseTranslate.COLUMN_TEXT_IN);
            int indexTTO = c.getColumnIndex(BaseTranslate.COLUMN_TEXT_TO);
            int indexCIN = c.getColumnIndex(BaseTranslate.COLUMN_CODE_IN);
            int indexCTO = c.getColumnIndex(BaseTranslate.COLUMN_CODE_TO);
            TranslateModel model = new TranslateModel(
                    c.getInt(indexID),
                    c.getString(indexTIN),
                    c.getString(indexTTO),
                    c.getString(indexCIN),
                    c.getString(indexCTO));

            c.close();
            emitter.onNext(model);
            emitter.onComplete();
        });
    }


    private Observable<JsonObject> getLanguagesNetwork(String token) {
        return new ApiServiceNew().getLanguages(token);
    }

    private Observable<JsonObject> getTranslateTextNetwork(String token, String text, String targetLanguage) {
        return new ApiServiceNew().getTranslateText(token, text, targetLanguage);
    }

}
