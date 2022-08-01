package com.b2.b2data.service;

import com.b2.b2data.model.Account;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
// allows @BeforeAll annotation
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// resets the database after each test
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccountServiceTests {

    @Autowired
    private AccountService svc;

    @Autowired
    private ElementService eSvc;

    @Autowired
    private PlayerService pSvc;
    private List<Account> initialState;

    @BeforeAll
    void setup() {
        initialState = svc.findAll();
        assert initialState.size() == 20;
    }

    @BeforeEach
    private void verifyDataReset() {
        assert svc.findAll().equals(initialState);
    }

    /**
     * public Account findById(String id)
     */
    @Nested
    @DisplayName("FindById")
    public class FindById {

        @DisplayName("can find account by id")
        @ParameterizedTest
        @ValueSource(strings = {
                "1000", "1001", "1002", "1003", "1004",
                "2000", "2010",
                "4000", "4001", "4010",
                "5000", "5001", "5002", "5010", "5011",
                "0", "99", "999",
                " `1234567890-=~!@#$%^&*()_+[]\\{}|;':\",./<>?`", "ABC"

        })
        public void findById_test1(String id) {
            Account account = svc.findById(id);
            assertNotNull(account);
        }

        @DisplayName("returns null if account id does not exist")
        @Test
        public void findById_test2() {
            Account account = svc.findById("-find-by-id-test-2");
            assertNull(account);
        }
    }

    /**
     * public Account findByName(String name)
     */
    @Nested
    @DisplayName("FindByName")
    public class FindByName {

        @DisplayName("can find account by name")
        @ParameterizedTest
        @ValueSource(strings = {
                "Cash", "Chase Checking", "Chase Savings", "USB Checking", "BOA Checking",
                "Accounts Payable", "USB Mortgage",
                "Wages & Salaries", "Payroll Tax", "Interest Income",
                "Property Tax", "Mortgage Principal", "Mortgage Interest", "Groceries", "Dining Out",
                "Statistic", "TEST", "Other",
                " `1234567890-=~!@#$%^&*()_+[]\\{}|;':\",./<>?`", "ABC"
        })
        public void findByName_test1(String name) {
            Account account = svc.findByName(name);
            assertNotNull(account);
        }

        @DisplayName("returns null if account name does not exist")
        @Test
        public void findByName_test2() {
            Account account = svc.findByName("-find-by-name-test-2-");
            assertNull(account);
        }

        @DisplayName("find by null name does not throw exception")
        @Test
        public void findByName_test3() {
            assertDoesNotThrow(() -> svc.findByName(null));
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

        @DisplayName("can find all accounts with the given element")
        @Test
        public void findAllByElement_test1() {
            int count = svc.findAll(eSvc.findById(5)).size();
            assertEquals(count, 10);
        }
    }

    /**
     * public List<Account> findAll(Player player)
     */
    @Nested
    @DisplayName("FindAllByPlayer")
    public class FindAllByPlayer {

        @DisplayName("can find all accounts with the given player")
        @Test
        public void findAllByPlayer_test1() {
            int count = svc.findAll(pSvc.findById(4)).size();
            assertEquals(count, 3);
        }
    }

    /**
     * public Account save(Account account)
     */
    @Nested
    @DisplayName("Save")
    public class Save {

        @DisplayName("can save one account")
        @Test
        public void save_test1() {
            Account account = new Account("1234567890", "-save-test-1-", eSvc.findById(99));

            svc.save(account);

            int count = svc.findAll().size();
            svc.delete(account);
            assertEquals(count, initialState.size()+1);
        }

        @DisplayName("save returns the account saved")
        @Test
        @Transactional // ensures method uses a single transaction, so that lazy loaded fields are fetched
        public void save_test2() {
            Account input = new Account("1234567890", "-save-test-2-", eSvc.findById(99));

            Account output = svc.save(input);

            svc.delete(input);
            assertEquals(output, input);
        }

        @DisplayName("can update one account")
        @Test
        public void save_test3() {
            String id = "99";
            Account original = svc.findById(id);
            String originalName = original.getName();
            original.setName("-save-test-3-");

            svc.save(original);

            String updatedName = svc.findById(id).getName();
            original.setName(originalName);
            svc.save(original);
            assertNotEquals(updatedName, originalName);
        }

        @DisplayName("duplicate entry does not save")
        @Test
        public void save_test4() {
            Account account = new Account("1234567890", "-save-test-4-", eSvc.findById(99));

            svc.save(account);
            svc.save(account);

            int count = svc.findAll().size();
            svc.delete(account);
            assertEquals(count, initialState.size()+1);
        }

        @DisplayName("saving null account throws exception")
        @Test
        public void save_test5() {
            assertThrows(Exception.class, () -> svc.save(null));
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
            String id = "99";
            Account account = svc.findById(id);

            svc.delete(account);

            Account result = svc.findById(id);
            svc.save(account);
            assertNull(result);
        }

        @DisplayName("deleting non-existing account does nothing")
        @Test
        public void delete_test2() {
            svc.delete(new Account("1234567890", "-delete-test-2", eSvc.findById(99)));

            int count = svc.findAll().size();
            assertEquals(count, initialState.size());
        }

        @DisplayName("deleting null account throws exception")
        @Test
        public void delete_test3() {
            assertThrows(Exception.class, () -> svc.delete(null));
        }
    }
}
