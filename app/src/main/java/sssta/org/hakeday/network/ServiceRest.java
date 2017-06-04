package sssta.org.hakeday.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import sssta.org.hakeday.Constants;

/**
 * Created by mac on 2017/6/3.
 */

public class ServiceRest {

    private ServiceApi serviceApi;
    private static final ServiceRest ourInstance = new ServiceRest();
    private Retrofit retrofit;

    public static ServiceRest getInstance() {
        return ourInstance;
    }

    private ServiceRest() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient c
                = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(c)
                .build();

    }

    public ServiceApi getServiceApi() {
        if (serviceApi == null) {
            serviceApi = retrofit.create(ServiceApi.class);
        }
        return serviceApi;
    }
}
