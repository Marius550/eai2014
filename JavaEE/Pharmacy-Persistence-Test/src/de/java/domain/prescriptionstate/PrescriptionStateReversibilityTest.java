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

public class PrescriptionStateReversibilityTest {

  @Test(expected=IllegalStatusTransitionException.class) public void
  entryHasNoPreviousState() {
    ENTRY.getPrevious();
  }

  @Test public void
  checkingIsPrecededByEntry() {
    assertThat(CHECKING, is(precededBy(ENTRY)));
  }

  @Test(expected=IllegalStatusTransitionException.class) public void
  fulfillingCannotTransitionIntoPreviousState() {
    FULFILLING.getPrevious();
  }

  @Test(expected=IllegalStatusTransitionException.class) public void
  fulfilledCannotTransitionIntoPreviousState() {
    FULFILLED.getPrevious();
  }

  private Matcher<PrescriptionState> precededBy(final PrescriptionState state) {
    return new TypeSafeMatcher<PrescriptionState>() {

      @Override
      public void describeTo(Description description) {
        description.appendText("preceded by " + state);
      }

      @Override
      public boolean matchesSafely(PrescriptionState initialState) {
        return initialState.getPrevious() == state;
      }
    };
  }

}
