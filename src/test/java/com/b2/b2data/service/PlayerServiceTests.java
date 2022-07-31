package com.b2.b2data.service;

import com.b2.b2data.model.Player;
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
// resets the database after each test
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PlayerServiceTests {

    @Autowired
    private PlayerService svc;
    private int recordCount;
    private List<Player> initialState;

    @BeforeAll
    void setup() {
        initialState = svc.findAll();
        recordCount = initialState.size();
        assert initialState.size() == 9;
    }

    @BeforeEach
    private void verifyDataReset() {
        assert svc.findAll().equals(initialState);
    }

    /********************************************************************************
     *                    public Player findById(long id)
     ********************************************************************************/

    @Tag("findById")
    @DisplayName("can find player by id")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    public void findById_test1(long id) {
        Player player = svc.findById(id);
        assertNotNull(player);
    }

    @Tag("findById")
    @DisplayName("returns null if player id does not exist")
    @Test
    public void findById_test2() {
        Player player = svc.findById(1234567890L);
        assertNull(player);
    }

    /********************************************************************************
     *                   public Player findByName(String name)
     ********************************************************************************/

    @Tag("findByName")
    @DisplayName("can find player by name")
    @ParameterizedTest
    @ValueSource(strings = {
            "McDonald's", "Target", "Costco", "Chase Bank", "US Bank",
            "Bank of America", "Google", "King County",
            " `1234567890-=~!@#$%^&*()_+[]\\{}|;':\",./<>?`"
    })
    public void findByName_test1(String name) {
        Player player = svc.findByName(name);
        assertNotNull(player);
    }

    @Tag("findByName")
    @DisplayName("returns null if player name does not exist")
    @Test
    public void findByName_test2() {
        Player player = svc.findByName("-find-by-name-test-2-");
        assertNull(player);
    }

    @Tag("findByName")
    @DisplayName("find by null name does not throw exception")
    @Test
    public void findByName_test3() {
        assertDoesNotThrow(() -> svc.findByName(null));
    }

    /********************************************************************************
     *                        public List<Player> findAll()
     ********************************************************************************/

    @Tag("findAll")
    @DisplayName("can find all players")
    @Test
    public void findAll_test1() {
        List<Player> players = svc.findAll();
        assertEquals(players, initialState);
    }

    /********************************************************************************
     *                     public Player save(Player player)
     ********************************************************************************/

    @Tag("save")
    @DisplayName("can save one player")
    @Test
    public void save_test1() {
        Player player = new Player("-save-test-1-", true);

        svc.save(player);

        int count = svc.findAll().size();
        svc.delete(player);
        assertEquals(count, recordCount+1);
    }

    @Tag("save")
    @DisplayName("save returns the player saved")
    @Test
    @Transactional // ensures method uses a single transaction, so that lazy loaded fields are fetched
    public void save_test2() {
        Player input = new Player("-save-test-2-", true);

        Player output = svc.save(input);

        svc.delete(input);
        assertEquals(output, input);
    }

    @Tag("save")
    @DisplayName("can update one player")
    @Test
    public void save_test3() {
        long id = 9;
        Player original = svc.findById(id);
        String originalName = original.getName();
        original.setName("-save-test-3-");

        svc.save(original);

        String updatedName = svc.findById(id).getName();
        original.setName(originalName);
        svc.save(original);
        assertNotEquals(updatedName, originalName);
    }

    @Tag("save")
    @DisplayName("duplicate entry does not save")
    @Test
    public void save_test4() {
        Player player = new Player("-save-test-4-", false);

        svc.save(player);
        svc.save(player);

        int count = svc.findAll().size();
        svc.delete(player);
        assertEquals(count, recordCount+1);
    }

    @Tag("save")
    @DisplayName("saving null player throws exception")
    @Test
    public void save_test5() {
        assertThrows(Exception.class, () -> svc.save(null));
    }

    @Tag("save")
    @DisplayName("saved player gets assigned an id")
    @Test
    public void save_test6() {
        Player player = new Player("-save-test-6-", true);
        assert player.getId() == null;

        Player saved = svc.save(player);

        svc.delete(player);
        assertNotNull(saved.getId());
    }

    /********************************************************************************
     *                      public void delete(Player player)
     ********************************************************************************/

    @Tag("delete")
    @DisplayName("can delete one player")
    @Test
    public void delete_test1() {
        Player player = new Player("delete-test-1", true);
        Player saved = svc.save(player);
        long id = saved.getId();
        assert svc.findAll().size() == recordCount+1;

        svc.delete(saved);

        Player result = svc.findById(id);
        assertNull(result);
    }

    @Tag("delete")
    @DisplayName("delete non-existing player does nothing")
    @Test
    public void delete_test2() {
        svc.delete(new Player("-delete-test-2", false));

        int count = svc.findAll().size();
        assertEquals(count, recordCount);
    }

    @Tag("delete")
    @DisplayName("deleting null player throws exception")
    @Test
    public void delete_test3() {
        assertThrows(Exception.class, () -> svc.delete(null));
    }
}
