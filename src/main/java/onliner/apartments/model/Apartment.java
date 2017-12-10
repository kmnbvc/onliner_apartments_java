package onliner.apartments.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "apartments")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Apartment implements Identifiable<Long> {

    //{"id":270771,"price":{"amount":"300.00","currency":"USD","converted":{"BYN":{"amount":"601.50","currency":"BYN"},"USD":{"amount":"300.00","currency":"USD"}}},"rent_type":"2_rooms","location":{"address":"\u041c\u0438\u043d\u0441\u043a, \u0443\u043b\u0438\u0446\u0430 \u0428\u0430\u0440\u0430\u043d\u0433\u043e\u0432\u0438\u0447\u0430, 60","user_address":"\u041c\u0438\u043d\u0441\u043a, \u0443\u043b\u0438\u0446\u0430 \u0428\u0430\u0440\u0430\u043d\u0433\u043e\u0432\u0438\u0447\u0430, 60","latitude":53.881855,"longitude":27.430285},"photo":"https:\/\/content.onliner.by\/apartment_rentals\/384755\/600x400\/b5a83dcf61d7e1e90ff8049a3da0f29d.jpeg","contact":{"owner":true},"created_at":"2017-11-01T23:54:00+0300","last_time_up":"2017-11-19T18:11:43+0300","up_available_in":79746,"url":"https:\/\/r.onliner.by\/ak\/apartments\/270771"}

    @Id
    private Long id;
    private String url;
    @Embedded
    private Price price;
    @Embedded
    private Location location;
    @JsonProperty("rent_type")
    private String type;
    private Boolean favorite = Boolean.FALSE;
    @JsonProperty("last_time_up")
    private Date updated;
    @JsonProperty("photo")
    private String photoUrl;
    private String text;
    private String phone;
    private Boolean active = Boolean.TRUE;
    @ManyToOne
    @NotNull
    private Source source;
    private Boolean ignored = Boolean.FALSE;
    private String note;
    @Embedded
    private Contact contact;
    @ElementCollection
    @CollectionTable(name = "images")
    @Column(name = "url")
    private List<String> images = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Boolean getIgnored() {
        return ignored;
    }

    public void setIgnored(Boolean ignored) {
        this.ignored = ignored;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Apartment apartment = (Apartment) o;

        return id != null ? id.equals(apartment.id) : apartment.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
