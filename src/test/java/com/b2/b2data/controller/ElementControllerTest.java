package com.b2.b2data.controller;

import com.b2.b2data.domain.Element;
import com.b2.b2data.dto.ElementDTO;
import com.b2.b2data.service.ElementService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ElementControllerTest {

    @Autowired
    private ElementController con;

    @Autowired
    private ElementService svc;

    private List<Element> initialState;

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

        @DisplayName("passing all null values gets all elements")
        @Test
        public void getAll_test1() {
            int count = Objects.requireNonNull(con.getAll().getBody()).getData().size();
            assertEquals(initialState.size(), count);
        }

        @DisplayName("response from getAll is OK")
        @Test
        public void getAll_test2() {
            HttpStatus status = con.getAll().getStatusCode();
            assertEquals(HttpStatus.OK, status);
        }
    }

    @Nested
    @DisplayName("GetByNumber")
    public class GetByNumber {

        @DisplayName("can get element by number")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6, 0, 99, 999, -1})
        public void getByNumber_test1(int elementNumber) {
            ElementDTO dto = Objects.requireNonNull(con.getByNumber(elementNumber).getBody()).getData().get(0);
            assertNotNull(dto);
        }

//        @DisplayName("search non-existent element returns null data")
//        @Test
//        public void getByNumber_test2() {
//            List<ElementDTO> data = Objects.requireNonNull(con.getByNumber(1234567890).getBody()).getData();
//            assertNull(data);
//        }

        @DisplayName("response from successful get by number is OK")
        @Test
        public void getByNumber_test3() {
            HttpStatus status = con.getByNumber(99).getStatusCode();
            assertEquals(HttpStatus.OK, status);
        }

//        @DisplayName("response from unsuccessful get by number is NOT FOUND")
//        @Test
//        public void getByNumber_tes4() {
//            HttpStatus status = con.getByNumber(1234567890).getStatusCode();
//            assertEquals(HttpStatus.NOT_FOUND, status);
//        }
    }

    @Nested
    @DisplayName("CreateOne")
    public class CreateOne {

        @DisplayName("can create one element")
        @Test
        public void createOne_test1() {
            int number = 1234567890;
            ElementDTO dto = new ElementDTO();
            dto.setNumber(number);
            dto.setName("-createOne-test1-");

            dto = Objects.requireNonNull(con.createOne(dto).getBody()).getData().get(0);
            Element element = svc.findByNumber(number);

            assert con.deleteOne(dto.getNumber()).getStatusCode().equals(HttpStatus.NO_CONTENT);
            assertNotNull(element);
        }

        @DisplayName("response from successful creation is CREATED")
        @Test
        public void createOne_test2() {
            int number = 1234567890;
            ElementDTO dto = new ElementDTO();
            dto.setNumber(number);
            dto.setName("-createOne-test2-");

            HttpStatus status = con.createOne(dto).getStatusCode();

            assert con.deleteOne(dto.getNumber()).getStatusCode().equals(HttpStatus.NO_CONTENT);
            assertEquals(HttpStatus.CREATED, status);
        }

        @DisplayName("location header URI contains new element number")
        @Test
        public void createOne_test3() {
            int number = 1234567890;
            ElementDTO dto = new ElementDTO();
            dto.setNumber(number);
            dto.setName("-createOne-test3-");

            String location = Objects.requireNonNull(con.createOne(dto).getHeaders().getLocation()).toString();
            String expectedLocation = ServletUriComponentsBuilder.fromCurrentRequest().toUriString()+"/"+number;

            assert con.deleteOne(dto.getNumber()).getStatusCode().equals(HttpStatus.NO_CONTENT);
            assertEquals(expectedLocation, location);
        }

//        @DisplayName("response from unsuccessful creation is BAD REQUEST")
//        @Test
//        public void createOne_test4() {
//            int number = 99; // duplicate number
//            ElementDTO dto = new ElementDTO();
//            dto.setNumber(number);
//            dto.setName("-createOne-test4-");
//
//            HttpStatus status = con.createOne(dto).getStatusCode();
//            assertEquals(HttpStatus.BAD_REQUEST, status);
//        }
    }

    @Nested
    @DisplayName("UpdateOne")
    public class UpdateOne {

        @DisplayName("can update one element")
        @Test
        @Transactional
        public void updateOne_test1() {
            int number = 99;
            Element element = svc.findByNumber(number);
            String originalName = element.getName();
            String newName = "-updateOne-test1-";

            ElementDTO dto = new ElementDTO();
            dto.setNumber(number);
            dto.setName(newName);

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
            int number = 99;
            Element element = svc.findByNumber(number);
            String originalName = element.getName();
            String newName = "-updateOne-test2-";

            ElementDTO dto = new ElementDTO();
            dto.setNumber(number);
            dto.setName(newName);

            HttpStatus status = con.updateOne(number, dto).getStatusCode();

            dto.setName(originalName);
            assert con.updateOne(number, dto).getStatusCode().equals(HttpStatus.OK);
            assertEquals(HttpStatus.OK, status);
        }

//        @DisplayName("response from non-existent element update is NOT FOUND")
//        @Test
//        public void updateOne_test3() {
//            int number = 1234567890;
//            ElementDTO dto = new ElementDTO();
//            dto.setNumber(number);
//            dto.setName("-updateOne-test3-");
//
//            HttpStatus status = con.updateOne(number, dto).getStatusCode();
//            assertEquals(HttpStatus.NOT_FOUND, status);
//        }

//        @DisplayName("response from bad update (duplicate number) is BAD REQUEST")
//        @Test
//        public void updateOne_test4() {
//            ElementDTO dto = new ElementDTO();
//            dto.setNumber(99); // duplicate element
//            dto.setName("-updateOne-test4-");
//
//            HttpStatus status = con.updateOne(0, dto).getStatusCode();
//            assertEquals(HttpStatus.BAD_REQUEST, status);
//        }

        @DisplayName("location header URI contains new account number")
        @Test
        @Transactional
        public void updateOne_test5() {
            int number = 99;
            Element element = svc.findByNumber(number);
            String originalName = element.getName();
            String newName = "-updateOne-test2-";

            ElementDTO dto = new ElementDTO();
            dto.setNumber(number);
            dto.setName(newName);

            String location = Objects.requireNonNull(con.updateOne(number, dto).getHeaders().getLocation()).toString();
            String expectedLocation = ServletUriComponentsBuilder.fromCurrentRequest().toUriString()+"/"+number;

            dto.setName(originalName);
            assert con.updateOne(number, dto).getStatusCode().equals(HttpStatus.OK);
            assertEquals(expectedLocation, location);
        }
    }

    @Nested
    @DisplayName("DeleteOne")
    public class DeleteOne {

        @DisplayName("can delete one")
        @Test
        public void deleteOne_test1() {
            int number = 1234567890;
            ElementDTO dto = new ElementDTO();
            dto.setNumber(number);
            dto.setName("-deleteOne-test1-");

            con.createOne(dto);
            assert svc.findByNumber(number) != null;

            con.deleteOne(number);
            assertThrows(NoSuchElementException.class, () -> svc.findByNumber(number));
        }

        @DisplayName("response from successful delete is NO CONTENT")
        @Test
        public void deleteOne_test2() {
            int number = 1234567890;
            ElementDTO dto = new ElementDTO();
            dto.setNumber(number);
            dto.setName("-deleteOne-test2-");

            con.createOne(dto);
            assert svc.findByNumber(number) != null;

            HttpStatus status = con.deleteOne(number).getStatusCode();
            assertEquals(HttpStatus.NO_CONTENT, status);
        }

//        @DisplayName("response from non-existent element delete is NOT FOUND")
//        @Test
//        public void deleteOne_test3() {
//            HttpStatus status = con.deleteOne(1234567890).getStatusCode();
//            assertEquals(HttpStatus.NOT_FOUND, status);
//        }

//        @DisplayName("response from bad element delete is BAD REQUEST")
//        @Test
//        public void deleteOne_test4() {
//            HttpStatus status = con.deleteOne(1).getStatusCode(); // cannot delete due to fk constraints
//            assertEquals(HttpStatus.BAD_REQUEST, status);
//        }
    }
}
