package mgpt.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "project_of_user")
public class ProjectOfUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int projectOfUserId;

    @ManyToMany
    @JoinColumn(name = "username")
    private List<User> username;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
