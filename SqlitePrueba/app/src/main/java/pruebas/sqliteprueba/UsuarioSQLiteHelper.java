package pruebas.sqliteprueba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alicia on 07/09/2016.
 */
public class UsuarioSQLiteHelper extends SQLiteOpenHelper {

    //Se creara la variable que contendra la sentencia SQL de creacion de la tabla
    //String sql = "CREATE TABLE IF NOT EXISTS Obra (Serie TEXT PRIMARY KEY, Nombre TEXT, Autor TEXT, Tecnica TEXT)";
    String sql = "CREATE TABLE IF NOT EXISTS Obra (Serie TEXT PRIMARY KEY, Nombre TEXT)";
    String drop = "DROP TABLE IF EXISTS Obra";

    public UsuarioSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Este metodo se ejecuta cuando se ve que no existe la base de datos
        //Cuando se ejecuta la primera vez
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Elimina la tabla si la base de datos cambia
        //Y crea es tabla nuevamente
        db.execSQL(drop);
        db.execSQL(sql);
    }
}
