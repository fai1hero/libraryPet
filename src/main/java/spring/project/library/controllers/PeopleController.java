package spring.project.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.project.library.models.Person;
import spring.project.library.services.BookService;
import spring.project.library.services.PeopleService;
import spring.project.library.utill.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final BookService bookService;
    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(BookService bookService, PeopleService peopleService, PersonValidator personValidator) {
        this.bookService = bookService;
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", peopleService.personBooks(id));

        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "people/new";
        }

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@ModelAttribute("person") Person person, @PathVariable("id") int id,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "people/edit";
        }

        peopleService.update(id, person);
        return "redirect:/people";
    }

    @PostMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
