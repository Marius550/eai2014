package de.java.domain.replenishment;

import static de.java.domain.replenishment.OrderState.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class ReplenishmentOrder implements Serializable {

  private static final long serialVersionUID = -5482259735269999562L;

  @Id
  @GeneratedValue
  private long id;

  @NotNull
  private OrderState state = OPEN;

  @OneToMany(mappedBy="order", cascade=CascadeType.ALL)
  private Collection<Position> positions = new ArrayList<Position>();

  private Date expectedDelivery;
 
  private Date actualDelivery;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public OrderState getState() {
    return state;
  }

  public void setState(OrderState state) {
    this.state = state;
  }

  public Date getExpectedDelivery() {
    return expectedDelivery;
  }

  public void setExpectedDelivery(Date expectedDelivery) {
    this.expectedDelivery = expectedDelivery;
  }

  public Date getActualDelivery() {
    return actualDelivery;
  }

  public void setActualDelivery(Date actualDelivery) {
    this.actualDelivery = actualDelivery;
  }

  public Collection<Position> getPositions() {
    return positions;
  }

  void addPosition(Position position) {
    getPositions().add(position);
  }

  void setPositions(Collection<Position> positions) {
    this.positions = positions;
  }

}
