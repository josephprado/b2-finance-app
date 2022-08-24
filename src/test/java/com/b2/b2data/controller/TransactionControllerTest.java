package com.b2.b2data.controller;

import com.b2.b2data.domain.Transaction;
import com.b2.b2data.dto.TransactionDTO;
import com.b2.b2data.dto.TransactionLineDTO;
import com.b2.b2data.service.TransactionLineService;
import com.b2.b2data.service.TransactionService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionControllerTest {

    @Autowired
    private TransactionController con;

    @Autowired
    private TransactionService svc;

    @Autowired
    private TransactionLineService lSvc;

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
    @DisplayName("GetAll")
    public class GetAll {

        @DisplayName("passing all null values gets all transactions")
        @Test
        public void getAll_test1() {
            int count = Objects.requireNonNull(con.getAll(null, null, null).getBody()).getData().size();
            assertEquals(initialState.size(), count);
        }

        @DisplayName("response from getAll is OK")
        @Test
        public void getAll_test2() {
            HttpStatus status = con.getAll(null, null, null).getStatusCode();
            assertEquals(HttpStatus.OK, status);
        }

        @DisplayName("can get all on or after from date")
        @ParameterizedTest
        @MethodSource("getAll_test3_generator")
        public void getAll_test3(LocalDate from, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(from, null, null).getBody()).getData().size();
            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test3_generator() {
            return Stream.of(
                    Arguments.of(LocalDate.of(2022,1,31), 12),
                    Arguments.of(LocalDate.of(2022,8,31), 6),
                    Arguments.of(LocalDate.of(2022,11,15), 2)
            );
        }

        @DisplayName("can get all on or before to date")
        @ParameterizedTest
        @MethodSource("getAll_test4_generator")
        public void getAll_test4(LocalDate to, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(null, to, null).getBody()).getData().size();
            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test4_generator() {
            return Stream.of(
                    Arguments.of(LocalDate.of(2022,1,31), 1),
                    Arguments.of(LocalDate.of(2022,8,31), 8),
                    Arguments.of(LocalDate.of(2022,11,15), 10)
            );
        }

        @DisplayName("can get all between from and to dates")
        @ParameterizedTest
        @MethodSource("getAll_test5_generator")
        public void getAll_test5(LocalDate from, LocalDate to, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(from, to, null).getBody()).getData().size();
            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test5_generator() {
            return Stream.of(
                    Arguments.of(LocalDate.of(2022,1,31), LocalDate.of(2022,1,31), 1),
                    Arguments.of(LocalDate.of(2022,3,15), LocalDate.of(2022,8,31), 6),
                    Arguments.of(LocalDate.of(2022,8,31), LocalDate.of(2022,11,15), 4)
            );
        }

        @DisplayName("can get all by memo pattern")
        @ParameterizedTest
        @MethodSource("getAll_test6_generator")
        public void getAll_test6(String memoPattern, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(null, null, memoPattern).getBody()).getData().size();
            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test6_generator() {
            return Stream.of(
                    Arguments.of("memo%", 4),
                    Arguments.of("%mo", 5),
                    Arguments.of("%memo%", 10),
                    Arguments.of("8%o", 2),
                    Arguments.of("_-%", 1)
            );
        }

        @DisplayName("can get all by all 3 parameters")
        @ParameterizedTest
        @MethodSource("getAll_test7_generator")
        public void getAll_test7(LocalDate from, LocalDate to, String memoPattern, int expectedCount) {
            int count = Objects.requireNonNull(con.getAll(from, to, memoPattern).getBody()).getData().size();
            assertEquals(expectedCount, count);
        }

        private static Stream<Arguments> getAll_test7_generator() {
            return Stream.of(
                    Arguments.of(LocalDate.of(2022,1,31), LocalDate.of(2022,6,30), "memo%", 4),
                    Arguments.of(LocalDate.of(2022,8,31), LocalDate.of(2022,8,31), "%mo", 2),
                    Arguments.of(LocalDate.of(2022,8,31), LocalDate.of(2022,12,31), "%memo%", 4),
                    Arguments.of(LocalDate.of(2021,12,31), LocalDate.of(2022,12,31), "8%o", 2),
                    Arguments.of(LocalDate.of(2021,12,31), LocalDate.of(2022,12,31), "_-%", 1)
            );
        }
    }

    @Nested
    @DisplayName("GetById")
    public class GetById {

        @DisplayName("can get transaction by id")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12})
        public void getById_test1(int transactionId) {
            TransactionDTO dto = Objects.requireNonNull(con.getById(transactionId).getBody()).getData().get(0);
            assertNotNull(dto);
        }

        @DisplayName("search non-existent id returns null data")
        @Test
        public void getById_test2() {
            List<TransactionDTO> data = Objects.requireNonNull(con.getById(1234567890).getBody()).getData();
            assertNull(data);
        }

        @DisplayName("response from successful get by id is OK")
        @Test
        public void getById_test3() {
            HttpStatus status = con.getById(12).getStatusCode();
            assertEquals(HttpStatus.OK, status);
        }

        @DisplayName("response from unsuccessful get by id is NOT FOUND")
        @Test
        public void getById_tes4() {
            HttpStatus status = con.getById(1234567890).getStatusCode();
            assertEquals(HttpStatus.NOT_FOUND, status);
        }
    }

    @Nested
    @DisplayName("CreateOne")
    public class CreateOne {

        @DisplayName("can create one transaction")
        @Test
        public void createOne_test1() {
            TransactionDTO dto = new TransactionDTO();
            dto.setDate(LocalDate.now());
            dto.setMemo("-createOne-test1-");

            TransactionLineDTO l1 = new TransactionLineDTO();
            l1.setAccountNumber("99");
            l1.setAmount(100.0);

            TransactionLineDTO l2 = new TransactionLineDTO();
            l2.setAccountNumber("99");
            l2.setAmount(-100.0);

            dto.setLines(List.of(l1, l2));

            dto = Objects.requireNonNull(con.createOne(dto).getBody()).getData().get(0);
            Transaction transaction = svc.findById(dto.getId());

            assert con.deleteOne(dto.getId()).getStatusCode().equals(HttpStatus.NO_CONTENT);
            assertNotNull(transaction);
        }

        @DisplayName("response from successful creation is CREATED")
        @Test
        public void createOne_test2() {
            TransactionDTO dto = new TransactionDTO();
            dto.setDate(LocalDate.now());
            dto.setMemo("-createOne-test2-");

            TransactionLineDTO l1 = new TransactionLineDTO();
            l1.setAccountNumber("99");
            l1.setAmount(100.0);

            TransactionLineDTO l2 = new TransactionLineDTO();
            l2.setAccountNumber("99");
            l2.setAmount(-100.0);

            dto.setLines(List.of(l1, l2));

            var responseEntity = con.createOne(dto);
            int id = Objects.requireNonNull(responseEntity.getBody()).getData().get(0).getId();
            HttpStatus status = responseEntity.getStatusCode();

            assert con.deleteOne(id).getStatusCode().equals(HttpStatus.NO_CONTENT);
            assertEquals(HttpStatus.CREATED, status);
        }

        @DisplayName("location header URI contains new transaction id")
        @Test
        public void createOne_test3() {
            TransactionDTO dto = new TransactionDTO();
            dto.setDate(LocalDate.now());
            dto.setMemo("-createOne-test3-");

            TransactionLineDTO l1 = new TransactionLineDTO();
            l1.setAccountNumber("99");
            l1.setAmount(100.0);

            TransactionLineDTO l2 = new TransactionLineDTO();
            l2.setAccountNumber("99");
            l2.setAmount(-100.0);

            dto.setLines(List.of(l1, l2));

            var responseEntity = con.createOne(dto);
            int id = Objects.requireNonNull(responseEntity.getBody()).getData().get(0).getId();
            String location = Objects.requireNonNull(responseEntity.getHeaders().getLocation()).toString();
            String expectedLocation = ServletUriComponentsBuilder.fromCurrentRequest().toUriString()+"/"+id;

            assert con.deleteOne(id).getStatusCode().equals(HttpStatus.NO_CONTENT);
            assertEquals(expectedLocation, location);
        }

        @DisplayName("response from bad creation (non-zero sum) is BAD REQUEST")
        @Test
        public void createOne_test4() {
            TransactionDTO dto = new TransactionDTO();
            dto.setDate(LocalDate.now());
            dto.setMemo("-createOne-test4-");

            TransactionLineDTO l1 = new TransactionLineDTO();
            l1.setAccountNumber("99");
            l1.setAmount(100.0);

            TransactionLineDTO l2 = new TransactionLineDTO();
            l2.setAccountNumber("99");
            l2.setAmount(100.0);

            dto.setLines(List.of(l1, l2));

            var responseEntity = con.createOne(dto);
            HttpStatus status = responseEntity.getStatusCode();

            assertEquals(HttpStatus.BAD_REQUEST, status);
        }

        @DisplayName("response from bad creation (< 2 lines) is BAD REQUEST")
        @Test
        public void createOne_test5() {
            TransactionDTO dto = new TransactionDTO();
            dto.setDate(LocalDate.now());
            dto.setMemo("-createOne-test5-");

            TransactionLineDTO l1 = new TransactionLineDTO();
            l1.setAccountNumber("99");
            l1.setAmount(100.0);

            dto.setLines(List.of(l1));

            var responseEntity = con.createOne(dto);
            HttpStatus status = responseEntity.getStatusCode();

            assertEquals(HttpStatus.BAD_REQUEST, status);
        }

        @DisplayName("response from bad creation (invalid line) is BAD REQUEST")
        @Test
        public void createOne_test6() {
            TransactionDTO dto = new TransactionDTO();
            dto.setDate(LocalDate.now());
            dto.setMemo("-createOne-test6-a-");

            TransactionLineDTO l1 = new TransactionLineDTO();
            l1.setAccountNumber("99");
            l1.setAmount(100.0);

            TransactionLineDTO l2 = new TransactionLineDTO();
            l2.setAccountNumber("99");
            l2.setAmount(-100.0);
            l2.setPlayerName("-createOne-test6-b-");

            dto.setLines(List.of(l1, l2));

            var responseEntity = con.createOne(dto);
            HttpStatus status = responseEntity.getStatusCode();

            assertEquals(HttpStatus.BAD_REQUEST, status);
        }
    }

    @Nested
    @DisplayName("UpdateOne")
    public class UpdateOne {

        @DisplayName("can update one transaction")
        @Test
        @Transactional
        public void updateOne_test1() {
            int id = 11;
            Transaction transaction = svc.findById(id);
            String originalMemo = transaction.getMemo();
            String newMemo = "-updateOne-test1-";

            TransactionDTO dto = new TransactionDTO();
            dto.setId(id);
            dto.setDate(transaction.getDate());
            dto.setMemo(newMemo);
            dto.setLines(lSvc.findAllByTransactionId(id).stream().map(TransactionLineDTO::new).toList());

            dto = Objects.requireNonNull(con.updateOne(id, dto).getBody()).getData().get(0);
            String memo = svc.findById(id).getMemo();

            dto.setMemo(originalMemo);
            assert con.updateOne(id, dto).getStatusCode().equals(HttpStatus.OK);
            assertEquals(newMemo, memo);
        }

        @DisplayName("response from successful update is OK")
        @Test
        @Transactional
        public void updateOne_test2() {
            int id = 11;
            Transaction transaction = svc.findById(id);
            String originalMemo = transaction.getMemo();
            String newMemo = "-updateOne-test2-";

            TransactionDTO dto = new TransactionDTO();
            dto.setId(id);
            dto.setDate(transaction.getDate());
            dto.setMemo(newMemo);
            dto.setLines(lSvc.findAllByTransactionId(id).stream().map(TransactionLineDTO::new).toList());

            HttpStatus status = con.updateOne(id, dto).getStatusCode();

            dto.setMemo(originalMemo);
            assert con.updateOne(id, dto).getStatusCode().equals(HttpStatus.OK);
            assertEquals(HttpStatus.OK, status);
        }

        @DisplayName("response from non-existent transaction update is NOT FOUND")
        @Test
        public void updateOne_test3() {
            int id = 1234567890;
            TransactionDTO dto = new TransactionDTO();
            dto.setId(id);
            dto.setDate(LocalDate.now());
            dto.setMemo("-updateOne-test3-");
            dto.setLines(lSvc.findAllByTransactionId(id).stream().map(TransactionLineDTO::new).toList());

            HttpStatus status = con.updateOne(id, dto).getStatusCode();
            assertEquals(HttpStatus.NOT_FOUND, status);
        }

        @DisplayName("location header URI contains new transaction id")
        @Test
        public void updateOne_test4() {
            int id = 11;
            Transaction transaction = svc.findById(id);
            String originalMemo = transaction.getMemo();
            String newMemo = "-updateOne-test4-";

            TransactionDTO dto = new TransactionDTO();
            dto.setId(id);
            dto.setDate(transaction.getDate());
            dto.setMemo(newMemo);
            dto.setLines(lSvc.findAllByTransactionId(id).stream().map(TransactionLineDTO::new).toList());

            String location = Objects.requireNonNull(con.updateOne(id, dto).getHeaders().getLocation()).toString();
            String expectedLocation = ServletUriComponentsBuilder.fromCurrentRequest().toUriString()+"/"+id;

            dto.setMemo(originalMemo);
            assert con.updateOne(id, dto).getStatusCode().equals(HttpStatus.OK);
            assertEquals(expectedLocation, location);
        }

        @DisplayName("response from bad update (non-zero sum) is BAD REQUEST")
        @Test
        public void updateOne_test5() {
            int id = 11;
            TransactionDTO dto = new TransactionDTO(svc.findById(id));

            TransactionLineDTO l1 = new TransactionLineDTO();
            l1.setAccountNumber("99");
            l1.setAmount(100.0);

            TransactionLineDTO l2 = new TransactionLineDTO();
            l2.setAccountNumber("99");
            l2.setAmount(100.0);

            dto.setLines(List.of(l1, l2));

            var responseEntity = con.updateOne(id, dto);
            HttpStatus status = responseEntity.getStatusCode();

            assertEquals(HttpStatus.BAD_REQUEST, status);
        }

        @DisplayName("response from bad update (< 2 lines) is BAD REQUEST")
        @Test
        public void updateOne_test6() {
            int id = 11;
            TransactionDTO dto = new TransactionDTO(svc.findById(id));

            TransactionLineDTO l1 = new TransactionLineDTO();
            l1.setAccountNumber("99");
            l1.setAmount(100.0);

            dto.setLines(List.of(l1));

            var responseEntity = con.updateOne(id, dto);
            HttpStatus status = responseEntity.getStatusCode();

            assertEquals(HttpStatus.BAD_REQUEST, status);
        }

        @DisplayName("response from bad update (invalid line) is BAD REQUEST")
        @Test
        public void updateOne_test7() {
            int id = 11;
            TransactionDTO dto = new TransactionDTO(svc.findById(id));

            TransactionLineDTO l1 = new TransactionLineDTO();
            l1.setAccountNumber("99");
            l1.setAmount(100.0);

            TransactionLineDTO l2 = new TransactionLineDTO();
            l2.setAccountNumber("99");
            l2.setAmount(-100.0);
            l2.setPlayerName("-updateOne-test7-");

            dto.setLines(List.of(l1, l2));

            var responseEntity = con.updateOne(id, dto);
            HttpStatus status = responseEntity.getStatusCode();

            assertEquals(HttpStatus.BAD_REQUEST, status);
        }
    }

    @Nested
    @DisplayName("DeleteOne")
    public class DeleteOne {

        @DisplayName("can delete one")
        @Test
        public void deleteOne_test1() {
            TransactionDTO dto = new TransactionDTO();
            dto.setDate(LocalDate.now());
            dto.setMemo("-deleteOne-test1-");
            dto.setLines(lSvc.findAllByTransactionId(11).stream().map(TransactionLineDTO::new).toList());

            int id = Objects.requireNonNull(con.createOne(dto).getBody()).getData().get(0).getId();
            assert svc.findById(id) != null;

            con.deleteOne(id);
            Transaction transaction = svc.findById(id);
            assertNull(transaction);
        }

        @DisplayName("response from successful delete is NO CONTENT")
        @Test
        public void deleteOne_test2() {
            TransactionDTO dto = new TransactionDTO();
            dto.setDate(LocalDate.now());
            dto.setMemo("-deleteOne-test2-");
            dto.setLines(lSvc.findAllByTransactionId(11).stream().map(TransactionLineDTO::new).toList());

            int id = Objects.requireNonNull(con.createOne(dto).getBody()).getData().get(0).getId();
            assert svc.findById(id) != null;

            HttpStatus status = con.deleteOne(id).getStatusCode();
            assertEquals(HttpStatus.NO_CONTENT, status);
        }

        @DisplayName("response from non-existent transaction delete is NOT FOUND")
        @Test
        public void deleteOne_test3() {
            HttpStatus status = con.deleteOne(1234567890).getStatusCode();
            assertEquals(HttpStatus.NOT_FOUND, status);
        }
    }
}
