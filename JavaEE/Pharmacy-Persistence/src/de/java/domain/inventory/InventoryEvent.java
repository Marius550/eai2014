package de.java.domain.inventory;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import de.java.domain.Drug;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
public abstract class InventoryEvent implements Serializable {

  private static final long serialVersionUID = -4623819670769125946L;

  @Id
  @GeneratedValue
  protected long id;

  @NotNull
  protected long quantity;
  
  @NotNull
  protected Date dateOfAction;

  @ManyToOne
  @NotNull
  private Drug drug;

  InventoryEvent() { }
  
  public InventoryEvent(long quantity, Date dateOfAction, Drug drug) {
    this.quantity = quantity;
    this.dateOfAction = dateOfAction;
    this.setDrug(drug);
  }

  public abstract String getEventType();

  public long getQuantity() {
    return quantity;
  }

  protected void setQuantity(long quantity) {
    this.quantity = quantity;
  }

  public Date getDateOfAction() {
    return dateOfAction;
  }

  protected void setDateOfAction(Date dateOfAction) {
    this.dateOfAction = dateOfAction;
  }

  public Drug getDrug() {
    return drug;
  }

  protected void setDrug(Drug drug) {
    this.drug = drug;
  }


}