package com.b2.b2data.controller;

import com.b2.b2data.domain.Account;
import com.b2.b2data.dto.AccountDTO;
import com.b2.b2data.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.*;

/**
 * Controls requests for {@link Account} resources
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController extends Controller<Account, AccountDTO, String> {

    @Autowired
    private AccountService svc;

    @Autowired
    private ElementController eCon;

    @Autowired
    private PlayerController pCon;

    /**
     * Gets all accounts from the database filtered by the given parameters
     *
     * @param elementNumber An element number
     * @param playerName A player name
     * @param isBank True or false
     * @return A response entity containing a list of account DTOs, sorted by account number ascending
     */
    @GetMapping("")
    public ResponseEntity<Response<AccountDTO>> getAll(
            @RequestParam(name = "elementNumber", required = false) Integer elementNumber,
            @RequestParam(name = "playerName", required = false) String playerName,
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
     * @return A response entity containing a DTO of the requested account, or an error message
     *         if it does not exist
     */
    @GetMapping("/{number}")
    public ResponseEntity<Response<AccountDTO>> getByNumber(@PathVariable(name = "number") String number) {
        Account account;

        try {
            account = getExistingEntry(number);

        } catch (ValidationException e) {
            return responseCodeNotFound(e.getMessage());
        }
        return responseCodeOk(List.of(new AccountDTO(account)));
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
            account = convertDtoToEntry(dto, new Account());

        } catch (ValidationException e) {
            return responseCodeBadRequest(e.getMessage());

        } try {
            account = svc.save(account);

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
        Account account;

        try {
            account = getExistingEntry(number);

        } catch (ValidationException e) {
            return responseCodeNotFound(e.getMessage());

        } try {
            account = convertDtoToEntry(dto, account);

        } catch (ValidationException e) {
            return responseCodeBadRequest(e.getMessage());

        } try {
            account = svc.save(account);

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
     * @return A response entity containing the result of the deletion
     */
    @DeleteMapping("/{number}")
    public ResponseEntity<Response<AccountDTO>> deleteOne(@PathVariable(name = "number") String number) {
        Account account;

        try {
            account = getExistingEntry(number);

        } catch (ValidationException e) {
            return responseCodeNotFound(e.getMessage());

        } try {
            svc.delete(account);

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeNoContent();
    }

    /**
     * Returns the account with the given number
     *
     * @param number An account number
     * @return The account with the given number
     * @throws ValidationException If the account number does not exist
     */
    @Override
    protected Account getExistingEntry(String number) throws ValidationException {
        Account account = svc.findByNumber(number);

        if (account == null)
            throw new ValidationException("Account number='"+number+"' does not exist.");

        return account;
    }

    /**
     * Transfers the given DTO's values into the given account
     *
     * @param dto An account DTO; must not be null
     * @param account An account; must not be null
     * @return An account with field values matching the account DTO
     * @throws ValidationException If the account DTO is invalid
     */
    @Override
    protected Account convertDtoToEntry(AccountDTO dto, Account account) throws ValidationException {
        assert dto != null;
        assert account != null;

        // to avoid mutating account parameter
        Integer id = account.getId();
        account = new Account();
        account.setId(id);

        account.setNumber(dto.getNumber());
        account.setName(dto.getName());
        account.setElement(eCon.getExistingEntry(dto.getElementNumber()));

        if (dto.getPlayerName() != null)
            account.setPlayer(pCon.getExistingEntry(dto.getPlayerName()));

        return account;
    }
}
