package ca.bc.gov.ls30;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api")
public class Ls30RestController {
	@GetMapping(path = "/openmaps", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonNode getOpenMaps() throws Exception {
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(
				"https://openmaps.gov.bc.ca/geo/pub/ows?service=WFS&version=2.0.0&request=GetFeature&typeName=pub:WHSE_LAND_AND_NATURAL_RESOURCE.PROT_CURRENT_FIRE_PNTS_SP&outputFormat=application%2Fjson"))
				.build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		return new ObjectMapper().readTree(response.body());
	}
}
