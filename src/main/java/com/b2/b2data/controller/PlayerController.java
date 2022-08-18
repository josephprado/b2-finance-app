package com.b2.b2data.controller;

import com.b2.b2data.domain.Player;
import com.b2.b2data.dto.PlayerDTO;
import com.b2.b2data.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controls requests for {@link Player} resources
 */
@RestController
@RequestMapping("/api/players")
public class PlayerController extends Controller<Player, PlayerDTO> {

    @Autowired
    private PlayerService svc;

    /**
     * Gets all players from the database filtered by the given parameters
     *
     * @param isBank True if the player is a bank, or false otherwise
     * @see Player#setBank(Boolean) Definition of a bank
     * @return A response entity containing a list of player DTOs, sorted by name ascending
     */
    @GetMapping("")
    public ResponseEntity<Response<PlayerDTO>> getAll(
            @RequestParam(name = "isBank", required = false) Boolean isBank) {

        List<PlayerDTO> data = (isBank == null ? svc.findAll() : svc.findAllByBankStatus(isBank))
                                .stream()
                                .map(PlayerDTO::new)
                                .toList();

        return responseCodeOk(data);
    }

    /**
     * Gets the player with the given name
     *
     * @param name A player name
     * @return A response entity containing a DTO of the requested player, or an error message
     *         if it does not exist
     */
    @GetMapping("/{name}")
    public ResponseEntity<Response<PlayerDTO>> getByName(@PathVariable(name = "name") String name) {
        Player player = svc.findByName(name);

        return player == null
                ? responseCodeNotFound(resourceDoesNotExistMessage(name))
                : responseCodeOk(List.of(new PlayerDTO(player)));
    }

    /**
     * Creates a new player in the database
     *
     * @param dto A player DTO
     * @return A response entity containing a DTO of the newly created player, or an error message
     *         if the creation was unsuccessful
     */
    @PostMapping("")
    public ResponseEntity<Response<PlayerDTO>> createOne(@Valid @RequestBody PlayerDTO dto) {
        Player player;

        try {
            player = svc.save(convertDTO(dto, new Player()));

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeCreated(
                List.of(new PlayerDTO(player)),
                "/"+player.getName()
        );
    }

    /**
     * Updates an existing player in the database
     *
     * @param name A player name
     * @param dto A player DTO containing the updates to be saved
     * @return A response entity containing a DTO of the updated player, or an error message
     *         if the update was unsuccessful
     */
    @PatchMapping("/{name}")
    public ResponseEntity<Response<PlayerDTO>> updateOne(@PathVariable(name = "name") String name,
                                                         @Valid @RequestBody PlayerDTO dto) {
        Player player = svc.findByName(name);

        if (player == null)
            return responseCodeNotFound(resourceDoesNotExistMessage(name));

        try {
            player = svc.save(convertDTO(dto, player));

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeOk(
                List.of(new PlayerDTO(player)),
                "/"+name,
                "/"+player.getName()
        );
    }

    /**
     * Deletes an existing player from the database
     *
     * @param name A player name
     * @return A response entity containing the result of the deletion
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Response<PlayerDTO>> deleteOne(@PathVariable(name = "name") String name) {
        Player player = svc.findByName(name);

        if (player == null)
            return responseCodeNotFound(resourceDoesNotExistMessage(name));

        try {
            svc.delete(player);

        } catch (Exception e) {
            return responseCodeBadRequest(e.getMessage());
        }
        return responseCodeNoContent();
    }

    @Override
    protected Player convertDTO(PlayerDTO dto, Player player) {
        assert dto != null;
        assert player != null;

        // to avoid mutating player parameter
        Integer id = player.getId();
        player = new Player();
        player.setId(id);

        player.setName(dto.getName());
        player.setBank(dto.getBank());

        return player;
    }

    @Override
    protected String resourceDoesNotExistMessage(Object key) {
        return "Player name='"+key+"' does not exist.";
    }
}
