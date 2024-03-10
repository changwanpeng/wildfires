package ca.bc.gov.wildfires;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(WildFireRestController.class)
public class WildFireRestControllerTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void getOpenMaps() throws Exception {
		mvc.perform(MockMvcRequestBuilders
			.get("/api/openmaps")
			.accept(MediaType.APPLICATION_JSON))
			.andReturn()
	        .getResponse()
	        .getContentAsString()
	        .contains("features");
	}
}
