package natos.fit_zone_spring;

import natos.fit_zone_spring.model.Customer;
import natos.fit_zone_spring.service.ICustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class FitZoneSpringApplication implements CommandLineRunner {

    @Autowired
    private ICustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(FitZoneSpringApplication.class);
    private final String nl = System.lineSeparator();

    public static void main(String[] args) {
        logger.info("Iniciando la aplicacion");
        SpringApplication.run(FitZoneSpringApplication.class, args);
        logger.info("Aplicacion finalizada");
    }

    @Override
    public void run(String... args) {
        this.fitZoneApp();
    }

    private void fitZoneApp() {
        var exit = false;
        var console = new Scanner(System.in);
        while (!exit) {
            try {
                var option = showMenu(console);
                exit = executeOptions(console, option, customerService);
                //logger.info(nl);
            } catch (Exception e) {
                logger.error("Error al ejecutar opciones: {}", e.getMessage());
            }
        }
    }

    private int showMenu(Scanner console) {
        logger.info("""
                \n*** Aplicacion Zona Fit (GYM) ***
                1. Listar Clientes
                2. Buscar Cliente
                3. Agregar Cliente
                4. Modificar Cliente
                5. Eliminar Cliente
                6. Salir
                Elije una opcion:\s""");
        return Integer.parseInt(console.nextLine());
    }

    private boolean executeOptions(Scanner console, int option, ICustomerService customerService) {
        boolean status = false;
        switch (option) {
            case 1 -> {
                logger.info("{}--- Listado de Clientes ---{}", nl, nl);
                List<Customer> customers = customerService.listCustomer();
                if (!customers.isEmpty()) {
                    customers.forEach(customer -> logger.info(customer.toString()));
                } else {
                    logger.error("Lista vacia.");
                }
            }
            case 2 -> {
                logger.info("{}--- Buscar Cliente por ID ---{}", nl, nl);
                logger.info("Ingrese ID del Cliente: ");
                var customerID = Integer.parseInt(console.nextLine());
                var customer = customerService.seachCustomerByID(customerID);
                if (customer != null) {
                    logger.info("Cliente encontrado: {}", customer);
                } else {
                    logger.error("Cliente NO encontrado: {}", customerID);
                }
            }
            case 3 -> {
                logger.info("{}--- Agregar Cliente ---{}", nl, nl);
                logger.info("Nombre: ");
                var name = console.nextLine();
                logger.info("Apellido: ");
                var lastname = console.nextLine();
                logger.info("Membresia: ");
                var membership = Integer.parseInt(console.nextLine());
                var customer = new Customer();
                customer.setName(name);
                customer.setLastname(lastname);
                customer.setMembership(membership);
                customerService.saveCustomer(customer);
                logger.info("Cliente agregado: {}", customer);
            }
            case 4 -> {
                logger.info("{}--- Modificar Cliente ---{}", nl, nl);
                logger.info("ID Cliente:");
                var customerID = Integer.parseInt(console.nextLine());
                Customer customer = customerService.seachCustomerByID(customerID);
                if (customer != null) {
                    logger.info("Nombre: ");
                    var name = console.nextLine();
                    customer.setName(name);
                    logger.info("Apellido: ");
                    var lastname = console.nextLine();
                    customer.setLastname(lastname);
                    logger.info("Membresia: ");
                    var membership = Integer.parseInt(console.nextLine());
                    customer.setMembership(membership);
                    customerService.saveCustomer(customer);
                    logger.info("Cliente modificado: " + customer);
                } else {
                    logger.info("Cliente con ID {} no encontrado.", customerID);
                }

            }
            case 5 -> {
                logger.info("{}--- Eliminar Cliente ---{}", nl, nl);
                logger.info("ID Cliente:");
                var customerID = Integer.parseInt(console.nextLine());
                var customer = customerService.seachCustomerByID(customerID);
                if (customer != null) {
                    customerService.deleteCustomer(customer);
                    logger.info("Cliente eliminado.");
                } else {
                    logger.info("Cliente con ID {} no encontrado.", customerID);
                }

            }
            case 6 -> {
                logger.info("Hasta pronto!.");
                status = true;
            }
            default -> logger.error("Opcion no reconocida: {}", option);
        }
        return status;
    }
}
