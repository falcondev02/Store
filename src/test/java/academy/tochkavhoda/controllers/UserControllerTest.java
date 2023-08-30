package academy.tochkavhoda.controllers;

import academy.tochkavhoda.dao.UserDao;
import academy.tochkavhoda.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDao userDao;

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setBirthday("1/10/1988");
        user.setSex("male");
        user.setCardNumber("1234554545");
        user.setHasKids(false);
        user.setMarried("true");
        user.setEducation("no");
        user.setResidencePlace("Omsk");

        when(userDao.insert(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"birthday\":\"1/10/1988\",\"sex\":\"male\",\"cardNumber\":\"1234554545\",\"hasKids\":false,\"married\":\"true\",\"education\":\"no\",\"residencePlace\":\"Omsk\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.birthday").value("1/10/1988"))
                .andExpect(jsonPath("$.sex").value("male"))
                .andExpect(jsonPath("$.cardNumber").value("1234554545"))
                .andExpect(jsonPath("$.hasKids").value(false))
                .andExpect(jsonPath("$.married").value("true"))
                .andExpect(jsonPath("$.education").value("no"))
                .andExpect(jsonPath("$.residencePlace").value("Omsk"));

        verify(userDao, times(1)).insert(any(User.class));
    }

    @Test
    public void testDeleteUser() throws Exception {
        int userId = 1;

        when(userDao.existsById(userId)).thenReturn(true);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userDao, times(1)).existsById(userId);
        verify(userDao, times(1)).deleteById(userId);
    }
}