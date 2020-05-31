package carservice.p5.mytranslater.dagger.Modules;

import carservice.p5.mytranslater.dagger.Annotations.PresenterScope;
import carservice.p5.mytranslater.mvp.Presenters.HistoryPresenter;
import carservice.p5.mytranslater.mvp.Presenters.HomePresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule{

    @PresenterScope
    @Provides
    public HomePresenter presenterHome(){
        return new HomePresenter();
    }

    @PresenterScope
    @Provides
    public HistoryPresenter presenterHistory(){
        return new HistoryPresenter();
    }
}