package sssta.org.hakeday.network;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by mac on 2017/6/3.
 */

public interface ServiceApi {
    @Multipart
    @POST("/identify")
    Observable<AnalysisResponse> uploadAudio(@Part MultipartBody.Part file);

    @GET("/list")
    Observable<IdentifyResponse> getList();
}
