package de.java.web;

import static de.java.web.util.Util.errorMessage;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("de.java.PznValidator")
public class PznValidator implements Validator {

  @Override
  public void validate(FacesContext context, UIComponent component, Object value)
      throws ValidatorException {
    try {
      if (value == null)
        return;
      final int pzn = ((Integer) value);
      if (pzn < 0)
        invalidPzn();
      String pznAsString = "" + value;
      if (pznAsString.length() > 8)
        invalidPzn();
    } catch (ClassCastException e) {
      invalidPzn();
    }
  }

  private void invalidPzn() {
    throw new ValidatorException(errorMessage("Not a valid PZN"));
  }

}
