package org.controlmatic.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.controlmatic.client.service.ServerService;
import org.controlmatic.shared.domain.Product;
import org.controlmatic.shared.domain.ProductOffer;
import org.controlmatic.shared.domain.WebClientException;
import org.controlmatic.shared.request.ProductUpdateRequest;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;


/**
 * Controller for the sale panel within the admin view.
 */
public class SalePanelController implements Initializable {

    private ServerService serverService;
    private ProductSearchController productSearchController;
    private Product selectedProduct;

    @FXML
    public DatePicker startDate;
    @FXML
    public DatePicker endDate;
    @FXML
    public TextField priceField;
    @FXML
    public CheckBox offerBonusOnly;
    @FXML
    public TextField offerPriceField;
    @FXML
    public Button setPriceButton;
    @FXML
    TableView<Product> tableView;
    @FXML
    TableColumn<Product, String> nameColumn;
    @FXML
    TableColumn<Product, BigDecimal> priceColumn;
    @FXML
    TableColumn<Product, Integer> vatColumn;
    @FXML
    TableColumn<Product, String> barcodeColumn;
    @FXML
    TextField productSearchTextField;

    public void productSearch(ActionEvent actionEvent) {
        try {
            productSearchController.searchAny(productSearchTextField.getText());
        } catch (Exception ignore) {}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverService = new ServerService("http://localhost:8080");
        productSearchController = new ProductSearchController(
                serverService,
                (p) -> {
                    selectedProduct = p;
                },
                tableView,
                nameColumn,
                priceColumn,
                vatColumn);
    }

    public void setProductPrice(ActionEvent actionEvent) {
        try {
            if (selectedProduct != null) {
                var u = new ProductUpdateRequest();
                u.setPrice((new BigDecimal(priceField.getText())));
                serverService.updateProduct(selectedProduct.getId(), u);
            }
        } catch(Exception ignore) {}
    }

    public void setProductOffer(ActionEvent actionEvent) throws WebClientException {
        if (selectedProduct != null) {
            var u = new ProductUpdateRequest();
            var offers = selectedProduct.getOffers();
            offers.add(new ProductOffer(
                    Integer.parseInt(offerPriceField.getText()),
                    startDate.getValue().atStartOfDay(),
                    endDate.getValue().atTime(23, 59, 59),
                    offerBonusOnly.isSelected()
            ));
            u.setOffers(offers);
            serverService.updateProduct(selectedProduct.getId(), u);
        }
    }

    public void clearProductOffers(ActionEvent actionEvent) throws WebClientException {
        if (selectedProduct != null) {
            var u = new ProductUpdateRequest();
            u.setOffers(Collections.emptyList());
            serverService.updateProduct(selectedProduct.getId(), u);
        }
    }
}