package com.todomvc.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Epic("TodoMVC React")
@Feature("Add Todo")
@DisplayName("Add Todo")
class AddTodoTest extends BaseTest {

    @Test
    @Story("Happy path")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Add a single todo item")
    @Description("Entering text and pressing Enter creates a new todo item visible in the list")
    void addSingleTodo() {
        todoPage
                .addTodo("Buy milk")
                .assertTodoCount(1)
                .assertTodoText(0, "Buy milk")
                .assertInputCleared()
                .assertItemsLeftCount(1)
                .assertFooterVisible();
    }

    @Test
    @Story("Happy path")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Add multiple todo items sequentially")
    void addMultipleTodos() {
        todoPage
                .addTodos("Task one", "Task two", "Task three")
                .assertTodoCount(3)
                .assertTodoText(0, "Task one")
                .assertTodoText(1, "Task two")
                .assertTodoText(2, "Task three")
                .assertItemsLeftCount(3);
    }

    @Test
    @Story("Edge case")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Pressing Enter on empty input does not create a todo")
    void emptyInputDoescNotCreateTodo() {
        todoPage
                .addTodo("")
                .assertEmpty()
                .assertFooterHidden();
    }

    @Test
    @Story("Edge case")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Input with only whitespace does not create a todo")
    void whitespaceOnlyDoesNotCreateTodo() {
        todoPage
                .addTodo("   ")
                .assertEmpty();
    }

    @ParameterizedTest(name = "Add todo with text: \"{0}\"")
    @ValueSource(strings = {
            "Simple task",
            "Task with special chars !@#$%^&*()",
            "Very long task name that exceeds typical single-line display in the viewport and wraps around"
    })
    @Story("Parameterized")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Add todos with various text values")
    void addTodoWithVariousTexts(String text) {
        todoPage
                .addTodo(text)
                .assertTodoCount(1)
                .assertTodoText(0, text.strip().replaceAll("&","&amp;"));
    }

    @Test
    @Story("Edge case")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Todo text is trimmed of surrounding whitespace")
    void todoTextIsTrimmed() {
        todoPage
                .addTodo("  trimmed text  ")
                .assertTodoText(0, "trimmed text");
    }
}
