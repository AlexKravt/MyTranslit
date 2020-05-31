package carservice.p5.mytranslater.dagger.Modules;

import android.content.Context;

import carservice.p5.mytranslater.dagger.Annotations.ApplicationContext;
import carservice.p5.mytranslater.dagger.Annotations.MyTranslateApplicationScope;
import carservice.p5.mytranslater.services.sqlite.TableBase;
import dagger.Module;
import dagger.Provides;

@Module(includes = AppContextModule.class)
public class DBModule {
    @MyTranslateApplicationScope
    @Provides
    public TableBase base(@ApplicationContext Context context){
        return new TableBase(context);
    }

}
