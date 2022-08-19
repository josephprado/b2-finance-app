package com.b2.b2data.controller;

import com.b2.b2data.domain.Transaction;
import com.b2.b2data.domain.TransactionLine;
import com.b2.b2data.dto.TransactionLineDTO;
import com.b2.b2data.service.AccountService;
import com.b2.b2data.service.PlayerService;
import com.b2.b2data.service.TransactionLineService;
import com.b2.b2data.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lines")
public class TransactionLineController extends Controller<TransactionLine, TransactionLineDTO> {

    @Autowired
    private TransactionLineService svc;

    @Autowired
    private TransactionService tSvc;

    @Autowired
    private AccountService aSvc;

    @Autowired
    private PlayerService pSvc;

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

    @Override
    protected TransactionLine convertDTO(TransactionLineDTO dto, TransactionLine transactionLine) {
        assert dto != null;
        assert transactionLine != null;

        // to avoid mutating transactionLine parameter
        Transaction transaction = transactionLine.getTransaction();
        Integer line = transactionLine.getLine();
        transactionLine = new TransactionLine();
        transactionLine.setTransaction(transaction);
        transactionLine.setLine(line);

        transactionLine.setAccount(aSvc.findByNumber(dto.getAccount()));
        transactionLine.setPlayer(pSvc.findByName(dto.getPlayer()));
        transactionLine.setAmount(dto.getAmount());
        transactionLine.setMemo(dto.getMemo());
        transactionLine.setDateReconciled(dto.getDateReconciled());

        return transactionLine;
    }

    @Override
    protected String resourceDoesNotExistMessage(Object key) {
        return "Transaction line='"+key+"' does not exist.";
    }
}
