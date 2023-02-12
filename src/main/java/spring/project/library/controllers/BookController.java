package spring.project.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.project.library.models.Book;
import spring.project.library.models.Person;
import spring.project.library.services.BookService;
import spring.project.library.services.PeopleService;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.findOne(id));

        Person bookOwner = bookService.getOwner(id);

        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        bookService.update(book, id);
        return "redirect:/books";
    }

    @PostMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PostMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookService.assign(id, person);
        return "redirect:/books/" + id;
    }

    @PostMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/searchbytitle")
    public String searchPageTitle() {
        return "books/searchByTitle";
    }

    @PostMapping("/searchbytitle")
    public String searchByTitle(Model model, @RequestParam("request") String request) {
        model.addAttribute("books", bookService.searchByTitle(request));

        if (request.length() == 0)
            throw new IllegalArgumentException("Поиск должен содержать не менее 1 символа");
        model.addAttribute("books", bookService.searchByAuthor(request));

        return "books/searchByTitle";
    }

    @GetMapping("/searchbyauthor")
    public String searchPageAuthor() {
        return "books/searchByAuthor";
    }

    @PostMapping("/searchbyauthor")
    public String searchByAuthor(Model model, @RequestParam("request") String request) {

        if (request.length() == 0)
            throw new IllegalArgumentException("Поиск должен содержать не менее 1 символа");
        model.addAttribute("books", bookService.searchByAuthor(request));

        return "books/searchByAuthor";
    }

    @ExceptionHandler
    private ResponseEntity<?> handleException(IllegalArgumentException e) {
        String response = e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
