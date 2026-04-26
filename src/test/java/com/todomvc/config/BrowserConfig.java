package com.todomvc.config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Centralised Selenide + Allure configuration.
 *
 * Parallel-safe design:
 * - Global Selenide config (browser, timeout, etc.) is set once — it is stateless and thread-safe.
 * - AllureSelenide listener is registered per-thread via addListener(),
 *   because SelenideLogger uses a ThreadLocal listener map internally.
 *   Each thread (parallel test class) gets its own listener instance.
 */
public final class BrowserConfig {

    private static final Logger log = LoggerFactory.getLogger(BrowserConfig.class);

    private static final String LISTENER_NAME = "allure";

    public static final String BASE_URL =
            System.getProperty("app.url", "https://todomvc.com/examples/react/dist/");

    private BrowserConfig() {}

    /**
     * Call once per JVM — sets global Selenide configuration.
     * Safe to call from multiple threads (idempotent).
     */
    public static void initGlobal() {
        boolean isHeadless = false;
        log.info("=== Global Selenide configuration ===");
        log.info("Target URL : {}", BASE_URL);
        log.info("Browser    : {}", System.getProperty("browser", "chrome"));
        log.info("Headless   : {}", System.getProperty("headless", Boolean.toString(isHeadless)));

        Configuration.browser     = System.getProperty("browser", "chrome");
        Configuration.headless    = Boolean.parseBoolean(System.getProperty("headless", Boolean.toString(isHeadless)));
        Configuration.timeout     = 8_000;
        Configuration.baseUrl     = BASE_URL;
        Configuration.browserSize = "1440x900";
        Configuration.screenshots = true;
        Configuration.savePageSource = false;

        // Each parallel thread gets its own WebDriver automatically via Selenide's ThreadLocal
        Configuration.reopenBrowserOnFail = true;

        if ("chrome".equalsIgnoreCase(Configuration.browser)) {
            ChromeOptions opts = new ChromeOptions();
            opts.addArguments(
                    "--disable-blink-features=AutomationControlled",
                    "--no-sandbox",
                    "--disable-dev-shm-usage",
                    "--disable-gpu",
                    "--remote-allow-origins=*"
            );
            opts.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
            Configuration.browserCapabilities = opts;
            log.debug("Chrome options configured");
        }
    }

    /**
     * Register Allure listener for the current thread.
     * Must be called in @BeforeAll of each test class (once per parallel thread).
     */
    public static void registerAllureListener() {
        if (!SelenideLogger.hasListener(LISTENER_NAME)) {
            SelenideLogger.addListener(LISTENER_NAME, new AllureSelenide()
                    .screenshots(true)
                    .savePageSource(false)
                    .includeSelenideSteps(true));
            log.debug("[Thread-{}] Allure listener registered", Thread.currentThread().getName());
        }
    }

    /**
     * Remove Allure listener for the current thread.
     * Must be called in @AfterAll of each test class.
     */
    public static void removeAllureListener() {
        SelenideLogger.removeListener(LISTENER_NAME);
        log.debug("[Thread-{}] Allure listener removed", Thread.currentThread().getName());
    }
}
