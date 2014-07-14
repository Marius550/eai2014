package de.java.web;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.java.domain.replenishment.OrderState;
import de.java.domain.replenishment.ReplenishmentOrder;
import de.java.ejb.ReplenishmentOrderService;

@ManagedBean
public class ReplenishmentOrderList implements Serializable {
  private static final long serialVersionUID = 337082669291191654L;

  @EJB
  private ReplenishmentOrderService orderService;

  private OrderState filterForState = null;

  public Collection<ReplenishmentOrder> getOrders() {
    if (filterForState != null) {
      return orderService.getReplenishmentOrdersInState(filterForState);
    }
    return orderService.getAllReplenishmentOrders();
  }

  public OrderState getFilterForState() {
    return filterForState;
  }

  public void setFilterForState(OrderState filterForState) {
    this.filterForState = filterForState;
  }
}
