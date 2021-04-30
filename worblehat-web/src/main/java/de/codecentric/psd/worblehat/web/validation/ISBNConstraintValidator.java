package de.codecentric.psd.worblehat.web.validation;

import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.ISBNValidator;

public class ISBNConstraintValidator implements ConstraintValidator<ISBN, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // Don't validate null, empty and blank strings, since these are validated by @NotNull,
    // @NotEmpty and @NotBlank
    if (StringUtils.isBlank(value)) {
      return true;
    }

    Set<String> maybeISBNs = Set.of(value.split(","));

    for (String maybeISBN : maybeISBNs) {
      if (!isValidISBN(maybeISBN)) {
        return false;
      }
    }

    return true;
  }

  private boolean isValidISBN(String value) {
    return ISBNValidator.getInstance().isValidISBN10(value)
        || ISBNValidator.getInstance().isValidISBN13(value);
  }
}
