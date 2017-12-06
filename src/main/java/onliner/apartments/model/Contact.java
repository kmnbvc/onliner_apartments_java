package onliner.apartments.model;

import javax.persistence.Embeddable;

@Embeddable
public class Contact {
    private Boolean owner = Boolean.FALSE;

    public Boolean getOwner() {
        return owner;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        return owner != null ? owner.equals(contact.owner) : contact.owner == null;
    }

    @Override
    public int hashCode() {
        return owner != null ? owner.hashCode() : 0;
    }
}
