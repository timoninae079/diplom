package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement buyButton = $(byText("Купить"));
    private final SelenideElement creditButton = $(byText("Купить в кредит"));

    final SelenideElement bankApproved = $(withText("Операция одобрена Банком."));


    public void cardPayment() {

        buyButton.click();
    }

    public void cardCredit() {
        creditButton.click();

    }

    public void expectApprovalFromBank() {

        bankApproved.should(visible, Duration.ofSeconds(20));
    }
}
