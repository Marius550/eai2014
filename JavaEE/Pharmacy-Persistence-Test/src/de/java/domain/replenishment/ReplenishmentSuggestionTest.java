package de.java.domain.replenishment;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

import de.java.domain.Drug;

public class ReplenishmentSuggestionTest {

  private static final long minimumInventoryLevel = 42;
  private static final long optimalInventoryLevel = 50;

  private static final long withNoPendingItems = 0;
  private static final long with20PendingItems = 20;
  private static final long with46PendingItems = 46;

  private Drug drug;

  @Before
  public void setup() {
    drug = new Drug();
    drug.setMinimumInventoryLevel(minimumInventoryLevel);
    drug.setOptimalInventoryLevel(optimalInventoryLevel);
  }

  @Test public void
  drugInitiallyHasEmptyStock() {
    assertThat(drug.getStock(), is(0l));
  }

  @Test public void
  shouldSuggestReplenishmentWhenBelowMinimum() { 
    assertThat(drug, is(inNeedOfReplenishment()));
  }

  @Test public void
  shouldSuggestNoReplenishmentWhenBetweenMinimumAndOptimalInventoryLevel() {
    drug.restock(46, null);
    assertThat(drug, is(not(inNeedOfReplenishment())));
  }

  @Test public void
  shouldSuggestNoReplenishmentWhenPendingQuantityAndStockAreAboveMinimumInventoryLevel() {
    assertThat(drug, is(not(inNeedOfReplenishment(with46PendingItems))));
  }

  @Test public void
  shouldSuggestReplenishmentWhenPendingQuantityAndStockStillFallBelowMinimumInventoryLevel() {
    assertThat(drug, is(inNeedOfReplenishment(with20PendingItems)));
  }

  @Test public void
  shouldSuggestReplenishmentQuantity() {
    assertThat(drug.getSuggestedReplenishmentQuantity(withNoPendingItems), is(50l));
    assertThat(drug.getSuggestedReplenishmentQuantity(with20PendingItems), is(30l));
    assertThat(drug.getSuggestedReplenishmentQuantity(with46PendingItems), is(0l));
  }

  private Matcher<Drug> inNeedOfReplenishment(final long numberOfPendingItems) {
    return new TypeSafeMatcher<Drug>() {
    
      @Override
      public void describeTo(Description description) {
        description.appendText("in need of replenishment with " + numberOfPendingItems + " pending items");
      }
    
      @Override
      public boolean matchesSafely(Drug drug) {
        return drug.isInNeedOfReplenishment(numberOfPendingItems);
      }
    };
  }

  private Matcher<Drug> inNeedOfReplenishment() {
    int noPendingItems = 0;
    return inNeedOfReplenishment(noPendingItems);
  }

}
