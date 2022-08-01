package com.b2.b2data.service;

import com.b2.b2data.model.Account;
import com.b2.b2data.model.Transaction;
import com.b2.b2data.model.TransactionLine;
import org.junit.jupiter.api.*;
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
class TransactionLineServiceTests {

    @Autowired
    private TransactionLineService svc;

    @Autowired
    private TransactionService tSvc;

    @Autowired
    private AccountService aSvc;
    private List<TransactionLine> initialState;

    @BeforeAll
    void setup() {
        initialState = svc.findAll();
        assert initialState.size() == 28;
    }

    @BeforeEach
    private void verifyDataReset() {
        assert svc.findAll().equals(initialState);
    }

    /**
     * public List<TransactionLine> findAll()
     */
    @Nested
    @DisplayName("FindAll")
    public class FindAll {

        @DisplayName("can find all transaction lines")
        @Test
        public void findAll_test1() {
            List<TransactionLine> lines = svc.findAll();
            assertEquals(lines, initialState);
        }
    }

    /**
     * public List<TransactionLine> findAll(Transaction transaction)
     */
    @Nested
    @DisplayName("FindAllByTransaction")
    public class FindAllByTransaction {

        @DisplayName("can find all lines for the given transaction")
        @Test
        public void findAllByTransaction_test1() {
            Transaction transaction = tSvc.findById(8);

            int count = svc.findAll(transaction).size();

            assertEquals(count, 6);
        }

        @DisplayName("searching for null transaction does not throw exception")
        @Test
        public void findAllByTransaction_test3() {
            assertDoesNotThrow(() -> svc.findAll((Transaction) null));
        }
    }

    /**
     * public List<TransactionLine> findAll(String memoPattern)
     */
    @Nested
    @DisplayName("FindAllByMemoLike")
    public class FindAllByMemoLike {

        @DisplayName("can find all lines with 'L' in memo (case-insensitive)")
        @Test
        public void findAllByMemoLike_test1() {
            int count = svc.findAll("%L%").size();
            assertEquals(count, 22);
        }

        @DisplayName("can find all lines with memo starting with 'L' (case-insensitive)")
        @Test
        public void findAllByMemoLike_test2() {
            int count = svc.findAll("L_%").size();
            assertEquals(count, 18);
        }

        @DisplayName("can find all lines with memo starting with 'Mortgage' and has a ' ' (case-insensitive)")
        @Test
        public void findAllByMemoLike_test3() {
            int count = svc.findAll("mortgage% %").size();
            assertEquals(count, 2);
        }

        @DisplayName("searching for null string does not throw exception")
        @Test
        public void findAllByMemoLike_test4() {
            assertDoesNotThrow(() -> svc.findAll((String) null));
        }
    }

    /**
     *  public TransactionLine save(TransactionLine line)
     */
    @Nested
    @DisplayName("Save")
    public class Save {

        @DisplayName("can save one line")
        @Test
        public void save_test1() {
            Transaction transaction = tSvc.findById(8);
            int originalCount = svc.findAll(transaction).size();
            Account account = aSvc.findById("99");
            TransactionLine line =
                    new TransactionLine(transaction, originalCount+1, account, 1000.00);

            TransactionLine saved = svc.save(line);

            Transaction transactionSaved = saved.getTransaction();
            svc.delete(line);
            assertEquals(transactionSaved, transaction);
        }

        @DisplayName("save returns the line saved")
        @Test
        @Transactional // ensures method uses a single transaction, so that lazy loaded fields are fetched
        public void save_test2() {
            Transaction transaction = tSvc.findById(8);
            Account account = aSvc.findById("99");
            TransactionLine input =
                    new TransactionLine(transaction, 1, account, -1000.00);

            TransactionLine output = svc.save(input);

            svc.delete(input);
            assertEquals(output, input);
        }

        @DisplayName("can update one line")
        @Test
        public void save_test3() {
            int lineId = 0;
            Transaction transaction = tSvc.findById(8);
            TransactionLine originalLine = svc.findAll(transaction).get(lineId);
            String originalMemo = originalLine.getMemo();
            originalLine.setMemo("-save-test-3-");

            svc.save(originalLine);

            String updatedMemo = svc.findAll(transaction).get(lineId).getMemo();
            originalLine.setMemo(originalMemo);
            svc.save(originalLine);
            assertNotEquals(updatedMemo, originalMemo);
        }

        @DisplayName("duplicate entry does not save")
        @Test
        public void save_test4() {
            Transaction transaction = tSvc.findById(8);
            int originalCount = svc.findAll(transaction).size();
            Account account = aSvc.findById("99");
            TransactionLine line =
                    new TransactionLine(transaction, originalCount+1, account, 11000.00);
            String memo = "-save-test-4-";
            line.setMemo(memo);

            svc.save(line);
            svc.save(line);

            int count = svc.findAll(memo).size();
            svc.delete(line);
            assertEquals(count, 1);
        }

        @DisplayName("saving null line throws exception")
        @Test
        public void save_test5() {
            assertThrows(Exception.class, () -> svc.save(null));
        }
    }

    /**
     * public void delete(TransactionLine line)
     */
    @Nested
    @DisplayName("Delete")
    public class Delete {

        @DisplayName("can delete one line")
        @Test
        public void delete_test1() {
            Transaction transaction = tSvc.findById(8);
            int originalCount = svc.findAll(transaction).size();
            Account account = aSvc.findById("99");
            TransactionLine line =
                    new TransactionLine(transaction, originalCount+1, account, 1);
            svc.save(line);
            assert svc.findAll(transaction).size() == originalCount+1;

            svc.delete(line);

            int count = svc.findAll(transaction).size();
            assertEquals(count, originalCount);
        }

        @DisplayName("deleting non-existing line does nothing")
        @Test
        public void delete_test2() {
            Transaction transaction = tSvc.findById(8);
            int originalCount = svc.findAll(transaction).size();
            Account account = aSvc.findById("99");

            svc.delete(new TransactionLine(transaction, originalCount+1, account, 1));

            int count = svc.findAll(transaction).size();
            assertEquals(count, originalCount);
        }

        @DisplayName("deleting null line throws exception")
        @Test
        public void delete_test3() {
            assertThrows(Exception.class, () -> svc.delete(null));
        }
    }
}
