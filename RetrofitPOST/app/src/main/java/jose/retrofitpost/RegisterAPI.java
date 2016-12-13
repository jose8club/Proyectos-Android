package jose.retrofitpost;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Alicia on 18/10/2016.
 */
public interface RegisterAPI {
    @FormUrlEncoded
    @POST("/obras/insert.php")
    public void insertUser(
            @Field("nombre") String nombre,
            @Field("autor") String autor,
            @Field("tipo") String tipo,
            @Field("estilo") String estilo,
            Callback<Response> callback);
}
