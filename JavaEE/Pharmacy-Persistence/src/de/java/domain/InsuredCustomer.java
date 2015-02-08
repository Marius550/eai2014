package de.java.domain;

import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
//import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class InsuredCustomer implements Serializable {

  private static final long serialVersionUID = 5610455835559750909L;

  @Id
  @GeneratedValue
  private long id;
  
  @Column(unique=false)
  @Size(min=1, message="Vorname erforderlich")
  @NotNull(message="Vorname erforderlich")
  private String vorname;
  
  @Column(unique=false)
  @Size(min=1, message="Nachname erforderlich")
  @NotNull(message="Nachname erforderlich")
  private String nachname;
  
  @Size(min=5, message="Keine gültige Email Adresse (z.B.: name@beispiel.com))")
  @NotNull(message="Email Adresse erforderlich")
  @Pattern(regexp="^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$",
  message="Keine gültige Email Adresse, bitte verwenden Sie das Format (z.B.: name@beispiel.com))")
  private String email;
  
  /*
  @NotNull(message="Telephone number required")
  @Pattern(regexp="\\+[1-9][0-9]{0,2}{0,2}[0-9 ]{3,13}[0-9]$",
    message="Not a valid phone number (use E.123 international, e.g.: +49 251 83 38250))")
  private String telephoneNumber;
  
  private String address;
  
  @Size(min=8, message="Not a valid birth date (E.g.: yyyy-MM-dd))")
  @NotNull(message="Birth date required")
  @Pattern(regexp="^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$", message="Not a birth day (use e.g.: yyyy-MM-dd))")
  private String birthDate;
  
  private double prescriptionBill;

  @OneToMany(mappedBy="customer")
  private Collection<Prescription> prescriptions = new ArrayList<>();

  public Prescription createPrescription() {
    Prescription newPrescription = new Prescription();
    newPrescription.setCustomer(this);
    prescriptions.add(newPrescription);
    return newPrescription;
  }
  */

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getVorname() {
    return vorname;
  }

  public void setVorname(String vorname) {
    this.vorname = vorname;
  }
  
  public String getNachname() {
	return nachname;
  }

  public void setNachname(String nachname) {
	this.nachname = nachname;
  }
  
  public String getEmail() {
	return email;
  }

  public void setEmail(String email) {
	this.email = email;
  }

  /*
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
  
  public double getPrescriptionBill() {
	return prescriptionBill;
  }

  public void setPrescriptionBill(double prescriptionBill) {
	this.prescriptionBill = prescriptionBill;
  }
  
  public String getEmail() {
	return email;
  }

  public void setEmail(String email) {
	this.email = email;
  }
  
  public String getBirthDate() {
	return birthDate;
  }

  public void setBirthDate(String birthDate) {
	this.birthDate = birthDate;
  }

  public Collection<Prescription> getPrescriptions() {
    return prescriptions;
  }

  public void setPrescriptions(Collection<Prescription> prescriptions) {
    this.prescriptions = prescriptions;
  }
  */
}
