package org.controlmatic.client.model;
import org.controlmatic.shared.domain.Product;
import org.controlmatic.shared.domain.ProductOffer;
import org.controlmatic.shared.domain.SaleProduct;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Product bundle. Combination of a product, amount, and an applied percentage
 */
public class ProductBundle {
    private final Product product;
    private int amount;
    private int discountPercentage;

    /**
     * Creates a new {@link ProductBundle} with one of the specified product.
     *
     * @param product the product to include in the bundle
     */
    public ProductBundle(Product product) {
        this.product = product;
        this.amount = 1;
        this.discountPercentage = 0;
    }

    public SaleProduct toSaleProduct() {
        return new SaleProduct(product.getId(), amount);
    }


    // ================= Product ===================

    public Product getProduct() {
        return product;
    }

    // ================== Price ====================

    public BigDecimal getTotalPrice(Boolean isBonusCustomer) {
        BigDecimal vat = new BigDecimal(getProduct().getVatPercentage()).divide(new BigDecimal(100)).add(new BigDecimal(1));
        BigDecimal discount;
        if (discountPercentage != 0) {
            discount = new BigDecimal(1).subtract(new BigDecimal(discountPercentage).divide(new BigDecimal(100)));
        } else {
            discount = product
                    .getOffers()
                    .stream()
                    .filter(ProductOffer::isActive)
                    .filter(o -> !o.isOnlyBonusCustomers() || isBonusCustomer)
                    .map(o -> new BigDecimal(1).subtract(new
 BigDecimal(o.getOfferPercentage()).divide(new BigDecimal(100))))
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ONE);
        }
        return getProduct().getPrice().multiply(vat).multiply(discount).multiply(new BigDecimal(getAmount())).setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }


    public int getDiscountPercentage() {
        return discountPercentage;
    }


    // ================== Amount ===================

    public void setAmount(int amount) {
        this.amount = Math.max(amount, 1);

    }

    public int getAmount() {
        return amount;
    }
}
