package com.b2.b2data.controller;

import com.b2.b2data.domain.Transaction;
import com.b2.b2data.domain.TransactionLine;
import com.b2.b2data.domain.TransactionLineId;
import com.b2.b2data.dto.TransactionLineDTO;
import com.b2.b2data.service.TransactionLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lines")
public class TransactionLineController extends Controller<TransactionLine, TransactionLineDTO, TransactionLineId> {

    @Autowired
    private TransactionLineService svc;

    @Autowired
    private AccountController aCon;

    @Autowired
    private PlayerController pCon;

    /**
     * Gets all transaction lines from the database filtered by the given parameters
     *
     * @param transactionId A transaction id
     * @param accountNumber An account number
     * @param playerName A player name
     * @param memoPattern A memo pattern
     * @param isReconciled True if reconciled date is not null, or false otherwise
     * @param from A minimum bounding date
     * @param to A maximum bounding date
     * @return A response entity containing a list of transaction line DTOs,
     *         sorted by transaction date descending
     */
    @GetMapping("")
    public ResponseEntity<Response<TransactionLineDTO>> getAll(
            @RequestParam(name = "transaction", required = false) Integer transactionId,
            @RequestParam(name = "account", required = false) String accountNumber,
            @RequestParam(name = "player", required = false) String playerName,
            @RequestParam(name = "memoPattern", required = false) String memoPattern,
            @RequestParam(name = "isReconciled", required = false) Boolean isReconciled,
            @RequestParam(name = "from", required = false) LocalDate from,
            @RequestParam(name = "to", required = false) LocalDate to) {

        List<TransactionLineDTO> data =
                svc.findAll(transactionId, accountNumber, playerName, memoPattern, isReconciled, from, to)
                   .stream()
                   .map(TransactionLineDTO::new)
                   .toList();

        return responseCodeOk(data);
    }

    /**
     * Returns the transaction line with the given transaction line id
     *
     * @param id A transaction line id
     * @return The transaction line with the given transaction line id
     * @throws ValidationException If the transaction line id does not exist
     */
    @Override
    protected TransactionLine getExistingEntry(TransactionLineId id) throws ValidationException {
        TransactionLine transactionLine = svc.findById(id.getTransaction(), id.getLine());

        if (transactionLine == null)
            throw new ValidationException("Transaction line="+id.getTransaction()+"-"+id.getLine()+" does not exist.");

        return transactionLine;
    }

    /**
     * Transfers the given transaction line DTO's values into the given transaction line
     *
     * @param dto A transaction line DTO; must not be null
     * @param transactionLine A transaction line; must not be null
     * @return A transaction line with field values matching the transaction line DTO
     * @throws ValidationException If the transaction line DTO is invalid
     */
    @Override
    protected TransactionLine convertDtoToEntry(TransactionLineDTO dto, TransactionLine transactionLine)
            throws ValidationException {

        assert dto != null;
        assert transactionLine != null;

        // to avoid mutating transactionLine parameter
        Transaction transaction = transactionLine.getTransaction();
        Integer line = transactionLine.getLine();
        transactionLine = new TransactionLine();
        transactionLine.setTransaction(transaction);
        transactionLine.setLine(line);

        transactionLine.setAccount(aCon.getExistingEntry(dto.getAccount()));
        transactionLine.setAmount(dto.getAmount());
        transactionLine.setMemo(dto.getMemo());
        transactionLine.setDateReconciled(dto.getDateReconciled());

        if (dto.getPlayer() != null)
            transactionLine.setPlayer(pCon.getExistingEntry(dto.getPlayer()));

        return transactionLine;
    }
}
