<?php
/**
 * Elimina una obra de arte la base de datos
 * distinguido por su identificador
 */
require 'Obra.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Decodificando formato Json
    $body = json_decode(file_get_contents("php://input"), true);
    $retorno = Obra::delete($body['idobra']);
    //$json_string = json_encode($clientes);
    //echo 'antes de entrar';
    if ($retorno) {
        $json_string = json_encode(array("estado" => 1,"mensaje" => "Eliminacion exitosa de la obra de arte"));
        echo $json_string;
    } else {
        $json_string = json_encode(array("estado" => 2,"mensaje" => "No se elimino el registro de la obra de arte"));
        echo $json_string;
    }
}
?>