package carservice.p5.mytranslater.mvp.Presenters;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import carservice.p5.mytranslater.interfaces.HistoryAction;
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


public class HistoryPresenter {

    @Inject
     TableBase table;

    @Inject
     PreferenceUser preferenceUser;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private HistoryAction action;

    public HistoryPresenter(){}

    public void inject(@NonNull HistoryAction homeAction) {
        this.action = homeAction;
        initListenerEditTextSearch();
        getHistoryListTranslate(GlobalConstant.EMPTY);
    }

    /**
     * Подписываемся на слушателя ввода текста
     */
    private void initListenerEditTextSearch() {
        Disposable disposable = RXEventEditText.getTextWatcherObservable(action.getEditTextSearch())
                .debounce(1, TimeUnit.SECONDS)
                //.filter(txt -> !txt.isEmpty())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getHistoryListTranslate);

        disposables.add(disposable);
    }

    /**
     * Получение списка истории перевода
     */
    private void getHistoryListTranslate(String text)
    {
        Disposable disposable = new ActionsSynchronisation(table)
                .getHistoryTranslate(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setDataInAdapter, error -> {
                    error.printStackTrace();
                    Log.d("Error", Objects.requireNonNull(error.getMessage()));
                });

        disposables.add(disposable);
    }

    /**
     * Мтод удоляет данные истории
     */
    public void dropHistory(){
       table.dropHistory();
       action.showAlert();
    }

    /**
     * Мтод сохраняет выбор пользователя из истории
     * @param id int
     */
    public void setSelected(int id){
        preferenceUser.getUserOptions().edit().putInt(GlobalConstant.ID_SELECTED,id).apply();
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

    /**
     * Метод для вставки данных в адаптер
     * @param models
     */
    private void setDataInAdapter(List<TranslateModel> models) {
        action.setDataInAdapter(models);
    }
}
