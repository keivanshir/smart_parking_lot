import com.example.smartparkinglotmanagementsystem.SmartParkingLotManagementSystemApplication;
import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {SmartParkingLotManagementSystemApplication.class})
@AutoConfigureMockMvc
public class UserServiceTest {


    @Autowired
    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private String token;

    @Test
    public void loginTest() throws Exception {

        Map<String, String> userMap = new HashMap<>();
        userMap.put("email", "kevin2_shir@gmail.com" );
        userMap.put("password", "keivan2");

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userMap);

        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Response responseDto = mapper.readValue(response.getContentAsString(), Response.class);
        token = responseDto.getToken();

    }

    @Test
    public void registerUserTest() throws Exception{
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", "keivan2 shir");
        userMap.put("email", "kevin2_shir@gmail.com" );
        userMap.put("password", "keivan2");

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userMap);

        MvcResult mvcResult = mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonResult))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Response responseDto = mapper.readValue(response.getContentAsString(), Response.class);
        token = responseDto.getToken();
    }

}
