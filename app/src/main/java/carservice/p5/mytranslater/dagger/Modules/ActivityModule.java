package carservice.p5.mytranslater.dagger.Modules;

import android.app.Activity;

import carservice.p5.mytranslater.dagger.Annotations.MyTranslateApplicationScope;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity){
        this.activity = activity;
    }

    @MyTranslateApplicationScope
    @Provides
    public Activity getActivity(){ return activity; }
}