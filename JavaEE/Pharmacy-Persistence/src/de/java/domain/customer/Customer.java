package de.java.domain.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import de.java.domain.prescription.Prescription;

@Entity
public class Customer implements Serializable {

  private static final long serialVersionUID = 5610455835559750909L;

  @Id
  @GeneratedValue
  private long id;
  
  @Column(unique=true)
  @Size(min=1, message="Name required")
  @NotNull(message="Name required")
  private String name;
  
  @NotNull(message="Telephone number required")
  @Pattern(regexp="\\+[1-9][0-9]{0,2}{0,2}[0-9 ]{3,13}[0-9]$",
    message="Not a valid phone number (use E.123 international, e.g.: +49 251 83 38250))")
  private String telephoneNumber;
  
  private String address;

  @OneToMany(mappedBy="customer")
  private Collection<Prescription> prescriptions = new ArrayList<>();

  public Prescription createPrescription() {
    Prescription newPrescription = new Prescription();
    newPrescription.setCustomer(this);
    prescriptions.add(newPrescription);
    return newPrescription;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTelephoneNumber() {
    return telephoneNumber;
  }

  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Collection<Prescription> getPrescriptions() {
    return prescriptions;
  }

  public void setPrescriptions(Collection<Prescription> prescriptions) {
    this.prescriptions = prescriptions;
  }
}
