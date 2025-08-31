package ManagerTests;
import manager.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ManagersTest {

    @Test
    void shouldBeNotNull() {
        Managers manager = new Managers();
        Assertions.assertNotNull(manager.getDefault(), "Не прошел");
        Assertions.assertNotNull(Managers.getDefaultHistory(), "Не прошел");
        //последним тестом был "убедитесь, что утилитарный класс всегда возвращает проинициализированные_
        // и готовые к работе экземпляры менеджеров"
    }
}