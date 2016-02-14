package com.bb.websrvclient.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bb.websrvclient.domains.EnvServiceReqHeaderFormBean;
import com.bb.websrvclient.domains.EnvironmentFormBean;
import com.bb.websrvclient.domains.EnvironmentServicesFormBean;
import com.bb.websrvclient.domains.WebServiceReq;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Controller
public class WebServiceClientController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView welcome() {
		return prepareIndexPage();
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView indexBuild(Model model) {
		return prepareIndexPage();
	}
	
	private ModelAndView prepareIndexPage(){
		//
		WebServiceReq reqObj = new WebServiceReq();
		Map<String, String> envsMap = new LinkedHashMap<String, String>();
		Map<String, String> srvsMap = new LinkedHashMap<String, String>();
		envsMap.put("", "");
		srvsMap.put("", "");
		//
		List<EnvironmentFormBean> envList = loadEnvList();
		for (EnvironmentFormBean env : envList) {
			envsMap.put(env.getEnvCode(), env.getEnvCode());
		}
		//
		reqObj.setEnviromentsMap(envsMap);
		reqObj.setEnvServicesMap(srvsMap);
		//
		return new ModelAndView("index", "WebServiceReq", reqObj);
	}
	
	@RequestMapping(value = "/loadSrvs", method = RequestMethod.POST)
	public ModelAndView loadServicesList(@ModelAttribute("WebServiceReq") WebServiceReq reqObj) {
		String envId = reqObj.getEnviromentId();
		loadData(reqObj, envId);
		return new ModelAndView("index", "WebServiceReq", reqObj);
	}
	
	private void loadData(WebServiceReq reqObj, String envId) {
		Map<String, String> envsMap = new LinkedHashMap<String, String>();
		Map<String, String> srvsMap = new LinkedHashMap<String, String>();
		envsMap.put("", "");
		srvsMap.put("", "");
		List<EnvironmentFormBean> envList = loadEnvList();
		for (EnvironmentFormBean env : envList) {
			envsMap.put(env.getEnvCode(), env.getEnvCode());
			if (env.getEnvCode().equals(envId)) {
				srvsMap = prepareSrvMap(env);
			}
		}
		reqObj.setEnviromentsMap(envsMap);
		reqObj.setEnvServicesMap(srvsMap);
	}
	
	private Map<String, String> prepareSrvMap(EnvironmentFormBean envHeader) {
		Map<String, String> srvsMap = new LinkedHashMap<String, String>();
		srvsMap.put("", "");
		List<EnvironmentServicesFormBean> services = envHeader.getServices();
		if (services != null) {
			for (EnvironmentServicesFormBean srv : services) {
				srvsMap.put(srv.getServiceCode(), srv.getServiceCode());
			}
		}
		return srvsMap;
	}
	
	@RequestMapping(value = "/callService", method = RequestMethod.POST)
	public ModelAndView callWebService(@ModelAttribute("WebServiceReq") WebServiceReq reqObj) {
		List<EnvironmentServicesFormBean> services = null;
		EnvironmentServicesFormBean selectSrv = null;
		CloseableHttpClient httpClient;
		HttpPost httpPost;
		StringEntity stringEntity;
		CloseableHttpResponse httpResponse;
		List<EnvServiceReqHeaderFormBean> headersList;
		try {
			String envId = reqObj.getEnviromentId();
			List<EnvironmentFormBean> envList = loadEnvList();
			for (EnvironmentFormBean env : envList) {
				if (env.getEnvCode().equals(envId)) {
					services = env.getServices();
					if (services != null) {
						for (EnvironmentServicesFormBean srv : services) {
							if (srv.getServiceCode().equals(reqObj.getServiceId())) {
								selectSrv = srv;
							}
						}
						httpClient = HttpClients.createDefault();
						httpPost = new HttpPost(selectSrv.getServiceURL());
						
						headersList = selectSrv.getHeaders();
						if(headersList!=null && headersList.size()>0){
							for(EnvServiceReqHeaderFormBean header:headersList){
								httpPost.addHeader(header.getHeaderCode(), header.getHeaderValue());
							}
						}
						stringEntity = new StringEntity(reqObj.getReqContent());
						httpPost.setEntity(stringEntity);
						httpResponse = httpClient.execute(httpPost);
						reqObj.setRespContent(httpRespToStr(httpResponse));
						httpClient.close();
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
	
	private List<EnvironmentFormBean> loadEnvList(){
		try{
			ClassPathResource jsonFile = new ClassPathResource("env_details.json");
			BufferedReader br = new BufferedReader(new InputStreamReader(jsonFile.getInputStream(), "UTF-8"));
			Gson gson = new GsonBuilder().create();
			Type listType = new TypeToken<List<EnvironmentFormBean>>() {}.getType();
			List<EnvironmentFormBean> obj = gson.fromJson(br, listType);
			br.close();
			return obj;
		}catch(Exception e){
			e.printStackTrace();
			return Collections.emptyList();
		}
		
	}
}
