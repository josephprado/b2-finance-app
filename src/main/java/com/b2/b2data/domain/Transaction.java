package com.b2.b2data.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "gl_transaction")
public class Transaction extends Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "date_entered", nullable = false)
    @NotNull
    private LocalDate date;

    @Column(name = "memo")
    private String memo;

    @OneToMany(
            mappedBy = "transaction",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @NotNull
    private List<TransactionLine> lines;

    public Transaction() {
    }

    public Transaction(LocalDate date, String memo) {
        this.date = date;
        this.memo = memo;
        this.lines = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Transaction that))
            return false;

        return Objects.equals(id, that.id)
                && date.equals(that.date)
                && Objects.equals(memo, that.memo)
                && lines.equals(that.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, memo, lines);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", memo='" + memo + '\'' +
                ", lines=" + lines.stream().map(TransactionLine::getLine) +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<TransactionLine> getLines() {
        return lines;
    }

    public void setLines(List<TransactionLine> lines) {
        this.lines = lines;
    }
}
