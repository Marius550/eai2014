package de.java.domain.replenishment;

import static javax.persistence.CascadeType.*;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import de.java.domain.Drug;

@Entity
public class Position implements Serializable {

  private static final long serialVersionUID = 4036453834298206981L;

  @Id
  @GeneratedValue
  private long id;

  @ManyToOne(cascade={DETACH,MERGE,PERSIST,REFRESH})
  private ReplenishmentOrder order;

  @ManyToOne
  private Drug replenishedDrug;

  @NotNull
  private long quantity;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public ReplenishmentOrder getOrder() {
    return order;
  }

  public void setOrder(ReplenishmentOrder order) {
    this.order = order;
    order.addPosition(this);
  }

  public Drug getReplenishedDrug() {
    return replenishedDrug;
  }

  public void setReplenishedDrug(Drug replenishedDrug) {
    this.replenishedDrug = replenishedDrug;
  }

  public long getQuantity() {
    return quantity;
  }

  public void setQuantity(long quantity) {
    this.quantity = quantity;
  }

}
