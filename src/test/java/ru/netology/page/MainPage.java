package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    final SelenideElement buttonBuy = $("[class='button button_size_m button_theme_alfa-on-white']");
    final SelenideElement buttonBuyCredit = $(byText("Купить в кредит"));

    public PaymentPage buyDebitCard() {
        buttonBuy.click();
        return new PaymentPage();
    }

    public PaymentPage buyCreditCard() {
        buttonBuyCredit.click();
        return new PaymentPage();
    }
}