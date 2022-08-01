package com.b2.b2data.service;

import com.b2.b2data.model.Element;
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
class ElementServiceTests {

    @Autowired
    private ElementService svc;
    private List<Element> initialState;

    @BeforeAll
    void setup() {
        initialState = svc.findAll();
        assert initialState.size() == 10;
    }

    @BeforeEach
    private void verifyDataReset() {
        assert svc.findAll().equals(initialState);
    }

    /**
     * public Element findById(int id)
     */
    @Nested
    @DisplayName("FindById")
    public class FindById {

        @DisplayName("can find element by id")
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5, 6, 0, 99, 999, -1})
        public void findById_test1(int id) {
            Element element = svc.findById(id);
            assertNotNull(element);
        }

        @DisplayName("returns null if element id does not exist")
        @Test
        public void findById_test2() {
            Element element = svc.findById(-1234567890);
            assertNull(element);
        }
    }

    /**
     * public Element findByName(String name)
     */
    @Nested
    @DisplayName("FindByName")
    public class FindByName {

        @DisplayName("can find element by name")
        @ParameterizedTest
        @ValueSource(strings = {
                "Asset",
                "Liability",
                "Equity",
                "Income",
                "Expense",
                "Accumulated Other Comprehensive Income",
                "Statistic",
                "Test",
                " `1234567890-=~!@#$%^&*()_+[]\\{}|;':\",./<>?`",
                "Other"
        })
        public void findByName_test1(String name) {
            Element element = svc.findByName(name);
            assertNotNull(element);
        }

        @DisplayName("returns null if element name does not exist")
        @Test
        public void findByName_test2() {
            Element element = svc.findByName("-find-by-name-test-2-");
            assertNull(element);
        }

        @DisplayName("find by null name does not throw exception")
        @Test
        public void findByName_test3() {
            assertDoesNotThrow(() -> svc.findByName(null));
        }
    }

    /**
     * public List<Element> findAll()
     */
    @Nested
    @DisplayName("FindAll")
    public class FindAll {

        @DisplayName("can find all elements")
        @Test
        public void findAll_test1() {
            List<Element> elements = svc.findAll();
            assertEquals(elements, initialState);
        }
    }

    /**
     * public Element save(Element element)
     */
    @Nested
    @DisplayName("Save")
    public class Save {

        @DisplayName("can save one element")
        @Test
        @Transactional // ensures method uses a single transaction, so that lazy loaded fields are fetched
        public void save_test1() {
            Element element = new Element(1234567890, "-save-test-1-");

            svc.save(element);

            int count = svc.findAll().size();
            svc.delete(element);
            assertEquals(count, initialState.size()+1);
        }

        @DisplayName("save returns the element saved")
        @Test
        public void save_test2() {
            Element input = new Element(-1234567890, "-save-test-2-");

            Element output = svc.save(input);

            svc.delete(input);
            assertEquals(output, input);
        }

        @DisplayName("can update one element")
        @Test
        public void save_test3() {
            int id = 99;
            Element original = svc.findById(id);
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
            Element element = new Element(1234567890, "-save-test-4-");

            svc.save(element);
            svc.save(element);

            int count = svc.findAll().size();
            svc.delete(element);
            assertEquals(count, initialState.size()+1);
        }

        @DisplayName("saving null element throws exception")
        @Test
        public void save_test5() {
            assertThrows(Exception.class, () -> svc.save(null));
        }
    }

    /**
     * public void delete(Element element)
     */
    @Nested
    @DisplayName("Delete")
    public class Delete {

        @DisplayName("can delete one element")
        @Test
        public void delete_test1() {
            int id = 99;
            Element element = svc.findById(id);

            svc.delete(element);

            Element result = svc.findById(id);
            svc.save(element);
            assertNull(result);
        }

        @DisplayName("deleting non-existing element does nothing")
        @Test
        public void delete_test2() {
            svc.delete(new Element(-1234567890, "-delete-test-2"));

            int count = svc.findAll().size();
            assertEquals(count, initialState.size());
        }

        @DisplayName("deleting null element throws exception")
        @Test
        public void delete_test3() {
            assertThrows(Exception.class, () -> svc.delete(null));
        }
    }
}
