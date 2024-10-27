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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {SmartParkingLotManagementSystemApplication.class})
@AutoConfigureMockMvc
public class ParkingManagementServiceTest {


    @Autowired
    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private String token;

    @BeforeEach
    public void init() throws Exception {

        Map<String, String> userMap = new HashMap<>();
        userMap.put("email", "admin@gmail.com" );
        userMap.put("password", "admin");

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
    public void viewRealtimeStatusTest() throws Exception{
        Map<String, String> pagination = new HashMap<>();
        pagination.put("page", "0");
        pagination.put("size", "5");
        MultiValueMap<String, String> paginationMap = new LinkedMultiValueMap<>();
        paginationMap.setAll(pagination);

        mockMvc.perform(get("/management/parkingSpotsStat")
                        .header("Authorization", "Bearer " + token)
                        .params(paginationMap))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void viewOccupancyRateTest() throws Exception{
        Map<String, String> pagination = new HashMap<>();
        pagination.put("page", "0");
        pagination.put("size", "5");
        MultiValueMap<String, String> paginationMap = new LinkedMultiValueMap<>();
        paginationMap.setAll(pagination);

        mockMvc.perform(get("/management/occupiedParkingSpots")
                        .header("Authorization", "Bearer " + token)
                        .params(paginationMap))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
