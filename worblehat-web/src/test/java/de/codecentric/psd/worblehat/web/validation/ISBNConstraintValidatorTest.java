package de.codecentric.psd.worblehat.web.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ISBNConstraintValidatorTest {

  private ISBNConstraintValidator isbnConstraintValidator;

  private ConstraintValidatorContext constraintValidatorContext;

  @BeforeEach
  public void setUp() {
    isbnConstraintValidator = new ISBNConstraintValidator();
    constraintValidatorContext = mock(ConstraintValidatorContext.class);
  }

  @Test
  void initializeShouldTakeIsbn() {
    ISBN isbn = mock(ISBN.class);
    assertDoesNotThrow(() -> isbnConstraintValidator.initialize(isbn));
  }

  @Test
  void shouldReturnTrueIfBlank() {
    boolean actual = isbnConstraintValidator.isValid("", constraintValidatorContext);
    assertTrue(actual);
  }

  @Test
  void shouldReturnTrueIfValidISBN() {
    boolean actual = isbnConstraintValidator.isValid("0132350882", constraintValidatorContext);
    assertTrue(actual);
  }

  @Test
  void shouldReturnFalseIfInvalidISBN() {
    boolean actual = isbnConstraintValidator.isValid("0123459789", constraintValidatorContext);
    assertFalse(actual);
  }

  @Test
  void shouldReturnFalseIfInValidISBN13() {
    boolean actual = isbnConstraintValidator.isValid("0123459789876", constraintValidatorContext);
    assertFalse(actual);
  }

  @Test
  void shouldReturnTrueIfValidISBN13() {
    boolean actual = isbnConstraintValidator.isValid("9783551557452", constraintValidatorContext);
    assertTrue(actual);
  }

  @Test
  void shouldReturnTrueIfValidISBN10andISBN13() {
    boolean actual =
        isbnConstraintValidator.isValid("0132350882,9783551557452", constraintValidatorContext);
    assertTrue(actual);
  }

  @Test
  void shouldReturnTrueIfValidISBN13s() {
    boolean actual =
        isbnConstraintValidator.isValid("9783551557452,9781234567897", constraintValidatorContext);
    assertTrue(actual);
  }

  @Test
  void shouldReturnFalseIfPartiallyValidIBANsOnly() {
    String invalidIBAN13 = "0123459789876";
    boolean actual =
        isbnConstraintValidator.isValid("0132350882," + invalidIBAN13, constraintValidatorContext);
    assertFalse(actual);
  }
}
