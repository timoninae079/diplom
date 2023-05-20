package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.page.CreditPage;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.DataHelper.getCreditRequestInfo;
import static ru.netology.data.DataHelper.getOrderInfo;

public class CreditTest {
    private MainPage mainPage;
    private CreditPage creditPage;


    @BeforeAll
    public static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openPage() {
        mainPage = open("http://localhost:8080", MainPage.class);
        mainPage.cardCredit();
        creditPage = new CreditPage();
    }

    @Test
    @DisplayName("Получение кредита на покупку по валидной карте")
    public void shouldCreditValidCard() {
        var info = getApprovedCard();
        creditPage.sendingData(info);
        creditPage.expectApprovalFromBank();
        var expected = "APPROVED";
        var creditRequestInfo = getCreditRequestInfo();
        var orderInfo = getOrderInfo();
        assertEquals(expected, creditRequestInfo.getStatus());
        assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());

    }

    @Test
    @DisplayName("Получение кредита на покупку по не валидной карте")
    public void shouldCreditInvalidCard() {
        var info = getDeclinedCard();
        creditPage.sendingData(info);
        creditPage.errorBankRefusal();
        var expected = "DECLINED";
        var creditRequestInfo = getCreditRequestInfo();
        var orderInfo = getOrderInfo();
        assertEquals(expected,getCreditRequestInfo());
        assertEquals(creditRequestInfo.getBank_id(),orderInfo.getCredit_id());

    }

    @Nested
    //Тесты на валидацию полей кредитной формы:
    public class CreditFormFieldValidation {
        private final CardInfo validCard = getApprovedCard();

        @Test
        @DisplayName("Отправка пустой формы")
        public void shouldEmpty() {
            creditPage.clickOnContinue();
            creditPage.errorMessageInvalidFormat();
        }

        @Test
        @DisplayName("Поле 'Номер карты', не полный номер карты")
        public void shouldCardWithIncompleteCardNumber() {
            creditPage.sendingData(getCardWithIncompleteCardNumber());
            creditPage.errorMessageInvalidFormat();
        }

        @Test
        @DisplayName("Поле 'Месяц', пустое поле")
        public void shouldEmptyMonthField() {
            creditPage.fillOutFields(validCard.getNumberCard(), "", validCard.getYear(), validCard.getOwner(), validCard.getCvc());
            creditPage.clickOnContinue();
            creditPage.errorMessageInvalidFormat();

        }

        @Test
        @DisplayName("Поле 'Месяц', просроченный месяц")
        public void shouldCardWithOverdueMonth() {
            creditPage.sendingData(getCardWithLowerMonthValue());
            creditPage.errorMessageInvalidDuration();
        }

        @Test
        @DisplayName("Поле 'Месяц', нижнее негативное значение '00'")
        public void shouldCardWithLowerMonthValue() {
            creditPage.sendingData(getCardWithLowerMonthValue());
            creditPage.clickOnContinue();
            creditPage.errorMessageInvalidDuration();
        }

        @Test
        @DisplayName("Поле 'Месяц', верхнее негативное значение '13'")
        public void shouldCardWithGreaterMonthValue() {
            creditPage.sendingData(getCardWithGreaterMonthValue());
            creditPage.clickOnContinue();
            creditPage.errorMessageInvalidDuration();
        }

        @Test
        @DisplayName("Поле 'Год', пустое поле")
        public void shouldEmptyYearField() {
            creditPage.fillOutFields(validCard.getNumberCard(), validCard.getMonth(), "", validCard.getOwner(), validCard.getCvc());
            creditPage.clickOnContinue();
            creditPage.errorMessageInvalidFormat();
        }

        @Test
        @DisplayName("Поле 'Год', просроченный год")
        public void shouldCardWithOverdueYear() {
            creditPage.sendingData(getCardWithOverdueYear());
            creditPage.clickOnContinue();
            creditPage.errorMessageInvalidYear();

        }

        @Test
        @DisplayName("Поле 'Год', год из отдаленного будущего")
        public void shouldCardWithYearFromFuture() {
            creditPage.sendingData(getCardWithYearFromFuture());
            creditPage.clickOnContinue();
            creditPage.errorMessageInvalidDuration();

        }

        @Test
        @DisplayName("Поле 'Владелец', пустое поле")
        public void shouldEmptyOwnerField() {
            creditPage.fillOutFields(validCard.getNumberCard(), validCard.getMonth(), validCard.getYear(), "", validCard.getCvc());
            creditPage.clickOnContinue();
            creditPage.errorMessageWhenOwnerFieldIsEmpty();
        }

        @Test
        @DisplayName("Поле 'Владелец', с пробелом или дефисом")
        public void shouldCardWithSpaceOrHyphenOwner() {
            creditPage.sendingData(getCardWithSpaceOrHyphenOwner());
            creditPage.clickOnContinue();
            creditPage.errorMessageWhenOwnerFieldIsEmpty();

        }

        @Test
        @DisplayName("Поле 'Владелец', с несколькими спец символами")
        public void shouldCardWithSpecialSymbolsOwner() {
            creditPage.sendingData(getCardWithSpecialSymbolsOwner());
            creditPage.errorMessageWhenOwnerFieldIsEmpty();
        }

        @Test
        @DisplayName("Поле 'Владелец', с цифрами")
        public void shouldCardWithNumbersOwner() {
            creditPage.sendingData(getCardWithNumbersOwner());
            creditPage.errorMessageWhenOwnerFieldIsEmpty();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', пустое поле")
        public void shouldEmptyCVCField() {
            creditPage.fillOutFields(validCard.getNumberCard(), validCard.getMonth(), validCard.getYear(), validCard.getOwner(), "");
            creditPage.clickOnContinue();
            creditPage.errorMessageWhenOwnerFieldIsEmpty();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', не полный номер")
        public void shouldCardWithIncompleteCVC() {
            creditPage.sendingData(getCardWithIncompleteCVC());
            creditPage.clickOnContinue();
            creditPage.expectApprovalFromBank();
        }
    }
}


