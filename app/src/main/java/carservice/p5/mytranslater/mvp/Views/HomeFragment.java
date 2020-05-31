package carservice.p5.mytranslater.mvp.Views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import carservice.p5.mytranslater.MyTranslateApplication;
import carservice.p5.mytranslater.R;
import carservice.p5.mytranslater.dagger.Components.DaggerPresenterComponent;
import carservice.p5.mytranslater.dagger.Components.PresenterComponent;
import carservice.p5.mytranslater.interfaces.HomeAction;
import carservice.p5.mytranslater.mvp.Presenters.HomePresenter;

public class HomeFragment extends Fragment implements HomeAction {

    private HomePresenter presenter;
    private TextView langIn,langTo,textViewTranslate;
    private ImageButton buttonRevers;
    private EditText editTextTranslate;
    private Context context;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         View root = inflater.inflate(R.layout.fragment_home, container, false);

         context = root.getContext();
         langIn = root.findViewById(R.id.langIn);
         langTo = root.findViewById(R.id.langTo);
         buttonRevers = root.findViewById(R.id.imageButton);
         editTextTranslate = root.findViewById(R.id.editTextTranslate);
         textViewTranslate = root.findViewById(R.id.text_home);


        init();

        return root;
    }

    /**
     * Инициализация презентера и дополнительных методов
     */
    private void init() {

        PresenterComponent component = DaggerPresenterComponent.builder()
                .myTranslateComponent(MyTranslateApplication.get(getActivity()).getApplicationComponent())
                .build();
        presenter = component.injectHomePresenter(component.getHomePresenter());
        presenter.inject(this);

        initListeners();
    }

    /**
     * Инициализация слушателей для компонентов UI
     */
    private void initListeners(){

        langIn.setOnClickListener(v->{
            presenter.showDialogListLanguage(HomePresenter.TranslateType.typeIn);
        });

        langTo.setOnClickListener(v->{
            presenter.showDialogListLanguage(HomePresenter.TranslateType.typeTo);
        });

        buttonRevers.setOnClickListener(v->{
            presenter.reversLanguage();
        });

    }

    @Override
    public Context getContext(){
        return context;
    }

    @Override
    public void setLangIn(String langIn) {
        this.langIn.setText(langIn);
    }

    @Override
    public void setLangTo(String langTo) {
        this.langTo.setText(langTo);
    }

    @Override
    public void setTextViewTranslate(String textTranslate) {
        this.textViewTranslate.setText(textTranslate);
    }

    @Override
    public String getTextViewTranslate() {
        return this.textViewTranslate.getText().toString();
    }

    @Override
    public EditText getEditTextTranslate() {
        return editTextTranslate;
    }

    @Override
    public void setEditTextTranslate(String textIn){
        this.editTextTranslate.setText(textIn);
    }

    @Override
    public void showDialogMenu(String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.select_language))
                .setItems(items, (dialog, id) -> {
                    dialog.dismiss();
                    presenter.selectedLanguage(id);
                });
        builder.create().show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(presenter!=null)
        presenter.onClear();
    }

    @Override
    public void onDestroy() {
        if(presenter!=null)
        presenter.onDispose();
        super.onDestroy();
    }
}
