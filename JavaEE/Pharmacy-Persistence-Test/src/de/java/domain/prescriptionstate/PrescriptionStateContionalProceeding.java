package de.java.domain.prescriptionstate;

import static de.java.domain.prescription.PrescriptionState.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import de.java.domain.prescription.Fulfillable;
import de.java.domain.prescription.PrescriptionState;

public class PrescriptionStateContionalProceeding {

  @Test public void
  shouldNotProceedToCheckingWithNoFulfillableItems() {
    Collection<Fulfillable> noFulfillableItems = emptyList();
    assertThat(ENTRY, is(not(proceedableWith(noFulfillableItems))));
  }

  @Test public void
  shouldProceedToCheckingWithAtLeastOneFulfillableItem() {
    Collection<Fulfillable> oneFulfillableItem = asList(newUnfulfilledItem());
    assertThat(ENTRY, is(proceedableWith(oneFulfillableItem)));
  }

  @Test public void
  shouldNotBeProceedableWithUnfulfilledItems() {
    Collection<Fulfillable> oneUnfulfilledItem = asList(newUnfulfilledItem());
    assertThat(FULFILLING, is(not(proceedableWith(oneUnfulfilledItem))));
  }

  @Test public void
  shouldProceedToFulfilledWithAllItemsFulfilled() {
    Collection<Fulfillable> oneFulfilledItem = asList(newFulfilledItem());
    assertThat(FULFILLING, is(proceedableWith(oneFulfilledItem)));
  }

  @Test public void
  shouldAlwaysProceedToFulfilling() {
    Collection<Fulfillable> anyFulfillables = emptyList();
    assertThat(CHECKING, is(proceedableWith(anyFulfillables)));
  }

  @Test public void
  shouldNotBeProceedableInFulfilled() {
    Collection<Fulfillable> anyFulfillables = emptyList();
    assertThat(FULFILLED, is(not(proceedableWith(anyFulfillables))));
  }

  private Fulfillable newUnfulfilledItem() {
    return new Fulfillable() {
      public boolean isFulfilled() {
        return false;
      }
    };
  }

  private Fulfillable newFulfilledItem() {
    return new Fulfillable() {
      public boolean isFulfilled() {
        return true;
      }
    };
  }

  private Matcher<PrescriptionState> proceedableWith(final Collection<Fulfillable> fulfillables) {
    return new TypeSafeMatcher<PrescriptionState>() {

      @Override
      public void describeTo(Description description) {
        description.appendText("proceedable with " + Arrays.toString(fulfillables.toArray()));
      }

      @Override
      public boolean matchesSafely(PrescriptionState state) {
        return state.isProceedable(fulfillables);
      }
    };
  }
}
