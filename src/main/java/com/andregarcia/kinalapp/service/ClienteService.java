package com.andregarcia.kinalapp.service;

import com.andregarcia.kinalapp.entity.Cliente;
import com.andregarcia.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

    // Anotacion que registra un Bean como Bean de Spring
//Que la clase contiene la logica del negocio
@Service
//Por defecto todos los metodos de esta clase serán transaccionales
//Una transaccion es que puede o no ocurrir algo

@Transactional
public class ClienteService implements IClienteService {
    /*: Private solo accesible dentro de la clase
        ClienteRepository: Es el repositorio para acceder a la DB
        Inyeccion de Dependencias Spring nos da el repositorio
     */
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository, ClienteRepository clienteRepository1){

        this.clienteRepository = clienteRepository1;
    }



    @Override
    /*
    * readOnly= true
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
        /*
        *Llama al metodo findAll() del repositorio de Spring dat Jpa
        * este metodo hace lo mismo que eñ select from
        *
         */
    }

        @Override
        public List<Cliente> listarActivos() {
            return clienteRepository.findByEstado(1);
        }

        @Override
    public Cliente guardar(Cliente cliente) {
        /*
        *Metodo de guardar crea un cliente
        * aca es donde colocamos la logica del negocio Antes de guardar
        * Primero validamos el dato
         */
        validarCliente(cliente);
        if (cliente.getEstado()==0){
            cliente.setEstado(1);
        }
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        //Busca un cliente por Dpi
        return clienteRepository.findById(dpi);
        //Optional nos evitaa eñ NullPointExceptioon
    }

    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        //Actualiza un cliente existente
        if(!clienteRepository.existsById(dpi)){
            throw new RuntimeException("Cliente no se encontro con Dpi" + dpi);
            //Si no exiatw, se lanza una excepcion (Error controlado)
        }
        /* 1. asergurar que el DPI del objeto coincida con el de la URL
        *   2.por seguridad usamos el Dpi de la URL y no el que veine en el JSON
         */
        cliente.setDpiCliente(dpi);
        validarCliente(cliente);

        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(String dpi) {
        // Desactivación lógica en lugar de eliminación física
        Cliente cliente = clienteRepository.findById(dpi)
                .orElseThrow(() -> new RuntimeException("El cliente no se encontró con el DPI: " + dpi));
        cliente.setEstado(0); // 0 = Inactivo
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeDPI(String dpi) {
        //Verifica si exiate el cliente
        return clienteRepository.existsById(dpi);
        //retorna true o false

    }

    //metodo privado(solo pueden usarse dentro de la clase)
        private void validarCliente(Cliente cliente){
        /*
        * Validaciones de negocio. Este metodo se hara privado porque es algo
        * interno del servicio
         */
            if(cliente.getDpiCliente() == null || cliente.getDpiCliente().trim().isEmpty()){
                throw new IllegalArgumentException("El Dpi es un dato obligatorio");
            }
            if(cliente.getNombreCliente()== null|| cliente.getNombreCliente().trim().isEmpty()){
                throw new IllegalArgumentException("El nombre es un dato obligatorio");
            }

            if (cliente.getApellidoCliente()== null || cliente.getApellidoCliente().trim().isEmpty()){
                throw  new IllegalArgumentException("el apellido es un dato obligatorio");
            }
}
}
