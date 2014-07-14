package de.java.domain.orderstate;

import static de.java.domain.replenishment.OrderState.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.java.domain.replenishment.OrderState;

@RunWith(Parameterized.class)
public class ParameterizedOrderStateTest {
  
  private OrderState state;
  private boolean canProceed;
  private boolean mayBeCancelled;

  @Parameters
  public static List<Object[]> data() {
    return Arrays.asList(new Object[][] {
        { OPEN, true, false },
        { POSTING, true, true },
        { ORDERED, true, false },
        { FINISHED, false, false },
        { CANCELLED, false, false }
    });
  }

  public ParameterizedOrderStateTest(OrderState state,
      boolean canProceed, boolean mayBeCancelled) {
        this.state = state;
        this.canProceed = canProceed;
        this.mayBeCancelled = mayBeCancelled;
  }

  @Test public void
  preceedability() {
    assertEquals(state.isProceedable(), canProceed);
  }

  @Test public void
  cancelbility() {
    assertEquals(state.isCancellable(), mayBeCancelled);
  }
 
}
