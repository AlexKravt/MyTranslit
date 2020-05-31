package carservice.p5.mytranslater;

import android.app.Activity;
import android.app.Application;

import carservice.p5.mytranslater.dagger.Components.DaggerMyTranslateComponent;
import carservice.p5.mytranslater.dagger.Components.MyTranslateComponent;
import carservice.p5.mytranslater.dagger.Modules.AppContextModule;

public class MyTranslateApplication extends Application {
    private   MyTranslateComponent myTranslateApplicationComponent;

    public static MyTranslateApplication get(Activity activity){
        return (MyTranslateApplication) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myTranslateApplicationComponent = DaggerMyTranslateComponent.builder()
                .appContextModule(new AppContextModule(this))
                .build();

    }


    public MyTranslateComponent getApplicationComponent(){
        return myTranslateApplicationComponent;
    }
}
