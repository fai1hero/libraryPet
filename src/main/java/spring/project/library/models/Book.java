package spring.project.library.models;


import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @Size(min = 2, max = 100, message = "Название книги должно содержать от 2 до 100 символов")
    private String title;

    @Column(name = "release_year")
    @Min(value = 1500, message = "Год выпуска книги не должен быть ниже 1500")
    @Max(value = 2100, message = "Год выпуска книги не должен привышать значение 2100")
    private int releaseYear;

    @Column(name = "author")
    @Size(min = 2, max = 50, message = "Имя автора должно содержать от 2 до 50 символов")
    private String author;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;

    public Date getTakenAt() {
        return takenAt;
    }

    @Transient
    private boolean expired;

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }


    public Book() {
    }

    public Book(String title, int releaseYear, String author) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
}
