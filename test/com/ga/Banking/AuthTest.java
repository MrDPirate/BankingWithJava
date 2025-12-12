package com.ga.Banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AuthTest {

    private static final String ROLE_BANKER = "Banker";
    private static final String DEFAULT_NAME = "TestUser";
    private static final String DEFAULT_SAVING_CARD = "Mastercard Platinum";
    private static final String DEFAULT_CHECKING_CARD = "Mastercard";

    @TempDir
    Path tempDir;

    private Path dbDir;

    @BeforeEach
    void setUp() throws IOException {
        dbDir = tempDir.resolve("db");
        Files.createDirectories(dbDir);
        dbHelper.path = dbDir.toString() + File.separator;
    }

    @Test
    void register_createsHashedBankerRecord() throws IOException {
        String username = "banker1";
        String password = "secret";

        Auth.register(username, password, ROLE_BANKER, DEFAULT_NAME, "true", "true",
                "1000", "500", DEFAULT_SAVING_CARD, DEFAULT_CHECKING_CARD, "0");

        Path storedFile = dbDir.resolve(ROLE_BANKER + "-" + DEFAULT_NAME + "-" + username);
        assertTrue(Files.exists(storedFile), "Register should create a user file");

        String[] record = Files.readString(storedFile).split(",");
        assertEquals(username, record[0]);
        assertEquals(Auth.hashPass256(password), record[1]);
        assertEquals(ROLE_BANKER, record[2]);
        assertEquals("No", record[7]);
        assertEquals(DEFAULT_NAME, record[8]);
    }

    @Test
    void login_withValidCredentialsPersistsLoggedInState() {
        String username = "bankerLogin";
        String password = "abc123";
        Users user = registerAndLoad(username, password);

        boolean result = Auth.login(user, username, password);

        assertTrue(result);
        assertTrue(user.getIsLoggedIn());
        assertTrue(reload(username).getIsLoggedIn(), "Login should persist logged-in flag");
    }

    @Test
    void logout_updatesUserRecord() {
        String username = "bankerLogout";
        String password = "pass123";
        Users user = registerAndLoad(username, password);
        assertTrue(Auth.login(user, username, password));

        boolean result = Auth.logout(user);

        assertTrue(result);
        assertFalse(user.getIsLoggedIn());
        assertFalse(reload(username).getIsLoggedIn(), "Logout should persist logged-out flag");
    }

    private Users registerAndLoad(String username, String password) {
        Auth.register(username, password, ROLE_BANKER, DEFAULT_NAME, "true", "true",
                "1000", "500", DEFAULT_SAVING_CARD, DEFAULT_CHECKING_CARD, "0");
        return reload(username);
    }

    private Users reload(String username) {
        return new Users(dbHelper.getUserData(username));
    }
}