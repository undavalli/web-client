package com.bb.websrvclient.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bb.websrvclient.domains.WebServiceReq;
import com.bb.websrvclient.persistence.entity.EnvironmentHeader;
import com.bb.websrvclient.persistence.entity.EnvironmentService;
import com.bb.websrvclient.services.IEnvironmentService;

@Controller
public class WebServiceClientController {
	@Autowired(required = true)
	private IEnvironmentService environmentService;
	@Autowired (required = true)
	private HttpSession httpSession;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView indexBuild(Model model) {
		//
		WebServiceReq reqObj = new WebServiceReq();
		Map<String, String> envsMap = new LinkedHashMap<String, String>();
		Map<String, String> srvsMap = new LinkedHashMap<String, String>();
		envsMap.put("", "");
		srvsMap.put("", "");
		//
		List<EnvironmentHeader> envList = environmentService.listEnvironments();
		if (envList != null) {
			int i = 0;
			for (EnvironmentHeader env : envList) {
				envsMap.put(env.getId(), env.getEnvCode());
				if (i == 0) {
					srvsMap = prepareSrvMap(env);
					i++;
				}
			}
		}
		//
		reqObj.setEnviromentsMap(envsMap);
		reqObj.setEnvServicesMap(srvsMap);
		//
		httpSession.setAttribute("EnvironmentsList", envList);
		//
		return new ModelAndView("index", "WebServiceReq", reqObj);
	}
	
	@RequestMapping(value = "/loadSrvs", method = RequestMethod.POST)
	public ModelAndView loadServicesList(@ModelAttribute("WebServiceReq") WebServiceReq reqObj) {
		String envId = reqObj.getEnviromentId();
		loadData(reqObj, envId);
		return new ModelAndView("index", "WebServiceReq", reqObj);
	}
	
	@SuppressWarnings("unchecked")
	private void loadData(WebServiceReq reqObj, String envId) {
		Map<String, String> envsMap = new LinkedHashMap<String, String>();
		Map<String, String> srvsMap = new LinkedHashMap<String, String>();
		envsMap.put("", "");
		srvsMap.put("", "");
		List<EnvironmentHeader> envList = (List<EnvironmentHeader>) httpSession.getAttribute("EnvironmentsList");
		if (envList != null) {
			for (EnvironmentHeader env : envList) {
				envsMap.put(env.getId(), env.getEnvCode());
				if (env.getId().equals(envId)) {
					srvsMap = prepareSrvMap(env);
				}
			}
		}
		reqObj.setEnviromentsMap(envsMap);
		reqObj.setEnvServicesMap(srvsMap);
	}
	
	private Map<String, String> prepareSrvMap(EnvironmentHeader envHeader) {
		Map<String, String> srvsMap = new LinkedHashMap<String, String>();
		srvsMap.put("", "");
		List<EnvironmentService> services = envHeader.getServices();
		if (services != null) {
			for (EnvironmentService srv : services) {
				srvsMap.put(srv.getId(), srv.getServiceCode());
			}
		}
		return srvsMap;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/callService", method = RequestMethod.POST)
	public ModelAndView callWebService(@ModelAttribute("WebServiceReq") WebServiceReq reqObj) {
		List<EnvironmentService> services = null;
		EnvironmentService selectSrv = null;
		CloseableHttpClient httpClient;
		HttpPost httpPost;
		StringEntity stringEntity;
		CloseableHttpResponse httpResponse;
		try {
			String envId = reqObj.getEnviromentId();
			List<EnvironmentHeader> envList = (List<EnvironmentHeader>) httpSession.getAttribute("EnvironmentsList");
			if (envList != null) {
				for (EnvironmentHeader env : envList) {
					if (env.getId().equals(envId)) {
						services = env.getServices();
						if (services != null) {
							for (EnvironmentService srv : services) {
								if (srv.getId().equals(reqObj.getServiceId())) {
									selectSrv = srv;
								}
							}
							httpClient = HttpClients.createDefault();
							httpPost = new HttpPost(selectSrv.getServiceURL());
							stringEntity = new StringEntity(reqObj.getReqContent());
							httpPost.setEntity(stringEntity);
							httpResponse = httpClient.execute(httpPost);
							reqObj.setRespContent(httpRespToStr(httpResponse));
							httpClient.close();
						}
					}
				}
			}
			loadData(reqObj, envId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("index", "WebServiceReq", reqObj);
	}
	
	private String httpRespToStr(CloseableHttpResponse httpResponse) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine+"\n");
		}
		reader.close();
		return response.toString();
	}
}
