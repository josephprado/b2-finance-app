package com.b2.b2data.controller;

import com.b2.b2data.domain.Transaction;
import com.b2.b2data.dto.TransactionDTO;
import com.b2.b2data.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * Controls requests for {@link Transaction} resources
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController extends Controller<Transaction, TransactionDTO> {

    @Autowired
    private TransactionService svc;

    /**
     * Gets all transactions from the database filtered by the given parameters
     *
     * @param from A minimum bounding date
     * @param to A maximum bounding date
     * @param memoPattern A memo pattern
     * @return A response entity containing a list of transaction DTOs, sorted by date descending
     */
    @GetMapping("")
    public ResponseEntity<Response<TransactionDTO>> getAll(
            @RequestParam(name = "from", required = false) LocalDate from,
            @RequestParam(name = "to", required = false) LocalDate to,
            @RequestParam(name = "memoPattern", required = false) String memoPattern) {

        List<TransactionDTO> data = svc.findAll(from, to, memoPattern)
                                       .stream()
                                       .map(TransactionDTO::new)
                                       .toList();

        return responseCodeOk(data);
    }

    /**
     * Gets the transaction with the given id
     *
     * @param id A transaction id
     * @return A response entity containing a DTO of the requested transaction, or an error message
     *         if it does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response<TransactionDTO>> getById(@PathVariable(name = "id") Integer id) {
        Transaction transaction = svc.findById(id);

        return transaction == null
                ? responseCodeNotFound(resourceDoesNotExistMessage(id))
                : responseCodeOk(List.of(new TransactionDTO(transaction)));
    }

    /**
     * Creates a new transaction in the database
     *
     * @param dto A transaction DTO
     * @return A response entity containing a DTO of the newly created transaction, or an error message
     *         if the creation was unsuccessful
     */
    @PostMapping("")
    public ResponseEntity<Response<TransactionDTO>> createOne(@Valid @RequestBody TransactionDTO dto) {
        Transaction transaction;

        try {
            transaction = svc.save(convertDTO(dto, new Transaction()));

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeCreated(
                List.of(new TransactionDTO(transaction)),
                "/"+transaction.getId()
        );
    }

    /**
     * Updates an existing transaction in the database
     *
     * @param id A transaction id
     * @param dto A transaction DTO containing the updates to be saved
     * @return A response entity containing a DTO of the updated transaction, or an error message
     *         if the update was unsuccessful
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Response<TransactionDTO>> updateOne(@PathVariable(name = "id") Integer id,
                                                              @Valid @RequestBody TransactionDTO dto) {
        Transaction transaction = svc.findById(id);

        if (transaction == null)
            return responseCodeNotFound(resourceDoesNotExistMessage(id));

        try {
            transaction = svc.save(convertDTO(dto, transaction));

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeOk(
                List.of(new TransactionDTO(transaction)),
                "/"+id,
                "/"+transaction.getId()
        );
    }

    /**
     * Deletes an existing transaction from the database
     *
     * @param id A transaction id
     * @return A response entity containing the result of the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<TransactionDTO>> deleteOne(@PathVariable(name = "id") Integer id) {
        Transaction transaction = svc.findById(id);

        if (transaction == null)
            return responseCodeNotFound(resourceDoesNotExistMessage(id));

        try {
            svc.delete(transaction);

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeNoContent();
    }

    @Override
    protected Transaction convertDTO(TransactionDTO dto, Transaction transaction) {
        assert dto != null;
        assert transaction != null;

        // to avoid mutating transaction parameter
        Integer id = transaction.getId();
        transaction = new Transaction();
        transaction.setId(id);

        transaction.setDate(dto.getDate());
        transaction.setMemo(dto.getMemo());

        return transaction;
    }

    @Override
    protected String resourceDoesNotExistMessage(Object key) {
        return "Transaction id="+key+" does not exist.";
    }
}
