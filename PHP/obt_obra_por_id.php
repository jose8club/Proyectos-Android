<?php
/**
 * Obtiene el detalle de una obra de arte especificado por
 * su identificador "idobra"
 */
require 'Obra.php';

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    if (isset($_GET['idobra'])) {
        // Obtener parámetro idobra
        $parametro = $_GET['idobra'];
        // Tratar retorno
        $retorno = Obra::getById($parametro);
        if ($retorno) {
            $obra["estado"] = 1;		// cambio "1" a 1 porque no coge bien la cadena.
            $obra["obra"] = $retorno;
            // Enviar objeto json de la obra
            print json_encode($obra);
        } else {
            // Enviar respuesta de error general
            print json_encode(
                array(
                    'estado' => '2',
                    'mensaje' => 'No se obtuvo la obra de arte'
                )
            );
        }
    } else {
        // Enviar respuesta de error
        print json_encode(
            array(
                'estado' => '3',
                'mensaje' => 'Se necesita un identificador de la obra'
            )
        );
    }
}