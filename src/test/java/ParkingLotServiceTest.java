import com.example.smartparkinglotmanagementsystem.SmartParkingLotManagementSystemApplication;
import com.example.smartparkinglotmanagementsystem.dto.Response;
import com.example.smartparkinglotmanagementsystem.enums.VehicleType;
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
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {SmartParkingLotManagementSystemApplication.class})
@AutoConfigureMockMvc
public class ParkingLotServiceTest {

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

    private static char rndChar() {
        int rnd = (int) (Math.random() * 52); // or use Random or whatever
        char base = (rnd < 26) ? 'A' : 'a';
        return (char) (base + rnd % 26);

    }

    @Test
    public void registerVehicleTest() throws Exception{
        Random random = new Random();

        Map<String, String> userMap = new HashMap<>();
        userMap.put("licensePlate", random.nextInt(10,100)
                + String.valueOf(rndChar())
                + random.nextInt(100,999)
                + "/" + random.nextInt(10,100));
        userMap.put("vehicleType", VehicleType.CAR.name());

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userMap);

        mockMvc.perform(post("/parkingLot/registerVehicle")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonResult))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    public void registerVehicleExitTest() throws Exception{

        mockMvc.perform(post("/parkingLot/registerVehicleExit/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

}
