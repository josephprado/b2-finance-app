package com.b2.b2data.service;

import com.b2.b2data.domain.Account;
import com.b2.b2data.domain.Element;
import com.b2.b2data.domain.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountServiceTest {

    @Autowired
    private AccountService svc;

    @Autowired
    private ElementService eSvc;

    @Autowired
    private PlayerService pSvc;
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

    /**
     * public Account findById(int id)
     */
    @Nested
    @DisplayName("FindById")
    public class FindById {

        @DisplayName("can find one by id")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
        public void findById_test1(int id) {
            Account account = svc.findById(id);
            assertNotNull(account);
        }

        @DisplayName("search for non-existent id returns null")
        @Test
        public void findById_test2() {
            Account account = svc.findById(-1);
            assertNull(account);
        }
    }

    /**
     * public Account findByNumber(String number)
     */
    @Nested
    @DisplayName("FindByNumber")
    public class FindByNumber {

        @DisplayName("can find one by number")
        @ParameterizedTest
        @ValueSource(strings = {"1000", "1001", "2000", "3000", "4000", "4001", "5000", "5001", "6000", "99"})
        public void findByNumber_test1(String number) {
            Account account = svc.findByNumber(number);
            assertNotNull(account);
        }

        @DisplayName("search for non-existent number returns null")
        @Test
        public void findByNumber_test2() {
            Account account = svc.findByNumber("-find-by-number-test-2-");
            assertNull(account);
        }

        @DisplayName("search for null number returns null")
        @Test
        public void findByNumber_test3() {
            Account account = svc.findByNumber(null);
            assertNull(account);
        }
    }

    /**
     * public Account findByName(String name)
     */
    @Nested
    @DisplayName("FindByName")
    public class FindByName {

        @DisplayName("can find one by name")
        @ParameterizedTest
        @ValueSource(strings = {
                "Checking",
                "Savings",
                "Accounts Payable",
                "Retained Earnings",
                "Salary",
                "Interest Income",
                "Food",
                "Entertainment",
                "Unrealized Gain/Loss",
                "NINETY-NINE"
        })
        public void findByName_test1(String name) {
            Account account = svc.findByName(name);
            assertNotNull(account);
        }

        @DisplayName("search for non-existent name returns null")
        @Test
        public void findByName_test2() {
            Account account = svc.findByName("-find-by-name-test-2-");
            assertNull(account);
        }

        @DisplayName("search for null name returns null")
        @Test
        public void findByName_test3() {
            Account account = svc.findByName(null);
            assertNull(account);
        }
    }

    /**
     * public List<Account> findAll()
     */
    @Nested
    @DisplayName("FindAll")
    public class FindAll {

        @DisplayName("can find all accounts")
        @Test
        public void findAll_test1() {
            List<Account> accounts = svc.findAll();
            assertEquals(accounts, initialState);
        }
    }

    /**
     * public List<Account> findAll(Element element)
     */
    @Nested
    @DisplayName("FindAllByElement")
    public class FindAllByElement {

        @DisplayName("can find all by element")
        @ParameterizedTest
        @MethodSource("findAllByElement_test1_generator")
        public void findAllByElement_test1(int elementId, int expectedCount) {
            Element element = eSvc.findById(elementId);
            int count = svc.findAll(element).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllByElement_test1_generator() {
            return Stream.of(
                    Arguments.of(1, 2),
                    Arguments.of(2, 1),
                    Arguments.of(3, 1),
                    Arguments.of(4, 2),
                    Arguments.of(5, 2),
                    Arguments.of(6, 2),
                    Arguments.of(7, 0),
                    Arguments.of(8, 0),
                    Arguments.of(9, 0),
                    Arguments.of(10, 0)
            );
        }
    }

    /**
     *  public List<Account> findAll(Player player)
     */
    @Nested
    @DisplayName("FindAllByPlayer")
    public class FindAllByPlayer {

        @DisplayName("can find all by player")
        @ParameterizedTest
        @MethodSource("findAllByPlayer_test1_generator")
        public void findAllByPlayer_test1(Integer playerId, int expectedCount) {
            Player player = pSvc.findById(playerId);
            int count = svc.findAll(player).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllByPlayer_test1_generator() {
            return Stream.of(
                    Arguments.of(1, 1),
                    Arguments.of(2, 1),
                    Arguments.of(3, 0),
                    Arguments.of(4, 0),
                    Arguments.of(5, 0),
                    Arguments.of(6, 0),
                    Arguments.of(7, 0),
                    Arguments.of(8, 0),
                    Arguments.of(9, 0),
                    Arguments.of(10, 0)
            );
        }

        @DisplayName("can find all by player (2)")
        @Test
        public void findAllByPlayer_test2() {
            int count = svc.findAll((Player) null).size();
            assertEquals(count, 8);
        }
    }

    /**
     * public Account save(Account account)
     */
    @Nested
    @DisplayName("Save")
    public class Save {

        @DisplayName("account is assigned id when saved")
        @Test
        public void save_test1() {
            Account account = svc.save(
                    new Account("1234567890", "-save-test-1-", eSvc.findById(1))
            );
            svc.delete(account);
            assertNotNull(account.getId());
        }

        @DisplayName("saved account is persisted in the database")
        @Test
        public void save_test2() {
            String number = "1234567890";
            Account account = svc.save(
                    new Account(number, "-save-test-2-", eSvc.findById(1))
            );
            boolean saved = svc.findByNumber(number) != null;
            svc.delete(account);
            assertTrue(saved);
        }
    }

    /**
     * public void delete(Account account)
     */
    @Nested
    @DisplayName("Delete")
    public class Delete {

        @DisplayName("can delete one account")
        @Test
        public void delete_test1() {
            String number = "1234567890";
            Account account = svc.save(
                    new Account(number, "-delete-test-1-", eSvc.findById(1))
            );
            assert svc.findByNumber(number) != null;
            svc.delete(account);
            boolean deleted = svc.findByNumber(number) == null;
            assertTrue(deleted);
        }
    }
}
