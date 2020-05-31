package carservice.p5.mytranslater.dagger.Modules;

import android.content.Context;

import carservice.p5.mytranslater.dagger.Annotations.ApplicationContext;
import carservice.p5.mytranslater.dagger.Annotations.MyTranslateApplicationScope;
import carservice.p5.mytranslater.services.PreferenceUser;
import dagger.Module;
import dagger.Provides;

@Module(includes = AppContextModule.class)
public class PreferenceUserModule {
    @MyTranslateApplicationScope
    @Provides
    public PreferenceUser preferenceUser(@ApplicationContext Context context){
        return new PreferenceUser(context);
    }

}
