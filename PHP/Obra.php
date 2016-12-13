<?php
/**
 * Representa el la estructura de las Obras de Arte
 * almacenadas en la base de datos
 */
require 'Database.php';
class Obra
{
    function __construct()
    {
    }
    /**
     * Retorna todas las filas especificadas en la tabla 'Obra'
     *
     */
    public static function getAll()
    {
        $consulta = "SELECT * FROM Obra";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();
            return $comando->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            return false;
        }
    }


    /**
     * Retorna todas las idobra especificadas en la tabla 'Obra'
     *
     */
    public static function getId()
    {
        $consulta = "SELECT idobra FROM Obra";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();
            return $comando->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            return false;
        }
    }

    /**
     * Retorna todas los nombres especificadas en la tabla 'Obra'
     *
     */
    public static function getNombre()
    {
        $consulta = "SELECT nombre FROM Obra";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();
            return $comando->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            return false;
        }
    }

    /**
     * Retorna todas los autores especificadas en la tabla 'Obra'
     *
     */
    public static function getAutores()
    {
        $consulta = "SELECT autor FROM Obra";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();
            return $comando->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            return false;
        }
    }

    /**
     * Obtiene los campos de una obra de Arte con un identificador
     * determinado
     *
     * @param $idobra Identificador de la obra de arte
     * @return mixed
     */
    public static function getById($idobra)
    {
        // Consulta de la tabla Obra
        $consulta = "SELECT idobra,
                            nombre,
                            autor,
                            audio,
                            video
                             FROM Obra
                             WHERE idobra = ?";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(array($idobra));
            // Capturar primera fila del resultado
            $row = $comando->fetch(PDO::FETCH_ASSOC);
            return $row;
        } catch (PDOException $e) {
            // Aquí puedes clasificar el error dependiendo de la excepción
            // para presentarlo en la respuesta Json
            return -1;
        }
    }
    /**
     * Actualiza un registro de la bases de datos basado
     * en los nuevos valores relacionados con un identificador
     *
     * @param $idobra      identificador
     * @param $nombre      nuevo nombre
     * @param $autor       nuevo autor
     * @param $audio       audio asociado a la obra
     * @param $video       video asociado a la obra
     */
    public static function update(
        $idobra,
        $nombre,
        $autor,
        $audio,
        $video

    )
    {
        // Creando consulta UPDATE
        $consulta = "UPDATE Obra" .
            " SET nombre=?, autor=?, audio=?, video=? " .
            "WHERE idobra=?";
        // Preparar la sentencia
        $cmd = Database::getInstance()->getDb()->prepare($consulta);
        // Relacionar y ejecutar la sentencia
        $cmd->execute(array($nombre, $autor, $audio, $video, $idobra));
        return $cmd;
    }
    /**
     * Insertar una nueva obra de Arte
     *
     * @param $idobra      nuevo identificador de la obra de arte
     * @param $nombre      nuevo nombre
     * @param $autor       nuevo autor
     * @param $audio       audio asociado a la obra
     * @param $video       video asociado a la obra
     * @return PDOStatement
     */
    public static function insert(
        $idobra,
        $nombre,
        $autor,
        $audio,
        $video
    )
    {
        // Sentencia INSERT
        $comando = "INSERT INTO Obra ( " .
            "idobra," .
            " nombre," .
            " autor,".
            " audio,".
            " video)" .
            " VALUES( ?,?,?,?,?)";
        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);
        return $sentencia->execute(
            array(
                $idobra,
                $nombre,
                $autor,
                $audio,
                $video
            )
        );
    }
    /**
     * Eliminar el registro con el identificador especificado
     *
     * @param $idobra identificador de la tabla Arte
     * @return bool Respuesta de la eliminación
     */
    public static function delete($idobra)
    {
        // Sentencia DELETE
        $comando = "DELETE FROM Obra WHERE idobra=?";
        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);
        return $sentencia->execute(array($idobra));
    }
}
?>