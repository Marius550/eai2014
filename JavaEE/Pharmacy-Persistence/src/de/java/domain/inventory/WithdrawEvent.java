package de.java.domain.inventory;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import de.java.domain.Drug;

@Entity
@DiscriminatorValue("withdraw")
public class WithdrawEvent extends InventoryEvent {

  private static final long serialVersionUID = -1038872386850514540L;

  WithdrawEvent() { }

  public WithdrawEvent(long quantity, Date dateOfAction, Drug drug) {
    super(negativeOf(quantity), dateOfAction, drug);
  }

  private static long negativeOf(long quantity) {
    return -quantity;
  }

  @Override
  public String getEventType() {
    return "withdraw";
  }

}
