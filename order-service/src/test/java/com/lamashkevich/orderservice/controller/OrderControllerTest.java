package com.lamashkevich.orderservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderControllerTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.0");

    @Container
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.7.0"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        Jwt fakeJwt = Jwt.withTokenValue("fake-token")
                .header("alg", "HS256")
                .claim("sub", "test-user-id")
                .build();

        when(jwtDecoder.decode(anyString())).thenReturn(fakeJwt);
    }

    @Test
    void placeOrder_shouldCreateAndReturnOrder() throws Exception {
        this.mockMvc.perform(post("/api/v1/orders")
                        .header("Authorization", "Bearer fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                     "email": "user@user.com",
                                     "name": "User",
                                     "items": [
                                         {
                                             "price": 1.11,
                                             "quantity": 1,
                                             "code": "CODE1",
                                             "brand": "BRAND1",
                                             "externalId": "1",
                                             "expectedDeliveryDate": "2024-11-03T10:15:30"
                                         },
                                         {
                                             "price": 2.22,
                                             "quantity": 2,
                                             "code": "CODE2",
                                             "brand": "BRAND2",
                                             "externalId": "2",
                                             "expectedDeliveryDate": "2024-12-03T10:15:30"
                                         }
                                     ]
                                }
                                """))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "id": 1,
                                    "email": "user@user.com",
                                    "name": "User",
                                    "status": "CREATED",
                                    "items": [
                                        {
                                            "price": 1.11,
                                            "quantity": 1,
                                            "code": "CODE1",
                                            "brand": "BRAND1",
                                            "externalId": "1",
                                            "expectedDeliveryDate": "2024-11-03T10:15:30",
                                            "status": "CREATED"
                                        },
                                        {
                                            "price": 2.22,
                                            "quantity": 2,
                                            "code": "CODE2",
                                            "brand": "BRAND2",
                                            "externalId": "2",
                                            "expectedDeliveryDate": "2024-12-03T10:15:30",
                                            "status": "CREATED"
                                        }
                                    ]
                                }
                                """)
                );
    }

    @Test
    @Sql(scripts = "/db/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getOrdersForUser_shouldReturnListOfOrders() throws Exception {
        this.mockMvc.perform(get("/api/v1/orders")
                        .header("Authorization", "Bearer fake-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                        "id": 1,
                                        "email": "user@user.com",
                                        "name": "User",
                                        "status": "CREATED",
                                        "items": [
                                            {
                                                "price": 1.11,
                                                "quantity": 1,
                                                "code": "CODE1",
                                                "brand": "BRAND1",
                                                "externalId": "1",
                                                "expectedDeliveryDate": "2024-11-03T10:15:30",
                                                "status": "CREATED"
                                            },
                                            {
                                                "price": 2.22,
                                                "quantity": 2,
                                                "code": "CODE2",
                                                "brand": "BRAND2",
                                                "externalId": "2",
                                                "expectedDeliveryDate": "2024-12-03T10:15:30",
                                                "status": "CREATED"
                                            }
                                        ]
                                    }
                                ]
                                """)
                );
    }

    @Test
    @Sql(scripts = "/db/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void changeOrderStatus_shouldReturnOrderStatus() throws Exception {
        this.mockMvc.perform(post("/api/v1/orders/status")
                        .header("Authorization", "Bearer fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                               "orderId": 1,
                               "status": "COMPLETED"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(content().string("\"COMPLETED\""));
    }

    @Test
    @Sql(scripts = "/db/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void changeOrderStatus_shouldThrowOrderNotFoundException_whenOrderDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/api/v1/orders/status")
                        .header("Authorization", "Bearer fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                               "orderId": 100,
                               "status": "COMPLETED"
                            }
                            """))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "/db/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void changeOrderStatus_shouldThrowChangeOrderStatusException_whenOrderDoesNotBelongToAuthorizedUser() throws Exception {
        this.mockMvc.perform(post("/api/v1/orders/status")
                        .header("Authorization", "Bearer fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                               "orderId": 2,
                               "status": "COMPLETED"
                            }
                            """))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(scripts = "/db/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void changeOrderItemStatus_shouldReturnOrderItemStatus() throws Exception {
        this.mockMvc.perform(post("/api/v1/orders/items/status")
                        .header("Authorization", "Bearer fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                               "orderItemId": 1,
                               "status": "COMPLETED"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(content().string("\"COMPLETED\""));
    }

    @Test
    @Sql(scripts = "/db/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void changeOrderItemStatus_shouldThrowOrderItemNotFoundException_whenOrderItemDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/api/v1/orders/items/status")
                        .header("Authorization", "Bearer fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                               "orderItemId": 100,
                               "status": "COMPLETED"
                            }
                            """))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "/db/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void changeOrderItemStatus_shouldThrowChangeOrderStatusException_whenOrderDoesNotBelongToAuthorizedUser() throws Exception {
        this.mockMvc.perform(post("/api/v1/orders/items/status")
                        .header("Authorization", "Bearer fake-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                               "orderItemId": 4,
                               "status": "COMPLETED"
                            }
                            """))
                .andExpect(status().isForbidden());
    }

}