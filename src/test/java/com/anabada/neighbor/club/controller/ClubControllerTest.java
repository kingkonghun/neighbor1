package com.anabada.neighbor.club.controller;

import com.anabada.neighbor.club.service.ClubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClubControllerTest {
    @Mock
    private ClubService clubService;

    @InjectMocks
    private ClubController clubController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clubController)
                .build();
    }

    @Test
    void clubCount_WhenJoinAction_ShouldCallJoinClubAndRedirect() throws Exception {
        long postId = 1L;
        String action = "join";

        mockMvc.perform(post("/clubDetail/action")
                        .param("postId", String.valueOf(postId))
                        .param("action", action))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/clubDetail/" + postId));

        verify(clubService).joinClub(postId);
        verifyNoInteractions(clubService);
    }

    @Test
    void clubCount_WhenLeaveAction_ShouldCallLeaveClubAndRedirect() throws Exception {
        long postId = 1L;
        String action = "leave";

        mockMvc.perform(post("/clubDetail/action")
                        .param("postId", String.valueOf(postId))
                        .param("action", action))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/clubDetail/" + postId));

        verify(clubService).leaveClub(postId);
        verifyNoInteractions(clubService);
    }

}