<?php
/**
 * Actualiza una obra especificado por su identificador
 */
require 'Obra.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Decodificando formato Json
    $body = json_decode(file_get_contents("php://input"), true);
    // Actualizar obra
    $retorno = Obra::update(
        $body['idobra'],
        $body['nombre'],
        $body['autor'],
        $body['audio'],
        $body['video']);
    if ($retorno) {
        $json_string = json_encode(array("estado" => 1,"mensaje" => "Actualizacion correcta de la obra"));
		echo $json_string;
    } else {
        $json_string = json_encode(array("estado" => 2,"mensaje" => "No se actualizo el registro de la obra"));
		echo $json_string;
    }
}
?>