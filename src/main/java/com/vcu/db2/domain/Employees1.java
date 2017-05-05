package com.vcu.db2.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Employees1.
 */
@Entity
@Table(name = "employees_1")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Employees1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 9, max = 9)
    @Column(name = "ssn", length = 9, nullable = false)
    private String ssn;

    @Column(name = "contact_number")
    private String contact_number;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "address")
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSsn() {
        return ssn;
    }

    public Employees1 ssn(String ssn) {
        this.ssn = ssn;
        return this;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getContact_number() {
        return contact_number;
    }

    public Employees1 contact_number(String contact_number) {
        this.contact_number = contact_number;
        return this;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public Employees1 first_name(String first_name) {
        this.first_name = first_name;
        return this;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Employees1 last_name(String last_name) {
        this.last_name = last_name;
        return this;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public Employees1 specialty(String specialty) {
        this.specialty = specialty;
        return this;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getAddress() {
        return address;
    }

    public Employees1 address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employees1 employees1 = (Employees1) o;
        if(employees1.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, employees1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Employees1{" +
            "id=" + id +
            ", ssn='" + ssn + "'" +
            ", contact_number='" + contact_number + "'" +
            ", first_name='" + first_name + "'" +
            ", last_name='" + last_name + "'" +
            ", specialty='" + specialty + "'" +
            ", address='" + address + "'" +
            '}';
    }
}
