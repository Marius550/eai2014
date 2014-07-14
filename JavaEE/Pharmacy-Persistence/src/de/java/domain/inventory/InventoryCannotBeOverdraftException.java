package de.java.domain.inventory;

public class InventoryCannotBeOverdraftException extends RuntimeException {
  
  private static final long serialVersionUID = 7419612127970666972L;

  public InventoryCannotBeOverdraftException(long quantity, long stock) {
    super(String.format("Cannot withdraw %s items from stock of %s items", quantity, stock));
  }

}
