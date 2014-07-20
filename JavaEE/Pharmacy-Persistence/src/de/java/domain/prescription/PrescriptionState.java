package de.java.domain.prescription;

import java.util.Collection;

import de.java.domain.IllegalStatusTransitionException;

public enum PrescriptionState {
  ENTRY {
    public PrescriptionState getNext() { return CHECKING; }
    public boolean isCancellable() { return true; }
    public boolean isProceedable(Collection<? extends Fulfillable> fulfillables) { return !fulfillables.isEmpty(); }
  },
  CHECKING {
    public PrescriptionState getNext() { return FULFILLING; }
    public boolean isCancellable() { return true; }
    public boolean isReversible() { return true; }
    public PrescriptionState getPrevious() { return ENTRY; }
  },
  FULFILLING {
    public PrescriptionState getNext() { return FULFILLED; }
    @Override
    public boolean isProceedable(Collection<? extends Fulfillable> fulfillables) {
      for (Fulfillable fulfillable : fulfillables) {
        if (!fulfillable.isFulfilled()) { return false; }
      }
      return true;
    }
  },
  FULFILLED {
    public PrescriptionState getNext() { throw new IllegalStatusTransitionException(); }
    public boolean isProceedable(Collection<? extends Fulfillable> fulfillables) { return false; }
  };

  public abstract PrescriptionState getNext();

  public boolean isProceedable(Collection<? extends Fulfillable> fulfillables) { return true; }

  public boolean isCancellable() { return false; }

  public boolean isReversible() { return false; }

  public PrescriptionState getPrevious() { throw new IllegalStatusTransitionException(); }

}
