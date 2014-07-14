package de.java.domain.orderstate;

import static de.java.domain.replenishment.OrderState.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import de.java.domain.IllegalStatusTransitionException;
import de.java.domain.replenishment.OrderState;

public class OrderStateTest {

  @Test public void
  openIsSucceededByPosting() {
    assertThat(OPEN, is(succeededBy(POSTING)));
  }

  @Test public void
  postingIsSucceededByOrdered() {
    assertThat(POSTING, is(succeededBy(ORDERED)));
  }
 
  @Test public void
  orderedIsSucceededFinished() {
    assertThat(ORDERED, is(succeededBy(FINISHED)));
  }
 
  @Test(expected=IllegalStatusTransitionException.class) public void
  finishedCannotProceedIntoOtherState() {
    FINISHED.getNext();
  }

  @Test(expected=IllegalStatusTransitionException.class) public void
  cancelledCannotProceedIntoOtherState() {
    CANCELLED.getNext();
  }

  private Matcher<OrderState> succeededBy(final OrderState state) {
    return new TypeSafeMatcher<OrderState>() {

      @Override
      public void describeTo(Description description) {
        description.appendText("succeeded by " + state);
      }

      @Override
      public boolean matchesSafely(OrderState initialState) {
        return initialState.getNext() == state;
      }
    };
  }

}
