package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.controllers.UserController;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Если некорректно заполнено поле mail, то возвращается код 400")
    void emaleCorrectTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"email\":\"userserver.com\",\"login\":\"shtras\",\"name\":\"Иван\"," +
                                "\"birthday\":\"1985-07-06\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Если  поле email пустое, то возвращается код 400")
    void emaleIsEmptyTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"email\":\"\",\"login\":\"shtras\",\"name\":\"Иван\",\"birthday\":\"1985-07-06\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}



