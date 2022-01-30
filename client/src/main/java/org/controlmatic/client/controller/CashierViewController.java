package org.controlmatic.client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlmatic.client.model.*;
import org.controlmatic.client.service.CardReaderService;
import org.controlmatic.client.service.CashBoxService;
import org.controlmatic.client.service.ServerService;
import org.controlmatic.client.util.StringUtil;
import org.controlmatic.shared.domain.BonusCard;
import org.controlmatic.shared.domain.Customer;
import org.controlmatic.shared.domain.Product;
import org.controlmatic.shared.domain.WebClientException;
import org.controlmatic.shared.request.SalesCreateRequest;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;


/**
 * Controller for Cashier View FXML
 **/
public class CashierViewController {

    private CustomerViewController customerViewController;
    private ProductSearchController productSearchController;

    private final CardReaderService cardReaderService;
    private final CashBoxService cashBoxService;
    private final ServerService serverService;


    private ArrayList<ShoppingCartController> shoppingCartShelf;

    private ShoppingCartController currShoppingCart;

    ProductBundleController selectedBundle;

    @FXML
    Button productSearchButton;

    @FXML
    Button printReceiptButton;

    @FXML
    Button completeSaleButton;

    @FXML
    Button cashBoxButton;

    @FXML
    Button cardPaymentButton;

    @FXML
    Button cashPaymentButton;

    @FXML
    Button selectedDiscount0;

    @FXML
    Button selectedDiscount30;

    @FXML
    Button selectedDiscount60;

    @FXML
    VBox shoppingCartVBox;

    @FXML
    VBox shoppingCartShelfVBox;

    @FXML
    Label selectedTitleLabel;

    @FXML
    Label selectedPriceLabel;

    @FXML
    Label selectedDiscountLabel;

    @FXML
    TextField selectedTextField;

    @FXML
    TextField productSearchTextField;

    @FXML
    TextField discountInput;

    @FXML
    TextField paymentSumInput;

    @FXML
    Label totalSum;

    @FXML
    Label totalPaidAmount;

    @FXML
    Label changeLabel;

    @FXML
    Label paymentStateLabel;

    @FXML
    Label bonusCardStateLabel;

    @FXML
    Pane bonusCardStatePane;

    @FXML
    Button selectedSpinnerAddButton;

    @FXML
    Button selectedSpinnerRemoveButton;

    @FXML
    TextField barcodeInput;

    @FXML
    Pane paymentStatePane;

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


    public CashierViewController() throws IOException {
        shoppingCartShelf = new ArrayList<>();
        cardReaderService = new CardReaderService("http://localhost:9002", this::onCardReaderUpdate);
        cashBoxService = new CashBoxService("http://localhost:9001");
        serverService = new ServerService("http://localhost:8080");

    }

    public void setCustomerViewController(CustomerViewController customerViewController) {
        this.customerViewController = customerViewController;
    }

    public void configureNodes() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        UnaryOperator<TextFormatter.Change> paymentFilter = change -> {
            String input = change.getText();
            String text = paymentSumInput.getText() + input;
            if ((input.matches("[0-9\\.]*")
                    && (text.matches("[0-9]*") || text.matches("[0-9]*\\.[0-9]{0,2}")))
                    || input.matches("\b}") || input.matches("[0-9]*\\.[0-9]{0,2}")) {
                return change;
            }
            return null;
        };

        paymentSumInput.setTextFormatter(new TextFormatter<String>(paymentFilter));
        discountInput.setTextFormatter(new TextFormatter<String>(integerFilter));
        selectedTextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        barcodeInput.setTextFormatter(new TextFormatter<String>(integerFilter));


        //sets up the search TableView using a productSearchController
        this.productSearchController = new ProductSearchController(serverService,
                p -> this.addProduct(p, currShoppingCart != null && currShoppingCart.isBonusCustomer()),
                tableView, nameColumn, priceColumn,
                vatColumn);

        setSelectedBundle(null);
    }


    //================== Shopping Cart ===========================

    public void openNewShoppingCart() {
        addCurrentShoppingCartToShelf();

        ShoppingCartController shoppingCart = new ShoppingCartController();
        shoppingCart.initPanes(this);

        setShoppingCart(shoppingCart);
    }

    public void setShoppingCart(ShoppingCartController shoppingCart) {
        //check the new shoppingcart bonus state, if a bonus card has already been scanned, this will reset the cardreader
        try {
            if (shoppingCart.isBonusCustomer()) {
                cardReaderService.resetReader();
            } else {
                cardReaderService.readBonusCard();
            }

        } catch (WebClientException e) {
            e.printStackTrace();
        }
        currShoppingCart = shoppingCart;
        refreshViews();
    }

    public ShoppingCartController getCurrShoppingCartController() {
        return currShoppingCart;
    }

    public void deleteCart() {
        getCurrShoppingCartController().clearCart();
        openNewShoppingCart();

    }

    public void addShoppingCartToShelf(ShoppingCartController shoppingCart) {
        if (currShoppingCart != null) {
            if (!shoppingCart.isEmpty()) {
                shoppingCartShelf.add(shoppingCart);
                addShoppingCartToShelfView(shoppingCart);
            }
        }
    }

    public void addCurrentShoppingCartToShelf() {
        addShoppingCartToShelf(currShoppingCart);
    }

    public void removeShoppingCartFromShelf(ShoppingCartController shoppingCart) {
        shoppingCartShelf.remove(shoppingCart);
        removeShoppingCartFromShelfView(shoppingCart);
    }

    public void setShoppingCartFromShelf(ShoppingCartController shoppingCart) {
        addCurrentShoppingCartToShelf();
        removeShoppingCartFromShelf(shoppingCart);
        setShoppingCart(shoppingCart);
    }

    // =================== Payment Logic ========================

    public void updateBonusCardState(){
        if(currShoppingCart.getBonusCustomerId() != 0){
            setBonusCardScanned();
        }else{
            setBonusCardUnscanned();
        }
    }


    public void makeCashPayment() throws WebClientException {
        //cashBox.open();
        BigDecimal cashPaymentAmount = new BigDecimal(paymentSumInput.getText());
        currShoppingCart.addPayment(cashPaymentAmount);
        updateViews();
    }

    public void makeCardPayment() throws WebClientException {
        BigDecimal paymentAmount = new BigDecimal(paymentSumInput.getText());
        cardReaderService.readPayment(paymentAmount);
    }

    public void onCardReaderUpdate(CardReaderCallback callback) {
        System.out.println("Card reader state changed: " + callback.getStatus());
        CardReaderStatus cardReaderStatus = callback.getStatus();
        switch (cardReaderStatus) {
            case WAITING_FOR_CARD:
                setPaymentStateAwaiting();
                break;
            case IDLE:
                setPaymentStateIdle();
                break;
            case DONE:
                CardReaderResult result = callback.getResult();

                if (result.IsBonusCard()) {
                    try {
                        if (result.getBonusState() != CardReaderResult.BonusCardState.ACCEPTED) {
                            throw new WebClientException("Invalid bonus card state " + result.getBonusState());
                        }

                        // Look up bonus customer
                        Customer customer = serverService.getCustomerByCard(result.getBonusCardNumber(), result.getGoodThruMonth(), result.getGoodThruYear());
                        if (customer.getBonusCards().stream().noneMatch(c -> c.getNumber().equals(result.getBonusCardNumber()) && c.getGoodThruMonth() == result.getGoodThruMonth() && c.getGoodThruYear() == result.getGoodThruYear() && !c.isBlocked() && !c.isExpired())) {
                            throw new WebClientException("Invalid bonus card");
                        }
                        currShoppingCart.setBonusCustomerId(customer.getId());
                        setPaymentStateBonusScanned();
                    }
                    catch (WebClientException e) {
                        System.out.println("Failed to get bonus customer: " + e);
                        setPaymentStateFailed();

                        // Assume we want to continue reading for a bonus card
                        try {
                            cardReaderService.readBonusCard();
                        }
                        catch (WebClientException ignored) {
                        }
                    }
                }
                else if (result.IsPaymentCard() && result.getPaymentState() == CardReaderResult.PaymentCardState.ACCEPTED) {
                    setPaymentStateDone();
                    currShoppingCart.addPayment(new BigDecimal(paymentSumInput.getText()));
                }
                else {
                    setPaymentStateFailed();
                }
                updateViews();
        }
    }

    // =================== ProductBundle stuff ========================

    public void addProduct(Product product, Boolean isBonusCustomer) {
        if (currShoppingCart == null) {
            openNewShoppingCart();
        }

        ProductBundleController bundle = new ProductBundleController(product);
        bundle.initPanes(this, isBonusCustomer);

        currShoppingCart.addBundle(bundle);

        addBundleToView(bundle);
        setSelectedBundle(bundle);
    }


    public void deleteProductBundle(ProductBundleController bundle) {
        if (bundle != null) {
            removeBundleFromView(bundle);
            getCurrShoppingCartController().removeBundle(bundle);
            updateViews();
        }
        setSelectedBundle(null);
    }


    public void deleteSelectedProductBundle() {
        deleteProductBundle(getSelectedBundle());
        updateViews();
    }

    public void setDiscountToSelectedProductBundle(int discountPercentage) {
        ProductBundleController selectedProductBundle;
        selectedProductBundle = getSelectedBundle();
        selectedProductBundle.setDiscountPercentage(discountPercentage);
        updateViews();
    }

    public void setSelectedBundle(ProductBundleController bundleController) {
        if (selectedBundle != null) {
            selectedBundle.unselect();
        }

        selectedBundle = bundleController;

        if (selectedBundle != null) {
            selectedBundle.select();
        }
        updateViews();
    }


    // =================== View Stuff =========================

    public void updatePaymentFieldAmount() {
        String newPriceString = currShoppingCart.getCashOwed().setScale(2, RoundingMode.HALF_EVEN).toString();
        paymentSumInput.clear();
        paymentSumInput.setText(newPriceString);
        formatPaymentFieldAmount();
    }

    public void formatPaymentFieldAmount() {
        String text = paymentSumInput.getText();
        paymentSumInput.clear();

        String formatedText;

        if (text == "") {
            formatedText = "";
        } else if (Double.valueOf(text) == 0.0) {
            formatedText = "";
        } else {
            formatedText = StringUtil.toPriceString(text);
        }

        paymentSumInput.setText(formatedText);
    }

    public void updateChangeSum() {
        String changeString = currShoppingCart.getCashOwed().setScale(2, RoundingMode.HALF_EVEN).toString();

        changeString = StringUtil.toEuro(changeString);
        changeLabel.setText(changeString);
        customerViewController.updateChangeSum(changeString);
    }


    public void addBundleToView(ProductBundleController bundle) {
        ProductBundlePane cashierPBP;
        ProductBundlePane customerPBP;

        // add to cashier view
        cashierPBP = bundle.getCashierPBP();
        shoppingCartVBox.getChildren().add(0, cashierPBP);

        // add to customer view
        customerPBP = bundle.getCustomerPBP();
        customerViewController.updateCart(customerPBP);

    }

    public void removeBundleFromView(ProductBundleController bundle) {
        ProductBundleController selectedProductBundle;

        selectedProductBundle = getSelectedBundle();

        ProductBundlePane cashierPBP = selectedProductBundle.getCashierPBP();
        ProductBundlePane customerPBP = selectedProductBundle.getCustomerPBP();

        shoppingCartVBox.getChildren().remove(cashierPBP);
        customerViewController.removeItem(customerPBP);
    }


    public void disableSelectedView() {
        selectedTextField.setDisable(true);
        selectedDiscount0.setDisable(true);
        selectedDiscount30.setDisable(true);
        selectedDiscount60.setDisable(true);
        discountInput.setDisable(true);
        selectedSpinnerAddButton.setDisable(true);
        selectedSpinnerRemoveButton.setDisable(true);
    }

    public void enableSelectedView() {
        selectedTextField.setDisable(false);
        selectedDiscount0.setDisable(false);
        selectedDiscount30.setDisable(false);
        selectedDiscount60.setDisable(false);
        discountInput.setDisable(false);
        selectedSpinnerAddButton.setDisable(false);
        selectedSpinnerRemoveButton.setDisable(false);
    }


    public void updateSelectedBundleView() {
        String title;
        String price;
        String amount;
        String discountString;
        String discountLabelString;
        String discountInputString;
        int discountInt;


        ProductBundleController selectedBundle = getSelectedBundle();

        if (selectedBundle != null) {
            title = selectedBundle.getProduct().getName();
            price = selectedBundle.getTotalPrice(currShoppingCart.isBonusCustomer()).toString();
            amount = String.valueOf(selectedBundle.getAmount());
            discountInt = selectedBundle.getDiscountPercentage();
            discountString = String.valueOf(discountInt);
            discountInputString = String.valueOf(discountInt);

            price = StringUtil.toEuro(price);

            enableSelectedView();

            // set text on labels that dont need modification
            selectedTitleLabel.setText(title);
            selectedPriceLabel.setText(price);
            selectedTextField.setText(amount);

            if (discountInt == 0) {
                discountLabelString = "";
            } else {
                discountLabelString = "(-" + discountString + "%)";
            }
            selectedDiscountLabel.setText(discountLabelString);

            if (discountInt == 0) {
                selectedDiscount0.setDisable(true);
            } else if (discountInt == 30) {
                selectedDiscount30.setDisable(true);
            } else if (discountInt == 60) {
                selectedDiscount60.setDisable(true);
            }

            selectedBundle.updatePanes(currShoppingCart.isBonusCustomer());
        } else {
            disableSelectedView();
            selectedTitleLabel.setText("");
            selectedPriceLabel.setText("");
            selectedTextField.setText("");
            selectedDiscountLabel.setText("");
        }
        discountInput.setText("");

        // otherwise focuses random stuff
        // like discount manual input, if -60% button was pressed
        // and if discount manual input was applied its just nice to unfocus it
        shoppingCartVBox.requestFocus();
    }

    public void updateTotalSum() {
        BigDecimal totalPriceSum = getCurrShoppingCartController().getTotalPrice();
        String totalPriceSumString = totalPriceSum.toString();
        totalPriceSumString = StringUtil.toEuro(totalPriceSumString);

        totalSum.setText(totalPriceSumString);
        customerViewController.updateTotalSum(totalPriceSumString);
    }

    public void updateTotalPaidSum() {
        String totalPaid = currShoppingCart.getTotalPaid().setScale(2, RoundingMode.HALF_EVEN).toString();
        totalPaidAmount.setText(StringUtil.toEuro(totalPaid));
        customerViewController.updateTotalPaidAmount(totalPaid);
    }

    public void updateShelfView() {
        shoppingCartShelfVBox.getChildren().clear();
        shoppingCartShelf.sort((o1, o2)
                -> o1.getDateTime().compareTo(
                o2.getDateTime()));
        for (ShoppingCartController shoppingCart : shoppingCartShelf) {
            addShoppingCartToShelfView(shoppingCart);
        }
    }

    public void updateViews() {
        if (currShoppingCart != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    updateSelectedBundleView();
                    updateTotalSum();
                    updatePaymentFieldAmount();
                    updateChangeSum();
                    updateTotalPaidSum();
                    updateBonusCardState();
                }
            });
        }
    }

    ;

    public void addShoppingCartToShelfView(ShoppingCartController shoppingCart) {
        shoppingCartShelfVBox.getChildren().add(shoppingCart.getShelfPane());
    }

    public void removeShoppingCartFromShelfView(ShoppingCartController shoppingCart) {
        shoppingCartShelfVBox.getChildren().remove(shoppingCart.getShelfPane());
    }

    public VBox getShoppingCartControllerVBox() {
        return shoppingCartVBox;
    }

    /**
     * clears all views and resets them
     * call this if current shopping cart has changed
     */
    public void refreshViews() {
        shoppingCartVBox.getChildren().clear();
        customerViewController.clearCart();

        setSelectedBundle(null);

        for (ProductBundleController bundle : getCurrShoppingCartController().getProductBundles()) {
            addBundleToView(bundle);
        }

        updateViews();
        updateShelfView();
    }

    public ProductBundleController getSelectedBundle() {
        return selectedBundle;
    }

    public void setPaymentStateIdle() {
        Platform.runLater(() -> {
            paymentStatePane.setStyle("-fx-background-color: #b6b6b6");
            paymentStateLabel.setText("Card Payment State");
        });
    }

    public void setPaymentStateAwaiting() {
        Platform.runLater(() -> {
            paymentStatePane.setStyle("-fx-background-color: #fdbf53");
            paymentStateLabel.setText("Awaiting Card Payment");
        });
    }

    public void setPaymentStateDone() {
        Platform.runLater(() -> {
            paymentStatePane.setStyle("-fx-background-color: #57fd57");
            paymentStateLabel.setText("Card Payment Accepted");
        });

    }

    public void setPaymentStateFailed() {
        Platform.runLater(() -> {
            paymentStatePane.setStyle("-fx-background-color: #ff6363");
            paymentStateLabel.setText("Card Payment Failed");
        });
    }


    public void setPaymentStateBonusScanned() {
        Platform.runLater(() -> {
            paymentStatePane.setStyle("-fx-background-color: #5ea9ff");
            paymentStateLabel.setText("Bonus Card Scanned");
        });
    }


    public void setBonusCardUnscanned() {
        Platform.runLater(() -> {
            bonusCardStatePane.setStyle("-fx-background-color: #5ea9ff");
            bonusCardStateLabel.setText("Bonus Card Not Scanned");
        });
    }
    public void setBonusCardScanned() {
        Platform.runLater(() -> {
            bonusCardStatePane.setStyle("-fx-background-color: #1fb21c");
            bonusCardStateLabel.setText("Bonus Card Scanned");
        });
    }




    // =================== Button Actions ===================

    @FXML
    public void productSearch() {
        try {
            productSearchController.searchAny(productSearchTextField.getText());
        } catch (Exception ignore) {}
    }

    @FXML
    public void shelfButtonAction() {
        openNewShoppingCart();
    }

    @FXML
    public void deleteCartButtonAction() {
        deleteCart();
    }

    @FXML
    public void deleteButtonAction() {
        deleteSelectedProductBundle();
    }

    @FXML
    public void openCashBox() throws WebClientException {
        cashBoxService.open();
    }

    @FXML
    public void setSelectedDiscount0() {
        setDiscountToSelectedProductBundle(0);
    }

    @FXML
    public void setSelectedDiscount30() {
        setDiscountToSelectedProductBundle(30);
    }

    @FXML
    public void setSelectedDiscount60() {
        setDiscountToSelectedProductBundle(60);
    }

    @FXML
    public void setSelectedDiscountManual(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String text = discountInput.getText();
            int amount = Integer.valueOf(text);

            setDiscountToSelectedProductBundle(amount);

        }
    }

    @FXML
    public void cardPayment() throws WebClientException {
        makeCardPayment();
    }

    @FXML
    public void cashPayment() throws WebClientException {
        makeCashPayment();
    }

    @FXML
    public void printReceipt() throws IOException, WebClientException {
        if (currShoppingCart != null && currShoppingCart.checkComplete()) {

            //Creates an png image file
            ReceiptAsImage receiptAsImage = new ReceiptAsImage();
            File file = receiptAsImage.printReceipt(currShoppingCart); //File is png image

            //The image will be opened on an image viewer and the png file still exists in the directory
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) {
                desktop.open(file);
            } else {
                System.out.println("The image file cannot be opened.");
            }
            completeSale();
        } else {
            System.out.println("Cannot print receipt, sale not completed");
        }
    }

    @FXML
    public void completeSale() throws WebClientException {
        if (currShoppingCart != null && currShoppingCart.checkComplete()) {
            // Store sale on server
            SalesCreateRequest request = new SalesCreateRequest();
            if (currShoppingCart.isBonusCustomer()) {
                request.setCustomerId(currShoppingCart.getBonusCustomerId());
            }
            request.setProducts(currShoppingCart.getProductBundles().stream().map((b) -> b.bundle.toSaleProduct()).collect(Collectors.toList()));
            request.setTotalPrice(currShoppingCart.getTotalPrice());
            request.setTimestamp(LocalDateTime.now());
            serverService.createSale(request);

            deleteCart();
            openNewShoppingCart();
        } else {
            System.out.println("Cannot Complete Sale");
        }
    }

    @FXML
    public void selectedSpinnerAdd() {
        ProductBundleController selectedBundle = getSelectedBundle();
        selectedBundle.addToAmount(1);
        updateViews();
    }

    @FXML
    public void selectedSpinnerRemove() {
        ProductBundleController selectedBundle = getSelectedBundle();
        selectedBundle.addToAmount(-1);
        updateViews();
    }

    @FXML
    public void onSelectedSpinnerChanged(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String text = selectedTextField.getText();
            int amount = Integer.valueOf(text);

            ProductBundleController selectedBundle = getSelectedBundle();

            selectedBundle.setAmount(amount);

            updateViews();
        }
    }

    @FXML
    public void onBarcodeInputEnter(ActionEvent ae) {
        String barcode = barcodeInput.getText();
        if (barcode.isEmpty()) {
            return;
        }

        try {
            Product product = serverService.getProductByBarcode(barcode);
            if (product == null) {
                throw new Exception("Product not found");
            }

            addProduct(product, currShoppingCart != null && currShoppingCart.isBonusCustomer());
            updateViews();
        } catch (Exception ex) {
            System.out.println("Failed to get product: " + ex);
        }
        barcodeInput.clear();
    }

    @FXML
    public void paymentInputEvent(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            formatPaymentFieldAmount();
            shoppingCartVBox.requestFocus();
        }
    }
}