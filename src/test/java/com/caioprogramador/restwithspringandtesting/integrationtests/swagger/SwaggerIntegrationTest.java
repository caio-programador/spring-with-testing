package com.caioprogramador.restwithspringandtesting.integrationtests.swagger;

import com.caioprogramador.restwithspringandtesting.config.TestConfig;
import com.caioprogramador.restwithspringandtesting.integrationtests.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest  extends AbstractIntegrationTest {

    @Test
    @DisplayName("Test should Display Swagger UI Page")
    void testShouldDisplaySwaggerUIPage() {
        String content = given().basePath("/swagger-ui/index.html")
                .port(TestConfig.SERVER_PORT)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();
        assertTrue(content.contains("Swagger UI"));
    }
}
