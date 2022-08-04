package com.b2.b2data.service;

import com.b2.b2data.domain.Account;
import com.b2.b2data.domain.Player;
import com.b2.b2data.domain.Transaction;
import com.b2.b2data.domain.TransactionLine;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionLineServiceTest {

    @Autowired
    private TransactionLineService svc;

    @Autowired
    private TransactionService tSvc;

    @Autowired
    private AccountService aSvc;

    @Autowired
    private PlayerService pSvc;
    private List<TransactionLine> initialState;

    @BeforeAll
    private void setup() {
        initialState = svc.findAll();
        assert initialState.size() == 26;
    }

    @BeforeEach
    private void verifyDataReset() {
        assert svc.findAll().equals(initialState);
    }

    /**
     * public TransactionLine findById(int transactionId, int lineId)
     */
    @Nested
    @DisplayName("FindById")
    public class FindById {

        @DisplayName("can find one by id")
        @ParameterizedTest
        @MethodSource("findById_test1_generator")
        public void findById_test1(int transactionId, int lineId) {
            TransactionLine line = svc.findById(transactionId, lineId);
            assertNotNull(line);
        }

        private static Stream<Arguments> findById_test1_generator() {
            return Stream.of(
                    Arguments.of(1, 1),
                    Arguments.of(1, 2),
                    Arguments.of(1, 3),
                    Arguments.of(1, 4),

                    Arguments.of(2, 1),
                    Arguments.of(2, 2),
                    Arguments.of(2, 3),
                    Arguments.of(2, 4),

                    Arguments.of(3, 1),
                    Arguments.of(3, 2),

                    Arguments.of(4, 1),
                    Arguments.of(4, 2)
            );
        }

        @DisplayName("search for non-existent id returns null")
        @Test
        public void findById_test2() {
            TransactionLine line = svc.findById(-1, -1);
            assertNull(line);
        }
    }

    /**
     * public List<TransactionLine> findAll()
     */
    @Nested
    @DisplayName("FindAll")
    public class FindAll {

        @DisplayName("can find all lines")
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

        @DisplayName("can find all lines of transaction")
        @ParameterizedTest
        @MethodSource("findAllByTransaction_test1_generator")
        public void findAllByTransaction_test1(int transactionId, int expectedCount) {
            Transaction transaction = tSvc.findById(transactionId);
            int count = svc.findAll(transaction).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllByTransaction_test1_generator() {
            return Stream.of(
                    Arguments.of(1, 4),
                    Arguments.of(2, 4),
                    Arguments.of(3, 2),
                    Arguments.of(4, 2),
                    Arguments.of(5, 2),
                    Arguments.of(6, 2),
                    Arguments.of(7, 2),
                    Arguments.of(8, 2),
                    Arguments.of(9, 2),
                    Arguments.of(10, 2),
                    Arguments.of(11, 2),
                    Arguments.of(12, 0)
            );
        }
    }

    /**
     * public List<TransactionLine> findAll(Account account)
     */
    @Nested
    @DisplayName("FindAllByAccount")
    public class FindAllByAccount {

        @DisplayName("can find all lines of account")
        @ParameterizedTest
        @MethodSource("findAllByAccount_test1_generator")
        public void findAllByAccount_test1(int accountId, int expectedCount) {
            Account account = aSvc.findById(accountId);
            int count = svc.findAll(account).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllByAccount_test1_generator() {
            return Stream.of(
                    Arguments.of(1, 9),
                    Arguments.of(2, 2),
                    Arguments.of(3, 0),
                    Arguments.of(4, 0),
                    Arguments.of(5, 1),
                    Arguments.of(6, 1),
                    Arguments.of(7, 10),
                    Arguments.of(8, 3),
                    Arguments.of(9, 0),
                    Arguments.of(10, 0)
            );
        }
    }

    /**
     * public List<TransactionLine> findAll(Player player)
     */
    @Nested
    @DisplayName("FindAllByPlayer")
    public class FindAllByPlayer {

        @DisplayName("can find all lines of player")
        @ParameterizedTest
        @MethodSource("findAllByPlayer_test1_generator")
        public void findAllByPlayer_test1(int playerId, int expectedCount) {
            Player player = pSvc.findById(playerId);
            int count = svc.findAll(player).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllByPlayer_test1_generator() {
            return Stream.of(
                    Arguments.of(1, 2),
                    Arguments.of(2, 2),
                    Arguments.of(3, 0),
                    Arguments.of(4, 0),
                    Arguments.of(5, 0),
                    Arguments.of(6, 18),
                    Arguments.of(7, 0),
                    Arguments.of(8, 0),
                    Arguments.of(9, 4),
                    Arguments.of(10, 0)
            );
        }
    }

    /**
     * public List<TransactionLine> findAll(String memoPattern)
     */
    @Nested
    @DisplayName("FindAllByMemo")
    public class FindAllByMemo {

        @DisplayName("can find all with memo like pattern")
        @ParameterizedTest
        @MethodSource("findAllByMemo_test1_generator")
        public void findAllByMemo_test1(String memoPattern, int expectedCount) {
            int count = svc.findAll(memoPattern).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllByMemo_test1_generator() {
            return Stream.of(
                    Arguments.of("memo%", 12),
                    Arguments.of("%mo", 20),
                    Arguments.of("%memo%", 22),
                    Arguments.of("1%o", 2),
                    Arguments.of("_-%", 4)
            );
        }
    }

    /**
     * public TransactionLine save(TransactionLine line)
     */
    @Nested
    @DisplayName("Save")
    public class Save {

        @DisplayName("saved line is persisted in the database")
        @Test
        public void save_test1() {
            int tranId = 12;
            int lineId = 1;
            TransactionLine line =
                    svc.save(new TransactionLine(tSvc.findById(tranId), lineId, aSvc.findById(1), 100.00));
            boolean saved = svc.findById(tranId, lineId) != null;
            svc.delete(line);
            assertTrue(saved);
        }

        @DisplayName("cannot save duplicate line")
        @Test
        public void save_test2() {
            int tranId = 12;
            int lineId = 1;
            String memo = "-save-test-2-";
            TransactionLine line =
                    new TransactionLine(tSvc.findById(tranId), lineId, aSvc.findById(1), 100.00);
            line.setMemo(memo);
            svc.save(line);
            svc.save(line);
            boolean saved = svc.findAll(memo).size() == 1;
            svc.delete(line);
            assertTrue(saved);
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
            int tranId = 12;
            int lineId = 1;
            TransactionLine line =
                    svc.save(new TransactionLine(tSvc.findById(tranId), lineId, aSvc.findById(1), 100.00));
            assert svc.findById(tranId, lineId) != null;
            svc.delete(line);
            boolean deleted = svc.findById(tranId, lineId) == null;
            assertTrue(deleted);
        }
    }
}
