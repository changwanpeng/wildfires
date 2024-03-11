package ca.bc.gov.wildfires;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WildFireRestController {
	@GetMapping(path = "/openmaps", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonNode getOpenMaps(@RequestParam(defaultValue = "") String geoDesc,
			                    @RequestParam(defaultValue = "") String fireCause,
			                    @RequestParam(defaultValue = "") String fireStatus)
			                    throws Exception {
		String baseURL = "https://openmaps.gov.bc.ca/geo/pub/ows?service=WFS&version=2.0.0&request=GetFeature&typeName=pub:WHSE_LAND_AND_NATURAL_RESOURCE.PROT_CURRENT_FIRE_PNTS_SP&outputFormat=application/json";
		String[] filterParams = { geoDesc, fireCause, fireStatus };
		List<String> filters = new ArrayList<>();
		for (String filterParam : filterParams) {
			if (!filterParam.isEmpty())
				filters.add(filterParam);
		}
		StringBuilder queryParams = new StringBuilder();
		for (int i = 0; i < filters.size(); i++) {
			if (i != 0)
				queryParams.append(" AND ");
			queryParams.append(filters.get(i));
		}

		String requestURL = queryParams.length() == 0 ? baseURL
				: baseURL + "&cql_filter=" + URLEncoder.encode(queryParams.toString(), "UTF-8");
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(requestURL)).build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		return new ObjectMapper().readTree(response.body());
	}
}
