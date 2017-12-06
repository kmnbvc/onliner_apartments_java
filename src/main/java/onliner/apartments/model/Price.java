package onliner.apartments.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Embeddable
public class Price {

    @Column(name = "price", columnDefinition = "DECIMAL(9,2)")
    private Double amount;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Transient
    @JsonIgnore
    private Map<Currency, Price> converted = new HashMap<>();

    public enum Currency {
        BYN, USD
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Map<Currency, Price> getConverted() {
        return converted;
    }

    public void setConverted(Map<Currency, Price> converted) {
        this.converted = converted;
    }
}
