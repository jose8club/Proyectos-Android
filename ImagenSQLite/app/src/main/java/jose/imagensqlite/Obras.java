package jose.imagensqlite;

/**
 * Created by Alicia on 28/11/2016.
 */
public class Obras {
    private String id = "";
    private String nombre = "";
    private String autor = "";

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setAutor(String autor) { this.autor = autor; }

    public String getAutor() {
        return autor;
    }
}
