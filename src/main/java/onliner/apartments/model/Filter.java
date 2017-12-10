package onliner.apartments.model;

import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "filters")
public class Filter implements Identifiable<String> {

    @Id
    private String name;
    @Column(name = "from_date")
    private Date from;
    @ManyToOne
    private Source source;
    @Enumerated(EnumType.STRING)
    private Active active = Active.ALL;
    @Enumerated(EnumType.STRING)
    private Owner owner = Owner.ANY;

    public enum Active {ALL, ACTIVE_ONLY, INACTIVE_ONLY}

    public enum Owner {ANY, OWNER, NOT_OWNER}

    public String getId() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Active getActive() {
        return active;
    }

    public void setActive(Active active) {
        this.active = active;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Filter filter = (Filter) o;

        return name != null ? name.equals(filter.name) : filter.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
