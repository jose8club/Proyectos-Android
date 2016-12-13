<?php
/**
 * Obtiene todas las obras de la base de datos
 */
require 'Obra.php';

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    // Manejar petición GET
    $obras = Obra::getAll();
    if ($obras) {
        $datos["estado"] = 1;
        $datos["obras"] = $obras;
        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error no se han obtenido datos"
        ));
    }
}