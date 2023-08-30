package academy.tochkavhoda.controllers;

import academy.tochkavhoda.dao.ProductDao;
import academy.tochkavhoda.dao.ProductReviewDao;
import academy.tochkavhoda.models.Product;
import academy.tochkavhoda.models.ProductReview;
import academy.tochkavhoda.models.User;
import academy.tochkavhoda.service.KpiCalculator;
import academy.tochkavhoda.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;
    @MockBean
    private ProductDao productDao;

    @MockBean
    private ProductReviewDao productReviewDao;

    @MockBean
    private KpiCalculator kpiCalculator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        String existingEan = "1231231231231";
        double price = 3.95;
        String description = "Best chocolate from Siberia with love";
        String country = "Russia";
        String manufacturerAddress = "top secret";

        Product existingProduct = new Product();
        existingProduct.setEan(existingEan);
        existingProduct.setPrice(2.99);
        existingProduct.setDescription("Old description");
        existingProduct.setCountry("Old country");
        existingProduct.setCompany("Old company");
        existingProduct.setManufacturerAddress("Old manufacturer address");

        when(productDao.findById(existingEan)).thenReturn(existingProduct);
        existingProduct.setPrice(price);
        existingProduct.setDescription(description);
        existingProduct.setCountry(country);
        existingProduct.setCompany(existingProduct.getCompany());
        existingProduct.setManufacturerAddress(manufacturerAddress);
        when(productDao.update(Mockito.any(Product.class))).thenReturn(existingProduct);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{ean}", existingEan)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"ean\": \"" + existingEan + "\",\n" +
                                "    \"price\": 3.95,\n" +
                                "    \"description\": \"Best chocolate from Siberia with love\",\n" +
                                "    \"country\": \"Russia\",\n" +
                                "    \"company\": \"Some company\",\n" +
                                "    \"manufacturerAddress\": \"top secret\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Product responseProduct = objectMapper.readValue(responseContent, Product.class);
        assertEquals(existingEan, responseProduct.getEan());
        assertEquals(price, responseProduct.getPrice());
        assertEquals(description, responseProduct.getDescription());
        assertEquals(country, responseProduct.getCountry());
        assertEquals(existingProduct.getCompany(), responseProduct.getCompany());
        assertEquals(manufacturerAddress, responseProduct.getManufacturerAddress());

        verify(productDao, times(1)).findById(existingEan);
        verify(productDao, times(1)).update(any(Product.class));
    }



    @Test
    public void testCreateReview() throws Exception {
        String ean = "1001001001";
        Product product = new Product();
        product.setEan(ean);
        User user = new User();

        when(productDao.existsById(ean)).thenReturn(true);
        when(productReviewDao.insert(Mockito.any(ProductReview.class))).thenReturn(new ProductReview());

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/products/{ean}/reviews", ean)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ean\": \"123\",\"rating\": 5,\"willRecommend\": true,\"brandLoyalty\": true,\"user\": " + userJson + "}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    public void testCreateReview_ProductNotFound() throws Exception {
        String ean = "123";

        when(productDao.existsById(ean)).thenReturn(false);
        User user = new User();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/products/{ean}/reviews", ean)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ean\": \"123\",\"rating\": 5,\"willRecommend\": true,\"brandLoyalty\": true,\"user\": " + userJson + "}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void testDeleteProduct() throws Exception {
        String ean = "1001001001";

        when(productDao.existsById(ean)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{ean}", ean))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testCalculateKpiMetrics() throws Exception {
        String ean = "1001001001";

        when(productDao.existsById(ean)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/products/{ean}/metrics/kpi", ean))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("{\"status\": \"creating\"}"));
    }

    @Test
    public void testCalculateKpiMetrics_ProductNotFound() throws Exception {
        String ean = "1001001001";

        when(productDao.existsById(ean)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/products/{ean}/metrics/kpi", ean))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetKpiMetrics() throws Exception {
        String ean = "1001001001";
        Map<String, Object> metrics = Collections.singletonMap("avgRating", 3.50);

        when(productDao.existsById(ean)).thenReturn(true);
        when(kpiCalculator.getKpiMetricsForProduct(ean)).thenReturn(metrics);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/{ean}/metrics/kpi", ean))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.avgRating").value(3.50));
    }

    @Test
    public void testGetKpiMetrics_ProductNotFound() throws Exception {
        String ean = "1001001001";

        when(productDao.existsById(ean)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/{ean}/metrics/kpi", ean))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}


/*
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-junit-jupiter</artifactId>
            <version>4.6.1</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.8.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-inline</artifactId>
            <version>4.6.1</version>
            <scope>test</scope>
        </dependency>
 */