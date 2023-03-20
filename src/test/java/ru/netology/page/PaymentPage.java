package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {

    final SelenideElement fieldCardNumber = $("[placeholder='0000 0000 0000 0000']");

    final SelenideElement fieldMonth = $("[placeholder='08']");

    final SelenideElement fieldYear = $("[placeholder='22']");

    final SelenideElement fieldOwner = $$("[class='input__control']").get(3);

    final SelenideElement fieldCvc = $("[placeholder='999']");

    final SelenideElement buttonContinue = $(byText("Продолжить"));

    final SelenideElement bankApproved = $(withText("Операция одобрена Банком."));

    final SelenideElement errorFormat = $(withText("Неверный формат"));

    final SelenideElement errorBankRefusal = $(withText("Ошибка! Банк отказал в проведении операции."));

    final SelenideElement invalidDurationCard = $(withText("Неверно указан срок действия карты"));

    final SelenideElement cardExpired = $(withText("Истёк срок действия карты"));

    final SelenideElement requiredField = $(withText("Поле обязательно для заполнения"));

    public void fillOutFields(String cardNumber, String month, String year, String owner, String cvc) {
        fieldCardNumber.setValue(cardNumber);
        fieldMonth.setValue(month);
        fieldYear.setValue(year);
        fieldOwner.setValue(owner);
        fieldCvc.setValue(cvc);
        buttonContinue.click();
    }
    public void errorMessageInvalidFormat() {
        errorFormat.shouldBe(visible, Duration.ofSeconds(2));
    }
    public void errorMessageWhenOwnerFieldIsEmpty() {
        requiredField.shouldBe(visible, Duration.ofSeconds(2));
    }
    public void expectApprovalFromBank() {
        bankApproved.should(visible, Duration.ofSeconds(20));
    }

    public void errorMessageInvalidDuration() {п
        invalidDurationCard.shouldBe(visible);
    }

    public void errorMessageInvalidYear() {
        cardExpired.shouldBe(visible);
    }
}
