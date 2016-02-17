package com.bb.websrvclient.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.util.StringUtils;

import com.bb.websrvclient.dataproviders.EnvDataProvider;
import com.bb.websrvclient.domains.EnvironmentServicesFormBean;
import com.bb.websrvclient.domains.ServicesJobDetailsFromBean;
import com.bb.websrvclient.executors.WebServiceInvoker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ServicesJobSchedulerListener implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired(required=true)
	private Scheduler scheduler;
	
	public static List<ServicesJobDetailsFromBean> loadSrvJobDtlsList() {
		BufferedReader br = null;
		File file = null;
		try {
			file = new File("services_job_details.json");
			if (file.exists()) {
				br = new BufferedReader(new FileReader(file));
			} else {
				ClassPathResource jsonFile = new ClassPathResource("services_job_details.json");
				br = new BufferedReader(new InputStreamReader(jsonFile.getInputStream(), "UTF-8"));
			}
			Gson gson = new GsonBuilder().create();
			Type listType = new TypeToken<List<ServicesJobDetailsFromBean>>() {}.getType();
			List<ServicesJobDetailsFromBean> obj = gson.fromJson(br, listType);
			br.close();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			List<ServicesJobDetailsFromBean> jobDtlsList = loadSrvJobDtlsList();
			for (ServicesJobDetailsFromBean jobDtlsBean : jobDtlsList) {
				scheduleSrvJOB(jobDtlsBean);
			}
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	private void scheduleSrvJOB(ServicesJobDetailsFromBean jobDtlsBean) {
		WebServiceInvoker webSrvJob;
		MethodInvokingJobDetailFactoryBean jobDetail = null;
		CronTriggerBean cronTrigger;
		EnvironmentServicesFormBean selectSrv;
		try {
			selectSrv = EnvDataProvider.loadServiceFormBean(jobDtlsBean.getEnvCode(), jobDtlsBean.getServiceCode());
			if (selectSrv != null && !StringUtils.isEmpty(jobDtlsBean.getCronExp())) {
				//
				webSrvJob = new WebServiceInvoker(jobDtlsBean);
				//
				jobDetail = new MethodInvokingJobDetailFactoryBean();
				jobDetail.setTargetObject(webSrvJob);
				jobDetail.setTargetMethod("callWebService");
				jobDetail.setName(jobDtlsBean.getJobName() + "JOB_DETAILS");
				jobDetail.setConcurrent(false);
				jobDetail.afterPropertiesSet();
				//
				cronTrigger = new CronTriggerBean();
				cronTrigger.setBeanName(jobDtlsBean.getJobName() + "_CRON_EXP");
				cronTrigger.setCronExpression(jobDtlsBean.getCronExp());
				cronTrigger.afterPropertiesSet();
				scheduler.scheduleJob((JobDetail) jobDetail.getObject(), cronTrigger);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}