package carservice.p5.mytranslater.dagger.Modules;

import android.app.Activity;
import android.content.Context;

import carservice.p5.mytranslater.dagger.Annotations.ActivityContext;
import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private final Context context;

    ContextModule(Activity context){
        this.context = context;
    }

    @ActivityContext
    @Provides
    public Context context(){ return context; }
}