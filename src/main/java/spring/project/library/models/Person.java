package spring.project.library.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name")
    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 100, message = "имя должно содержать не менее 2 и не более 100 символов")
    private String fullName;

    @Email(message = "Поле должно быть формата email")
    @NotEmpty(message = "Поле не должно быть пустым")
    @Column(name = "email")
    private String email;

    @Transient
    private boolean admin;

    @OneToMany(mappedBy = "owner")
    private List<Book> bookList;

    public Person(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.admin = false;
    }

    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}
