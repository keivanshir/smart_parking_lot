package com.example.smartparkinglotmanagementsystem.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI parkingSystemManagementOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Parking System APIs")
                        .contact(new Contact().name("Keivan Shirkoubian")
                                .email("keivan.shir.74@gmail.com")
                                .url("https://github.com/keivanshir/smart_parking_lot"))
                        .description("This is a parking lot system API documentation")
                        .version("1.0.0"));
    }
}
