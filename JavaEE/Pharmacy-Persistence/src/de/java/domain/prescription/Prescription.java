package de.java.domain.prescription;

import static de.java.domain.prescription.PrescriptionState.ENTRY;
import static javax.persistence.CascadeType.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import de.java.domain.customer.Customer;

@Entity
public class Prescription implements Serializable {

  private static final long serialVersionUID = 4728846105423995587L;

  @Id
  @GeneratedValue
  private long id;

  @NotNull
  private PrescriptionState state = ENTRY;

  @ManyToOne(cascade={DETACH,MERGE,PERSIST,REFRESH})
  private Customer customer;

  @NotNull(message="Issuing physician required")
  @Size(min=1, message="Issuing physician required")
  private String issuer;
  
  //Java Regex to Validate Full Name allow only Spaces and Letters
  @NotNull(message="Diagnosis required")
  @Size(min=1, message="Diagnosis required")
  @Pattern(regexp="^[\\p{L} .'-]+$",
  message="Not a valid diagnosis, use string to define the diagnosis (e.g.: Low blood pressure)")
  private String diagnosis;
  
  private double PrescriptionDrugsPrice;

  @Temporal(TemporalType.DATE)
  @NotNull(message="Issue date required")
  private Date issueDate = new Date();

  @NotNull(message="Entry date required")
  private Date entryDate = new Date();

  private Date fulfilmentDate;

  @OneToMany(mappedBy="prescription", cascade={CascadeType.ALL})
  private Collection<Item> items = new ArrayList<>();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public PrescriptionState getState() {
    return state;
  }

  public void setState(PrescriptionState state) {
    this.state = state;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public String getDiagnosis() {
	return diagnosis;
  }
  
  public void setDiagnosis(String diagnosis) {
    this.diagnosis = diagnosis;
  }

  public void setIssuer(String issuer) {
	    this.issuer = issuer;
	  }

  public String getIssuer() {
		return issuer;
  	  }
	  
  public Date getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(Date issueDate) {
    this.issueDate = issueDate;
  }

  public Date getEntryDate() {
    return entryDate;
  }

  public void setEntryDate(Date entryDate) {
    this.entryDate = entryDate;
  }

  public Date getFulfilmentDate() {
    return fulfilmentDate;
  }

  public void setFulfilmentDate(Date fulfilmentDate) {
    this.fulfilmentDate = fulfilmentDate;
  }

  public Collection<Item> getItems() {
    return items;
  }

  public void setItems(Collection<Item> items) {
    this.items = items;
  }

}
