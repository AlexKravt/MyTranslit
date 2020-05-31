package carservice.p5.mytranslater.dagger.Components;

import android.app.Activity;

import carservice.p5.mytranslater.dagger.Annotations.ActivityScope;
import carservice.p5.mytranslater.dagger.Modules.ActivityModule;
import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class, dependencies = MyTranslateComponent.class)
public interface ActivityComponent {
   void injectActivity(Activity activity);
}
