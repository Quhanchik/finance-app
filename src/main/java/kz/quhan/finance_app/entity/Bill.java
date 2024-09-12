package kz.quhan.finance_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_bill", schema = "finance_app")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "c_name", nullable = false, length = Integer.MAX_VALUE)
    private String Name;

    @Column(name = "c_description", nullable = false, length = Integer.MAX_VALUE)
    private String Description;

    @Column(name = "c_total_money")
    private Double totalMoney;

    @Column(name = "c_total_income")
    private double totalIncome;

    @Column(name = "c_total_expenses")
    private double totalExpenses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private AppUser creator;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_user_bill",
            joinColumns = @JoinColumn(name = "bill_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<AppUser> members;
}