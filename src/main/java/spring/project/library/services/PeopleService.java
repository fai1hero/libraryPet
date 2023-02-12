package spring.project.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.project.library.models.Book;
import spring.project.library.models.Person;
import spring.project.library.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> personBooks(int id) {
        Optional<Person> personOptional = peopleRepository.findById(id);

        if (personOptional.isPresent()) {
            Hibernate.initialize(personOptional.get().getBookList());

            personOptional.get().getBookList().forEach(book -> {
                long diff = Math.abs(new Date().getTime() - book.getTakenAt().getTime());
                // 14 суток
                if (diff > 1209600000) {
                    book.setExpired(true);
                }
            });

            return personOptional.get().getBookList();
        } else {
            return Collections.emptyList();
        }
    }

    public Optional<Person> findByEmail(String email) {
        return peopleRepository.findPersonByEmail(email);
    }

}
