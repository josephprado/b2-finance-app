package com.b2.b2data.service;

import com.b2.b2data.domain.Element;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ElementServiceTest {

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
    @DisplayName("FindById")
    public class FindById {

        @DisplayName("can find one by id")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
        public void findById_test1(int id) {
            Element element = svc.findById(id);
            assertNotNull(element);
        }

        @DisplayName("search for non-existent id throws NoSuchElementException")
        @Test
        public void findById_test2() {
            assertThrows(NoSuchElementException.class, () -> svc.findById(-1));
        }
    }

    @Nested
    @DisplayName("FindByNumber")
    public class FindByNumber {

        @DisplayName("can find one by number")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6, 0, 99, 999, -1})
        public void findByNumber_test1(int number) {
            Element element = svc.findByNumber(number);
            assertNotNull(element);
        }

        @DisplayName("search for non-existent number throws NoSuchElementException")
        @Test
        public void findByNumber_test2() {
            assertThrows(NoSuchElementException.class, () -> svc.findByNumber(1234567890));
        }
    }

    @Nested
    @DisplayName("FindByName")
    public class FindByName {

        @DisplayName("can find one by name")
        @ParameterizedTest
        @ValueSource(strings = {
                "Asset",
                "Liability",
                "Equity",
                "Income",
                "Expense",
                "Accumulated Other Comprehensive Income",
                "ZERO",
                "NINETY-NINE",
                "NINE-NINETY-NINE",
                "NEGATIVE-ONE"
        })
        public void findByName_test1(String name) {
            Element element = svc.findByName(name);
            assertNotNull(element);
        }

        @DisplayName("search for non-existent name throws NoSuchElementException")
        @Test
        public void findByName_test2() {
            assertThrows(NoSuchElementException.class, () -> svc.findByName("-find-by-name-test-2-"));
        }

        @DisplayName("search for null name throws NoSuchElementException")
        @Test
        public void findByName_test3() {
            assertThrows(NoSuchElementException.class, () -> svc.findByName(null));
        }
    }

    @Nested
    @DisplayName("FindAll")
    public class FindAll {

        @DisplayName("can find all elements")
        @Test
        public void findAll_test1() {
            List<Element> elements = svc.findAll();
            assertEquals(initialState, elements);
        }
    }

    @Nested
    @DisplayName("Save")
    public class Save {

        @DisplayName("element is assigned id when saved")
        @Test
        public void save_test1() {
            Element element = svc.save(new Element(1234567890, "-save-test-1-"));
            svc.delete(element);
            assertNotNull(element.getId());
        }

        @DisplayName("saved element is persisted in the database")
        @Test
        public void save_test2() {
            int number = 1234567890;
            Element element = svc.save(new Element(number, "-save-test-2-"));
            boolean saved = svc.findByNumber(number) != null;
            svc.delete(element);
            assertTrue(saved);
        }

        @DisplayName("can update one element")
        @Test
        public void save_test3() {
            int number = 99;
            Element element = svc.findByNumber(number);
            String originalName = element.getName();
            element.setName("-save-test-3-");
            svc.save(element);
            String newName = svc.findByNumber(number).getName();
            element.setName(originalName);
            svc.save(element);
            assertNotEquals(originalName, newName);
        }
    }

    @Nested
    @DisplayName("Delete")
    public class Delete {

        @DisplayName("can delete one element")
        @Test
        public void delete_test1() {
            int number = 1234567890;
            Element element = svc.save(new Element(number, "-delete-test-1-"));
            assert svc.findByNumber(number) != null;
            svc.delete(element);
            assertThrows(NoSuchElementException.class, () -> svc.findByNumber(number));
        }
    }
}

