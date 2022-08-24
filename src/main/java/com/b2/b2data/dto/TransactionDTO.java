package com.b2.b2data.dto;

import com.b2.b2data.domain.Transaction;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * A data transfer object for transmitting {@link Transaction} entities to and from the client
 */
public class TransactionDTO extends DTO {

    private Integer id;

    @NotNull
    private LocalDate date;
    private String memo;
    private List<TransactionLineDTO> lines;

    /**
     * Constructs a new transaction DTO
     */
    public TransactionDTO() {
    }

    /**
     * Constructs a new transaction DTO
     *
     * @param transaction A transaction; must not be null
     * @throws IllegalArgumentException If the transaction is null
     */
    public TransactionDTO(Transaction transaction) throws IllegalArgumentException {
        if (transaction == null)
            throw new IllegalArgumentException("transaction must not be null.");

        id = transaction.getId();
        date = transaction.getDate();
        memo = transaction.getMemo();
    }

    /**
     * Checks the equality of two transaction DTOs
     *
     * @param o The other transaction DTO to compare with this transaction DTO
     * @return True if the other transaction DTO is equal to this transaction DTO, or false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof TransactionDTO that))
            return false;

        return Objects.equals(id, that.id)
                && Objects.equals(date, that.date)
                && Objects.equals(memo, that.memo);
    }

    /**
     * Returns a hash code value for the transaction DTO
     *
     * @return A hash code value for the transaction DTO
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, date, memo);
    }

    /**
     * Returns a string representation of the transaction DTO
     *
     * @return A string representation of the transaction DTO in the following format:
     * <br/><br/>TransactionDTO{id=id, date=date, memo='memo', lines=lines}
     */
    @Override
    public String toString() {
        return "TransactionDTO{" +
                "id=" + id +
                ", date=" + date +
                ", memo='" + memo + '\'' +
                ", lines=" + lines +
                '}';
    }

    /**
     * Gets the id of the transaction DTO
     *
     * @return The id of the transaction DTO
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the transaction DTO
     *
     * @param id A unique identifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the date of the transaction DTO
     *
     * @return The date of the transaction DTO
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the transaction DTO
     *
     * @param date A date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the memo of the transaction DTO
     *
     * @return The memo of the transaction DTO
     */
    public String getMemo() {
        return memo;
    }

    /**
     * Sets the memo of the transaction DTO
     *
     * @param memo A memo describing the transaction DTO
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * Gets the lines of the transaction DTO
     *
     * @return A list of transaction line DTOs
     */
    public List<TransactionLineDTO> getLines() {
        return lines;
    }

    /**
     * Sets the lines of the transaction DTO
     *
     * @param lines A list of transaction line DTOs
     */
    public void setLines(List<TransactionLineDTO> lines) {
        this.lines = lines;
    }
}
