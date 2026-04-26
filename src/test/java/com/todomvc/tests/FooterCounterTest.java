package com.todomvc.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;

@Epic("TodoMVC React")
@Feature("Footer Counter")
@DisplayName("Footer items-left counter")
class FooterCounterTest extends BaseTest {

    @Test
    @Story("Counter")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Counter increments with each new todo")
    void counterIncrementsOnAdd() {
        todoPage
                .addTodo("First")
                .assertItemsLeftCount(1)
                .addTodo("Second")
                .assertItemsLeftCount(2)
                .addTodo("Third")
                .assertItemsLeftCount(3);
    }

    @Test
    @Story("Counter")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Counter decrements when a todo is completed")
    void counterDecrementsOnComplete() {
        todoPage
                .addTodos("AAA", "BBB", "CCC")
                .completeTodo(0)
                .assertItemsLeftCount(2)
                .completeTodo(1)
                .assertItemsLeftCount(1);
    }

    @Test
    @Story("Counter")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Counter shows 0 when all todos are completed")
    void counterIsZeroWhenAllCompleted() {
        todoPage
                .addTodos("X", "Y")
                .toggleAll()
                .assertItemsLeftCount(0);
    }

    @Test
    @Story("Counter")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Counter decrements when a todo is deleted")
    void counterDecrementsOnDelete() {
        todoPage
                .addTodos("Keep", "Remove")
                .deleteTodo(1)
                .assertItemsLeftCount(1);
    }

    @Test
    @Story("Footer visibility")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Footer is hidden on fresh load with no todos")
    void footerHiddenOnFreshLoad() {
        todoPage.assertFooterHidden();
    }

    @Test
    @Story("Footer visibility")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Footer appears as soon as the first todo is added")
    void footerAppearsAfterFirstTodo() {
        todoPage
                .assertFooterHidden()
                .addTodo("First todo")
                .assertFooterVisible();
    }
}
