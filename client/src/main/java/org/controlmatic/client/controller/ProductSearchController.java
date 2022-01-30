package org.controlmatic.client.controller;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlmatic.client.service.ServerService;
import org.controlmatic.shared.domain.Product;
import org.controlmatic.shared.domain.WebClientException;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

/**
 * Controller for the product search module. The action to be taken on selection is supplied by a lambda.
 */
public class ProductSearchController {

    private ServerService serverService;

    private TableView<Product> tableView;
    private TableColumn<Product, String> nameColumn;
    private TableColumn<Product, BigDecimal> priceColumn;
    private TableColumn<Product, Integer> vatColumn;
    private TableColumn<Product, String> barcodeColumn;


    public ProductSearchController(ServerService serverService,
                                   Consumer<Product> onSelect,
                                   TableView<Product> tableView,
                                   TableColumn<Product, String> nameColumn,
                                   TableColumn<Product, BigDecimal> priceColumn,
                                   TableColumn<Product, Integer> vatColumn) {
        this.tableView = tableView;
        this.nameColumn = nameColumn;
        this.priceColumn = priceColumn;
        this.vatColumn = vatColumn;
        //this.barcodeColumn = barcodeColumn;
        this.serverService = serverService;

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        vatColumn.setCellValueFactory(new PropertyValueFactory<>("vatPercentage"));
        //barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));

        // Adds ability to add product to shoppingCart by single clicking the object in the table
        tableView.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Product productToAdd = row.getItem();
                    onSelect.accept(productToAdd);
                }
            });
            return row;
        });
    }

    public void searchAny(String search) throws WebClientException {
        tableView.getItems().clear();
        updateResults(serverService.searchProducts(search));
    }

    private void updateResults(List<Product> products) {
        if(products.isEmpty()) {
            tableView.setPlaceholder(new Label("No products found!"));
        }else{
            for(Product product: products){
                tableView.getItems().add(product);
            }
        }
    }
}
