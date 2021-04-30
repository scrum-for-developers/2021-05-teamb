package de.codecentric.psd.worblehat.web.controller;

import de.codecentric.psd.worblehat.domain.Book;
import de.codecentric.psd.worblehat.domain.BookService;
import de.codecentric.psd.worblehat.domain.Borrowing;
import de.codecentric.psd.worblehat.web.formdata.BookBorrowFormData;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/** Controller for BorrowingBook */
@RequestMapping("/borrow")
@Controller
public class BorrowBookController {

  private static final String BORROW = "borrow";
  private BookService bookService;

  @Autowired
  public BorrowBookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping
  public void setupForm(final ModelMap model) {
    model.put("borrowFormData", new BookBorrowFormData());
  }

  @Transactional
  @PostMapping
  public String processSubmit(
      @ModelAttribute("borrowFormData") @Valid BookBorrowFormData borrowFormData,
      BindingResult result) {
    if (result.hasErrors()) {
      return BORROW;
    }

    if (borrowFormData.getIsbn() == null) {
      result.rejectValue("isbn", "noBookExists");
      return BORROW;
    }
    final Set<String> allIsbns =
        Arrays.stream(borrowFormData.getIsbn().split(","))
            .map(String::trim)
            .collect(Collectors.toSet());

    for (String isbn : allIsbns) {
      Set<Book> books = bookService.findBooksByIsbn(borrowFormData.getIsbn());
      if (books.isEmpty()) {
        result.rejectValue("isbn", "noBookExists", List.of(isbn).toArray(), "");
      }
    }

    Set<Borrowing> borrowings = bookService.borrowBooks(allIsbns, borrowFormData.getEmail());

    if (borrowings.isEmpty()) {
      result.rejectValue("isbn", "noBorrowableBooks");
      return BORROW;
    } else {
      return "home";
    }
  }

  @ExceptionHandler(Exception.class)
  public String handleErrors(Exception ex, HttpServletRequest request) {
    return "home";
  }
}
