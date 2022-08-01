package com.b2.b2data.service;

import com.b2.b2data.model.Transaction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
// allows @BeforeAll annotation
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionServiceTests {

    @Autowired
    private TransactionService svc;
    private List<Transaction> initialState;

    @BeforeAll
    void setup() {
        initialState = svc.findAll();
        assert initialState.size() == 8;
    }

    @BeforeEach
    private void verifyDataReset() {
        assert svc.findAll().equals(initialState);
    }

    /**
     * public Transaction findById(long id)
     */
    @Nested
    @DisplayName("FindById")
    public class FindById {

        @DisplayName("can find transaction by id")
        @ParameterizedTest
        @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8})
        public void findById_test1(long id) {
            Transaction transaction = svc.findById(id);
            assertNotNull(transaction);
        }

        @DisplayName("returns null if transaction id does not exist")
        @Test
        public void findById_test2() {
            Transaction transaction = svc.findById(-1234567890L);
            assertNull(transaction);
        }
    }

    /**
     * public List<Transaction> findAll()
     */
    @Nested
    @DisplayName("FindAll")
    public class FindAll {

        @DisplayName("can find all transactions")
        @Test
        public void findAll_test1() {
            List<Transaction> transactions = svc.findAll();
            assertEquals(transactions, initialState);
        }
    }

    /**
     * public List<Transaction> findAll(String memoPattern)
     */
    @Nested
    @DisplayName("FindAllByMemoLike")
    public class FindAllByMemoLike {

        @DisplayName("can find all transactions with 'memo' in memo (case-insensitive)")
        @Test
        public void findAllByMemoLike_test1() {
            String memo = "%MeMo%";

            int count = svc.findAll(memo).size();
            assertEquals(count, 7);
        }

        @DisplayName("can find all transactions with memo starting with 'memo' (case-insensitive)")
        @Test
        public void findAllByMemoLike_test2() {
            String memo = "mEmO%";

            int count = svc.findAll(memo).size();
            assertEquals(count, 4);
        }

        @DisplayName("can find all transactions with memo starting with '7' and ending with 'memo' (case-insensitive)")
        @Test
        public void findAllByMemoLike_test3() {
            String memo = "7%MEMO";

            int count = svc.findAll(memo).size();
            assertEquals(count, 1);
        }
    }

    /**
     * public List<Transaction> findAll(LocalDateTime from, LocalDateTime to)
     */
    @Nested
    @DisplayName("FindAllByDateBetween")
    public class FindAllByDateBetween {

        @DisplayName("can find all transactions between given dates")
        @Test
        public void findAllByDateBetween_test1() {
            int count = svc.findAll(
                    LocalDate.of(2022,1,1),
                    LocalDate.of(2022,6,30)
            ).size();
            assertEquals(count, 6);
        }

        @DisplayName("can find all transactions between given dates")
        @Test
        public void findAllByDateBetween_test2() {
            int count = svc.findAll(
                    LocalDate.of(2022,4,1),
                    LocalDate.of(2022,12,31)
            ).size();
            assertEquals(count, 3);
        }
    }

    /**
     * public Transaction save(Transaction transaction)
     */
    @Nested
    @DisplayName("Save")
    public class Save {

        @DisplayName("can save one transaction")
        @Test
        public void save_test1() {
            Transaction transaction = new Transaction(LocalDate.now());

            svc.save(transaction);

            int count = svc.findAll().size();
            svc.delete(transaction);
            assertEquals(count, initialState.size()+1);
        }

        @DisplayName("save returns the transaction saved")
        @Test
        @Transactional // ensures method uses a single transaction, so that lazy loaded fields are fetched
        public void save_test2() {
            Transaction input = new Transaction(LocalDate.now());

            Transaction output = svc.save(input);

            svc.delete(input);
            assertEquals(output, input);
        }

        @DisplayName("can update one transaction")
        @Test
        public void save_test3() {
            long id = 8;
            Transaction original = svc.findById(id);
            String originalMemo = original.getMemo();
            original.setMemo("-save-test-3-");

            svc.save(original);

            String updatedMemo = svc.findById(id).getMemo();
            original.setMemo(originalMemo);
            svc.save(original);
            assertNotEquals(updatedMemo, originalMemo);
        }

        @DisplayName("duplicate entry does not save")
        @Test
        public void save_test4() {
            Transaction transaction = new Transaction(LocalDate.now());

            svc.save(transaction);
            svc.save(transaction);

            int count = svc.findAll().size();
            svc.delete(transaction);
            assertEquals(count, initialState.size()+1);
        }

        @DisplayName("saving null transaction throws exception")
        @Test
        public void save_test5() {
            assertThrows(Exception.class, () -> svc.save(null));
        }

        @DisplayName("saved transaction gets assigned an id")
        @Test
        public void save_test6() {
            Transaction transaction = new Transaction(LocalDate.now());
            assert transaction.getId() == null;

            Transaction saved = svc.save(transaction);

            svc.delete(transaction);
            assertNotNull(saved.getId());
        }
    }

    /**
     * public void delete(Transaction transaction)
     */
    @Nested
    @DisplayName("Delete")
    public class Delete {

        @DisplayName("can delete one transaction")
        @Test
        public void delete_test1() {
            Transaction transaction = new Transaction(LocalDate.now());
            Transaction saved = svc.save(transaction);
            long id = saved.getId();
            assert svc.findAll().size() == initialState.size()+1;

            svc.delete(transaction);

            Transaction result = svc.findById(id);
            assertNull(result);
        }

        @DisplayName("deleting non-existing transaction does nothing")
        @Test
        public void delete_test2() {
            svc.delete(new Transaction(LocalDate.now()));

            int count = svc.findAll().size();
            assertEquals(count, initialState.size());
        }

        @DisplayName("deleting null transaction throws exception")
        @Test
        public void delete_test3() {
            assertThrows(Exception.class, () -> svc.delete(null));
        }
    }
}
