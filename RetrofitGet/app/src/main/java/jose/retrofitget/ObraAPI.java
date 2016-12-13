package jose.retrofitget;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
/**
 * Created by Alicia on 19/10/2016.
 */
public interface ObraAPI {
    /**
     * Uso del un txt creado para llamar a la obra
     * @param response
     */
    @GET("/obras/obras.json")
    public void getObra(Callback<List<Obra>> response);
}
