package pruebas.sqliteprueba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
/**
 * Created by Alicia on 07/09/2016.
 */
public class Controlador {

    // variables de control
    private UsuarioSQLiteHelper helper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public Controlador(Context c) {
        ourcontext = c;
    }

    public Controlador abrirBaseDeDatos() throws SQLException {
        UsuarioSQLiteHelper user = new UsuarioSQLiteHelper(ourcontext,"DbObras",null,1);
        database = user.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        helper.close();
    }

    public Cursor leerDatos() {
        String[] todasLasColumnas = new String[] {
                "Serie",
                "Nombre"
        };
        Cursor c = database.query("Obra", todasLasColumnas, null,
                null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public int actualizarDatos(String memberID, String memberName) {
        ContentValues cvActualizar = new ContentValues();
        cvActualizar.put("Nombre", memberName);
        int i = database.update("Obra", cvActualizar,
                "Serie" + " = " + memberID, null);
        return i;
    }

    public void deleteData(String memberID) {
        database.delete("Obra", "Serie" + "="
                + memberID, null);
    }
}
