package de.java.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.java.domain.inventory.InventoryCannotBeOverdraftException;
import de.java.domain.inventory.InventoryEvent;
import de.java.domain.inventory.ReplenishEvent;
import de.java.domain.inventory.RestockEvent;
import de.java.domain.inventory.WithdrawEvent;

@Entity
public class Drug implements Serializable {

  private static final long serialVersionUID = -9007410528310411219L;

  @Id
  private int pzn;

  @NotNull(message="Name required")
  @Size(min=1, message="Name required")
  private String name;

  private String description;

  @NotNull
  @Min(value=0, message="Stock can not be negative")
  private long stock = 0;

  @NotNull
  @Min(value=0, message="Minimum inventory has to be greater or equal to zero")
  private long minimumInventoryLevel = 0;

  @NotNull
  @Min(value=0, message="Optimal inventory has to be greater or equal to zero")
  private long optimalInventoryLevel = 0;

  @OneToMany(mappedBy="drug", cascade=CascadeType.ALL)
  private Collection<InventoryEvent> events = new ArrayList<InventoryEvent>();

  public Drug() { }

  public Drug(int pzn, String name) {
    this.pzn = pzn;
    this.name = name;
  }

  public void withdraw(long quantity, Date dateOfAction) {
    validateWithdrawal(quantity);
    apply(new WithdrawEvent(quantity, dateOfAction, this));
  }

  private void validateWithdrawal(long quantity) {
    if (quantity > stock) {
      throw new InventoryCannotBeOverdraftException(quantity, stock);
    }
  }

  private void apply(InventoryEvent event) {
    stock += event.getQuantity();
    addEvent(event);
  }

  void addEvent(InventoryEvent event) {
    events.add(event);
  }

  public void restock(long quantity, Date dateOfAction) {
    apply(new RestockEvent(quantity, dateOfAction, this));
  }

  public void replenish(long quantity, Date dateOfAction) {
    apply(new ReplenishEvent(quantity, dateOfAction, this));
  }

  public boolean isInNeedOfReplenishment(long pendingQuantity) {
    return (stock + pendingQuantity) < minimumInventoryLevel;
  }

  @Transient
  public long getSuggestedReplenishmentQuantity(long pendingQuantity) {
    if (isInNeedOfReplenishment(pendingQuantity)) {
      return itemsNeededToReachOptimalInventoryLevel(pendingQuantity);
    }
    return 0;
  }

  private long itemsNeededToReachOptimalInventoryLevel(long pendingQuantity) {
    return optimalInventoryLevel - (stock + pendingQuantity);
  }

  @Override
  public String toString() {
    return name + " (PZN: " + pzn + ")";
  }

  public int getPzn() {
    return pzn;
  }

  public void setPzn(int pzn) {
    this.pzn = pzn;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public long getStock() {
    return stock;
  }

  void setStock(long stock) {
    this.stock = stock;
  }

  public long getMinimumInventoryLevel() {
    return minimumInventoryLevel;
  }

  public void setMinimumInventoryLevel(long minimumInventoryLevel) {
    this.minimumInventoryLevel = minimumInventoryLevel;
  }

  public long getOptimalInventoryLevel() {
    return optimalInventoryLevel;
  }

  public void setOptimalInventoryLevel(long optimalInventoryLevel) {
    this.optimalInventoryLevel = optimalInventoryLevel;
  }

  public Collection<InventoryEvent> getEvents() {
    return events;
  }

  void setEvents(Collection<InventoryEvent> events) {
    this.events = events;
  }

}
