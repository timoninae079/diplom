package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.page.CreditPage;
import ru.netology.page.MainPage;
import ru.netology.page.PaymentPage;

import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.open;
import static java.util.concurrent.TimeUnit.SECONDS;
import static ru.netology.data.DataGenerator.getApprovedCard;
import static ru.netology.data.DataGenerator.getDeclinedCard;

public class PaymentTest {
    private final MainPage main = new MainPage();
    private final CreditPage credit = new CreditPage();
    private final PaymentPage payment = new PaymentPage();

    @BeforeAll
    public static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }


    @BeforeEach
    public void openPage() {
        open("http://localhost:8080");
        main.cardPayment();
    }

    @Test
    @SneakyThrows
    @DisplayName("Покупка валидной картой")
    public void shouldPaymentValidCard() {
        var info = getApprovedCard();
        payment.sendingData(info);
        //Время отправки данных в базу данных, в секундах:
        SECONDS.sleep(10);
        //  var expected = "APPROVED";
        //    var paymentInfo = getPaymentInfo();
        //    var orderInfo = getOrderInfo();
        //Проверка соответствия статуса в базе данных в таблице покупок:
//        assertEquals(expected, paymentInfo.getStatus());
//        //Проверка соответствия в базе данных id в таблице покупок и в таблице заявок:
//        assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
        //Проверка вывода соответствующего уведомления пользователю на странице покупок:
        main.expectApprovalFromBank();
    }
    @Test
    @SneakyThrows
    @DisplayName("Покупка не валидной картой")
    public void shouldPaymentInvalidCard() {
       var info = getDeclinedCard();
        payment.sendingData(info);
        //Время отправки данных в базу данных, в секундах:
        TimeUnit.SECONDS.sleep(10);
 //       var expected = "DECLINED";
//        var orderInfo = getOrderInfo();
        //Проверка соответствия статуса в базе данных в таблице покупок:
 //       assertEquals(expected, paymentInfo.getStatus());
        //Проверка соответствия в базе данных id в таблице покупок и в таблице заявок:
 //       assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
        //Проверка вывода соответствующего уведомления пользователю на странице покупок:
    }

    @Test
    @DisplayName("Поле 'Номер карты', пустое поле")
    public void shouldEmptyCardNumberField() {
        payment.fillOutFields("", "03", "23", "Ivanov", "123");
        payment.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Отправка пустой формы")
    public void shouldEmpty() {
        payment.clickOnContinue();
        payment.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'Номер карты', не полный номер карты")
    public void shouldCardWithIncompleteCardNumber() {
        payment.fillOutFields("4444 4444 4444", "03", "23", "Ivanov", "123");
        payment.clickOnContinue();
        payment.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'Месяц', пустое поле")
    public void shouldEmptyMonthField() {
        payment.fillOutFields("4444 4444 4444 4441", "", "23", "Ivanov", "123");
        payment.clickOnContinue();
        payment.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'Месяц', просроченный месяц")
    public void shouldCardWithOverdueMonth() {
        payment.fillOutFields("4444 4444 4444 4441", "01", "23", "Ivanov", "123");
        payment.errorMessageInvalidDuration();
    }

    @Test
    @DisplayName("Поле 'Месяц', нижнее негативное значение '00'")
    public void shouldCardWithLowerMonthValue() {
        payment.fillOutFields("4444 4444 4444 4441", "00", "23", "Ivanov", "123");
        payment.errorMessageInvalidDuration();
    }

    @Test
    @DisplayName("Поле 'Месяц', верхнее негативное значение '13'")
    public void shouldCardWithGreaterMonthValue() {
        payment.fillOutFields("4444 4444 4444 4441", "13", "23", "Ivanov", "123");
        payment.errorMessageInvalidDuration();
    }

    @Test
    @DisplayName("Поле 'Год', пустое поле")
    public void shouldEmptyYearField() {
        payment.fillOutFields("4444 4444 4444 4441", "11", "", "Ivanov", "123");
        payment.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'Год', просроченный год")
    public void shouldCardWithOverdueYear() {
        payment.fillOutFields("4444 4444 4444 4441", "11", "20", "Ivanov", "123");
        payment.errorMessageInvalidYear();
    }

    @Test
    @DisplayName("Поле 'Год', год из будущего")
    public void shouldCardWithYearFromFuture() {
        payment.fillOutFields("4444 4444 4444 4441", "11", "30", "Ivanov", "123");
        payment.errorMessageInvalidDuration();
    }

    @Test
    @DisplayName("Поле 'Владелец', пустое поле")
    public void shouldEmptyOwnerField() {
        payment.fillOutFields("4444 4444 4444 4441", "11", "24", "", "123");
        payment.errorMessageWhenOwnerFieldIsEmpty();
    }

    @Test
    @DisplayName("Поле 'Владелец', с пробелом или дефисом")
    public void shouldCardWithSpaceOrHyphenOwner() {
        payment.fillOutFields("4444 4444 4444 4441", "11", "24", "- -", "123");
        payment.clickOnContinue();
        payment.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'Владелец', с несколькими спец символами")
    public void shouldCardWithSpecialSymbolsOwner() {
        payment.fillOutFields("4444 4444 4444 4441", "11", "24", "@@Ivanov$$", "123");
        payment.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'Владелец', с цифрами")
    public void shouldCardWithNumbersOwner() {
        payment.fillOutFields("4444 4444 4444 4441", "11", "24", "12356", "123");
        payment.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'CVC/CVV', пустое поле")
    public void shouldEmptyCVCField() {
        payment.fillOutFields("4444 4444 4444 4441", "11", "24", "ivanov", "");
        payment.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'CVC/CVV', не полный номер")
    public void shouldCardWithIncompleteCVC() {
        payment.fillOutFields("4444 4444 4444 4441", "11", "24", "ivanov", "12");
        payment.errorMessageInvalidFormat();
    }
}


