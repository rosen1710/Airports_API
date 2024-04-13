package com.example.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Country implements Serializable {

    @NaturalId
    @Column(unique = true, nullable = false)
    private String name;

    @Id
    @Column(unique = true, nullable = false)
    private String iso2CountryCode;

    @NaturalId
    @Column(unique = true, nullable = false)
    private String iso3CountryCode;

    @OneToMany(mappedBy = "country")
    private Set<City> cities = new HashSet<>();

    public Country(String name, String iso2CountryCode, String iso3CountryCode) {
        this.name = name;
        this.iso2CountryCode = iso2CountryCode;
        this.iso3CountryCode = iso3CountryCode;
    }

    public Set<City> getCities() {
        return cities;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso2CountryCode() {
        return iso2CountryCode;
    }

    public void setIso2CountryCode(String iso2CountryCode) {
        this.iso2CountryCode = iso2CountryCode;
    }

    public String getIso3CountryCode() {
        return iso3CountryCode;
    }

    public void setIso3CountryCode(String iso3CountryCode) {
        this.iso3CountryCode = iso3CountryCode;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Country country = (Country) o;
        return getIso2CountryCode() != null && Objects.equals(getIso2CountryCode(), country.getIso2CountryCode());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "name = " + name + ", " +
                "iso2CountryCode = " + iso2CountryCode + ", " +
                "iso3CountryCode = " + iso3CountryCode + ")";
    }
}
