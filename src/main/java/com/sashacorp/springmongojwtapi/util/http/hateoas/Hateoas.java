package com.sashacorp.springmongojwtapi.util.http.hateoas;

import java.util.Map;
import java.util.stream.Collectors;

import com.sashacorp.springmongojwtapi.util.http.hateoas.Rel.MimeUtil;

/**
 * Interface representing a HATEOAS-compliant entity exposing its resources
 * @author matteo
 *
 */
public interface Hateoas {
	
	/**
	 * Get hypertext representation of relevant resources
	 * @return
	 */
	public Links get_resources();
	
	/**
	 * Build a detailed list of 
	 * @param uris
	 */
	public void setResources(Map<String, String> uris);

	public String replacePathVariables(String url);
	
	/**
	 * Core method of this lightweight HATEOAS middleware: 
	 * leverages the {@code replacePathVariables(String url)} method enforced by the interface
	 * to initialize a {@link Link} for each entry of the {@code Map<String, String> uris}, with the correct MIME type of the URL, obtained through {@link MimeUtil}
	 * @param uris
	 * @return
	 */
	default Links computeResources(Map<String, String> uris) {
		return new Links(uris.entrySet().stream().collect(
				Collectors.toMap(entry -> entry.getKey(), 
						entry -> new Link(replacePathVariables(entry.getValue()), MimeUtil.mime(entry.getValue())))));
	}

	default String placeholder(String placeholder) {
		return String.format("{%s}", placeholder);
	}
}
