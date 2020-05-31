package carservice.p5.mytranslater.interfaces;

import android.content.Context;
import android.database.Cursor;
import android.widget.BaseAdapter;
import android.widget.EditText;

public interface HomeAction {
    Context getContext();

    void setLangIn(String langIn);
    void setLangTo(String langTo);
    void setTextViewTranslate(String textTranslate);
    String getTextViewTranslate();
    EditText getEditTextTranslate();
    void setEditTextTranslate(String textIn);
    void showDialogMenu(String[] items);

}
