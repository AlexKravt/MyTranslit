package carservice.p5.mytranslater.mvp.Presenters;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import carservice.p5.mytranslater.interfaces.HomeAction;
import carservice.p5.mytranslater.mvp.Models.LanguageModel;
import carservice.p5.mytranslater.mvp.Models.TranslateModel;
import carservice.p5.mytranslater.services.GlobalConstant;
import carservice.p5.mytranslater.services.PreferenceUser;
import carservice.p5.mytranslater.services.observables.ActionsSynchronisation;
import carservice.p5.mytranslater.services.sqlite.TableBase;
import carservice.p5.mytranslater.utilites.RXEventEditText;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter {

    @Inject
     TableBase table;

    @Inject
     PreferenceUser preferenceUser;

    private List<LanguageModel> languageModel;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private HomeAction homeAction;
    private TranslateType typeLang;
    private String codeIn="ru", codeTo="en";


    public enum TranslateType {
        typeIn,
        typeTo
    }

    public HomePresenter() {
    }

    /**
     * Инициализация презентера и его зависимомтей
     * @param homeAction HomeAction
     */
    public void inject(@NonNull HomeAction homeAction) {
        this.homeAction = homeAction;

        SharedPreferences preferences = preferenceUser.getUserOptions();
        if (!preferences.contains(GlobalConstant.APP_PREFERENCES_TABLE)) {
            //Создаём таблицы и заполняем данными
            table.createTables();
            preferences.edit().putBoolean(GlobalConstant.APP_PREFERENCES_TABLE, true).apply();
        }

        getPreferenceCode();
        getDataLanguages();
        initListenerEditTextTranslate();
    }

    /**
     * Получение списка языков и сохранение в базу данных
     */
    private void getDataLanguages() {
        Disposable disposable = new ActionsSynchronisation(table)
                .getLanguages()
                .filter(ja-> ja.size() > 0)
                .subscribe(ja -> {
                    setDataLanguages(ja);
                    Log.d("Languages ", ja.toString());
                }, error -> {
                    error.printStackTrace();
                    Log.d("Error", Objects.requireNonNull(error.getMessage()));
                });

        disposables.add(disposable);
    }

    /**
     * Подписываемся на слушателя ввода текста
     */
    private void initListenerEditTextTranslate() {
        Disposable disposable = RXEventEditText.getTextWatcherObservable(homeAction.getEditTextTranslate())
                .debounce(1, TimeUnit.SECONDS)
                .filter(txt -> !txt.isEmpty())
                // .filter(txt -> txt.length() >= 10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getDataTranslate);

        disposables.add(disposable);
    }

    /**
     * Получение перевода
     */
    private void getDataTranslate(String text) {

        if(text==null || text.isEmpty())
            return;

        Disposable disposable = new ActionsSynchronisation()
                .getTranslateText(text, codeTo)
                .filter(ja->ja.size() > 0)
                .map(ja->ja.get(0).getAsJsonObject())
                .filter(jo->jo.has("text"))
                .map(jo->jo.get("text").getAsString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(translateTo -> {
                    setTranslateText(text, translateTo);
                    Log.d("Превод текста ", translateTo);
                }, error -> {
                    error.printStackTrace();
                    Log.d("Error", Objects.requireNonNull(error.getMessage()));
                });

        disposables.add(disposable);
    }

    /**
     * Метод для получения записи по id из истории перевода
     * @param id int
     */
    private void getDataFromHistory(int id){
        Disposable disposable = new ActionsSynchronisation(table)
                .getHistoryById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setDataItemFromHistory, error -> {
                    error.printStackTrace();
                    Log.d("Error", Objects.requireNonNull(error.getMessage()));
                });

        disposables.add(disposable);
    }

    /**
     * Метод для заполнения модели данных о языках
     * @param ja JsonArray
     */
    private void setDataLanguages(JsonArray ja) {
        languageModel = new ArrayList<>();
        for (JsonElement je : ja) {

            JsonObject jo = je.getAsJsonObject();
            String code = jo.get("code").getAsString();
            String name = jo.get("name").getAsString();

            languageModel.add(new LanguageModel(code, name));

            if (codeIn != null && codeIn.equals(code))
                homeAction.setLangIn(name);

            if (codeTo != null && codeTo.equals(code))
                homeAction.setLangTo(name);

        }
    }


    /**
     * Метод выводит диалог со списком языков
     * @param type DialogType
     */
    public void showDialogListLanguage(TranslateType type) {

        if(languageModel==null)
            return;

        typeLang = type;

        String[] items = new String[languageModel.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = languageModel.get(i).getName();
        }

        homeAction.showDialogMenu(items);
    }

    /**
     * Метод меняет местами язык для перевода
     */
    public void reversLanguage() {

        if(languageModel==null)
            return;

        int count = languageModel.size();
        String codeIn0 = null;
        String codeTo0 = null;
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String name = languageModel.get(i).getName();
                String code = languageModel.get(i).getCode();

                if (code.equals(codeIn)) {
                    codeTo0 = code;
                    homeAction.setLangTo(name);
                }

                if (code.equals(codeTo)) {
                    codeIn0 = code;
                    homeAction.setLangIn(name);

                }
            }

            codeIn = codeIn0;
            codeTo = codeTo0;
            setPreferenceCode(TranslateType.typeIn);
            setPreferenceCode(TranslateType.typeTo);
            String text = homeAction.getTextViewTranslate();
            homeAction.setEditTextTranslate(text);
            getDataTranslate(homeAction.getTextViewTranslate());
        }
    }

    /**
     * Метод показывает данные из истории
     */
    private void setDataItemFromHistory(TranslateModel model) {

        codeIn = model.getLangCodeIn();
        codeTo = model.getLangCodeTo();

        homeAction.setEditTextTranslate(model.getTextIn());
        homeAction.setTextViewTranslate(model.getTextTo());
    }

    /**
     * Метод для выбора языка и показ его на экране пользователя
     * @param index int
     */
    public void selectedLanguage(int index) {

        String name = languageModel.get(index).getName();
        String code = languageModel.get(index).getCode();

        switch (typeLang) {
            case typeIn:
                codeIn = code;
                homeAction.setLangIn(name);
                break;
            case typeTo:
                codeTo = code;
                homeAction.setLangTo(name);
                break;
        }

        setPreferenceCode(typeLang);
    }

    /**
     * Метод для сохранения настроек пользователя
     * @param type TranslateType
     */
    private void setPreferenceCode(TranslateType type) {
        SharedPreferences preferences = preferenceUser.getUserOptions();
        switch (type) {
            case typeIn:
                preferences.edit().putString(GlobalConstant.CODE_IN, codeIn).apply();
                break;
            case typeTo:
                preferences.edit().putString(GlobalConstant.CODE_TO, codeTo).apply();
                break;
        }
    }

    /**
     * Метод для получения последних настроек пользователя
     */
    private void getPreferenceCode() {
        SharedPreferences preferences = preferenceUser.getUserOptions();
        codeIn = preferences.getString(GlobalConstant.CODE_IN, codeIn);
        codeTo = preferences.getString(GlobalConstant.CODE_TO, codeTo);
        int selectedId = preferences.getInt(GlobalConstant.ID_SELECTED,-1);
        if(selectedId > 0)
        {
            preferences.edit().remove(GlobalConstant.ID_SELECTED).apply();

            getDataFromHistory(selectedId);
        }
    }

    /**
     * Метод получает данные перевода текста записывает их в базу данных и
     * выводит перевод пользователю
     * @param translateIn String
     * @param translateTo String
     */
    private void setTranslateText(String translateIn, String translateTo) {
        table.setData(new TranslateModel(0,translateIn,translateTo,codeIn,codeTo));
        homeAction.setTextViewTranslate(translateTo);
    }

    /**
     * Метод для удоления всех подписчиков
     */
    public void onDispose() {
        disposables.dispose();
    }

    /**
     * Мтод очищает подписчики
     */
    public void onClear() {
        disposables.clear();
    }

}
