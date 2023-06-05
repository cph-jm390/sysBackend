package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "animals")
public class Animal {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "animal_name", length = 100)
    private String animalName;

    @Column(name = "taxonomy", length = 1000, nullable = false)
    private String taxonomy;

    @Basic(optional = false)
    @Column(name = "characteristics", length = 1000, nullable = false)
    private String characteristics;

    @ManyToMany(mappedBy = "animalList")
    private List<User> userList;

    public Animal() {
    }

    public Animal(String animalName, String taxonomy, String characteristics) {
        this.animalName = animalName;
        this.taxonomy = taxonomy;
        this.characteristics = characteristics;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }
}
