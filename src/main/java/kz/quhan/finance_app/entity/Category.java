package kz.quhan.finance_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_category", schema = "finance_app")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    public Category(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "c_name")
    private String name;

}
