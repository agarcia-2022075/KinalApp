package com.andregarcia.kinalapp.service;
import com.andregarcia.kinalapp.entity.Cliente;

import java.util.List;
import java.util.Optional;


public interface IClienteService {
    //Interfaz: Es in contrato que dice  QUE metodos debe tener cualquier servicio de Clientes, No tiene
    //implementacion, solo  la definicion de  los metodos

    //Metodo que devuelve una lista de todos los clientes
    List<Cliente> listarTodos();
    //List<Cliente> lo que hace es devolver una lista
    //de obhetos de la entidad Clientes

    // metodo que devuelve una lista de los clientes con estado activo
    List<Cliente> listarActivos();

    //Metodo que guarda un clinete en la BD
    Cliente guardar(Cliente cliente);
    //Parámetros - Recibe un objeto de tipo cliente con los datos a guardar

    //Optional - Contenedor  que puede ono tener un valor
    //evita el error de NullPointerException
    Optional<Cliente> buscarPorDPI(String dpi);

    //Metodo que actualiza un cliente
    Cliente actualizar(String dpi, Cliente cliente);
    //Parametros - dpi: DPI del cliemte a actualizar
    //Cliente cliente: Objeto con los datps nuevos
    //Retorna un objeto de tipo cliente ya actualizado

    //Metodo de tipo void para eliminat a un cliente
    //void: no retrona ningun dato
    //Elimina un cliente por su dpi
    void eliminar(String dpi);

    //boolean retorna verdadero si existe, false si no existe
    boolean existeDPI(String dpi);









}
