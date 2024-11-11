import com.example.smartparkinglotmanagementsystem.SmartParkingLotManagementSystemApplication;
import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.enums.SpotSize;
import com.example.smartparkinglotmanagementsystem.mapper.EntityDtoMapper;
import com.example.smartparkinglotmanagementsystem.repository.ParkingSpotRepository;
import com.example.smartparkinglotmanagementsystem.service.ParkingSpotService;
import com.example.smartparkinglotmanagementsystem.service.implementation.ParkingSpotServiceImpl;
import com.example.smartparkinglotmanagementsystem.service.implementation.ParkingWebsocketServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
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
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = {SmartParkingLotManagementSystemApplication.class})
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParkingSpotServiceTest {


    @Autowired
    private MockMvc mockMvc;

    public static final MediaType APPLICATION_JSON_UTF8 =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private String token;

    ParkingSpotService parkingSpotService;

    @Autowired
    ParkingSpotRepository parkingSpotRepository;

    @Autowired
    EntityDtoMapper entityDtoMapper;

    @Autowired
    private ParkingWebsocketServiceImpl parkingWebSocketService;

    @BeforeEach
    public void init() throws Exception {

        parkingSpotService = new ParkingSpotServiceImpl(parkingSpotRepository,
                entityDtoMapper,
                parkingWebSocketService);

        Map<String, String> userMap = new HashMap<>();
        userMap.put("email", "admin@gmail.com");
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

        System.out.println(responseDto);

    }


    @Test
    @Order(1)
    public void addParkingSpotTest() throws Exception {

        Random random = new Random();

        Map<String, String> userMap = new HashMap<>();
        userMap.put("spotId", "parking_spot_large_" + random.nextInt(0, 100));
        userMap.put("spotSize", SpotSize.SMALL.name());

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userMap);

        mockMvc.perform(post("/parkingSpot/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonResult))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();


    }

    /**
     * first read the id saved from method addParkingSpotTest
     * then add it to this delete api call
     */
    @Test
    @Order(2)
    public void deleteParkingSpotTest() throws Exception {

        mockMvc.perform(delete("/parkingSpot/delete/")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }


    /**
     * first read the id saved from method addParkingSpotTest
     * then add it to this delete api call
     */

    @Test
    @Order(3)
    public void freeUpParkingSpotTest(){
        assertThat(parkingSpotService.freeUpParkingSpot(702L)).isNotNull();

    }
}
