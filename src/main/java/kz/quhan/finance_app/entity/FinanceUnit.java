package kz.quhan.finance_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "t_finance_unit", schema = "finance_app")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinanceUnit {
    public FinanceUnit(Boolean isProfit, Double money, String description, Date timestamp, Category category, Bill bill, AppUser creator) {
        this.isProfit = isProfit;
        this.money = money;
        this.description = description;
        this.timestamp = timestamp;
        this.category = category;
        this.bill = bill;
        this.creator = creator;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "c_is_profit")
    private Boolean isProfit;

    @Column(name = "c_money")
    private Double money;

    @Column(name = "c_description")
    private String description;

    @Column(name = "c_timestamp")
    private Date timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private AppUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    private Bill bill;


}
