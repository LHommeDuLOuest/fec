package fr.bzh.rzh.web.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;

import fr.bzh.rzh.service.IManager;
import fr.bzh.rzh.web.controllers.Home;

/**
 * 
 * @author KHERBICHE L
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class HomeTest {
	
	@InjectMocks
	private Home controller;
	@Mock
	private IManager manager;
	@Mock
	private View mockView;
	private MockMvc mockMvc;
	

	
	@Before
	public void setUp () throws Exception {
		MockitoAnnotations.initMocks(this);
	    mockMvc = MockMvcBuilders.standaloneSetup(controller)
	    		.setSingleView(mockView)
	    		.build();
		//mockMvc = MockMvcBuilders.standaloneSetup(new EventController()).build();
	}
	
	@Test
	public void testGetUploadForm () throws Exception {
		mockMvc.perform(get("/upload"))
			                .andExpect(status().isOk())
			                .andExpect(view().name("test"));
	}

}
