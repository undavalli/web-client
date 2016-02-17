package com.bb.websrvclient.executors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.StringUtils;

import com.bb.websrvclient.dataproviders.EnvDataProvider;
import com.bb.websrvclient.domains.EnvServiceReqHeaderFormBean;
import com.bb.websrvclient.domains.EnvironmentServicesFormBean;
import com.bb.websrvclient.domains.ServicesJobDetailsFromBean;
import com.bb.websrvclient.domains.WebServiceReq;
import com.bb.websrvclient.email.MailSenderUtility;
import com.bb.websrvclient.helpers.ApplicationContextProvider;

public class WebServiceInvoker {
	private ServicesJobDetailsFromBean jobDtlsBean;

	public WebServiceInvoker(ServicesJobDetailsFromBean webServiceReq) {
		this.jobDtlsBean = webServiceReq;
	}

	public void callWebService() {
		WebServiceReq webServiceReq = null;
		try {
			webServiceReq = new WebServiceReq();
			webServiceReq.setEnviromentId(jobDtlsBean.getEnvCode());
			webServiceReq.setServiceId(jobDtlsBean.getServiceCode());
			File reqFile = new File(jobDtlsBean.getReqContFileName());
			if (reqFile.exists() && reqFile.isFile()) {
				String fileContent = FileUtils.readFileToString(reqFile);
				if (!StringUtils.isEmpty(fileContent)) {
					webServiceReq.setReqContent(fileContent);
					WebServiceInvoker.callWebService(webServiceReq);
					sendMail(webServiceReq, "Job Executed sucessfully.");
				} else {
					sendMail(webServiceReq, "Empty file found");
				}
			} else {
				sendMail(webServiceReq, "File not found");
			}
		} catch (Exception e) {
			sendMail(webServiceReq, e.getMessage());
			e.printStackTrace();
		}
	}

	public void sendMail(WebServiceReq webServiceReq, String message) {
		try {
			MailSenderUtility mailSender = ApplicationContextProvider.getApplicationContext().getBean(MailSenderUtility.class);
			String subject = "Job execution status for env code " + jobDtlsBean.getEnvCode() + " service code " + jobDtlsBean.getServiceCode();
			mailSender.sendMail(jobDtlsBean.getNotificationEmail(), subject, message, new File(jobDtlsBean.getReqContFileName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void callWebService(WebServiceReq webServiceReq) throws Exception {
		CloseableHttpClient httpClient;
		HttpPost httpPost;
		StringEntity stringEntity;
		CloseableHttpResponse httpResponse;
		EnvironmentServicesFormBean selectSrv;
		List<EnvServiceReqHeaderFormBean> headersList;
		try {
			selectSrv = EnvDataProvider.loadServiceFormBean(webServiceReq.getEnviromentId(), webServiceReq.getServiceId());
			if (selectSrv != null) {
				httpClient = HttpClients.createDefault();
				httpPost = new HttpPost(selectSrv.getServiceURL());
				//
				headersList = selectSrv.getHeaders();
				if (headersList != null && headersList.size() > 0) {
					for (EnvServiceReqHeaderFormBean header : headersList) {
						httpPost.addHeader(header.getHeaderCode(), header.getHeaderValue());
					}
				}
				//
				stringEntity = new StringEntity(webServiceReq.getReqContent());
				httpPost.setEntity(stringEntity);
				httpResponse = httpClient.execute(httpPost);
				webServiceReq.setRespContent(httpRespToStr(httpResponse));
				httpClient.close();
			}
		} finally {

		}
	}

	private static String httpRespToStr(CloseableHttpResponse httpResponse) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine + "\n");
		}
		reader.close();
		return response.toString();
	}
}
