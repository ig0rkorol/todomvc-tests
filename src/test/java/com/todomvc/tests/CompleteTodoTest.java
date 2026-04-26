package com.todomvc.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;

@Epic("TodoMVC React")
@Feature("Complete Todo")
@DisplayName("Complete / Uncomplete Todo")
class CompleteTodoTest extends BaseTest {

    @BeforeEach
    void addInitialTodos() {
        todoPage.addTodos("Buy milk", "Walk the dog", "Read a book");
    }

    @Test
    @Story("Complete single")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Completing a todo marks it as completed")
    void completeSingleTodo() {
        todoPage
                .completeTodo(0)
                .assertCompleted(0)
                .assertActive(1)
                .assertItemsLeftCount(2);
    }

    @Test
    @Story("Complete single")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Completed todo can be unchecked back to active")
    void uncompleteTodo() {
        todoPage
                .completeTodo(0)
                .assertCompleted(0)
                .completeTodo(0)         // uncheck
                .assertActive(0)
                .assertItemsLeftCount(3);
    }

    @Test
    @Story("Toggle all")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Toggle-all marks all todos as completed")
    void toggleAllCompletesAll() {
        todoPage
                .toggleAll()
                .assertCompletedCount(3)
                .assertItemsLeftCount(0)
                .assertToggleAllChecked();
    }

    @Test
    @Story("Toggle all")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Toggle-all again uncompletes all todos")
    void toggleAllTwiceUncompletes() {
        todoPage
                .toggleAll()
                .toggleAll()
                .assertCompletedCount(0)
                .assertItemsLeftCount(3)
                .assertToggleAllUnchecked();
    }

    @Test
    @Story("Toggle all")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Toggle-all becomes checked when all items are individually completed")
    void toggleAllCheckedWhenAllManuallyCompleted() {
        todoPage
                .completeTodo(0)
                .completeTodo(1)
                .completeTodo(2)
                .assertToggleAllChecked();
    }

    @Test
    @Story("Toggle all")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Toggle-all uncompletes all when all are completed")
    void toggleAllWhenAllCompletedUncompletes() {
        todoPage
                .toggleAll()             // complete all
                .toggleAll()             // uncomplete all
                .assertItemsLeftCount(3);
    }
}
