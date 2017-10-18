package aroundwise.nepi.network.serviceGenerator;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mihai on 05/09/16.
 */
public class ServiceGenerator {
    private static ServiceGenerator serviceGenerator;
    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    private Retrofit.Builder builder = new Retrofit.Builder();
    private Retrofit retrofit;

    public static ServiceGenerator getServiceGenerator() {
        if (serviceGenerator == null)
            serviceGenerator = new ServiceGenerator();
        return serviceGenerator;
    }

    private ServiceGenerator() {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient
                .addInterceptor(loggingInterceptor)
                /*.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return onOnIntercept(chain);
                    }
                })*/
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);
        builder.baseUrl(BaseUrl.getBaseUrl());
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        builder.addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.client(httpClient.build()).build();
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    private Response onOnIntercept(Interceptor.Chain chain) throws IOException {
        try {
            Response response = chain.proceed(chain.request());
            return response;
        } catch (SocketTimeoutException exception) {

        }

        return chain.proceed(chain.request());
    }

}
