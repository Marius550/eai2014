package de.java.domain.inventory;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import de.java.domain.Drug;

@Entity
@DiscriminatorValue("restock")
public class RestockEvent extends InventoryEvent {

  private static final long serialVersionUID = -5127225122819756958L;

  RestockEvent() { }

  public RestockEvent(long quantity, Date dateOfAction, Drug drug) {
    super(quantity, dateOfAction, drug);
  }

  @Override
  public String getEventType() {
    return "restock";
  }

}
