package carservice.p5.mytranslater.dagger.Components;


import carservice.p5.mytranslater.dagger.Annotations.PresenterScope;
import carservice.p5.mytranslater.dagger.Modules.PresenterModule;
import carservice.p5.mytranslater.mvp.Presenters.HistoryPresenter;
import carservice.p5.mytranslater.mvp.Presenters.HomePresenter;
import dagger.Component;

@PresenterScope
@Component(modules = {PresenterModule.class}, dependencies = MyTranslateComponent.class)
public interface PresenterComponent {

    HomePresenter getHomePresenter();
    HomePresenter injectHomePresenter(HomePresenter presenter);

    HistoryPresenter getHistoryPresenter();
    HistoryPresenter injectHistoryPresenter(HistoryPresenter presenter);

}
