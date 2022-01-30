package org.controlmatic.server.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.controlmatic.shared.domain.ProductBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class for representing the data in an external Product Catalog.
 */
@JacksonXmlRootElement(localName = "products")
public class CatalogProduct {
    private final int id;
    private final int optLockVersion;
    private final String barcode;
    private final String name;
    private final double vat;
    private final List<String> keywords;

    public CatalogProduct() {
        this.id = 0;
        this.optLockVersion = 0;
        this.barcode = null;
        this.name = null;
        this.vat = 0;
        this.keywords = Collections.emptyList();
    }

    public int getId() {
        return id;
    }

    public int getOptLockVersion() {
        return optLockVersion;
    }

    @JacksonXmlProperty(localName = "barCode")
    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public double getVat() {
        return vat;
    }

    @JacksonXmlProperty(localName = "keyword")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<String> getKeywords() {
        // Strip empty elements
        return Optional.ofNullable(keywords).map(List::stream).orElseGet(Stream::empty).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    /**
     * Sets fields in a {@link ProductBuilder} with data from this object.
     *
     * @param builder the builder to set fields in
     */
    public void decorate(ProductBuilder builder) {
        builder.id(id).barcode(barcode).name(name).vatPercentage((int)vat).keywords(getKeywords());
    }
}
