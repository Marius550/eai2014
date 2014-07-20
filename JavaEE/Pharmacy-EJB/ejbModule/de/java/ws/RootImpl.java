package de.java.ws;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

public class RootImpl implements Root {

	public Map<String, Map<String, URI>> links(UriInfo uriInfo) {
		Map<String, URI> links = new HashMap<>();
		links.put("drug", drugUri(uriInfo));
		
		Map<String, Map<String, URI>> map = new HashMap<>();
		map.put("links", links);
		return map;
	}

	private URI drugUri(UriInfo uriInfo) {
		return uriInfo.getBaseUriBuilder()
				.path(DrugResource.class)
				.build();
	}
}
