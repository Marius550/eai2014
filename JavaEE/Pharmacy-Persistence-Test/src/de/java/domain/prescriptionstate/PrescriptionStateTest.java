package de.java.domain.prescriptionstate;

import static de.java.domain.prescription.PrescriptionState.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import de.java.domain.IllegalStatusTransitionException;
import de.java.domain.prescription.PrescriptionState;

public class PrescriptionStateTest {

  @Test public void
  entryIsSucceededByChecking() {
    assertThat(ENTRY, is(succeededBy(CHECKING)));
  }

  @Test public void
  checkingIsSucceededByFulFilling() {
    assertThat(CHECKING, is(succeededBy(FULFILLING)));
  }

  @Test public void
  fulfillingIsSucceededByFulfilled() {
    assertThat(FULFILLING, is(succeededBy(FULFILLED)));
  }

  @Test(expected=IllegalStatusTransitionException.class) public void
  fulfilledHasNoNextState() {
    FULFILLED.getNext();
  }

  private Matcher<PrescriptionState> succeededBy(final PrescriptionState state) {
    return new TypeSafeMatcher<PrescriptionState>() {

      @Override
      public void describeTo(Description description) {
        description.appendText("succeeded by " + state);
      }

      @Override
      public boolean matchesSafely(PrescriptionState initialState) {
        return initialState.getNext() == state;
      }
    };
  }

}
