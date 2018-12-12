package com.ifood.project.router;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import com.ifood.project.businesslogic.RegionalClimateFeelingPlaylist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

public class RouterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testWorldwidePlaylistCall_whenPassingCityName_expectsOk() throws Exception {
        this.mockMvc.perform(get("/worldwidePlaylist?city=sao paulo")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("musics")));
    }

    @Test
    public void testWorldwidePlaylistCall_whenPassingCityLocation_expectsOk() throws Exception {
        this.mockMvc.perform(get("/worldwidePlaylist?lat=-23.55&lon=-46.64")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("musics")));
    }
}