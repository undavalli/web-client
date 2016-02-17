package com.bb.websrvclient.rest.services;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/testsrvs")
public class TestService {

	@POST
	@Path("/")
	public Response testSrv(String req) {
		System.out.println(req);
		return Response.status(200).entity(req).build();
	}
}
