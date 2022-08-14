package com.b2.b2data.service;

import com.b2.b2data.domain.*;
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
     * public List<TransactionLine> findAllByTransaction(Integer transactionId)
     */
    @Nested
    @DisplayName("FindAllByTransaction")
    public class FindAllByTransaction {

        @DisplayName("can find all lines of transaction")
        @ParameterizedTest
        @MethodSource("findAllByTransaction_test1_generator")
        public void findAllByTransaction_test1(int transactionId, int expectedCount) {
            int count = svc.findAllByTransaction(transactionId).size();
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
     * public List<TransactionLine> findAllByAccount(String account)
     */
    @Nested
    @DisplayName("FindAllByAccount")
    public class FindAllByAccount {

        @DisplayName("can find all lines of account")
        @ParameterizedTest
        @MethodSource("findAllByAccount_test1_generator")
        public void findAllByAccount_test1(String accountNumber, int expectedCount) {
            int count = svc.findAllByAccount(accountNumber).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllByAccount_test1_generator() {
            return Stream.of(
                    Arguments.of("1000", 9),
                    Arguments.of("1001", 2),
                    Arguments.of("2000", 0),
                    Arguments.of("3000", 0),
                    Arguments.of("4000", 1),
                    Arguments.of("4001", 1),
                    Arguments.of("5000", 10),
                    Arguments.of("5001", 3),
                    Arguments.of("6000", 0),
                    Arguments.of("99", 0)
            );
        }
    }

    /**
     * public List<TransactionLine> findAllByPlayer(String player)
     */
    @Nested
    @DisplayName("FindAllByPlayer")
    public class FindAllByPlayer {

        @DisplayName("can find all lines of player")
        @ParameterizedTest
        @MethodSource("findAllByPlayer_test1_generator")
        public void findAllByPlayer_test1(String playerName, int expectedCount) {
            int count = svc.findAllByPlayer(playerName).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllByPlayer_test1_generator() {
            return Stream.of(
                    Arguments.of("Chase Bank", 2),
                    Arguments.of("Bank of America", 2),
                    Arguments.of("US Bank", 0),
                    Arguments.of("Vanguard", 0),
                    Arguments.of("McDonald's", 0),
                    Arguments.of("Walmart", 18),
                    Arguments.of("Target", 0),
                    Arguments.of("Costco", 0),
                    Arguments.of("Amazon", 4),
                    Arguments.of("99", 0)
            );
        }
    }

    /**
     * public List<TransactionLine> findAllByMemo(String memoPattern)
     */
    @Nested
    @DisplayName("FindAllByMemo")
    public class FindAllByMemo {

        @DisplayName("can find all with memo like pattern")
        @ParameterizedTest
        @MethodSource("findAllByMemo_test1_generator")
        public void findAllByMemo_test1(String memoPattern, int expectedCount) {
            int count = svc.findAllByMemo(memoPattern).size();
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

    @Nested
    @DisplayName("FindAllParams")
    public class FindAllParams {

        @DisplayName("can find all by transaction id")
        @ParameterizedTest
        @MethodSource("findAllParams_test1_generator")
        public void findAllParams_test1(int transactionId, int expectedCount) {
            int count = svc.findAll(transactionId, null, null, null, null).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllParams_test1_generator() {
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

        @DisplayName("can find all by account number")
        @ParameterizedTest
        @MethodSource("findAllParams_test2_generator")
        public void findAllParams_test2(String accountNumber, int expectedCount) {
            int count = svc.findAll(null, accountNumber, null, null, null).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllParams_test2_generator() {
            return Stream.of(
                    Arguments.of("1000", 9),
                    Arguments.of("1001", 2),
                    Arguments.of("2000", 0),
                    Arguments.of("3000", 0),
                    Arguments.of("4000", 1),
                    Arguments.of("4001", 1),
                    Arguments.of("5000", 10),
                    Arguments.of("5001", 3),
                    Arguments.of("6000", 0),
                    Arguments.of("99", 0)
            );
        }

        @DisplayName("can find all by player name")
        @ParameterizedTest
        @MethodSource("findAllParams_test3_generator")
        public void findAllParams_test3(String playerName, int expectedCount) {
            int count = svc.findAll(null, null, playerName, null, null).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllParams_test3_generator() {
            return Stream.of(
                    Arguments.of("Chase Bank", 2),
                    Arguments.of("Bank of America", 2),
                    Arguments.of("US Bank", 0),
                    Arguments.of("Vanguard", 0),
                    Arguments.of("McDonald's", 0),
                    Arguments.of("Walmart", 18),
                    Arguments.of("Target", 0),
                    Arguments.of("Costco", 0),
                    Arguments.of("Amazon", 4),
                    Arguments.of("99", 0)
            );
        }

        @DisplayName("can find all by memo pattern")
        @ParameterizedTest
        @MethodSource("findAllParams_test4_generator")
        public void findAllParams_test4(String memoPattern, int expectedCount) {
            int count = svc.findAll(null, null, null, memoPattern, null).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllParams_test4_generator() {
            return Stream.of(
                    Arguments.of("memo%", 12),
                    Arguments.of("%mo", 20),
                    Arguments.of("%memo%", 22),
                    Arguments.of("1%o", 2),
                    Arguments.of("_-%", 4)
            );
        }

        @DisplayName("can find all by reconciled status")
        @ParameterizedTest
        @MethodSource("findAllParams_test5_generator")
        public void findAllParams_test5(boolean isReconciled, int expectedCount) {
            int count = svc.findAll(null, null, null, null, isReconciled).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllParams_test5_generator() {
            return Stream.of(
                    Arguments.of(true, 6),
                    Arguments.of(false, 20)
            );
        }

        @DisplayName("can find all by all 5 parameters")
        @ParameterizedTest
        @MethodSource("findAllParams_test6_generator")
        public void findAllParams_test6(int transactionId, String accountNumber, String playerName,
                                        String memoPattern, boolean isReconciled, int expectedCount) {

            int count = svc.findAll(transactionId, accountNumber, playerName, memoPattern, isReconciled).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllParams_test6_generator() {
            return Stream.of(
                    Arguments.of(1, "5000", "Walmart", "%", true, 1),
                    Arguments.of(1, "5000", "Walmart", "%", false, 2)
            );
        }

        @DisplayName("passing all null parameters finds all")
        @Test
        public void findAllParams_test7() {
            int count = svc.findAll(null, null, null, null, null).size();
            assertEquals(count, initialState.size());
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
            boolean saved = svc.findAllByMemo(memo).size() == 1;
            svc.delete(line);
            assertTrue(saved);
        }

        @DisplayName("can update one line")
        @Test
        public void save_test3() {
            int tranId = 11;
            int lineId = 1;
            TransactionLine line = svc.findById(tranId, lineId);
            String originalMemo = line.getMemo();
            line.setMemo("-save-test-3-");
            svc.save(line);
            String newMemo = svc.findById(tranId, lineId).getMemo();
            line.setMemo(originalMemo);
            svc.save(line);
            assertNotEquals(newMemo, originalMemo);
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
            boolean deleted = svc.delete(line);
            assertTrue(deleted);
        }
    }
}
