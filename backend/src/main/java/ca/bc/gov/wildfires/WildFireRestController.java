package ca.bc.gov.wildfires;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api")
public class WildFireRestController {
	@GetMapping(path = "/openmaps", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonNode getOpenMaps(@RequestParam(defaultValue = "all") String filter) throws Exception {
		String requestURL = (filter.equals("geo"))
				? "https://openmaps.gov.bc.ca/geo/pub/ows?service=WFS&version=2.0.0&request=GetFeature&typeName=pub:WHSE_LAND_AND_NATURAL_RESOURCE.PROT_CURRENT_FIRE_PNTS_SP&cql_filter=GEOGRAPHIC_DESCRIPTION=%27Goose%20Creek%27&outputFormat=application%2Fjson"
				: (filter.equals("fire"))
						? "https://openmaps.gov.bc.ca/geo/pub/ows?service=WFS&version=2.0.0&request=GetFeature&typeName=pub:WHSE_LAND_AND_NATURAL_RESOURCE.PROT_CURRENT_FIRE_PNTS_SP&cql_filter=FIRE_CAUSE%3C%3E%27Person%27%20AND%20FIRE_STATUS=%27Out%27&count=10&outputFormat=application%2Fjson"
						: "https://openmaps.gov.bc.ca/geo/pub/ows?service=WFS&version=2.0.0&request=GetFeature&typeName=pub:WHSE_LAND_AND_NATURAL_RESOURCE.PROT_CURRENT_FIRE_PNTS_SP&outputFormat=application%2Fjson";
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(requestURL)).build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		return new ObjectMapper().readTree(response.body());
	}
}
