package org.controlmatic.client.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlmatic.client.service.ServerService;
import org.controlmatic.shared.domain.Product;
import org.controlmatic.shared.domain.Sale;
import org.controlmatic.shared.domain.SaleProduct;
import org.controlmatic.client.model.SaleProductBundle;
import org.controlmatic.shared.domain.WebClientException;
import org.controlmatic.shared.request.SalesQueryRequest;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.UnaryOperator;

public class SalesDataViewController implements Initializable {

    ServerService serverService;
    @FXML
    Button searchButton;
    @FXML
    Button clearButton;
    @FXML
    TextField itemSearchInput;
    @FXML
    TextField ageLowInput;
    @FXML
    TextField ageHighInput;
    @FXML
    TableView searchResultsTable;
    @FXML
    TableColumn<SaleProductBundle, String> itemNameColumn;
    @FXML
    TableColumn<SaleProductBundle, Integer> amountColumn;
    @FXML
    ComboBox sexSelect;
    @FXML
    DatePicker dateStart;
    @FXML
    DatePicker dateEnd;

    String itemTitle;
    String sex;
    LocalDateTime startDate;
    LocalDateTime endDate;
    int ageLow;
    int ageHigh;

    List<SaleProductBundle> tableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.serverService = new ServerService("http://localhost:8080");

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        ageLowInput.setTextFormatter(new TextFormatter<String>(integerFilter));
        ageHighInput.setTextFormatter(new TextFormatter<String>(integerFilter));

        String sexes[] = {"UNSPECIFIED","MALE","FEMALE"};
        sexSelect.setItems(FXCollections.observableArrayList(sexes));

        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    private void setSearchArgs(){
        itemTitle = itemSearchInput.getText();
        sex = (String) sexSelect.getValue();
        ageLow = 0;
        ageHigh = 0;
        startDate = null;
        endDate = null;

        if (!ageLowInput.getText().equals("")){
            ageLow = Integer.parseInt(ageLowInput.getText());
        }

        if (!ageHighInput.getText().equals("")){
            ageHigh = Integer.parseInt(ageHighInput.getText());
        }

        if(dateStart.getValue() != null){
            startDate = dateStart.getValue().atStartOfDay();
        }

        if(dateEnd.getValue() != null){
            endDate = dateEnd.getValue().atTime(23,59, 59);
        }
    }

    public SalesQueryRequest configureSaleRequest(){
        SalesQueryRequest request = new SalesQueryRequest();

        if(ageLow > 0){
            request.setCustomerAgeStart(ageLow);
        }

        if(ageHigh > 0){
            request.setCustomerAgeEnd(ageHigh);
        }

        if(startDate != null){
            request.setDateTimeStart(startDate);
        }

        if(endDate != null){
            request.setDateTimeEnd(endDate);
        }

        if(sex != null && !sex.equals("All")) {
            request.setCustomerSex(sex);
        }
        
        return request;
    }

    public void search() throws WebClientException {
        searchResultsTable.getItems().clear();

        setSearchArgs();

        SalesQueryRequest saleRequest = configureSaleRequest();
        List<Sale> salesList = serverService.querySales(saleRequest);

        List<Product> productList = serverService.searchProducts(itemTitle);

        tableList = mergeSales(salesList, productList);
        addAll(tableList);
    }

    public void addAll(List<SaleProductBundle> tableList){
        for(SaleProductBundle spb : tableList){
            searchResultsTable.getItems().add(spb);
        }
    }

    private List<SaleProductBundle> mergeSales(List<Sale> saleList, List<Product> productList){
        HashMap<Integer, SaleProductBundle> tempSaleMap = new HashMap<>();
        List<Integer> tempProductIdList = new ArrayList<>();
        List<SaleProductBundle> saleProductBundleList = new ArrayList<>();

        for(Product product: productList){
            tempProductIdList.add(product.getId());
        }

        for(Sale sale : saleList){
            for(SaleProduct saleProduct : sale.getProducts()){
                int id = saleProduct.getId();

                if(tempProductIdList.contains(id) || productList.isEmpty()){
                    String title = saleProduct.getName();

                    // if map does not contain title, create new SaleProductBundle
                    if(!tempSaleMap.containsKey(id)){
                        SaleProductBundle spb = new SaleProductBundle(title);
                        tempSaleMap.put(id, spb);
                    }

                    // add amount from saleProduct to the total
                    tempSaleMap.get(id).addSaleProduct(saleProduct);
                }
            }
        }

        for(Map.Entry<Integer, SaleProductBundle> entry: tempSaleMap.entrySet()){
            saleProductBundleList.add(entry.getValue());
        }

        return saleProductBundleList;
    }



    public void clearTable(){
        ;
    }

    public void clearSearchArgs(){
        itemSearchInput.clear();
        ageLowInput.clear();
        ageHighInput.clear();
        sexSelect.valueProperty().set(null);
        dateStart.setValue(null);
        dateEnd.setValue(null);

    }


    @FXML
    public void searchButtonAction() throws WebClientException {
        search();
    }

    @FXML
    public void clearButtonAction(){
        clearSearchArgs();
    }




}



