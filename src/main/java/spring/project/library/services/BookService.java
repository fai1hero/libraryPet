package spring.project.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.project.library.models.Book;
import spring.project.library.models.Person;
import spring.project.library.repositories.BooksRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class BookService {

    private final BooksRepository booksRepository;

    @Autowired
    public BookService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll(Sort.by("title"));
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    public void update(Book updatedBook, int id) {
        updatedBook.setOwner(findOne(id).getOwner());
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public Person getOwner(int id) {
        return booksRepository.findById(id).map(book -> book.getOwner()).orElse(null);
    }

    @Transactional
    public void assign(int id, Person person) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(person);
                    book.setTakenAt(new Date());
                });
    }

    @Transactional
    public void release(int id) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTakenAt(null);
                    book.setExpired(false);
                });
    }

    public List<Book> searchByTitle(String request) {
        return booksRepository.findBooksByTitleStartingWith(request);
    }

    public List<Book> searchByAuthor(String request) {
        return booksRepository.findBooksByAuthorStartingWith(request);
    }
}
