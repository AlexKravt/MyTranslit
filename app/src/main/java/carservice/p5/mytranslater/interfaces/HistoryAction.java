package carservice.p5.mytranslater.interfaces;

import android.content.Context;
import android.widget.EditText;

import java.util.List;

import carservice.p5.mytranslater.mvp.Models.TranslateModel;

public interface HistoryAction {
    Context getContext();
    void showAlert();
    void setDataInAdapter(List<TranslateModel> models);
    EditText getEditTextSearch();
}
