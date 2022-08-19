package com.b2.b2data.controller;

import com.b2.b2data.domain.Account;
import com.b2.b2data.dto.AccountDTO;
import com.b2.b2data.service.AccountService;
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
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountControllerTest {

    @Autowired
    private AccountController con;

    @Autowired
    private AccountService svc;

    private List<Account> initialState;

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

        @DisplayName("passing all null values gets all accounts")
        @Test
        public void getAll_test1() {
            int count = Objects.requireNonNull(con.getAll(null, null, null).getBody()).getData().size();
            assertEquals(initialState.size(), count);
        }

        @DisplayName("can get all accounts by element number")
        @ParameterizedTest
        @MethodSource("getAll_test2_generator")
        public void getAll_test2(int elementNumber, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(elementNumber, null, null).getBody()).getData().size();
            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test2_generator() {
            return Stream.of(
                    Arguments.of(1, 2),
                    Arguments.of(2, 1),
                    Arguments.of(99, 0)
            );
        }

        @DisplayName("can get all accounts by player name")
        @ParameterizedTest
        @MethodSource("getAll_test3_generator")
        public void getAll_test3(String playerName, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(null, playerName, null).getBody()).getData().size();
            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test3_generator() {
            return Stream.of(
                    Arguments.of("Chase Bank", 1),
                    Arguments.of("Bank of America", 1),
                    Arguments.of("US Bank", 1),
                    Arguments.of("Vanguard", 1),
                    Arguments.of("McDonald's", 1),
                    Arguments.of("Walmart", 1),
                    Arguments.of("Target", 1),
                    Arguments.of("Costco", 0),
                    Arguments.of("Amazon", 0),
                    Arguments.of("99", 0)
            );
        }

        @DisplayName("can get all accounts by isBank status")
        @ParameterizedTest
        @MethodSource("getAll_test4_generator")
        public void getAll_test4(boolean isBank, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(null, null, isBank).getBody()).getData().size();
            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test4_generator() {
            return Stream.of(
                    Arguments.of(true, 4),
                    Arguments.of(false, 6)
            );
        }

        @DisplayName("can get all accounts by all 3 parameters")
        @ParameterizedTest
        @MethodSource("getAll_test5_generator")
        public void getAll_test5(int elementNumber, String playerName, boolean isBank, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(elementNumber, playerName, isBank).getBody())
                               .getData()
                               .size();
            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test5_generator() {
            return Stream.of(
                    Arguments.of(1, "Chase Bank", true, 1),
                    Arguments.of(2, "US Bank", true, 1),
                    Arguments.of(3, "Vanguard", true, 1),
                    Arguments.of(4, "McDonald's", false, 1),
                    Arguments.of(99, "-getAll-test5-", true, 0)
            );
        }

        @DisplayName("response from getAll is OK")
        @Test
        public void getAll_test6() {
            HttpStatus status = con.getAll(null, null, null).getStatusCode();
            assertEquals(HttpStatus.OK, status);
        }
    }

    @Nested
    @DisplayName("GetByNumber")
    public class GetByNumber {

        @DisplayName("can get account by number")
        @ParameterizedTest
        @ValueSource(strings = {"1000", "1001", "2000", "3000", "4000", "4001", "5000", "5001", "6000", "99"})
        public void getByNumber_test1(String accountNumber) {
            AccountDTO dto = Objects.requireNonNull(con.getByNumber(accountNumber).getBody()).getData().get(0);
            assertNotNull(dto);
        }

        @DisplayName("search non-existent account returns null data")
        @Test
        public void getByNumber_test2() {
            List<AccountDTO> data = Objects.requireNonNull(con.getByNumber("-getByNumber-test2-").getBody()).getData();
            assertNull(data);
        }

        @DisplayName("response from successful get by number is OK")
        @Test
        public void getByNumber_test3() {
            HttpStatus status = con.getByNumber("99").getStatusCode();
            assertEquals(HttpStatus.OK, status);
        }

        @DisplayName("response from unsuccessful get by number is NOT FOUND")
        @Test
        public void getByNumber_tes43() {
            HttpStatus status = con.getByNumber("-getByNumber-test4-").getStatusCode();
            assertEquals(HttpStatus.NOT_FOUND, status);
        }
    }

    @Nested
    @DisplayName("CreateOne")
    public class CreateOne {

        @DisplayName("can create one account")
        @Test
        public void createOne_test1() {
            String number = "-createOne-test1-a-";
            AccountDTO dto = new AccountDTO();
            dto.setNumber(number);
            dto.setName("-createOne-test1-b-");
            dto.setElement(99);
            dto.setPlayer("99");

            dto = Objects.requireNonNull(con.createOne(dto).getBody()).getData().get(0);
            Account account = svc.findByNumber(number);

            assert con.deleteOne(dto.getNumber()).getStatusCode().equals(HttpStatus.NO_CONTENT);
            assertNotNull(account);
        }

        @DisplayName("response from successful creation is CREATED")
        @Test
        public void createOne_test2() {
            String number = "-createOne-test2-a-";
            AccountDTO dto = new AccountDTO();
            dto.setNumber(number);
            dto.setName("-createOne-test2-b-");
            dto.setElement(99);
            dto.setPlayer("99");

            HttpStatus status = con.createOne(dto).getStatusCode();

            assert con.deleteOne(dto.getNumber()).getStatusCode().equals(HttpStatus.NO_CONTENT);
            assertEquals(HttpStatus.CREATED, status);
        }

        @DisplayName("location header URI contains new account number")
        @Test
        public void createOne_test3() {
            String number = "-createOne-test3-a-";
            AccountDTO dto = new AccountDTO();
            dto.setNumber(number);
            dto.setName("-createOne-test3-b-");
            dto.setElement(99);
            dto.setPlayer("99");

            String location = Objects.requireNonNull(con.createOne(dto).getHeaders().getLocation()).toString();
            String expectedLocation = ServletUriComponentsBuilder.fromCurrentRequest().toUriString()+"/"+number;

            assert con.deleteOne(dto.getNumber()).getStatusCode().equals(HttpStatus.NO_CONTENT);
            assertEquals(expectedLocation, location);
        }

        @DisplayName("response from unsuccessful creation is BAD REQUEST")
        @Test
        public void createOne_test4() {
            String number = "99"; // duplicate number
            AccountDTO dto = new AccountDTO();
            dto.setNumber(number);
            dto.setName("-createOne-test4-");
            dto.setElement(99);
            dto.setPlayer("99");
            
            HttpStatus status = con.createOne(dto).getStatusCode();
            assertEquals(HttpStatus.BAD_REQUEST, status);
        }
    }

    @Nested
    @DisplayName("UpdateOne")
    public class UpdateOne {

        @DisplayName("can update one account")
        @Test
        @Transactional
        public void updateOne_test1() {
            String number = "99";
            Account account = svc.findByNumber(number);
            String originalName = account.getName();
            String newName = "-updateOne-test1-";

            AccountDTO dto = new AccountDTO();
            dto.setNumber(number);
            dto.setName(newName);
            dto.setElement(account.getElement().getNumber());

            if (account.getPlayer() != null)
                dto.setPlayer(account.getPlayer().getName());

            dto = Objects.requireNonNull(con.updateOne(number, dto).getBody()).getData().get(0);
            String name = svc.findByNumber(number).getName();

            dto.setName(originalName);
            assert con.updateOne(number, dto).getStatusCode().equals(HttpStatus.OK);
            assertEquals(newName, name);
        }

        @DisplayName("response from successful update is OK")
        @Test
        @Transactional
        public void updateOne_test2() {
            String number = "99";
            Account account = svc.findByNumber(number);
            String originalName = account.getName();
            String newName = "-updateOne-test2-";

            AccountDTO dto = new AccountDTO();
            dto.setNumber(number);
            dto.setName(newName);
            dto.setElement(account.getElement().getNumber());

            if (account.getPlayer() != null)
                dto.setPlayer(account.getPlayer().getName());

            HttpStatus status = con.updateOne(number, dto).getStatusCode();

            dto.setName(originalName);
            assert con.updateOne(number, dto).getStatusCode().equals(HttpStatus.OK);
            assertEquals(HttpStatus.OK, status);
        }

        @DisplayName("response from non-existent account update is NOT FOUND")
        @Test
        public void updateOne_test3() {
            String number = "1234567890"; // non-existent account
            AccountDTO dto = new AccountDTO();
            dto.setNumber(number);
            dto.setName("-updateOne-test3-a-");
            dto.setElement(99);

            HttpStatus status = con.updateOne(number, dto).getStatusCode();
            assertEquals(HttpStatus.NOT_FOUND, status);
        }

        @DisplayName("response from bad update is BAD REQUEST")
        @Test
        public void updateOne_test4() {
            AccountDTO dto = new AccountDTO();
            dto.setNumber("-updateOne-test4-a-");
            dto.setName("-updateOne-test4-b-");
            dto.setElement(1234567890); // non-existent element

            HttpStatus status = con.updateOne("99", dto).getStatusCode();
            assertEquals(HttpStatus.BAD_REQUEST, status);
        }
    }

    @Nested
    @DisplayName("DeleteOne")
    public class DeleteOne {

        @DisplayName("can delete one")
        @Test
        public void deleteOne_test1() {
            String number = "-deleteOne-test1-a-";
            AccountDTO dto = new AccountDTO();
            dto.setNumber(number);
            dto.setName("-deleteOne-test1-b-");
            dto.setElement(99);

            con.createOne(dto);
            assert svc.findByNumber(number) != null;

            con.deleteOne(number);
            Account account = svc.findByNumber(number);
            assertNull(account);
        }

        @DisplayName("response from successful delete is NO CONTENT")
        @Test
        public void deleteOne_test2() {
            String number = "-deleteOne-test2-a-";
            AccountDTO dto = new AccountDTO();
            dto.setNumber(number);
            dto.setName("-deleteOne-test2-b-");
            dto.setElement(99);

            con.createOne(dto);
            assert svc.findByNumber(number) != null;

            HttpStatus status = con.deleteOne(number).getStatusCode();
            assertEquals(HttpStatus.NO_CONTENT, status);
        }

        @DisplayName("response from non-existent account delete is NOT FOUND")
        @Test
        public void deleteOne_test3() {
            HttpStatus status = con.deleteOne("-deleteOne-test3-a-").getStatusCode();
            assertEquals(HttpStatus.NOT_FOUND, status);
        }

        @DisplayName("response from bad account delete is BAD REQUEST")
        @Test
        public void deleteOne_test4() {
            HttpStatus status = con.deleteOne("1000").getStatusCode(); // cannot delete due to fk constraints
            assertEquals(HttpStatus.BAD_REQUEST, status);
        }
    }
}
