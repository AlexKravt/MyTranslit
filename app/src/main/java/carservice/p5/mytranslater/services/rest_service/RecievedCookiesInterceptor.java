package carservice.p5.mytranslater.services.rest_service;

// Original written by tsuharesu
// Adapted to create a "drop it in and watch it work" approach by Nikhil Jha.
// Just add your package statement and drop it in the folder with all your other classes.

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashSet;

import carservice.p5.mytranslater.services.GlobalConstant;
import okhttp3.Interceptor;
import okhttp3.Response;

public class RecievedCookiesInterceptor implements Interceptor {

    public RecievedCookiesInterceptor(){}
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
           /* HashSet<String> cookies = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet(GlobalConstant.PREF_COOKIES, new HashSet<String>());

            for (String header : originalResponse.headers("Set-Cookie"))
            {
                cookies = saveCookies(header,cookies);
            }*/

           /* SharedPreferences.Editor memes = PreferenceManager.getDefaultSharedPreferences(context).edit();
            memes.putStringSet(GlobalConstant.PREF_COOKIES, cookies).apply();
            memes.commit();*/
        }

        return originalResponse;
    }


    private HashSet<String> saveCookies(String header, HashSet<String> cookies){
        if(header.startsWith("_csrf")){
            cookies = removeCookies("_csrf",cookies);
            cookies.add(header);
        }
        if(header.startsWith("PHPSESSID")){
            cookies = removeCookies("PHPSESSID",cookies);
            cookies.add(header);
        }
        return cookies;
    }

    private HashSet<String> removeCookies(String key, HashSet<String> cookies){
        HashSet<String> hashSet = new HashSet<>();
        for (String cookie :cookies){
            if(!cookie.startsWith(key)){
                hashSet.add(cookie);
            }
        }
        return hashSet;
    }
}