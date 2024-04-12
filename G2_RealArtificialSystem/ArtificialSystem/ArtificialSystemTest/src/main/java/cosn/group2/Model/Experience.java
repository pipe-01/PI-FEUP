package cosn.group2.Model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Experience")
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int experienceID;

    /*@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "experienceID")
    private Set<Task> taskSet = new HashSet<>();*/

    public Experience(int experienceID) {
        this.experienceID = experienceID;
    }

    @Override
    public String toString() {
        return "Experience{" +
                "experienceID=" + experienceID +
                '}';
    }

    public Experience() {

    }
}
