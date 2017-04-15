package com.bb.websrvclient.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bb.websrvclient.dataproviders.EnvDataProvider;
import com.bb.websrvclient.domains.WebServiceReq;
import com.bb.websrvclient.executors.WebServiceInvoker;

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
		WebServiceReq reqObj = new WebServiceReq();
		reqObj.setEnviromentsMap(EnvDataProvider.loadEnvCodesMap());
		reqObj.setEnvServicesMap(EnvDataProvider.emptyMap());
		return new ModelAndView("index", "WebServiceReq", reqObj);
	}
	
	@RequestMapping(value = "/loadSrvs", method = RequestMethod.POST)
	public ModelAndView loadServicesList(@ModelAttribute("WebServiceReq") WebServiceReq reqObj) {
		reqObj.setEnviromentsMap(EnvDataProvider.loadEnvCodesMap());
		reqObj.setEnvServicesMap(EnvDataProvider.loadServiceCodesMap(reqObj.getEnviromentId()));
		return new ModelAndView("index", "WebServiceReq", reqObj);
	}
		
	@RequestMapping(value = "/callService", method = RequestMethod.POST)
	public ModelAndView callWebService(@ModelAttribute("WebServiceReq") WebServiceReq reqObj) {
		try{
			WebServiceInvoker.callWebService(reqObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		reqObj.setEnviromentsMap(EnvDataProvider.loadEnvCodesMap());
		reqObj.setEnvServicesMap(EnvDataProvider.loadServiceCodesMap(reqObj.getEnviromentId()));
		return new ModelAndView("index", "WebServiceReq", reqObj);
	}
}
