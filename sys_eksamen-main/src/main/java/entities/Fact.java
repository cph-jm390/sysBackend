package entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "facts")
public class Fact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "fact", nullable = false, unique = true)
    private String body;

    @ManyToMany(mappedBy = "factList")
    private List<User> userList;

    public Fact() {
    }

    public Fact(String body) {
        this.body = body;
    }

    public Fact (Long id, String body) {
        this.id = id;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String fact) {
        this.body = fact;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
