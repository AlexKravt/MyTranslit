package carservice.p5.mytranslater.services.rest_service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenAuthInterceptor implements Interceptor {

    private String credentials;

    public TokenAuthInterceptor(String token) {
        this.credentials = " Bearer ".concat(token);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Content-Type", "application/json")
                .header("Authorization", credentials)
                .build();

        return chain.proceed(authenticatedRequest);
    }

}
