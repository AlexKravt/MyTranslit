package carservice.p5.mytranslater.dagger.Modules;

import android.content.Context;

import carservice.p5.mytranslater.dagger.Annotations.ApplicationContext;
import dagger.Module;
import dagger.Provides;

@Module
public class AppContextModule {

    Context context;

    public AppContextModule(Context context){
        this.context = context;
    }

    @ApplicationContext
    @Provides
    public Context context(){ return context.getApplicationContext(); }
}
