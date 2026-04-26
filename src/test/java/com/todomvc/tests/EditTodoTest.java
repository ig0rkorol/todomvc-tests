package com.todomvc.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;

@Epic("TodoMVC React")
@Feature("Edit Todo")
@DisplayName("Edit Todo")
class EditTodoTest extends BaseTest {

    @BeforeEach
    void addInitialTodo() {
        todoPage.addTodos("Original text", "Another task");
    }

    @Test
    @Story("Edit text")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Double-clicking a todo allows editing and Enter saves the new text")
    void editTodoAndSaveWithEnter() {
        todoPage
                .startEditing(0)
                .finishEditing(0, "Updated text")
                .assertTodoText(0, "Updated text")
                .assertTodoCount(2);
    }

    @Test
    @Story("Edit text")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Pressing Escape during editing reverts to the original text")
    @Disabled("BUG-002: Escape key does not exit edit mode")
    void cancelEditingWithEscape() {
        todoPage
                .startEditing(0)
                .cancelEditing(0)
                .assertTodoText(0, "Original text");
    }

    @Test
    @Story("Edge case")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Saving an empty edit field deletes the todo")
    @Disabled("BUG-003: Saving empty edit field does not delete the todo. " +
            "Expected: todo removed. Actual: field stays in edit mode with blank text.")
    void savingEmptyEditDeletesTodo() {
        todoPage
                .startEditing(0)
                .finishEditing(0, "")
                .assertTodoCount(1)
                .assertTodoText(0, "Another task");
    }

    @Test
    @Story("Edge case")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Editing trims whitespace from the saved text")
    void editingTrimsTodo() {
        todoPage
                .startEditing(0)
                .finishEditing(0, "  trimmed  ")
                .assertTodoText(0, "trimmed");
    }

    @Test
    @Story("Edit text")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Completed todo can still be edited")
    void editCompletedTodo() {
        todoPage
                .completeTodo(0)
                .startEditing(0)
                .finishEditing(0, "Edited completed task")
                .assertTodoText(0, "Edited completed task");
    }
}
