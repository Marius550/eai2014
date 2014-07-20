package de.java.ejb;

import static de.java.domain.replenishment.OrderState.OPEN;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.java.domain.Drug;
import de.java.domain.replenishment.OrderState;
import de.java.domain.replenishment.Position;
import de.java.domain.replenishment.ReplenishmentOrder;

@Stateless
public class ReplenishmentOrderServiceBean implements ReplenishmentOrderService {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Collection<ReplenishmentOrder> getAllReplenishmentOrders() {
    // ordering is done on the ordinal numbers (not on the state names)
    return em.createQuery("FROM ReplenishmentOrder o ORDER BY o.state",
        ReplenishmentOrder.class).getResultList();
  }

  @Override
  public Collection<ReplenishmentOrder> getReplenishmentOrdersInState(
      OrderState state) {
    final String query = "FROM ReplenishmentOrder o WHERE o.state = :state";
    return em
        .createQuery(query, ReplenishmentOrder.class)
        .setParameter("state", state)
        .getResultList();
  }

  @Override
  public Collection<Position> getPendingPositionsForDrug(int pzn) {
    String query = "FROM Position"
        + "  WHERE replenishedDrug.pzn = :pzn"
        + "  AND order.state IN"
        + "    (de.java.domain.replenishment.OrderState.OPEN,"
        + "    de.java.domain.replenishment.OrderState.POSTING,"
        + "    de.java.domain.replenishment.OrderState.ORDERED)";
    return em.createQuery(query, Position.class)
        .setParameter("pzn", pzn)
        .getResultList();
  }

  @Override
  public long getQuantityPendingForDrug(int pzn) {
    long quantityPending = 0;
    for (Position p : getPendingPositionsForDrug(pzn)) {
      quantityPending += p.getQuantity();
    }
    return quantityPending;
  }

  @Override
  public ReplenishmentOrder getOrder(long id) {
    return em.find(ReplenishmentOrder.class, id);
  }

  @Override
  public ReplenishmentOrder getOrderWithPositions(long id) {
    return forceLoadOfPositions(getOrder(id));
  }

  private ReplenishmentOrder forceLoadOfPositions(ReplenishmentOrder order) {
    if (order == null) return order;
    order.getPositions().size();
    return order;
  }

  @Override
  public void proceedToNextState(long id) {
    ReplenishmentOrder order = getOrder(id);
    order.setState(order.getState().getNext());
    if (order.getState() == OrderState.FINISHED) {
      replenish(order.getPositions());
    }
  }

  private void replenish(Collection<Position> positions) {
    for (Position position : positions) {
      replenish(position);
    }
  }

  private void replenish(Position position) {
    Date actualDelivery = position.getOrder().getActualDelivery();
    long quantity = position.getQuantity();
    position.getReplenishedDrug().replenish(quantity, actualDelivery);
  }

  @Override
  public void cancel(long id) {
    ReplenishmentOrder order = getOrder(id);
    order.setState(order.getState().cancel());
    for (Position oldPosition : order.getPositions()) {
      initiateReplenishmentForDrug(oldPosition.getReplenishedDrug(), oldPosition.getQuantity());
    }
  }

  @Override
  public void updateExpectedDeliveryDate(long id, Date expectedDelivery) {
    getOrder(id).setExpectedDelivery(expectedDelivery);
  }

  @Override
  public void updateActualDeliveryDate(long id, Date actualDelivery) {
    getOrder(id).setActualDelivery(actualDelivery);
  }

  @Override
  public void initiateReplenishmentForDrug(Drug drug, long quantity) {
    if (hasOpenOrders(drug)) {
      modifyPositionOfOpenOrder(drug, quantity);
    } else {
      addPositionToOpenOrNewOrder(drug, quantity);
    }
  }

  private boolean hasOpenOrders(Drug drug) {
    Collection<Position> pendingPositions = getPendingPositionsForDrug(drug.getPzn());
    for (Position position : pendingPositions) {
      if (position.getOrder().getState() == OPEN) {
        return true;
      }
    }
    return false;
  }

  private void modifyPositionOfOpenOrder(Drug drug, long quantity) {
    for (Position p : getPendingPositionsForDrug(drug.getPzn())) {
      if (p.getOrder().getState() == OPEN) {
        p.setQuantity(quantity + p.getQuantity());
        break;
      }
    }
  }

  private void addPositionToOpenOrNewOrder(Drug drug, long quantity) {
    Position position = createPosition(drug, quantity);
    if (openOrdersAvailable()) {
      addToOpenOrder(position);
    } else {
      addToNewOrder(position);
    }
  }

  private Position createPosition(Drug drug, long quantity) {
    Position p = new Position();
    p.setQuantity(quantity);
    p.setReplenishedDrug(drug);
    return p;
  }

  private boolean openOrdersAvailable() {
    return getReplenishmentOrdersInState(OPEN).size() > 0;
  }

  private void addToOpenOrder(Position position) {
    for (ReplenishmentOrder order : getReplenishmentOrdersInState(OPEN)) {
      position.setOrder(order);
      return;
    }
  }

  private void addToNewOrder(Position position) {
    ReplenishmentOrder order = new ReplenishmentOrder();
    position.setOrder(order);
    em.persist(position);
  }

  @Override
  public void removePosition(Position detachedPosition) {
    Position position = em.merge(detachedPosition);
    ReplenishmentOrder order = position.getOrder();

    validateOpenState(order);

    order.getPositions().remove(position);
    em.remove(position);

    removeOrderIfEmpty(order);
  }

  private void validateOpenState(ReplenishmentOrder order) {
    if (order.getState() != OPEN) {
      throw new RuntimeException("Cannot remove positions from non-open orders!");
    }
  }

  private void removeOrderIfEmpty(ReplenishmentOrder order) {
    if (order.getPositions().isEmpty()) {
      em.remove(order);
    }
  }

}
