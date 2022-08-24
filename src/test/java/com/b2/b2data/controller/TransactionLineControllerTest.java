package com.b2.b2data.controller;

import com.b2.b2data.domain.TransactionLine;
import com.b2.b2data.service.TransactionLineService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionLineControllerTest {

    @Autowired
    private TransactionLineController con;

    @Autowired
    private TransactionLineService svc;

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

    @Nested
    @DisplayName("GetAll")
    public class GetAll {

        @DisplayName("passing all null values gets all transaction lines")
        @Test
        public void getAll_test1() {
            int count = Objects.requireNonNull(con.getAll(null, null, null, null, null, null, null).getBody())
                                .getData().size();

            assertEquals(initialState.size(), count);
        }

        @DisplayName("can get all transaction lines by transaction id")
        @ParameterizedTest
        @MethodSource("getAll_test2_generator")
        public void getAll_test2(int transactionId, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(transactionId, null, null, null, null, null, null).getBody())
                                .getData().size();

            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test2_generator() {
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

        @DisplayName("can get all transaction lines by account number")
        @ParameterizedTest
        @MethodSource("getAll_test3_generator")
        public void getAll_test3(String accountNumber, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(null, accountNumber, null, null, null, null, null).getBody())
                                .getData().size();

            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test3_generator() {
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

        @DisplayName("can get all transaction lines by player name")
        @ParameterizedTest
        @MethodSource("getAll_test4_generator")
        public void getAll_test4(String playerName, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(null, null, playerName, null, null, null, null).getBody())
                                .getData().size();

            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test4_generator() {
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

        @DisplayName("can get all transaction lines by memo pattern")
        @ParameterizedTest
        @MethodSource("getAll_test5_generator")
        public void getAll_test5(String memoPattern, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(null, null, null, memoPattern, null, null, null).getBody())
                                .getData().size();

            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test5_generator() {
            return Stream.of(
                    Arguments.of("memo%", 12),
                    Arguments.of("%mo", 20),
                    Arguments.of("%memo%", 22),
                    Arguments.of("1%o", 2),
                    Arguments.of("_-%", 4)
            );
        }

        @DisplayName("can get all transaction lines by reconciliation status")
        @ParameterizedTest
        @MethodSource("getAll_test6_generator")
        public void getAll_test6(boolean isReconciled, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(null, null, null, null, isReconciled, null, null).getBody())
                                .getData().size();

            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test6_generator() {
            return Stream.of(
                    Arguments.of(true, 6),
                    Arguments.of(false, 20)
            );
        }

        @DisplayName("can get all transaction lines on or after from date")
        @ParameterizedTest
        @MethodSource("getAll_test7_generator")
        public void getAll_test7(LocalDate from, int expectedCount) {
            int count = svc.findAll(null, null, null, null, null, from, null).size();
            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test7_generator() {
            return Stream.of(
                    Arguments.of(LocalDate.of(2022,1,31), 26),
                    Arguments.of(LocalDate.of(2022,8,31), 10),
                    Arguments.of(LocalDate.of(2022,11,15), 2)
            );
        }

        @DisplayName("can get all transaction lines on or before to date")
        @ParameterizedTest
        @MethodSource("getAll_test8_generator")
        public void getAll_test8(LocalDate to, int expectedCount) {
            int count = svc.findAll(null, null, null, null, null, null, to).size();
            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test8_generator() {
            return Stream.of(
                    Arguments.of(LocalDate.of(2022,1,31), 4),
                    Arguments.of(LocalDate.of(2022,8,31), 20),
                    Arguments.of(LocalDate.of(2022,11,15), 24)
            );
        }

        @DisplayName("can get all transaction lines by all 7 parameters")
        @ParameterizedTest
        @MethodSource("getAll_test9_generator")
        public void getAll_test9(int transactionId, String accountNumber, String playerName, String memoPattern,
                                 boolean isReconciled, LocalDate from, LocalDate to, int expectedCount) {

            int count = Objects.requireNonNull(
                    con.getAll(
                            transactionId,
                            accountNumber,
                            playerName,
                            memoPattern,
                            isReconciled,
                            from,
                            to
                    ).getBody())
                    .getData()
                    .size();

            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test9_generator() {
            return Stream.of(
                    Arguments.of(1, "5000", "Walmart", "%-memo", false, LocalDate.of(2022,1,31), LocalDate.of(2022,1,31), 2),
                    Arguments.of(1, "5000", "Walmart", "%-memo", true, LocalDate.of(2022,1,31), LocalDate.of(2022,1,31), 1),
                    Arguments.of(2, "5001", "Amazon", "%.memo", true, LocalDate.of(2022,2,28), LocalDate.of(2022,2,28), 1),
                    Arguments.of(2, "5001", "Amazon", "%.memo", false, LocalDate.of(2022,2,28), LocalDate.of(2022,2,28), 2)
            );
        }

        @DisplayName("response from getAll is OK")
        @Test
        public void getAll_test10() {
            HttpStatus status = con.getAll(null, null, null, null, null, null, null).getStatusCode();
            assertEquals(HttpStatus.OK, status);
        }
    }
}
