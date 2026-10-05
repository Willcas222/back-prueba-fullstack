package com.example.prueba_fullstack;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PruebaFullstackApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void createsTicketsChangesStatusAndReturnsHistory() throws Exception {
		String response = mockMvc.perform(post("/api/tickets")
						.contentType("application/json")
						.content("""
								{"title":"Laptop sin red","description":"No conecta","requestUser":"Ana",
								"notificationPhone":"+573001234567","category":"HARDWARE","priority":"CRITICA"}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.status").value("PENDIENTE"))
				.andReturn().getResponse().getContentAsString();
		Number ticketIdValue = com.jayway.jsonpath.JsonPath.read(response, "$.id");
		long ticketId = ticketIdValue.longValue();

		mockMvc.perform(patch("/api/tickets/{id}/status", ticketId)
						.contentType("application/json")
						.content("""
								{"status":"EN_PROGRESO","responsibleUser":"Soporte","observation":"En revisión"}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("EN_PROGRESO"));

		mockMvc.perform(get("/api/tickets/{id}/history", ticketId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].previousStatus").value("PENDIENTE"))
				.andExpect(jsonPath("$[0].responsibleUser").value("Soporte"));
	}

	@Test
	void rejectsInvalidTicketPayload() throws Exception {
		mockMvc.perform(post("/api/tickets")
						.contentType("application/json")
						.content("""
								{"title":" ","description":"Descripción","requestUser":"Ana",
								"category":"HARDWARE","priority":"MEDIA"}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").exists());
	}
}
