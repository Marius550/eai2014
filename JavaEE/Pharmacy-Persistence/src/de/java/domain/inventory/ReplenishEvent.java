package de.java.domain.inventory;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import de.java.domain.Drug;

@Entity
@DiscriminatorValue("replenish")
public class ReplenishEvent extends InventoryEvent {
  
  private static final long serialVersionUID = -2577671043171520382L;

  ReplenishEvent() { }

  public ReplenishEvent(long quantity, Date dateOfAction, Drug drug) {
    super(quantity, dateOfAction, drug);
  }

  @Override
  public String getEventType() {
    return "replenish";
  }

}
