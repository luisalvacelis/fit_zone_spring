package natos.fit_zone_spring.view;

import natos.fit_zone_spring.model.Customer;
import natos.fit_zone_spring.service.CustomerService;
import natos.fit_zone_spring.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class FitZoneView extends JFrame implements ActionListener {

    private JPanel jpBackground;
    private JPanel jpBGBody;
    private JPanel jpBGFooter;
    private JPanel jpInputs;
    private JPanel jpTable;
    private JTextField jtxtName;
    private JTextField jtxtLastname;
    private JTextField jtxtMembership;
    private JTable jtCustomers;
    private JButton jbtSave;
    private JButton jbtDelete;
    private JButton jbtClear;
    private Integer customerID;

    private ICustomerService customerService;
    private DefaultTableModel defaultTableModel;

    @Autowired
    public FitZoneView(CustomerService customerService) {
        this.customerService = customerService;
        this.initForm();
        this.registerEvents();
    }

    private void initForm() {
        setContentPane(jpBackground);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }

    private void registerEvents() {
        this.jbtClear.addActionListener(this);
        this.jbtDelete.addActionListener(this);
        this.jbtSave.addActionListener(this);
        this.jtCustomers.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        super.mouseClicked(mouseEvent);
                        loadCustomerSelected();
                    }
                });
    }

    private void createUIComponents() {
        this.defaultTableModel =
                new DefaultTableModel(0, 4) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
        String[] headers = {"ID", "Nomhre", "Apellido", "Membresia"};
        this.defaultTableModel.setColumnIdentifiers(headers);
        this.jtCustomers = new JTable(defaultTableModel);
        this.jtCustomers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listCustomer();
    }

    private void listCustomer() {
        this.defaultTableModel.setRowCount(0);
        var customers = customerService.listCustomer();
        customers.forEach(
                customer -> {
                    Object[] row = {
                        customer.getId(),
                        customer.getName(),
                        customer.getLastname(),
                        customer.getMembership()
                    };
                    this.defaultTableModel.addRow(row);
                });
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == jbtSave) {
            this.save();
        } else if (actionEvent.getSource() == jbtDelete) {
            this.delete();
        } else if (actionEvent.getSource() == jbtClear) {
            this.clear();
        }
    }

    private void save() {
        String name = jtxtName.getText();
        String lastname = jtxtLastname.getText();
        String membershipString = jtxtMembership.getText();
        if (!name.isEmpty()) {
            if (!lastname.isEmpty()) {
                if (!membershipString.isEmpty()) {
                    int membership = Integer.parseInt(membershipString);
                    if (membership > 0) {
                        var customer = new Customer();
                        if (customerID != -1) {
                            customer.setId(customerID);
                            customer.setName(name);
                            customer.setLastname(lastname);
                            customer.setMembership(membership);
                            this.customerService.saveCustomer(customer);
                            this.sendMessage("Cliente actualizado con éxito.");
                            this.clear();
                            this.listCustomer();
                        } else {
                            customer.setName(name);
                            customer.setLastname(lastname);
                            customer.setMembership(membership);
                            this.customerService.saveCustomer(customer);
                            this.sendMessage("Cliente agregado con éxito.");
                            this.clear();
                            this.listCustomer();
                        }
                    } else {
                        this.sendMessage("Error: Membresia no puede ser menor o igual a 0");
                    }
                } else {
                    this.sendMessage("Error: Membresia no ingresada.");
                }
            } else {
                this.sendMessage("Error: Apellido no ingresado.");
            }
        } else {
            this.sendMessage("Error: Nombre no ingresado.");
        }
    }

    private void delete() {
        if (customerID != -1) {
            var customer = customerService.seachCustomerByID(customerID);
            if (customer != null) {
                this.customerService.deleteCustomer(customer);
                this.sendMessage("Cliente eliminado con éxito.");
                this.listCustomer();
                this.clear();
            } else {
                this.sendMessage("Error: Cliente no encontrado.");
            }
        } else {
            this.sendMessage("Error: Cliente no seleccionado");
        }
    }

    private void clear() {
        this.jtxtName.setText(null);
        this.jtxtName.setName(null);
        this.jtxtLastname.setText(null);
        this.jtxtMembership.setText(null);
        this.customerID = -1;
        this.jtCustomers.getSelectionModel().clearSelection();
    }

    private void loadCustomerSelected() {
        var row = jtCustomers.getSelectedRow();
        if (row != -1) {
            var id = jtCustomers.getModel().getValueAt(row, 0).toString();
            this.customerID = Integer.parseInt(id);
            var name = jtCustomers.getModel().getValueAt(row, 1).toString();
            var lastname = jtCustomers.getModel().getValueAt(row, 2).toString();
            var membership = jtCustomers.getModel().getValueAt(row, 3).toString();
            this.jtxtName.setText(name);
            this.jtxtLastname.setText(lastname);
            this.jtxtMembership.setText(membership);
        }
    }

    private void sendMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
