package by.russianzak.kafkatask.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
public class ProductCreateEvent {
    private String productId;
    private String title;
    private BigDecimal price;
    private Long quantity;
    private boolean isResponse;

    public ProductCreateEvent() {
    }

    public ProductCreateEvent(String productId, String title, BigDecimal price, Long quantity) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public boolean isResponse() {
        return isResponse;
    }

    public void setResponse(boolean response) {
        isResponse = response;
    }
}
