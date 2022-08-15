package com.b2.b2data.service;

import com.b2.b2data.domain.Transaction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionServiceTest {

    @Autowired
    private TransactionService svc;

    private List<Transaction> initialState;

    @BeforeAll
    private void setup() {
        initialState = svc.findAll();
        assert initialState.size() == 12;
    }

    @BeforeEach
    private void verifyDataReset() {
        assert svc.findAll().equals(initialState);
    }

    @Nested
    @DisplayName("FindById")
    public class FindById {

        @DisplayName("can find one by id")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12})
        public void findById_test1(int id) {
            Transaction transaction = svc.findById(id);
            assertNotNull(transaction);
        }

        @DisplayName("search for non-existent id returns null")
        @Test
        public void findById_test2() {
            Transaction transaction = svc.findById(-1);
            assertNull(transaction);
        }
    }

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

    @Nested
    @DisplayName("FindAllParams")
    public class FindAllParams {

        @DisplayName("can find all on or after from date")
        @ParameterizedTest
        @MethodSource("findAllParams_test1_generator")
        public void findAllParams_test1(int[] from, int expectedCount) {
            int count = svc.findAll(LocalDate.of(from[0], from[1], from[2]), null, null).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllParams_test1_generator() {
            return Stream.of(
                    Arguments.of(new int[]{2022,1,31}, 12),
                    Arguments.of(new int[]{2022,8,31}, 6),
                    Arguments.of(new int[]{2021,12,31}, 12)
            );
        }

        @DisplayName("can find all on or before to date")
        @ParameterizedTest
        @MethodSource("findAllParams_test2_generator")
        public void findAllParams_test2(int[] to, int expectedCount) {
            int count = svc.findAll(null, LocalDate.of(to[0], to[1], to[2]), null).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllParams_test2_generator() {
            return Stream.of(
                    Arguments.of(new int[]{2022,1,31}, 1),
                    Arguments.of(new int[]{2022,8,31}, 8),
                    Arguments.of(new int[]{2021,12,31}, 0)
            );
        }

        @DisplayName("can find all between from and to dates")
        @ParameterizedTest
        @MethodSource("findAllParams_test3_generator")
        public void findAllParams_test3(int[] from, int[] to, int expectedCount) {
            int count = svc.findAll(
                    LocalDate.of(from[0], from[1], from[2]),
                    LocalDate.of(to[0], to[1], to[2]),
                    null
            ).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllParams_test3_generator() {
            return Stream.of(
                    Arguments.of(new int[]{2022,1,31}, new int[]{2022,6,30}, 6),
                    Arguments.of(new int[]{2022,8,31}, new int[]{2022,8,31}, 2),
                    Arguments.of(new int[]{2022,8,31}, new int[]{2022,11,15}, 4),
                    Arguments.of(new int[]{2021,12,31}, new int[]{2022,1,30}, 0)
            );
        }

        @DisplayName("can find all by memo pattern")
        @ParameterizedTest
        @MethodSource("findAllParams_test4_generator")
        public void findAllParams_test4(String memo, int expectedCount) {
            int count = svc.findAll(null, null, memo).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllParams_test4_generator() {
            return Stream.of(
                    Arguments.of("memo%", 4),
                    Arguments.of("%mo", 5),
                    Arguments.of("%memo%", 10),
                    Arguments.of("8%o", 2),
                    Arguments.of("_-%", 1)
            );
        }

        @DisplayName("can find all by all 3 params")
        @ParameterizedTest
        @MethodSource("findAllParams_test5_generator")
        public void findAllParams_test5(int[] from, int[] to, String memo, int expectedCount) {
            int count = svc.findAll(
                    LocalDate.of(from[0], from[1], from[2]),
                    LocalDate.of(to[0], to[1], to[2]),
                    memo
            ).size();
            assertEquals(count, expectedCount);
        }

        private static Stream<Arguments> findAllParams_test5_generator() {
            return Stream.of(
                    Arguments.of(new int[]{2022,1,31}, new int[]{2022,6,30}, "memo%", 4),
                    Arguments.of(new int[]{2022,8,31}, new int[]{2022,8,31}, "%mo", 2),
                    Arguments.of(new int[]{2022,8,31}, new int[]{2022,12,31}, "%memo%", 4),
                    Arguments.of(new int[]{2021,12,31}, new int[]{2022,12,31}, "8%o", 2),
                    Arguments.of(new int[]{2021,12,31}, new int[]{2022,12,31}, "_-%", 1)
            );
        }
    }

    @Nested
    @DisplayName("Save")
    public class Save {

        @DisplayName("transaction is assigned id when saved")
        @Test
        public void save_test1() {
            Transaction transaction = svc.save(new Transaction(LocalDate.now(), "-save-test-1-"));
            svc.delete(transaction);
            assertNotNull(transaction.getId());
        }

        @DisplayName("saved transaction is persisted in the database")
        @Test
        public void save_test2() {
            String memo = "-save-test-2-";
            Transaction transaction = svc.save(new Transaction(LocalDate.now(), memo));
            boolean saved = svc.findAll(null, null, memo).size() == 1;
            svc.delete(transaction);
            assertTrue(saved);
        }

        @DisplayName("can update one transaction")
        @Test
        public void save_test3() {
            int id = 10;
            Transaction transaction = svc.findById(id);
            String originalMemo = transaction.getMemo();
            transaction.setMemo("-save-test-3-");
            svc.save(transaction);
            String newMemo = svc.findById(id).getMemo();
            transaction.setMemo(originalMemo);
            svc.save(transaction);
            assertNotEquals(newMemo, originalMemo);
        }
    }

    @Nested
    @DisplayName("Delete")
    public class Delete {

        @DisplayName("can delete one transaction")
        @Test
        public void delete_test1() {
            String memo = "-delete-test-1-";
            Transaction transaction = svc.save(new Transaction(LocalDate.now(), memo));
            assert svc.findAll(null, null, memo).size() == 1;
            svc.delete(transaction);
            boolean deleted = svc.findById(transaction.getId()) == null;
            assertTrue(deleted);
        }
    }
}
