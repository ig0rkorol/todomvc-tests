package com.todomvc.tests;

import com.codeborne.selenide.Selenide;
import com.todomvc.config.BrowserConfig;
import com.todomvc.pages.TodoPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all UI tests.
 *
 * Parallel strategy:
 * - @Execution(CONCURRENT) on the class level → test CLASSES run in parallel
 * - Methods within each class run sequentially (same_thread default)
 * - Each class gets its own thread → its own WebDriver (Selenide ThreadLocal)
 * - Allure listener is registered/removed per thread in @BeforeAll/@AfterAll
 */
@Execution(ExecutionMode.CONCURRENT)
public abstract class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    // Called once when the entire test suite JVM starts
    static {
        BrowserConfig.initGlobal();
    }

    protected TodoPage todoPage;

    @BeforeAll
    static void suiteSetUp() {
        // Register Allure listener for THIS thread (parallel-safe)
        BrowserConfig.registerAllureListener();
        log.info("[Thread-{}] Suite started: {}",
                Thread.currentThread().getName(),
                Thread.currentThread().getStackTrace()[2].getClassName());
    }

    @AfterAll
    static void suiteTearDown() {
        BrowserConfig.removeAllureListener();
        log.info("[Thread-{}] Suite finished", Thread.currentThread().getName());
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        log.info("[Thread-{}] ▶ START: {}",
                Thread.currentThread().getName(),
                testInfo.getDisplayName());
        todoPage = new TodoPage();
        todoPage.openApp();
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        log.info("[Thread-{}] ■ END  : {}",
                Thread.currentThread().getName(),
                testInfo.getDisplayName());
        Selenide.closeWebDriver();
    }
}
