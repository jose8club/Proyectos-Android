<?php
/**
 * Insertar una nueva obra en la base de datos
 */
require 'Obra.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Decodificando formato Json
    $body = json_decode(file_get_contents("php://input"), true);
    // Insertar Obra
    $retorno = Obra::insert(
        $body['idobra'],
        $body['nombre'],
        $body['autor'],
        $body['audio'],
        $body['video']);
    if ($retorno) {
        $json_string = json_encode(array("estado" => 1,"mensaje" => "Creacion correcta de la obra"));
		echo $json_string;
    } else {
        $json_string = json_encode(array("estado" => 2,"mensaje" => "No se creo la obra de arte"));
		echo $json_string;
    }
}
?>