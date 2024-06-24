package com.bideafactory.house_reservation;

import com.bideafactory.house_reservation.dto.DiscountResponse;
import com.bideafactory.house_reservation.service.BookService;
import com.bideafactory.house_reservation.service.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HouseReservationApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private BookService bookService;

	@MockBean
	private DiscountService discountService;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testCreateReservationWithoutHouseId() throws Exception {
		String reservation = "{\"id\": \"1456088-4\", \"name\": \"Gonzalo\", \"lastname\": \"Pérez\", \"age\": 33, \"phoneNumber\": \"56988123222\", \"startDate\": \"2022-03-04\", \"endDate\": \"2022-03-04\", \"discountCode\": \"D054A23\"}";

		mockMvc.perform(post("/bideafactory/book")
						.contentType(MediaType.APPLICATION_JSON)
						.content(reservation))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateReservationWithInvalidDiscount() throws Exception {
		String reservation = "{\"id\": \"1456088-4\", \"name\": \"Gonzalo\", \"lastname\": \"Pérez\", \"age\": 33, \"phoneNumber\": \"56988123222\", \"startDate\": \"2022-03-04\", \"endDate\": \"2022-03-04\", \"houseId\": \"213132\", \"discountCode\": \"D054A23\"}";

		when(discountService.validateDiscount(any())).thenThrow(new RuntimeException("Discount API validation failed"));

		mockMvc.perform(post("/bideafactory/book")
						.contentType(MediaType.APPLICATION_JSON)
						.content(reservation))
				.andExpect(status().isConflict());
	}

	@Test
	public void testCreateReservationSuccess() throws Exception {
		String reservation = "{\"id\": \"1456088-4\", \"name\": \"Gonzalo\", \"lastname\": \"Pérez\", \"age\": 33, \"phoneNumber\": \"56988123222\", \"startDate\": \"2022-03-04\", \"endDate\": \"2022-03-04\", \"houseId\": \"213132\", \"discountCode\": \"D054A23\"}";

		when(discountService.validateDiscount(any())).thenReturn(new DiscountResponse(true));

		mockMvc.perform(post("/bideafactory/book")
						.contentType(MediaType.APPLICATION_JSON)
						.content(reservation))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetAllReservations() throws Exception {
		mockMvc.perform(get("/bideafactory/book"))
				.andExpect(status().isOk());
	}
}
