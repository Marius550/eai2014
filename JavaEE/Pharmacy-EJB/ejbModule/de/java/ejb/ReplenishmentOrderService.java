package de.java.ejb;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Remote;

import de.java.domain.Drug;
import de.java.domain.replenishment.OrderState;
import de.java.domain.replenishment.Position;
import de.java.domain.replenishment.ReplenishmentOrder;

@Remote
public interface ReplenishmentOrderService {

  Collection<ReplenishmentOrder> getAllReplenishmentOrders();

  Collection<ReplenishmentOrder> getReplenishmentOrdersInState(
      OrderState filterForState);

  ReplenishmentOrder getOrder(long id);

  /**
   * @param id {@link ReplenishmentOrder#getId()}
   * @return {@link ReplenishmentOrder} with positions loaded
   */
  ReplenishmentOrder getOrderWithPositions(long id);

  void proceedToNextState(long id);

  void cancel(long id);

  void updateExpectedDeliveryDate(long id, Date expectedDelivery);

  void updateActualDeliveryDate(long id, Date actualDelivery);

  Collection<Position> getPendingPositionsForDrug(int pzn);
  
  long getQuantityPendingForDrug(int pzn);

  void initiateReplenishmentForDrug(Drug drug, long quantity);

  void removePosition(Position position);

}
