package com.todomvc.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;

@Epic("TodoMVC React")
@Feature("Delete Todo")
@DisplayName("Delete Todo")
class DeleteTodoTest extends BaseTest {

    @Test
    @Story("Delete single")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Deleting the only todo hides footer and clears the list")
    void deleteOnlyTodo() {
        todoPage
                .addTodo("Single item")
                .deleteTodo(0)
                .assertEmpty()
                .assertFooterHidden();
    }

    @Test
    @Story("Delete single")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Deleting one of many todos leaves the rest intact")
    void deleteOneOfManyTodos() {
        todoPage
                .addTodos("First", "Second", "Third")
                .deleteTodo(1)           // delete "Second"
                .assertTodoCount(2)
                .assertTodoText(0, "First")
                .assertTodoText(1, "Third");
    }

    @Test
    @Story("Clear completed")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Clear-completed removes all completed todos and hides its button")
    void clearCompletedRemovesAll() {
        todoPage
                .addTodos("Done 1", "Active 1", "Done 2")
                .completeTodo(0)
                .completeTodo(2)
                .assertClearCompletedVisible()
                .clearCompleted()
                .assertTodoCount(1)
                .assertTodoText(0, "Active 1")
                .assertClearCompletedHidden();
    }

    @Test
    @Story("Clear completed")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Clear-completed button is not shown when no todos are completed")
    void clearCompletedHiddenWhenNoneCompleted() {
        todoPage
                .addTodos("Active 1", "Active 2")
                .assertClearCompletedHidden();
    }

    @Test
    @Story("Clear completed")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("After clear-completed, remaining active count updates correctly")
    void itemsLeftUpdatesAfterClearCompleted() {
        todoPage
                .addTodos("Keep 1", "Keep 2", "Remove")
                .completeTodo(2)
                .clearCompleted()
                .assertItemsLeftCount(2);
    }
}
