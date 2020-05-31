package carservice.p5.mytranslater.services;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kravtsov.a on 03.02.2017.
 */

public class PreferenceUser {
    private SharedPreferences userOptions;

    public PreferenceUser(Context context)
    {
        //Получаем файл настроек
        userOptions = context.getSharedPreferences(GlobalConstant.APP_USER_DATA, Context.MODE_PRIVATE);
    }

    public SharedPreferences getUserOptions(){
        return userOptions;
    }

    public static SharedPreferences getSharedPref(Context context){
       return context.getSharedPreferences(GlobalConstant.APP_USER_DATA, Context.MODE_PRIVATE);
    }

}
