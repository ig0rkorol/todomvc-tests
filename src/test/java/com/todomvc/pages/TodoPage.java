package com.todomvc.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Page Object for TodoMVC React application.
 * Encapsulates all selectors and user-facing actions.
 */
public class TodoPage {

    // ── Selectors ────────────────────────────────────────────────────────────

    private final SelenideElement newTodoInput    = $(".new-todo");
    private final SelenideElement toggleAllBtn    = $(".toggle-all");
    private final SelenideElement todoCount       = $(".todo-count");
    private final SelenideElement clearCompleted  = $(".clear-completed");
    private final SelenideElement footer          = $(".footer");
    private final SelenideElement main            = $(".main");

    private ElementsCollection todoItems()          { return $$(".todo-list li"); }
    private ElementsCollection completedItems()     { return $$(".todo-list li.completed"); }

    private SelenideElement todoLabel(int index)    { return todoItems().get(index).$("label"); }
    private SelenideElement todoCheckbox(int index) { return todoItems().get(index).$(".toggle"); }
    private SelenideElement todoDestroy(int index)  { return todoItems().get(index).$(".destroy"); }
    private SelenideElement todoEdit(int index)     { return todoItems().get(index).$(".todo-list .new-todo"); }

    private SelenideElement filterLink(String name) {
        return switch (name) {
            case "All"       -> $(".filters a[href='#/']");
            case "Active"    -> $(".filters a[href*='active']");
            case "Completed" -> $(".filters a[href*='completed']");
            default -> throw new IllegalArgumentException("Unknown filter: " + name);
        };
    }
    // ── Navigation ───────────────────────────────────────────────────────────

    @Step("Open TodoMVC application")
    public TodoPage openApp() {
        open("");
        newTodoInput.shouldBe(visible);
        return this;
    }

    // ── Add ──────────────────────────────────────────────────────────────────

    @Step("Add todo: \"{text}\"")
    public TodoPage addTodo(String text) {
        newTodoInput.click();
        newTodoInput.sendKeys(text + Keys.ENTER);
        return this;
    }

    @Step("Add todos: {texts}")
    public TodoPage addTodos(String... texts) {
        for (String text : texts) addTodo(text);
        return this;
    }

    // ── Complete ─────────────────────────────────────────────────────────────

    @Step("Complete todo at index {index}")
    public TodoPage completeTodo(int index) {
        todoCheckbox(index).click();
        return this;
    }

    @Step("Toggle all todos")
    public TodoPage toggleAll() {
        toggleAllBtn.click();
        return this;
    }

    // ── Delete ───────────────────────────────────────────────────────────────

    @Step("Delete todo at index {index}")
    public TodoPage deleteTodo(int index) {
        SelenideElement item = todoItems().get(index);
        item.hover();
        todoDestroy(index).click();
        return this;
    }

    @Step("Clear all completed todos")
    public TodoPage clearCompleted() {
        clearCompleted.shouldBe(visible).click();
        return this;
    }

    // ── Edit ─────────────────────────────────────────────────────────────────

    @Step("Double-click to edit todo at index {index}")
    public TodoPage startEditing(int index) {
        todoLabel(index).doubleClick();
        return this;
    }

    @Step("Set edit field value to \"{newText}\" and confirm with Enter")
    public TodoPage finishEditing(int index, String newText) {
        SelenideElement edit = todoEdit(index).shouldBe(visible);

        // Set value via JS and trigger React synthetic events
        executeJavaScript(
                "var input = arguments[0];" +
                        "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(input, arguments[1]);" +
                        "input.dispatchEvent(new Event('input', { bubbles: true }));",
                edit, newText
        );
        edit.pressEnter();
        return this;
    }

    @Step("Cancel editing with Escape")
    public TodoPage cancelEditing(int index) {
        todoEdit(index).shouldBe(visible).pressEscape();
        return this;
    }

    // ── Filters ──────────────────────────────────────────────────────────────

    @Step("Select filter: All")
    public TodoPage filterAll() {
        filterLink("All").click();
        return this;
    }

    @Step("Select filter: Active")
    public TodoPage filterActive() {
        filterLink("Active").click();
        return this;
    }

    @Step("Select filter: Completed")
    public TodoPage filterCompleted() {
        filterLink("Completed").click();
        return this;
    }

    // ── Assertions ───────────────────────────────────────────────────────────

    @Step("Assert todo list has exactly {count} item(s)")
    public TodoPage assertTodoCount(int count) {
        todoItems().shouldHave(size(count));
        return this;
    }

    @Step("Assert todo list is empty")
    public TodoPage assertEmpty() {
        todoItems().shouldHave(size(0));
        return this;
    }

    @Step("Assert todo at index {index} has text \"{expectedText}\"")
    public TodoPage assertTodoText(int index, String expectedText) {
        todoLabel(index).shouldHave(exactText(expectedText));
        return this;
    }

    @Step("Assert todo at index {index} is completed")
    public TodoPage assertCompleted(int index) {
        todoItems().get(index).shouldHave(cssClass("completed"));
        return this;
    }

    @Step("Assert todo at index {index} is active (not completed)")
    public TodoPage assertActive(int index) {
        todoItems().get(index).shouldNotHave(cssClass("completed"));
        return this;
    }

    @Step("Assert active item counter shows {expected}")
    public TodoPage assertItemsLeftCount(int expected) {
        todoCount.shouldHave(text(String.valueOf(expected)));
        return this;
    }

    @Step("Assert footer is hidden (no todos)")
    public TodoPage assertFooterHidden() {
        footer.shouldNotBe(visible);
        return this;
    }

    @Step("Assert footer is visible")
    public TodoPage assertFooterVisible() {
        footer.shouldBe(visible);
        return this;
    }

    @Step("Assert 'Clear completed' button is visible")
    public TodoPage assertClearCompletedVisible() {
        clearCompleted.shouldBe(visible);
        return this;
    }

    @Step("Assert 'Clear completed' button is hidden")
    public TodoPage assertClearCompletedHidden() {
        clearCompleted.shouldNotBe(visible);
        return this;
    }

    @Step("Assert completed items count is {count}")
    public TodoPage assertCompletedCount(int count) {
        completedItems().shouldHave(size(count));
        return this;
    }

    @Step("Assert input field is empty after adding a todo")
    public TodoPage assertInputCleared() {
        newTodoInput.shouldHave(value(""));
        return this;
    }

    @Step("Assert filter '{name}' is selected")
    public TodoPage assertFilterSelected(String name) {
        filterLink(name).shouldHave(cssClass("selected"));
        return this;
    }

    @Step("Assert toggle-all checkbox is checked (all completed)")
    public TodoPage assertToggleAllChecked() {
        toggleAllBtn.shouldBe(checked);
        return this;
    }

    @Step("Assert toggle-all checkbox is unchecked")
    public TodoPage assertToggleAllUnchecked() {
        toggleAllBtn.shouldNotBe(checked);
        return this;
    }
}
