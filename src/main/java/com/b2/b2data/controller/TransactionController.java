package com.b2.b2data.controller;

import com.b2.b2data.domain.Transaction;
import com.b2.b2data.domain.TransactionLine;
import com.b2.b2data.dto.TransactionDTO;
import com.b2.b2data.dto.TransactionLineDTO;
import com.b2.b2data.service.TransactionLineService;
import com.b2.b2data.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controls requests for {@link Transaction} resources
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController extends Controller<Transaction, TransactionDTO, Integer> {

    @Autowired
    private TransactionService svc;

    @Autowired
    private TransactionLineService lSvc;

    @Autowired
    private TransactionLineController lCon;

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
        Transaction transaction;

        try {
            transaction = getExistingEntry(id);

        } catch (ValidationException e) {
            return responseCodeNotFound(e.getMessage());
        }
        return responseCodeOk(List.of(getDtoWithLines(transaction)));
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
        List<TransactionLine> lines;

        try {
            lines = validLines(dto.getLines());

        } catch (ValidationException e) {
            return responseCodeBadRequest(e.getMessage());
        }
        Transaction transaction = convertDtoToEntry(dto, new Transaction());

        try {
            transaction = svc.save(transaction, lines);
        }
        catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeCreated(
                List.of(getDtoWithLines(transaction)),
                "/"+transaction.getId()
        );
    }

    // TODO: if just updating transaction fields (date and/or memo), need to ignore lines
    /**
     * Updates an existing transaction in the database
     *
     * @param id A transaction id
     * @param dto A transaction DTO
     * @return A response entity containing a DTO of the updated transaction, or an error message
     *         if the update was unsuccessful
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Response<TransactionDTO>> updateOne(@PathVariable(name = "id") Integer id,
                                                              @Valid @RequestBody TransactionDTO dto) {
        Transaction transaction;

        try {
            transaction = getExistingEntry(id);

        } catch (ValidationException e) {
            return responseCodeNotFound(e.getMessage());
        }
        transaction = convertDtoToEntry(dto, transaction);
        List<TransactionLine> lines;

        try {
            lines = validLines(dto.getLines());

        } catch (ValidationException e) {
            return responseCodeBadRequest(e.getMessage());

        } try {
            transaction = svc.save(transaction, lines);

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeOk(
                List.of(getDtoWithLines(transaction)),
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
        Transaction transaction;

        try {
            transaction = getExistingEntry(id);

        } catch (ValidationException e) {
            return responseCodeNotFound(e.getMessage());

        } try {
            svc.delete(transaction);

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeNoContent();
    }

    /**
     * Returns a list of valid transaction lines from the given list of transaction line DTOs
     *
     * @param lineDTOs A list of transaction line DTOs
     * @return A list of transaction lines
     * @throws ValidationException If the list of transaction line DTOs is invalid.
     *                             A list of line DTOs is valid if:
     * <ul>
     *     <li>it contains at least 2 lines</li>
     *     <li>each account number is valid</li>
     *     <li>each player name is valid or null</li>
     *     <li>the sum of all line amounts equals zero</li>
     * </ul>
     */
    private List<TransactionLine> validLines(List<TransactionLineDTO> lineDTOs) throws ValidationException {

        if (lineDTOs == null || lineDTOs.size() < 2)
            throw new ValidationException("Lines must contain >= 2 transaction lines.");

        List<TransactionLine> lines = new ArrayList<>(lineDTOs.size());
        double sum = 0;

        for (TransactionLineDTO dto : lineDTOs) {
            lines.add(lCon.convertDtoToEntry(dto, new TransactionLine()));
            sum += dto.getAmount();
        }
        if (sum != 0) {
            throw new ValidationException("Sum of line amounts must equal zero.");
        }
        return lines;
    }

    /**
     * Returns a transaction DTO with all associated transaction lines
     *
     * @param transaction A transaction; must not be null
     * @return A transaction DTO with all associated transaction lines
     */
    private TransactionDTO getDtoWithLines(Transaction transaction) {
        assert transaction != null;

        TransactionDTO dto = new TransactionDTO(transaction);
        dto.setLines(
                lSvc.findAllByTransactionId(transaction.getId())
                        .stream()
                        .map(TransactionLineDTO::new)
                        .toList()
        );
        return dto;
    }

    /**
     * Returns the transaction with the given id
     *
     * @param id A transaction id
     * @return The transaction with the given id
     * @throws ValidationException If the transaction id does not exist
     */
    @Override
    protected Transaction getExistingEntry(Integer id) throws ValidationException {
        Transaction transaction = svc.findById(id);

        if (transaction == null)
            throw new ValidationException("Transaction id="+id+" does not exist.");

        return transaction;
    }

    /**
     * Transfers the given DTO's values into the given transaction
     *
     * @param dto A transaction DTO; must not be null
     * @param transaction A transaction; must not be null
     * @return A transaction with field values matching the transaction DTO
     */
    @Override
    protected Transaction convertDtoToEntry(TransactionDTO dto, Transaction transaction) {
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
}
