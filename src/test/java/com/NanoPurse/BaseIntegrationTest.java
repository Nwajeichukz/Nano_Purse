package com.NanoPurse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockserver.integration.ClientAndServer;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseIntegrationTest {
    public static ClientAndServer mockServer;

    @BeforeAll
    public static void startMockServer() {
        mockServer = ClientAndServer.startClientAndServer(1080);
    }

    @AfterAll
    public static void stopMockServer() {
        mockServer.stop();
    }

}
