package de.java.domain.orderstate;

import static de.java.domain.replenishment.OrderState.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import de.java.domain.IllegalStatusTransitionException;

public class OrderStateCancellationTest {
  @Test public void
  postingCancellingLeadsToCancelled() {
    assertThat(POSTING.cancel(), is(CANCELLED));
  }

  @Test(expected=IllegalStatusTransitionException.class) public void
  openCannotProceedIntoCancelled() {
    OPEN.cancel();
  }

  @Test(expected=IllegalStatusTransitionException.class) public void
  orderedCannotProceedIntoCancelled() {
    ORDERED.cancel();
  }

  @Test(expected=IllegalStatusTransitionException.class) public void
  finishedCannotProceedIntoCancelled() {
    FINISHED.cancel();
  }

  @Test(expected=IllegalStatusTransitionException.class) public void
  cancelledCannotBeCancelled() {
    CANCELLED.cancel();
  }
}
