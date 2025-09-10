package ar.edu.utn.frbb.tup.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.repository.ClienteRepository;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Da de alta un nuevo cliente en el sistema
     *
     * @param clienteDto Datos del cliente a crear
     * @return Cliente creado
     * @throws ClienteAlreadyExistsException Si ya existe un cliente con ese DNI
     */
    public Cliente darDeAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException {
        Cliente cliente = new Cliente(clienteDto);

        // Verificar si ya existe un cliente con ese DNI
        if (clienteRepository.existsByDni(cliente.getDni())) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
        }

        // Validar edad mínima
        if (cliente.getEdad() < 18) {
            throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");
        }

        // Guardar el cliente
        return clienteRepository.save(cliente);
    }

    /**
     * Agrega una cuenta a un cliente existente
     *
     * @param cuenta Cuenta a agregar
     * @param dniTitular DNI del cliente titular
     * @throws TipoCuentaAlreadyExistsException Si el cliente ya tiene una cuenta de ese tipo y moneda
     */
    public void agregarCuenta(Cuenta cuenta, long dniTitular) throws TipoCuentaAlreadyExistsException {
        Optional<Cliente> clienteOpt = clienteRepository.findByDni(dniTitular);
        if (!clienteOpt.isPresent()) {
            throw new IllegalArgumentException("El cliente no existe");
        }
        Cliente cliente = clienteOpt.get();
        if (cliente.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("El cliente ya tiene una cuenta de tipo "
                    + cuenta.getTipoCuenta() + " y moneda " + cuenta.getMoneda());
        }
        cliente.addCuenta(cuenta);
        clienteRepository.save(cliente);
    }

    /**
     * Busca un cliente por DNI
     *
     * @param dni DNI del cliente
     * @return Cliente encontrado
     * @throws IllegalArgumentException Si el cliente no existe
     */
    @Transactional(readOnly = true)
    public Cliente buscarClientePorDni(long dni) {
        Optional<Cliente> cliente = clienteRepository.findByDniWithCuentasAndPrestamos(dni);
        if (!cliente.isPresent()) {
            throw new IllegalArgumentException("El cliente no existe");
        }
        return cliente.get();
    }

    /**
     * Busca un cliente por DNI (solo datos básicos, sin relaciones)
     *
     * @param dni DNI del cliente
     * @return Cliente encontrado
     * @throws IllegalArgumentException Si el cliente no existe
     */
    @Transactional(readOnly = true)
    public Cliente buscarClienteBasicoPorDni(long dni) {
        Optional<Cliente> cliente = clienteRepository.findByDni(dni);
        if (!cliente.isPresent()) {
            throw new IllegalArgumentException("El cliente no existe");
        }
        return cliente.get();
    }

    /**
     * Obtiene todos los clientes del sistema con sus cuentas cargadas
     *
     * @return Lista de todos los clientes
     */
    @Transactional(readOnly = true)
    public List<Cliente> obtenerClientes() {
        return clienteRepository.findAllWithCuentas();
    }

    /**
     * Verifica si un cliente existe por DNI
     *
     * @param dni DNI a verificar
     * @return true si existe, false si no
     */
    @Transactional(readOnly = true)
    public boolean existeCliente(long dni) {
        return clienteRepository.existsByDni(dni);
    }

    /**
     * Actualiza un cliente existente
     *
     * @param cliente Cliente a actualizar
     * @return Cliente actualizado
     */
    public Cliente actualizarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    /**
     * Elimina un cliente por DNI
     *
     * @param dni DNI del cliente a eliminar
     * @throws IllegalArgumentException Si el cliente no existe
     */
    public void eliminarCliente(long dni) {
        if (!clienteRepository.existsByDni(dni)) {
            throw new IllegalArgumentException("El cliente no existe");
        }
        clienteRepository.deleteById(dni);
    }

    public void asociarCuenta(Cuenta cuenta, long dniTitular) {
        Cliente cliente = buscarClientePorDni(dniTitular);
        cliente.addCuenta(cuenta);
        clienteRepository.save(cliente);
    }
}