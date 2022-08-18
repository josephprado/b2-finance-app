package com.b2.b2data.controller;

import com.b2.b2data.domain.Account;
import com.b2.b2data.dto.AccountDTO;
import com.b2.b2data.service.AccountService;
import com.b2.b2data.service.ElementService;
import com.b2.b2data.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * Controls requests for {@link Account} resources
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController extends Controller<Account, AccountDTO> {

    @Autowired
    private AccountService svc;

    @Autowired
    private ElementService eSvc;

    @Autowired
    private PlayerService pSvc;

    /**
     * Gets all accounts from the database filtered by the given parameters
     *
     * @param elementNumber An element number
     * @param playerName A player name
     * @param isBank True or false
     * @return A response entity containing a list of account DTOs, sorted by account number ascending
     */
    @GetMapping(value = "")
    public ResponseEntity<Response<AccountDTO>> getAll(
            @RequestParam(name = "element", required = false) Integer elementNumber,
            @RequestParam(name = "player", required = false) String playerName,
            @RequestParam(name = "isBank", required = false) Boolean isBank) {

        List<AccountDTO> data = svc.findAll(elementNumber, playerName, isBank)
                                   .stream()
                                   .map(AccountDTO::new)
                                   .toList();

        return responseCodeOk(data);
    }

    /**
     * Gets the account with the given number
     *
     * @param number An account number
     * @return A response entity containing the requested account, or an error message if it does not exist
     */
    @GetMapping(value = "/{number}")
    public ResponseEntity<Response<AccountDTO>> getByNumber(@PathVariable(name = "number") String number) {
        Account account = svc.findByNumber(number);

        return account == null
                ? responseCodeNotFound(resourceDoesNotExistMessage(number))
                : responseCodeOk(List.of(new AccountDTO(account)));
    }

    /**
     * Creates a new account in the database
     *
     * @param dto An account DTO
     * @return A response entity containing a DTO of the newly created account, or an error message
     *         if the creation was unsuccessful
     */
    @PostMapping("")
    public ResponseEntity<Response<AccountDTO>> createOne(@Valid @RequestBody AccountDTO dto) {
        Account account;

        try {
            account = svc.save(convertDTO(dto, new Account()));

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeCreated(
                List.of(new AccountDTO(account)),
                "/"+account.getNumber()
        );
    }

    /**
     * Updates an existing account in the database
     *
     * @param number An account number
     * @param dto An account DTO containing the updates to be saved
     * @return A response entity containing a DTO of the updated account, or an error message
     *         if the update was unsuccessful
     */
    @PatchMapping("/{number}")
    public ResponseEntity<Response<AccountDTO>> updateOne(@PathVariable(name = "number") String number,
                                                          @Valid @RequestBody AccountDTO dto) {
        Account account = svc.findByNumber(number);

        if (account == null)
            return responseCodeNotFound(resourceDoesNotExistMessage(number));

        try {
            account = svc.save(convertDTO(dto, account));

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeOk(
                List.of(new AccountDTO(account)),
                "/"+number,
                "/"+account.getNumber()
        );
    }

    /**
     * Deletes an existing account from the database
     *
     * @param number An account number
     * @return A response entity containing the result of the delete operation
     */
    @DeleteMapping("/{number}")
    public ResponseEntity<Response<AccountDTO>> deleteOne(@PathVariable(name = "number") String number) {
        Account account = svc.findByNumber(number);

        if (account == null)
            return responseCodeNotFound(resourceDoesNotExistMessage(number));

        try {
            svc.delete(account);

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeNoContent();
    }

    @Override
    protected Account convertDTO(AccountDTO dto, Account account) {
        assert dto != null;
        assert account != null;

        // to avoid mutating account parameter
        Integer id = account.getId();
        account = new Account();
        account.setId(id);

        account.setNumber(dto.getNumber());
        account.setName(dto.getName());
        account.setElement(eSvc.findByNumber(dto.getElement()));
        account.setPlayer(pSvc.findByName(dto.getPlayer()));

        return account;
    }

    @Override
    protected String resourceDoesNotExistMessage(Object key) {
        return "Account number='"+key+"' does not exist.";
    }
}
