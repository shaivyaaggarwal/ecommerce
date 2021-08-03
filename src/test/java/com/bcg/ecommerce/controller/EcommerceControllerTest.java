package com.bcg.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EcommerceControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetTotalPriceOfItemsInCartSuccess() throws Exception {
        List<String> listOfIds = Arrays.asList("001", "001", "001", "001", "001", "003", "003", "003", "003", "003", "003");
        MvcResult mvcResult = this.mvc.perform(post("/api/ecommerce/checkout")
                .contentType(APPLICATION_JSON)
                .content(asJsonString(listOfIds))
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
        assertThat(mvcResult.getResponse().getContentAsString()).contains("700");
    }

    @Test
    public void testGetTotalPriceOfItemsInCartFailure() throws Exception {
        List<String> listOfIds = Arrays.asList("001", "001", "001", "001", "001", "213", "003", "003", "003", "003", "003", "003");
        MvcResult mvcResult = this.mvc.perform(post("/api/ecommerce/checkout")
                .contentType(APPLICATION_JSON)
                .content(asJsonString(listOfIds))
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), status);
        assertThat(mvcResult.getResponse().getContentAsString()).contains("No watch found in the database with the id: 213");
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
