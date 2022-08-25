package com.b2.b2data.controller;

import com.b2.b2data.domain.Player;
import com.b2.b2data.dto.PlayerDTO;
import com.b2.b2data.service.PlayerService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerControllerTest {

    @Autowired
    private PlayerController con;

    @Autowired
    private PlayerService svc;

    private List<Player> initialState;

    @BeforeAll
    private void setup() {
        initialState = svc.findAll();
        assert initialState.size() == 10;
    }

    @BeforeEach
    private void verifyDataReset() {
        assert svc.findAll().equals(initialState);
    }

    @Nested
    @DisplayName("GetAll")
    public class GetAll {

        @DisplayName("passing all null values gets all players")
        @Test
        public void getAll_test1() {
            int count = Objects.requireNonNull(con.getAll(null).getBody()).getData().size();
            assertEquals(initialState.size(), count);
        }


        @DisplayName("response from getAll is OK")
        @Test
        public void getAll_test2() {
            HttpStatus status = con.getAll(null).getStatusCode();
            assertEquals(HttpStatus.OK, status);
        }

        @DisplayName("can find all banks/non-banks")
        @ParameterizedTest
        @MethodSource("getAll_test3_generator")
        public void getAll_test3(boolean isBank, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(isBank).getBody()).getData().size();
            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test3_generator() {
            return Stream.of(
                    Arguments.of(true, 4),
                    Arguments.of(false, 6)
            );
        }
    }

    @Nested
    @DisplayName("GetByName")
    public class GetByName {

        @DisplayName("can get player by name")
        @ParameterizedTest
        @ValueSource(strings = {
                "Chase Bank",
                "Bank of America",
                "US Bank",
                "Vanguard",
                "McDonald's",
                "Walmart",
                "Target",
                "Costco",
                "Amazon",
                "99"
        })
        public void getByName_test1(String playerName) {
            PlayerDTO dto = Objects.requireNonNull(con.getByName(playerName).getBody()).getData().get(0);
            assertNotNull(dto);
        }

//        @DisplayName("search non-existent player returns null data")
//        @Test
//        public void getByName_test2() {
//            List<PlayerDTO> data = Objects.requireNonNull(con.getByName("-getByName-test2-").getBody()).getData();
//            assertNull(data);
//        }

        @DisplayName("response from successful get by name is OK")
        @Test
        public void getByName_test3() {
            HttpStatus status = con.getByName("99").getStatusCode();
            assertEquals(HttpStatus.OK, status);
        }

//        @DisplayName("response from unsuccessful get by name is NOT FOUND")
//        @Test
//        public void getByName_tes4() {
//            HttpStatus status = con.getByName("-getByName-test4-").getStatusCode();
//            assertEquals(HttpStatus.NOT_FOUND, status);
//        }
    }

    @Nested
    @DisplayName("CreateOne")
    public class CreateOne {

        @DisplayName("can create one player")
        @Test
        public void createOne_test1() {
            String name = "-createOne-test1-";
            PlayerDTO dto = new PlayerDTO();
            dto.setName(name);
            dto.setBank(true);

            dto = Objects.requireNonNull(con.createOne(dto).getBody()).getData().get(0);
            Player player = svc.findByName(name);

            assert con.deleteOne(dto.getName()).getStatusCode().equals(HttpStatus.NO_CONTENT);
            assertNotNull(player);
        }

        @DisplayName("response from successful creation is CREATED")
        @Test
        public void createOne_test2() {
            String name = "-createOne-test2-";
            PlayerDTO dto = new PlayerDTO();
            dto.setName(name);
            dto.setBank(true);

            HttpStatus status = con.createOne(dto).getStatusCode();

            assert con.deleteOne(dto.getName()).getStatusCode().equals(HttpStatus.NO_CONTENT);
            assertEquals(HttpStatus.CREATED, status);
        }

        @DisplayName("location header URI contains new player name")
        @Test
        public void createOne_test3() {
            String name = "-createOne-test3-";
            PlayerDTO dto = new PlayerDTO();
            dto.setName(name);
            dto.setBank(true);

            String location = Objects.requireNonNull(con.createOne(dto).getHeaders().getLocation()).toString();
            String expectedLocation = ServletUriComponentsBuilder.fromCurrentRequest().toUriString()+"/"+name;

            assert con.deleteOne(dto.getName()).getStatusCode().equals(HttpStatus.NO_CONTENT);
            assertEquals(expectedLocation, location);
        }

//        @DisplayName("response from unsuccessful creation is BAD REQUEST")
//        @Test
//        public void createOne_test4() {
//            String name = "99"; // duplicate name
//            PlayerDTO dto = new PlayerDTO();
//            dto.setName(name);
//            dto.setBank(true);
//
//            HttpStatus status = con.createOne(dto).getStatusCode();
//            assertEquals(HttpStatus.BAD_REQUEST, status);
//        }
    }

    @Nested
    @DisplayName("UpdateOne")
    public class UpdateOne {

        @DisplayName("can update one player")
        @Test
        @Transactional
        public void updateOne_test1() {
            int id = 10;
            Player player = svc.findById(id);
            String originalName = player.getName();
            String newName = "-updateOne-test1-";

            PlayerDTO dto = new PlayerDTO();
            dto.setName(newName);
            dto.setBank(player.getBank());

            dto = Objects.requireNonNull(con.updateOne(originalName, dto).getBody()).getData().get(0);
            String name = svc.findById(id).getName();

            dto.setName(originalName);
            assert con.updateOne(newName, dto).getStatusCode().equals(HttpStatus.OK);
            assertEquals(newName, name);
        }

        @DisplayName("response from successful update is OK")
        @Test
        @Transactional
        public void updateOne_test2() {
            int id = 10;
            Player player = svc.findById(id);
            String originalName = player.getName();
            String newName = "-updateOne-test2-";

            PlayerDTO dto = new PlayerDTO();
            dto.setName(newName);
            dto.setBank(player.getBank());

            HttpStatus status = con.updateOne(originalName, dto).getStatusCode();

            dto.setName(originalName);
            assert con.updateOne(newName, dto).getStatusCode().equals(HttpStatus.OK);
            assertEquals(HttpStatus.OK, status);
        }

//        @DisplayName("response from non-existent player update is NOT FOUND")
//        @Test
//        public void updateOne_test3() {
//            String name = "-updateOne-test3-a-";
//            PlayerDTO dto = new PlayerDTO();
//            dto.setName("-updateOne-test3-b-");
//            dto.setBank(true);
//
//            HttpStatus status = con.updateOne(name, dto).getStatusCode();
//            assertEquals(HttpStatus.NOT_FOUND, status);
//        }

//        @DisplayName("response from bad update is BAD REQUEST")
//        @Test
//        public void updateOne_test4() {
//            PlayerDTO dto = new PlayerDTO();
//            dto.setName("99"); // duplicate player
//            dto.setBank(true);
//
//            HttpStatus status = con.updateOne("Chase Bank", dto).getStatusCode();
//            assertEquals(HttpStatus.BAD_REQUEST, status);
//        }

        @DisplayName("location header URI contains new player name")
        @Test
        @Transactional
        public void updateOne_test5() {
            int id = 10;
            Player player = svc.findById(id);
            String originalName = player.getName();
            String newName = "-updateOne-test5-";

            PlayerDTO dto = new PlayerDTO();
            dto.setName(newName);
            dto.setBank(player.getBank());

            String location = Objects.requireNonNull(con.updateOne(originalName, dto).getHeaders().getLocation()).toString();
            String expectedLocation = ServletUriComponentsBuilder.fromCurrentRequest().toUriString()+"/"+newName;

            dto.setName(originalName);
            assert con.updateOne(newName, dto).getStatusCode().equals(HttpStatus.OK);
            assertEquals(expectedLocation, location);
        }
    }

    @Nested
    @DisplayName("DeleteOne")
    public class DeleteOne {

        @DisplayName("can delete one")
        @Test
        public void deleteOne_test1() {
            String name = "-deleteOne-test1-";
            PlayerDTO dto = new PlayerDTO();
            dto.setName(name);
            dto.setBank(true);

            con.createOne(dto);
            assert svc.findByName(name) != null;

            con.deleteOne(name);
            assertThrows(NoSuchElementException.class, () -> svc.findByName(name));
        }

        @DisplayName("response from successful delete is NO CONTENT")
        @Test
        public void deleteOne_test2() {
            String name = "-deleteOne-test2-";
            PlayerDTO dto = new PlayerDTO();
            dto.setName(name);
            dto.setBank(true);

            con.createOne(dto);
            assert svc.findByName(name) != null;

            HttpStatus status = con.deleteOne(name).getStatusCode();
            assertEquals(HttpStatus.NO_CONTENT, status);
        }

//        @DisplayName("response from non-existent player delete is NOT FOUND")
//        @Test
//        public void deleteOne_test3() {
//            HttpStatus status = con.deleteOne("-deleteOne-test3-").getStatusCode();
//            assertEquals(HttpStatus.NOT_FOUND, status);
//        }

//        @DisplayName("response from bad player delete is BAD REQUEST")
//        @Test
//        public void deleteOne_test4() {
//            HttpStatus status = con.deleteOne("Chase Bank").getStatusCode(); // cannot delete due to fk constraints
//            assertEquals(HttpStatus.BAD_REQUEST, status);
//        }
    }
}
