package carservice.p5.mytranslater.mvp.Views;

import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import carservice.p5.mytranslater.MainActivity;
import carservice.p5.mytranslater.MyTranslateApplication;
import carservice.p5.mytranslater.R;
import carservice.p5.mytranslater.adapters.TranslateListAdapter;
import carservice.p5.mytranslater.dagger.Components.DaggerPresenterComponent;
import carservice.p5.mytranslater.dagger.Components.PresenterComponent;
import carservice.p5.mytranslater.interfaces.HistoryAction;
import carservice.p5.mytranslater.interfaces.PageCallback;
import carservice.p5.mytranslater.mvp.Models.TranslateModel;
import carservice.p5.mytranslater.mvp.Presenters.HistoryPresenter;


public class HistoryFragment extends Fragment implements HistoryAction {

    private HistoryPresenter presenter;

    private Context context;
    private EditText searchEditText;
    private TextView alertText;
    private RecyclerView listPost;
    private TranslateListAdapter translateAdapter;
    private PageCallback mCallbacks;
    private Menu currentMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (!(getActivity() instanceof MainActivity) || !(getActivity() instanceof PageCallback)) {
            throw new ClassCastException("Activity must implement PageCallback");
        }

        mCallbacks = (PageCallback) getActivity();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = root.getContext();

        searchEditText = root.findViewById(R.id.editTextSearch);
        alertText = root.findViewById(R.id.text_dashboard);
        listPost = (RecyclerView) root.findViewById(R.id.listItems);

        init();

        return root;
    }

    /**
     * Инициализация презентера и дополнительных методов
     */
    private void init() {
        showAlert();

        listPost.setLayoutManager(new LinearLayoutManager(context));
        translateAdapter = new TranslateListAdapter(context);
        translateAdapter.setListenerPost(id -> {
            presenter.setSelected(id);
            mCallbacks.onPreview();
        });

        listPost.setAdapter(translateAdapter);

        PresenterComponent component = DaggerPresenterComponent.builder()
                .myTranslateComponent(MyTranslateApplication.get(getActivity()).getApplicationComponent())
                .build();
        presenter = component.injectHistoryPresenter(component.getHistoryPresenter());
        presenter.inject(this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
         currentMenu = menu;
        if(alertText.isShown())
            menu.findItem(R.id.action_delete).setVisible(false);

        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       // int id = item.getItemId();
        presenter.dropHistory();
        currentMenu.findItem(R.id.action_delete).setVisible(false);

        return super.onOptionsItemSelected(item);
    }



    @Override
    public Context getContext() {
       return context;
    }

    @Override
    public void showAlert()
    {
        alertText.setText(getString(R.string.no_history));
        alertText.setVisibility(View.VISIBLE);
        listPost.setVisibility(View.GONE);
        searchEditText.setVisibility(View.GONE);
    }

    @Override
    public void setDataInAdapter(List<TranslateModel> models) {
        alertText.setVisibility(View.GONE);
        listPost.setVisibility(View.VISIBLE);
        searchEditText.setVisibility(View.VISIBLE);

        translateAdapter.setData(models);

        if(currentMenu!=null)
        currentMenu.findItem(R.id.action_delete).setVisible(true);
    }

    @Override
    public EditText getEditTextSearch()
    {
        return searchEditText;
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
