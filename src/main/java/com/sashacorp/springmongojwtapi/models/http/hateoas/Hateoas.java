package com.sashacorp.springmongojwtapi.models.http.hateoas;

import java.util.Map;
import java.util.stream.Collectors;

import com.sashacorp.springmongojwtapi.models.http.hateoas.Rel.MimeUtil;

public interface Hateoas {

	public Links get_resources();
	
	public void setResources(Map<String, String> uris);

	public String replacePathVariables(String url);

	default Links computeResources(Map<String, String> uris) {
		return new Links(uris.entrySet().stream().collect(
				Collectors.toMap(entry -> entry.getKey(), entry -> new Link(replacePathVariables(entry.getValue()), MimeUtil.mime(entry.getValue())))));
	}

	default String placeholder(String placeholder) {
		return String.format("{%s}", placeholder);
	}
}
