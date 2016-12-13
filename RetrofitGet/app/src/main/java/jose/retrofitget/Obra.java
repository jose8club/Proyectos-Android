package jose.retrofitget;

/**
 * Created by Alicia on 19/10/2016.
 */
public class Obra {

    //Variables en el Json de mis obras
    private int Id;
    private String nombre;
    private String autor;
    private String tipo;
    private String estilo;

    //Getters and setters
    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setInStock(String tipo) { this.tipo = tipo; }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) { this.estilo = estilo; }
}
