package jose.museonfc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Alicia on 23/09/2016.
 */
public class BD extends SQLiteOpenHelper {


    public BD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "BdMuseo", factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table obra(serie text primary key not null," +
                "nombre text," +
                "autor text," +
                "tipo text," +
                "estilo text," +
                "fecha_creacion text," +
                "nacionalidad text," +
                "fecha_adquisicion text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String guardar(String serie, String nombre, String autor, String tipo, String estilo){
        String mensaje="";
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contenedor = new ContentValues();
        contenedor.put("serie",serie);
        contenedor.put("nombre", nombre);
        contenedor.put("autor", autor);
        contenedor.put("tipo", tipo);
        contenedor.put("estilo", estilo);
        try {
            database.insertOrThrow("obras",null,contenedor);
            mensaje="Obra Ingresada Correctamente";
        }catch (SQLException e){
            mensaje="Obra no pudo ser ingresada";
        }
        database.close();
        return mensaje;
    }

    public String[] buscar_registros(String buscar) {
        String[] datos= new String[9];
        SQLiteDatabase database = this.getWritableDatabase();
        String q = "SELECT * FROM obras WHERE serie ='"+buscar+"'";
        Cursor registros = database.rawQuery(q, null);
        if(registros.moveToFirst()){
            for(int i = 0 ; i<8;i++){
                datos[i]= registros.getString(i);

            }
            datos[8]= "Encontrado";
        }else{

            datos[8]= "No se encontro a "+buscar;
        }
        database.close();
        return datos;
    }

    public ArrayList<String> llenar_lista_serie() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        String q = "SELECT * FROM obras";
        Cursor registros = database.rawQuery(q, null);
        if(registros.moveToFirst()){
            do{
                lista.add(registros.getString(0));
                //lista.add(registros.getString(1));
            }while(registros.moveToNext());
        }
        return lista;
    }

    public ArrayList<String> llenar_lista_nombre() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        String q = "SELECT * FROM obras";
        Cursor registros = database.rawQuery(q, null);
        if(registros.moveToFirst()){
            do{
                lista.add(registros.getString(1));
                //lista.add(registros.getString(1));
            }while(registros.moveToNext());
        }
        return lista;
    }


    public String actualizar(String buscar, String nombre, String autor, String tipo,
                             String estilo, String fecha_creacion, String nacionalidad, String fecha_adquisicion) {
        String Mensaje ="";
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contenedor = new ContentValues();

        contenedor.put("nombre",nombre);
        contenedor.put("autor",autor);
        contenedor.put("tipo",tipo);
        contenedor.put("estilo",estilo);
        contenedor.put("fecha_creacion",fecha_creacion);
        contenedor.put("nacionalidad",nacionalidad);
        contenedor.put("fecha_adquisicion",fecha_adquisicion);
        int cantidad = database.update("obras", contenedor, "serie='" + buscar + "'", null);
        if(cantidad!=0){
            Mensaje="Actualizada la Obra Correctamente";
        }else{
            Mensaje="No Actualizado";
        }
        database.close();
        return Mensaje;
    }

    public String eliminar(String serie) {
        String mensaje ="";
        SQLiteDatabase database = this.getWritableDatabase();
        int cantidad =database.delete("obras", "serie='" + serie + "'", null);
        if (cantidad !=0){
            mensaje="Eliminado Correctamente";

        }
        else{
            mensaje = "No existe";
        }
        database.close();
        return mensaje;
    }

    public String actualizar_vacios(String buscar, String fecha_creacion, String nacionalidad, String fecha_adquisicion) {
        String Mensaje ="";
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contenedor = new ContentValues();
        //contenedor.put("serie",serie);
        contenedor.put("fecha_creacion",fecha_creacion);
        contenedor.put("nacionalidad",nacionalidad);
        contenedor.put("fecha_adquisicion",fecha_adquisicion);
        int cantidad = database.update("obras", contenedor, "serie='" + buscar + "'", null);
        if(cantidad!=0){
            Mensaje="Actualizada la Obra Correctamente";
        }else{
            Mensaje="No Actualizado";
        }
        database.close();
        return Mensaje;
    }

    public ArrayList<String> llenar_spinner(){
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        String q = "SELECT * FROM obras";
        Cursor registros = database.rawQuery(q, null);
        lista.add("Escoja obra a mostrar");
        if(registros.moveToFirst()){
            do{
                lista.add(registros.getString(0));
                //lista.add(registros.getString(0));
            }while(registros.moveToNext());
        }
        return lista;
    }
}
