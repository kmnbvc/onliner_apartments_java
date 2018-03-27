package onliner.apartments.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;

@Embeddable
public class PriceRange {
    @Column(name = "price_min")
    @Min(1)
    private Integer min;
    @Column(name = "price_max")
    private Integer max;
    @Column(name = "price_currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
