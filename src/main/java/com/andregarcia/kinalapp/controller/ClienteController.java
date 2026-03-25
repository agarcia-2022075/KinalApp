package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.Cliente;
import com.andregarcia.kinalapp.repository.ClienteRepository;
import com.andregarcia.kinalapp.service.ClienteService;
import com.andregarcia.kinalapp.service.IClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController = @cONTROLLER + @ ResponseBody
@RequestMapping("/clientes")
// Todas las rutas en este controlador empeiezan con /clientes
public class ClienteController {

    //Inyectamos el SERVICIO Y NO EL REPOSITORIO
    //El Controlador solo debe ener conexion con el servicio
    private final IClienteService clienteService;

    //Como bunea practica, la inyeccion de dependencias debe hacerse por el constructo
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;

    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar(){
        List<Cliente> clientes= clienteService.listarTodos();
        //delegamos el servicio
        return  ResponseEntity.ok(clientes);
        //200 OK CON LA LISTA DE CLIENTES
    }

    // GET: Obtiene únicamente la lista de clientes activos
    @GetMapping("/activos")
    public ResponseEntity<List<Cliente>> listarActivos() {
        // Delegamos al servicio la búsqueda de los activos
        List<Cliente> clientesActivos = clienteService.listarActivos();

        // Devolvemos 200 OK con la lista filtrada
        return ResponseEntity.ok(clientesActivos);
    }




    @GetMapping("/{dpi}")
    public  ResponseEntity<Cliente> buscarPorId(@PathVariable String dpi){

        return  clienteService.buscarPorDPI(dpi)
                //Si OPTIONAL tiene valor, devuelve 200 ok con el cliente
                .map(ResponseEntity::ok)
                //Si Optional ESTA VACIO, DEVUELVE 404 not found
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cliente cliente){
        // Toma el JSON del cuerpo y lo convierte a un objetp de tipo Cliente
        //?: Significa tipo generico, puede ser un cliente o un string
        try{
            Cliente nuevoCliente = clienteService.guardar(cliente);
            //iNTYENTAMOS GUARDAR EL CLIENTE Pero puede lanzar una excepcion
            //de IllegalArgumentExcepcion
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
            //201 CREATED(mucho mas especifico que el 2200 para la creacion de un cliente)
        }catch (IllegalArgumentException e){
            //Si hay error de validacion
            return  ResponseEntity.badRequest().body(e.getMessage());
            //400 BAD REQUEST con el mensaje de error
        }
    }

    //DELETE, elimina un cliente
    @DeleteMapping("/{dpi}")
    public ResponseEntity<Void> eliminar(@PathVariable String dpi){
        //ResponseEntity<Void> No devuelve cuerpo en la respuesta
        try {
            if (!clienteService.existeDPI(dpi)){
                return ResponseEntity.notFound().build();
            }
                clienteService.eliminar(dpi);
            return  ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return  ResponseEntity.notFound().build();
        }
    }

// Actualizar cliente a través de DPI
    @PutMapping("/{dpi}")
    public ResponseEntity<?> actualizar(@PathVariable String dpi,@RequestBody Cliente cliente){
        try{
            if (!clienteService.existeDPI(dpi)){
                //Verficar si existe antes de poder actualizar
                //404 NOT FOUND
                return ResponseEntity.notFound().build();
            }
            //Actualizar el cliente pero esto puede lanzar una excepcion
            Cliente clienteActualizando = clienteService.actualizar(dpi,cliente);
            return ResponseEntity.ok(clienteActualizando);
            //200 ok con el cliente
        }catch (IllegalArgumentException e){
            // error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            //pisblmente cualquier otro error como: clienbte no encontrado
            // este es 404 NOT FOUND
            return ResponseEntity.notFound().build();

        }
    }





}
