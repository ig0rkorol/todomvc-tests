package com.todomvc.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;

@Epic("TodoMVC React")
@Feature("Filter Todos")
@DisplayName("Filter Todos")
class FilterTodoTest extends BaseTest {

    @BeforeEach
    void setupMixedTodos() {
        todoPage.addTodos("Active 1", "Completed 1", "Active 2", "Completed 2");
        todoPage.completeTodo(1);
        todoPage.completeTodo(3);
        // State: Active 1 (active), Completed 1 (done), Active 2 (active), Completed 2 (done)
    }

    @Test
    @Story("All filter")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("'All' filter shows all todos regardless of status")
    void allFilterShowsEverything() {
        todoPage
                .filterAll()
                .assertTodoCount(4)
                .assertFilterSelected("All");
    }

    @Test
    @Story("Active filter")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("'Active' filter shows only incomplete todos")
    void activeFilterShowsOnlyActive() {
        todoPage
                .filterActive()
                .assertTodoCount(2)
                .assertTodoText(0, "Active 1")
                .assertTodoText(1, "Active 2")
                .assertFilterSelected("Active");
    }

    @Test
    @Story("Completed filter")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("'Completed' filter shows only completed todos")
    void completedFilterShowsOnlyCompleted() {
        todoPage
                .filterCompleted()
                .assertTodoCount(2)
                .assertTodoText(0, "Completed 1")
                .assertTodoText(1, "Completed 2")
                .assertFilterSelected("Completed");
    }

    @Test
    @Story("Filter + action")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Completing a todo while on Active filter removes it from view immediately")
    void completingOnActiveFilterHidesTodo() {
        todoPage
                .filterActive()
                .completeTodo(0)         // complete "Active 1"
                .assertTodoCount(1)
                .assertTodoText(0, "Active 2");
    }

    @Test
    @Story("Filter + action")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Clearing completed while on Completed filter empties the list")
    void clearCompletedOnCompletedFilterEmptiesList() {
        todoPage
                .filterCompleted()
                .clearCompleted()
                .assertEmpty();
    }

    @Test
    @Story("Filter + action")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Active filter shows empty list when all todos are completed")
    void activeFilterEmptyWhenAllCompleted() {
        todoPage
                .toggleAll()
                .filterActive()
                .assertEmpty();
    }

    @Test
    @Story("Filter + action")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Completed filter is empty when no todos are completed")
    void completedFilterEmptyWhenNoneCompleted() {
        // Start fresh — override @BeforeEach side-effects
        todoPage.filterAll();
        // Complete all, then uncomplete all
        todoPage.toggleAll().toggleAll();
        todoPage.filterCompleted().assertEmpty();
    }
}
