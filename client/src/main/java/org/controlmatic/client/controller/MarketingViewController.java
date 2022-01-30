package org.controlmatic.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlmatic.client.service.ServerService;
import org.controlmatic.shared.domain.*;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the marketing view pane within the admin view
 */
public class MarketingViewController implements Initializable {

    private ServerService serverService;
    private Customer selectedCustomer = null;
    @FXML
    Button searchButton;

    @FXML
    TableView<Customer> searchResultsTable;

    @FXML
    TableView<Sale> salesTable;

    @FXML
    TableView<SaleProduct> productsTable;

    @FXML
    TextField customerIDField;

    @FXML
    TableColumn<Customer, String> customerNameColumn;

    @FXML
    TableColumn<Customer, Integer> customerIDColumn;

    @FXML
    TableColumn<Customer, Integer> customerSaleIDColumn;

    @FXML
    TableColumn<Customer, LocalDateTime> customerSaleDateColumn;

    @FXML
    TableColumn<Customer, BigDecimal> customerSalePriceColumn;

    @FXML
    TableColumn<Customer, String> saleProductNameColumn;

    @FXML
    TableColumn<Customer, Integer> saleProductAmountColumn;

    @FXML
    Label idLabel;

    @FXML
    Label firstNameLabel;

    @FXML
    Label lastNameLabel;

    @FXML
    Label dobLabel;

    @FXML
    Label sexLabel;

    @FXML
    Label addressLabel;

    @FXML
    Label bonusPointsLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.serverService = new ServerService("http://localhost:8080");

        //Customer ID Table
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        //Sales Table
        customerSaleIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerSaleDateColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        customerSalePriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        //Sales Info Table
        saleProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        saleProductAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));


        //Single-click results to view extra customer data
        searchResultsTable.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    if(selectedCustomer != row.getItem()) {
                        Customer customer = row.getItem();
                        updateLabelData(customer);
                        try {
                            updateSales(customer);
                        } catch (WebClientException e) {
                            e.printStackTrace();
                        }
                        selectedCustomer = customer;
                        System.out.println("testing");
                    }else{
                        Customer customer = row.getItem();
                        try {
                            updateSales(customer);
                        } catch (WebClientException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            return row;
        });

        //Singe-click to view content of clicked sale
        salesTable.setRowFactory(tv -> {
            TableRow<Sale> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Sale sale = row.getItem();
                    updateSalesInfo(sale);
                }
            });
            return row;
        });

    }

    public void search() throws WebClientException {
        String searchString = customerIDField.getText();
        if (searchString.isEmpty()) {
            getAllCustomers();
        } else {
            List<Customer> customers = serverService.searchCustomers(searchString);
            searchResultsTable.getItems().clear();
            for(Customer customer : customers){
                searchResultsTable.getItems().add(customer);
            }
        }
    }

    public void getAllCustomers() throws WebClientException {
        searchResultsTable.getItems().clear();
        List<Customer> customers = serverService.getAllCustomers();
        for(Customer customer: customers){
            searchResultsTable.getItems().add(customer);
        }
    }

    public void updateSales(Customer customer) throws WebClientException {
        int customerID = customer.getId();
        salesTable.getItems().clear();
        List<Sale> customerSales = serverService.getSalesByCustomerId(customerID);
        for(Sale sale: customerSales){
            salesTable.getItems().add(sale);
        }
    }

    public void updateSalesInfo(Sale sale){
        productsTable.getItems().clear();
        List<SaleProduct> saleProducts = sale.getProducts();
        for(SaleProduct saleProduct : saleProducts){
            productsTable.getItems().add(saleProduct);
        }
    }

    public void updateLabelData(Customer customer){
        idLabel.setText("Customer ID : " + customer.getId());
        firstNameLabel.setText("First Name : " + customer.getFirstName());
        lastNameLabel.setText("Last Name : " + customer.getLastName());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dobLabel.setText("Date Of Birth : " + customer.getBirthDate().format(dtf));
        sexLabel.setText("Sex : " + customer.getSex());
        addressLabel.setText("Address : " + customer.getAddress().getStreetAddress() + "," + customer.getAddress().getCountry());
        bonusPointsLabel.setText("Customer Bonus Points : " + customer.getBonusPoints());
    }

    public void test(){
        System.out.println("this is a test function please kill it!!!!!!");
        //adds a fake thign to the thing. COME ON YOU KNOW.... THE THING!
        Customer customer = new Customer();
        ArrayList<BonusCard> cards = new ArrayList<>();
        var bonuspoint = new BigDecimal("46566156");
        var address = new CustomerAddress("Casper Please stop it with this now!", 666, "This is too much nueanced support", "my fingers hurt");
        var cust = new Customer(16516556, "Testman", "TestingTon", LocalDateTime.now(), address, cards, bonuspoint, "Sexy");
        searchResultsTable.getItems().add(cust);
        System.out.println(cust.getLastName());
        var saleprod = new SaleProduct(46456,3,"a4sd56s");
        productsTable.getItems().add(saleprod);
        var sls = new Sale(2,15156,null,new BigDecimal("22"),LocalDateTime.now());
        salesTable.getItems().add(sls);
    }
}
