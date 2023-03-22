package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.page.CreditPage;
import ru.netology.page.MainPage;

import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.getApprovedCard;
import static ru.netology.data.DataGenerator.getDeclinedCard;
import static ru.netology.data.DataHelper.getCreditRequestInfo;
import static ru.netology.data.DataHelper.getOrderInfo;

public class CreditTest {
    private final MainPage main = new MainPage();
    private final CreditPage credit = new CreditPage();
    

    @BeforeAll
    public static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openPage() {

        open("http://localhost:8080");
    }

    @Test
    void test() {
        main.cardCredit();
    }



    @Test
    @SneakyThrows
    @DisplayName("Получение кредита на покупку по валидной карте")
    public void shouldCreditValidCard() {
        main.cardCredit();
        var info = getApprovedCard();
        credit.sendingData(info);
        //Время отправки данных в базу данных, в секундах:
        TimeUnit.SECONDS.sleep(20);
        var expected = "APPROVED";
        var creditRequestInfo = getCreditRequestInfo();
        var orderInfo = getOrderInfo();
        //Проверка соответствия статуса в базе данных в таблице запросов кредита:
        assertEquals(expected, creditRequestInfo.getStatus());
        //Проверка соответствия в базе данных id в таблице запросов кредита и в таблице заявок:
        assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());
        //Проверка вывода соответствующего уведомления пользователю на странице покупок:
        main.expectApprovalFromBank();
    }

    @Test
    @SneakyThrows
    @DisplayName("Получение кредита на покупку по не валидной карте")
    public void shouldCreditInvalidCard() {
        main.cardCredit();
        var info = getDeclinedCard();
        credit.sendingData(info);
        //Время отправки данных в базу данных, в секундах:
        TimeUnit.SECONDS.sleep(10);
//        var expected = "DECLINED";
//        var creditRequestInfo = getCreditRequestInfo();
//        var orderInfo = getOrderInfo();
//        //Проверка соответствия статуса в базе данных в таблице запросов кредита:
//        assertEquals(expected, creditRequestInfo.getStatus());
//        //Проверка соответствия в базе данных id в таблице запросов кредита и в таблице заявок:
//        assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());
        //Проверка вывода соответствующего уведомления пользователю на странице покупок:
        credit.errorBankRefusal();
    }

    @Nested
    //Тесты на валидацию полей кредитной формы:
    public class CreditFormFieldValidation {

        @BeforeEach
        public void setPayment() {
            open("http://localhost:8080");
            main.cardCredit();
        }


        @Test
        @DisplayName("Отправка пустой формы")
        public void shouldEmpty() {
            credit.clickOnContinue();
            credit.errorMessageInvalidFormat();
        }

        @Test
        @DisplayName("Поле 'Номер карты', пустое поле")
        public void shouldEmptyCardNumberField() {
            credit.fillOutFields("","11","23","OwnerIam","432");
            credit.clickOnContinue();
            credit.fieldCardIsEmpty();

        }

        @Test
        @DisplayName("Поле 'Номер карты', не полный номер карты")
        public void shouldCardWithIncompleteCardNumber() {
            credit.fillOutFields("4444 4444 4444 444","11","23","ivanov","123");
            credit.errorMessageInvalidFormat();
        }

        @Test
        @DisplayName("Поле 'Месяц', пустое поле")
        public void shouldEmptyMonthField() {
            credit.fillOutFields("4444 4444 4444 4441","","23","ivanov","123");
            credit.clickOnContinue();
            credit.errorMessageInvalidFormat();


        }

        @Test
        @DisplayName("Поле 'Месяц', просроченный месяц")
        public void shouldCardWithOverdueMonth() {
            credit.fillOutFields("4444 4444 4444 4441","01","23","ivanov","456");
            credit.errorMessageInvalidDuration();
        }

        @Test
        @DisplayName("Поле 'Месяц', нижнее негативное значение '00'")
        public void shouldCardWithLowerMonthValue() {
            credit.fillOutFields("4444 4444 4444 4441","00","23","ivanov","123");
            credit.clickOnContinue();
            credit.errorMessageInvalidDuration();
        }

        @Test
        @DisplayName("Поле 'Месяц', верхнее негативное значение '13'")
        public void shouldCardWithGreaterMonthValue() {
            credit.fillOutFields("4444 4444 4444 4441","13","23","ivanov","123");
            credit.clickOnContinue();
            credit.errorMessageInvalidDuration();
        }

        @Test
        @DisplayName("Поле 'Год', пустое поле")
        public void shouldEmptyYearField() {
            credit.fillOutFields("4444 4444 4444 4441","03","","ivanov","123");
            credit.clickOnContinue();
            credit.errorMessageInvalidFormat();
        }

        @Test
        @DisplayName("Поле 'Год', просроченный год")
        public void shouldCardWithOverdueYear() {
            credit.fillOutFields("4444 4444 4444 4441","03","21","ivanov","123");
            credit.clickOnContinue();
            credit.errorMessageInvalidYear();

        }

        @Test
        @DisplayName("Поле 'Год', год из отдаленного будущего")
        public void shouldCardWithYearFromFuture() {
            credit.fillOutFields("4444 4444 4444 4441","03","30","ivanov","123");
            credit.clickOnContinue();
            credit.errorMessageInvalidDuration();

        }

        @Test
        @DisplayName("Поле 'Владелец', пустое поле")
        public void shouldEmptyOwnerField() {
            credit.fillOutFields("4444 4444 4444 4441","03","30","","123");
            credit.clickOnContinue();
            credit.errorMessageWhenOwnerFieldIsEmpty();
        }

        @Test
        @DisplayName("Поле 'Владелец', с пробелом или дефисом")
        public void shouldCardWithSpaceOrHyphenOwner() {
            credit.fillOutFields("4444 4444 4444 4441","03","23"," - ","123");
            credit.clickOnContinue();
            credit.errorMessageWhenOwnerFieldIsEmpty();

        }

        @Test
        @DisplayName("Поле 'Владелец', с несколькими спец символами")
        public void shouldCardWithSpecialSymbolsOwner() {
            credit.fillOutFields("4444 4444 4444 4441","03","23","@@&&ivanov","123");
            credit.errorMessageWhenOwnerFieldIsEmpty();
        }

        @Test
        @DisplayName("Поле 'Владелец', с цифрами")
        public void shouldCardWithNumbersOwner() {
            credit.fillOutFields("4444 4444 4444 4441","03","23","123456","123");
            credit.errorMessageWhenOwnerFieldIsEmpty();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', пустое поле")
        public void shouldEmptyCVCField() {
            credit.fillOutFields("4444 4444 4444 4441","03","23","ivanov","");
            credit.clickOnContinue();
            credit.errorMessageWhenOwnerFieldIsEmpty();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', не полный номер")
        public void shouldCardWithIncompleteCVC() {
            credit.fillOutFields("4444 4444 4444 4441","03","23","ivanov","12");
            credit.clickOnContinue();
            credit.errorMessageWhenOwnerFieldIsEmpty();
        }
    }
}


