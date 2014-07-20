package de.java.domain.prescriptionstate;

import static de.java.domain.prescription.PrescriptionState.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.java.domain.prescription.PrescriptionState;

@RunWith(Parameterized.class)
public class ParameterizedPrescriptionStateTest {
  
  private PrescriptionState state;
  private boolean cancellable;
  private boolean reversible;

  @Parameters
  public static List<Object[]> data() {
    return Arrays.asList(new Object[][] {
        { ENTRY, true, false },
        { CHECKING, true, true },
        { FULFILLING, false, false },
        { FULFILLED, false, false }
    });
  }

  public ParameterizedPrescriptionStateTest(PrescriptionState state,
      boolean cancellable, boolean reversible) {
        this.state = state;
        this.cancellable = cancellable;
        this.reversible = reversible;
  }

  @Test public void
  cancelbility() {
    assertEquals(state.toString(), state.isCancellable(), cancellable);
  }

  @Test public void
  reversibility() {
    assertEquals(state.toString(), state.isReversible(), reversible);
  }
 
}
