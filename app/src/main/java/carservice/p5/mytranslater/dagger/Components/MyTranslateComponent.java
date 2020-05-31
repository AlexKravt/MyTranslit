package carservice.p5.mytranslater.dagger.Components;


import carservice.p5.mytranslater.dagger.Annotations.MyTranslateApplicationScope;
import carservice.p5.mytranslater.dagger.Modules.DBModule;
import carservice.p5.mytranslater.dagger.Modules.PreferenceUserModule;
import carservice.p5.mytranslater.services.PreferenceUser;
import carservice.p5.mytranslater.services.sqlite.TableBase;
import dagger.Component;

@MyTranslateApplicationScope
@Component(modules = {DBModule.class, PreferenceUserModule.class})
public interface MyTranslateComponent {
    TableBase getDBService();
    PreferenceUser getPreferenceUser();
}
