package carservice.p5.mytranslater.services.rest_service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderJsonType implements Interceptor {

    public HeaderJsonType(){}

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request nRequest = request.newBuilder()
                .header("Content-Type", "application/json")
                .build();

        return chain.proceed(nRequest);
    }

}
