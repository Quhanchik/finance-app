package kz.quhan.finance_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_role", schema = "finance_app")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    public Role(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "c_name")
    private String name;


}


