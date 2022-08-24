package com.b2.b2data.service;

import com.b2.b2data.domain.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerServiceTest {

    @Autowired
    private PlayerService svc;

    private List<Player> initialState;

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
            Player player = svc.findById(id);
            assertNotNull(player);
        }

        @DisplayName("search for non-existent id returns null")
        @Test
        public void findById_test2() {
            Player player = svc.findById(-1);
            assertNull(player);
        }
    }

    @Nested
    @DisplayName("FindByName")
    public class FindByName {

        @DisplayName("can find one by name")
        @ParameterizedTest
        @ValueSource(strings = {
                "Chase Bank",
                "Bank of America",
                "US Bank",
                "Vanguard",
                "McDonald's",
                "Walmart",
                "Target",
                "Costco",
                "Amazon",
                "99"
        })
        public void findByName_test1(String name) {
            Player player = svc.findByName(name);
            assertNotNull(player);
        }

        @DisplayName("search for non-existent name returns null")
        @Test
        public void findByName_test2() {
            Player player = svc.findByName("-find-by-name-test-2-");
            assertNull(player);
        }

        @DisplayName("search for null name returns null")
        @Test
        public void findByName_test3() {
            Player player = svc.findByName(null);
            assertNull(player);
        }
    }

    @Nested
    @DisplayName("FindAll")
    public class FindAll {

        @DisplayName("can find all players")
        @Test
        public void findAll_test1() {
            List<Player> players = svc.findAll();
            assertEquals(initialState, players);
        }
    }

    @Nested
    @DisplayName("FindAllByBankStatus")
    public class FindAllByBankStatus {

        @DisplayName("can find all banks")
        @Test
        public void findAllByBankStatus_test1() {
            int count = svc.findAllByBankStatus(true).size();
            assertEquals(4, count);
        }

        @DisplayName("can find all non-banks")
        @Test
        public void findAllByBankStatus_test2() {
            int count = svc.findAllByBankStatus(false).size();
            assertEquals(6, count);
        }
    }

    @Nested
    @DisplayName("Save")
    public class Save {

        @DisplayName("player is assigned id when saved")
        @Test
        public void save_test1() {
            Player player = svc.save(new Player("-save-test-1-", true));
            svc.delete(player);
            assertNotNull(player.getId());
        }

        @DisplayName("saved player is persisted in the database")
        @Test
        public void save_test2() {
            String name = "-save-test-2-";
            Player player = svc.save(new Player(name, false));
            boolean saved = svc.findByName(name) != null;
            svc.delete(player);
            assertTrue(saved);
        }

        @DisplayName("can update one player")
        @Test
        public void save_test3() {
            int id = 10;
            Player player = svc.findById(id);
            String originalName = player.getName();
            player.setName("-save-test-3-");
            svc.save(player);
            String newName = svc.findById(id).getName();
            player.setName(originalName);
            svc.save(player);
            assertNotEquals(originalName, newName);
        }
    }

    @Nested
    @DisplayName("Delete")
    public class Delete {

        @DisplayName("can delete one player")
        @Test
        public void delete_test1() {
            String name = "-delete-test-1-";
            Player player = svc.save(new Player(name, true));
            assert svc.findByName(name) != null;
            svc.delete(player);
            boolean deleted = svc.findByName(name) == null;
            assertTrue(deleted);
        }
    }
}
