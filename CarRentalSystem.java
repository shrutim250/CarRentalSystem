
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

// Main class that initializes the application
public class CarRentalSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

// Login Frame
class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginFrame() {
        setTitle("Car Rental System - Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel titleLabel = new JLabel("Car Rental System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(usernameLabel, gbc);
        
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);
        
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);
        
        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                // Simple authentication (in real app, use proper authentication)
                if ("admin".equals(username) && "admin123".equals(password)) {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                    dispose(); // Close login frame
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, 
                        "Invalid username or password", "Login Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        add(panel);
    }
}

// Main application frame
class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private CarPanel carPanel;
    private CustomerPanel customerPanel;
    private RentalPanel rentalPanel;
    
    public MainFrame() {
        setTitle("Car Rental System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize data repository
        DataRepository repository = new DataRepository();
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Car Rental System v1.0\nDeveloped with Java Swing", 
            "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create panels
        carPanel = new CarPanel(repository);
        customerPanel = new CustomerPanel(repository);
        rentalPanel = new RentalPanel(repository);
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Cars", new ImageIcon(), carPanel, "Manage Cars");
        tabbedPane.addTab("Customers", new ImageIcon(), customerPanel, "Manage Customers");
        tabbedPane.addTab("Rentals", new ImageIcon(), rentalPanel, "Manage Rentals");
        
        add(tabbedPane);
    }
}

// Car management panel
class CarPanel extends JPanel {
    private JTextField idField, makeField, modelField, yearField, colorField, rateField;
    private JCheckBox availableCheck;
    private JTable carTable;
    private DefaultTableModel tableModel;
    private DataRepository repository;
    
    public CarPanel(DataRepository repository) {
        this.repository = repository;
        setLayout(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Car Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Car ID:"), gbc);
        
        gbc.gridx = 1;
        idField = new JTextField(10);
        formPanel.add(idField, gbc);
        
        // Make
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Make:"), gbc);
        
        gbc.gridx = 1;
        makeField = new JTextField(10);
        formPanel.add(makeField, gbc);
        
        // Model
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Model:"), gbc);
        
        gbc.gridx = 1;
        modelField = new JTextField(10);
        formPanel.add(modelField, gbc);
        
        // Year
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Year:"), gbc);
        
        gbc.gridx = 1;
        yearField = new JTextField(10);
        formPanel.add(yearField, gbc);
        
        // Color
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Color:"), gbc);
        
        gbc.gridx = 1;
        colorField = new JTextField(10);
        formPanel.add(colorField, gbc);
        
        // Daily Rate
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Daily Rate:"), gbc);
        
        gbc.gridx = 1;
        rateField = new JTextField(10);
        formPanel.add(rateField, gbc);
        
        // Available
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Available:"), gbc);
        
        gbc.gridx = 1;
        availableCheck = new JCheckBox();
        availableCheck.setSelected(true);
        formPanel.add(availableCheck, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        // Add the form and buttons to the top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add the top panel
        add(topPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Make", "Model", "Year", "Color", "Daily Rate", "Available"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        carTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(carTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load initial data
        loadCarData();
        
        // Action listeners
        addButton.addActionListener(e -> addCar());
        updateButton.addActionListener(e -> updateCar());
        deleteButton.addActionListener(e -> deleteCar());
        clearButton.addActionListener(e -> clearFields());
        
        carTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && carTable.getSelectedRow() != -1) {
                displaySelectedCar();
            }
        });
    }
    
    private void loadCarData() {
        tableModel.setRowCount(0);
        for (Car car : repository.getAllCars()) {
            Object[] row = {
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                car.getColor(),
                car.getDailyRate(),
                car.isAvailable()
            };
            tableModel.addRow(row);
        }
    }
    
    private void displaySelectedCar() {
        int row = carTable.getSelectedRow();
        idField.setText(tableModel.getValueAt(row, 0).toString());
        makeField.setText(tableModel.getValueAt(row, 1).toString());
        modelField.setText(tableModel.getValueAt(row, 2).toString());
        yearField.setText(tableModel.getValueAt(row, 3).toString());
        colorField.setText(tableModel.getValueAt(row, 4).toString());
        rateField.setText(tableModel.getValueAt(row, 5).toString());
        availableCheck.setSelected((Boolean) tableModel.getValueAt(row, 6));
    }
    
    private void addCar() {
        try {
            String id = idField.getText();
            String make = makeField.getText();
            String model = modelField.getText();
            int year = Integer.parseInt(yearField.getText());
            String color = colorField.getText();
            double rate = Double.parseDouble(rateField.getText());
            boolean available = availableCheck.isSelected();
            
            if (id.isEmpty() || make.isEmpty() || model.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields");
                return;
            }
            
            Car car = new Car(id, make, model, year, color, rate, available);
            repository.addCar(car);
            loadCarData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Car added successfully");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid number values");
        }
    }
    
    private void updateCar() {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to update");
            return;
        }
        
        try {
            String id = idField.getText();
            String make = makeField.getText();
            String model = modelField.getText();
            int year = Integer.parseInt(yearField.getText());
            String color = colorField.getText();
            double rate = Double.parseDouble(rateField.getText());
            boolean available = availableCheck.isSelected();
            
            Car car = new Car(id, make, model, year, color, rate, available);
            repository.updateCar(car);
            loadCarData();
            JOptionPane.showMessageDialog(this, "Car updated successfully");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid number values");
        }
    }
    
    private void deleteCar() {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to delete");
            return;
        }
        
        String id = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this car?", "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            repository.deleteCar(id);
            loadCarData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Car deleted successfully");
        }
    }
    
    private void clearFields() {
        idField.setText("");
        makeField.setText("");
        modelField.setText("");
        yearField.setText("");
        colorField.setText("");
        rateField.setText("");
        availableCheck.setSelected(true);
        carTable.clearSelection();
    }
}

// Customer management panel
class CustomerPanel extends JPanel {
    private JTextField idField, nameField, phoneField, emailField, addressField;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private DataRepository repository;
    
    public CustomerPanel(DataRepository repository) {
        this.repository = repository;
        setLayout(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Customer ID:"), gbc);
        
        gbc.gridx = 1;
        idField = new JTextField(15);
        formPanel.add(idField, gbc);
        
        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Phone:"), gbc);
        
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        formPanel.add(phoneField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Address:"), gbc);
        
        gbc.gridx = 1;
        addressField = new JTextField(15);
        formPanel.add(addressField, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        // Add the form and buttons to the top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add the top panel
        add(topPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Name", "Phone", "Email", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load initial data
        loadCustomerData();
        
        // Action listeners
        addButton.addActionListener(e -> addCustomer());
        updateButton.addActionListener(e -> updateCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        clearButton.addActionListener(e -> clearFields());
        
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && customerTable.getSelectedRow() != -1) {
                displaySelectedCustomer();
            }
        });
    }
    
    private void loadCustomerData() {
        tableModel.setRowCount(0);
        for (Customer customer : repository.getAllCustomers()) {
            Object[] row = {
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress()
            };
            tableModel.addRow(row);
        }
    }
    
    private void displaySelectedCustomer() {
        int row = customerTable.getSelectedRow();
        idField.setText(tableModel.getValueAt(row, 0).toString());
        nameField.setText(tableModel.getValueAt(row, 1).toString());
        phoneField.setText(tableModel.getValueAt(row, 2).toString());
        emailField.setText(tableModel.getValueAt(row, 3).toString());
        addressField.setText(tableModel.getValueAt(row, 4).toString());
    }
    
    private void addCustomer() {
        String id = idField.getText();
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        
        if (id.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields");
            return;
        }
        
        Customer customer = new Customer(id, name, phone, email, address);
        repository.addCustomer(customer);
        loadCustomerData();
        clearFields();
        JOptionPane.showMessageDialog(this, "Customer added successfully");
    }
    
    private void updateCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to update");
            return;
        }
        
        String id = idField.getText();
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        
        Customer customer = new Customer(id, name, phone, email, address);
        repository.updateCustomer(customer);
        loadCustomerData();
        JOptionPane.showMessageDialog(this, "Customer updated successfully");
    }
    
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete");
            return;
        }
        
        String id = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this customer?", "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            repository.deleteCustomer(id);
            loadCustomerData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Customer deleted successfully");
        }
    }
    
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        customerTable.clearSelection();
    }
}

// Rental management panel
class RentalPanel extends JPanel {
    private JTextField idField, totalField;
    private JComboBox<String> carCombo, customerCombo;
    private JFormattedTextField startDateField, endDateField;
    private JTable rentalTable;
    private DefaultTableModel tableModel;
    private DataRepository repository;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public RentalPanel(DataRepository repository) {
        this.repository = repository;
        setLayout(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Rental Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Rental ID:"), gbc);
        
        gbc.gridx = 1;
        idField = new JTextField(10);
        formPanel.add(idField, gbc);
        
        // Car
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Car:"), gbc);
        
        gbc.gridx = 1;
        carCombo = new JComboBox<>();
        formPanel.add(carCombo, gbc);
        
        // Customer
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Customer:"), gbc);
        
        gbc.gridx = 1;
        customerCombo = new JComboBox<>();
        formPanel.add(customerCombo, gbc);
        
        // Start Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Start Date (yyyy-MM-dd):"), gbc);
        
        gbc.gridx = 1;
        startDateField = new JFormattedTextField(dateFormat);
        startDateField.setColumns(10);
        formPanel.add(startDateField, gbc);
        
        // End Date
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("End Date (yyyy-MM-dd):"), gbc);
        
        gbc.gridx = 1;
        endDateField = new JFormattedTextField(dateFormat);
        endDateField.setColumns(10);
        formPanel.add(endDateField, gbc);
        
        // Total
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Total Cost:"), gbc);
        
        gbc.gridx = 1;
        totalField = new JTextField(10);
        totalField.setEditable(false);
        formPanel.add(totalField, gbc);
        
        // Calculate Button
        JButton calculateButton = new JButton("Calculate Total");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(calculateButton, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Create Rental");
        JButton returnButton = new JButton("Return Car");
        JButton clearButton = new JButton("Clear Fields");
        
        buttonPanel.add(addButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(clearButton);
        
        // Add the form and buttons to the top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add the top panel
        add(topPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Car", "Customer", "Start Date", "End Date", "Total Cost", "Returned"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        rentalTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(rentalTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load combo boxes
        loadComboBoxes();
        
        // Load initial data
        loadRentalData();
        
        // Action listeners
        calculateButton.addActionListener(e -> calculateTotal());
        addButton.addActionListener(e -> createRental());
        returnButton.addActionListener(e -> returnCar());
        clearButton.addActionListener(e -> clearFields());
        
        rentalTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && rentalTable.getSelectedRow() != -1) {
                displaySelectedRental();
            }
        });
    }
    
    private void loadComboBoxes() {
        carCombo.removeAllItems();
        customerCombo.removeAllItems();
        
        for (Car car : repository.getAllCars()) {
            if (car.isAvailable()) {
                carCombo.addItem(car.getId() + " - " + car.getMake() + " " + car.getModel());
            }
        }
        
        for (Customer customer : repository.getAllCustomers()) {
            customerCombo.addItem(customer.getId() + " - " + customer.getName());
        }
    }
    
    private void loadRentalData() {
        tableModel.setRowCount(0);
        for (Rental rental : repository.getAllRentals()) {
            Object[] row = {
                rental.getId(),
                rental.getCar().getMake() + " " + rental.getCar().getModel(),
                rental.getCustomer().getName(),
                dateFormat.format(rental.getStartDate()),
                dateFormat.format(rental.getEndDate()),
                String.format("$%.2f", rental.getTotalCost()),
                rental.isReturned() ? "Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }
    
    private void displaySelectedRental() {
        int row = rentalTable.getSelectedRow();
        
        String rentalId = tableModel.getValueAt(row, 0).toString();
        Rental rental = repository.getRentalById(rentalId);
        
        if (rental != null) {
            idField.setText(rental.getId());
            startDateField.setText(dateFormat.format(rental.getStartDate()));
            endDateField.setText(dateFormat.format(rental.getEndDate()));
            totalField.setText(String.format("%.2f", rental.getTotalCost()));
            
            // These might not match exactly due to filtering
            // In a real app, you'd want to check by ID
            for (int i = 0; i < carCombo.getItemCount(); i++) {
                if (carCombo.getItemAt(i).contains(rental.getCar().getId())) {
                    carCombo.setSelectedIndex(i);
                    break;
                }
            }
            
            for (int i = 0; i < customerCombo.getItemCount(); i++) {
                if (customerCombo.getItemAt(i).contains(rental.getCustomer().getId())) {
                    customerCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    private void calculateTotal() {
        try {
            if (carCombo.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Please select a car");
                return;
            }
            
            String carId = carCombo.getSelectedItem().toString().split(" - ")[0];
            Car car = repository.getCarById(carId);
            
            if (car == null) {
                JOptionPane.showMessageDialog(this, "Selected car not found");
                return;
            }
            
            Date startDate = dateFormat.parse(startDateField.getText());
            Date endDate = dateFormat.parse(endDateField.getText());
            
            if (endDate.before(startDate)) {
                JOptionPane.showMessageDialog(this, "End date cannot be before start date");
                return;
            }
            
            long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
            long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            // Ensure at least 1 day
            days = Math.max(1, days);
            
            double total = days * car.getDailyRate();
            totalField.setText(String.format("%.2f", total));
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid dates in yyyy-MM-dd format");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error calculating total: " + e.getMessage());
        }
    }
    
    private void createRental() {
        try {
            String id = idField.getText();
            
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter rental ID");
                return;
            }
            
            if (carCombo.getSelectedIndex() == -1 || customerCombo.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Please select car and customer");
                return;
            }
            
          String carId = carCombo.getSelectedItem().toString().split(" - ")[0];
            String customerId = customerCombo.getSelectedItem().toString().split(" - ")[0];
            
            Car car = repository.getCarById(carId);
            Customer customer = repository.getCustomerById(customerId);
            
            if (car == null || customer == null) {
                JOptionPane.showMessageDialog(this, "Car or customer not found");
                return;
            }
            
            Date startDate = dateFormat.parse(startDateField.getText());
            Date endDate = dateFormat.parse(endDateField.getText());
            
            if (endDate.before(startDate)) {
                JOptionPane.showMessageDialog(this, "End date cannot be before start date");
                return;
            }
            
            double total;
            if (totalField.getText().isEmpty()) {
                calculateTotal();
            }
            total = Double.parseDouble(totalField.getText());
            
            Rental rental = new Rental(id, car, customer, startDate, endDate, total, false);
            repository.addRental(rental);
            
            // Update car availability
            car.setAvailable(false);
            repository.updateCar(car);
            
            loadRentalData();
            loadComboBoxes();
            clearFields();
            
            JOptionPane.showMessageDialog(this, "Rental created successfully");
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid dates in yyyy-MM-dd format");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating rental: " + e.getMessage());
        }
    }
    
    private void returnCar() {
        int selectedRow = rentalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a rental");
            return;
        }
        
        String rentalId = tableModel.getValueAt(selectedRow, 0).toString();
        boolean isReturned = tableModel.getValueAt(selectedRow, 6).toString().equals("Yes");
        
        if (isReturned) {
            JOptionPane.showMessageDialog(this, "Car has already been returned");
            return;
        }
        
        Rental rental = repository.getRentalById(rentalId);
        if (rental != null) {
            rental.setReturned(true);
            repository.updateRental(rental);
            
            // Update car availability
            Car car = rental.getCar();
            car.setAvailable(true);
            repository.updateCar(car);
            
            loadRentalData();
            loadComboBoxes();
            clearFields();
            
            JOptionPane.showMessageDialog(this, "Car returned successfully");
        }
    }
    
    private void clearFields() {
        idField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        totalField.setText("");
        carCombo.setSelectedIndex(-1);
        customerCombo.setSelectedIndex(-1);
        rentalTable.clearSelection();
    }
}

// Models
class Car {
    private String id;
    private String make;
    private String model;
    private int year;
    private String color;
    private double dailyRate;
    private boolean available;
    
    public Car(String id, String make, String model, int year, String color, double dailyRate, boolean available) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.dailyRate = dailyRate;
        this.available = available;
    }
    
    public String getId() {
        return id;
    }
    
    public String getMake() {
        return make;
    }
    
    public String getModel() {
        return model;
    }
    
    public int getYear() {
        return year;
    }
    
    public String getColor() {
        return color;
    }
    
    public double getDailyRate() {
        return dailyRate;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
}

class Customer {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String address;
    
    public Customer(String id, String name, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getAddress() {
        return address;
    }
}

class Rental {
    private String id;
    private Car car;
    private Customer customer;
    private Date startDate;
    private Date endDate;
    private double totalCost;
    private boolean returned;
    
    public Rental(String id, Car car, Customer customer, Date startDate, Date endDate, double totalCost, boolean returned) {
        this.id = id;
        this.car = car;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.returned = returned;
    }
    
    public String getId() {
        return id;
    }
    
    public Car getCar() {
        return car;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public double getTotalCost() {
        return totalCost;
    }
    
    public boolean isReturned() {
        return returned;
    }
    
    public void setReturned(boolean returned) {
        this.returned = returned;
    }
}

// Data Repository (In-memory database)
class DataRepository {
    private ArrayList<Car> cars;
    private ArrayList<Customer> customers;
    private ArrayList<Rental> rentals;
    
    public DataRepository() {
        // Initialize with sample data
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
        
        // Sample cars
        cars.add(new Car("C001", "Toyota", "Camry", 2022, "Silver", 60.0, true));
        cars.add(new Car("C002", "Honda", "Accord", 2023, "Black", 65.0, true));
        cars.add(new Car("C003", "Ford", "Mustang", 2022, "Red", 85.0, true));
        cars.add(new Car("C004", "Chevrolet", "Malibu", 2023, "White", 55.0, true));
        cars.add(new Car("C005", "Nissan", "Altima", 2022, "Blue", 50.0, true));
        
        // Sample customers
        customers.add(new Customer("CUS001", "John Smith", "555-1234", "john@example.com", "123 Main St"));
        customers.add(new Customer("CUS002", "Sarah Johnson", "555-5678", "sarah@example.com", "456 Oak Ave"));
        customers.add(new Customer("CUS003", "Michael Brown", "555-9012", "michael@example.com", "789 Pine Rd"));
    }
    
    // Car methods
    public ArrayList<Car> getAllCars() {
        return cars;
    }
    
    public Car getCarById(String id) {
        for (Car car : cars) {
            if (car.getId().equals(id)) {
                return car;
            }
        }
        return null;
    }
    
    public void addCar(Car car) {
        cars.add(car);
    }
    
    public void updateCar(Car updatedCar) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getId().equals(updatedCar.getId())) {
                cars.set(i, updatedCar);
                return;
            }
        }
    }
    
    public void deleteCar(String id) {
        cars.removeIf(car -> car.getId().equals(id));
    }
    
    // Customer methods
    public ArrayList<Customer> getAllCustomers() {
        return customers;
    }
    
    public Customer getCustomerById(String id) {
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                return customer;
            }
        }
        return null;
    }
    
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }
    
    public void updateCustomer(Customer updatedCustomer) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId().equals(updatedCustomer.getId())) {
                customers.set(i, updatedCustomer);
                return;
            }
        }
    }
    
    public void deleteCustomer(String id) {
        customers.removeIf(customer -> customer.getId().equals(id));
    }
    
    // Rental methods
    public ArrayList<Rental> getAllRentals() {
        return rentals;
    }
    
    public Rental getRentalById(String id) {
        for (Rental rental : rentals) {
            if (rental.getId().equals(id)) {
                return rental;
            }
        }
        return null;
    }
    
    public void addRental(Rental rental) {
        rentals.add(rental);
    }
    
    public void updateRental(Rental updatedRental) {
        for (int i = 0; i < rentals.size(); i++) {
            if (rentals.get(i).getId().equals(updatedRental.getId())) {
                rentals.set(i, updatedRental);
                return;
            }
        }
    }
    
    public void deleteRental(String id) {
        rentals.removeIf(rental -> rental.getId().equals(id));
    }
}
