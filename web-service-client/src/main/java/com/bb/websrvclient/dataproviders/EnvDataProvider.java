package com.bb.websrvclient.dataproviders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import com.bb.websrvclient.domains.EnvironmentFormBean;
import com.bb.websrvclient.domains.EnvironmentServicesFormBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class EnvDataProvider {
	
	public static List<EnvironmentFormBean> loadEnvList(){
		BufferedReader br=null;
		File file = null;
		try{
			file = new File("env_details.json");
			if(file.exists()){
				br = new BufferedReader(new FileReader(file));
			}else{
				ClassPathResource jsonFile = new ClassPathResource("env_details.json");
				br = new BufferedReader(new InputStreamReader(jsonFile.getInputStream(), "UTF-8"));
			}
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
	
	public static Map<String, String> emptyMap() {
		Map<String, String> emptyMap = new LinkedHashMap<String, String>();
		emptyMap.put("", "");
		return emptyMap;
	}
	
	public static Map<String, String> loadEnvCodesMap() {
		Map<String, String> envsMap = emptyMap();
		List<EnvironmentFormBean> envList = loadEnvList();
		for (EnvironmentFormBean env : envList) {
			envsMap.put(env.getEnvCode(), env.getEnvCode());
		}
		return envsMap;
	}
	
	public static Map<String, String> loadServiceCodesMap(String envCode) {
		Map<String, String> srvsMap = emptyMap();
		List<EnvironmentFormBean> envList = loadEnvList();
		for (EnvironmentFormBean env : envList) {
			if (env.getEnvCode().equals(envCode)) {
				List<EnvironmentServicesFormBean> services = env.getServices();
				if (services != null) {
					for (EnvironmentServicesFormBean srv : services) {
						srvsMap.put(srv.getServiceCode(), srv.getServiceCode());
					}
				}
			}
		}
		return srvsMap;
	}
	
	public static EnvironmentServicesFormBean loadServiceFormBean(String envCode, String serviceCode) {
		List<EnvironmentFormBean> envList = loadEnvList();
		for (EnvironmentFormBean env : envList) {
			if (env.getEnvCode().equals(envCode)) {
				List<EnvironmentServicesFormBean> services = env.getServices();
				if (services != null) {
					for (EnvironmentServicesFormBean srv : services) {
						if (srv.getServiceCode().equals(serviceCode)) {
							return srv;
						}
					}
				}
			}
		}
		return null;
	}
	
}