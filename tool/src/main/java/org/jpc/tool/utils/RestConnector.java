 package org.jpc.tool.utils;

 import java.net.SocketException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;
import org.jpc.tool.enums.ResponseCodeEnum;
import org.jpc.tool.models.Response;
 
 @SuppressWarnings({"unchecked", "rawtypes"})
 @Slf4j
 public class RestConnector {
    private static final int TIME_OUT_IN_MIN = 3; 

    @Bean
    public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
       RestTemplate restTemplate = new RestTemplate(this.getClientHttpRequestFactory());
       restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
       return restTemplate;
    }
 

	private ClientHttpRequestFactory getClientHttpRequestFactory()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
		CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
		HttpComponentsClientHttpRequestFactory customRequestFactory = new HttpComponentsClientHttpRequestFactory();
		customRequestFactory.setHttpClient(httpClient);
		customRequestFactory.setConnectionRequestTimeout(TIME_OUT_IN_MIN*60000);
		customRequestFactory.setReadTimeout(TIME_OUT_IN_MIN*60000);
		return customRequestFactory;
	}
 
    public ResponseEntity<String> exchange(String requestBody, String urlToCall, HttpHeaders headers, HttpMethod httpMethod) throws JsonProcessingException {
       try {
          log.info("CALLING URL:::" + urlToCall);
          if (headers == null) {
             headers = new HttpHeaders();
          }
 
          HttpEntity<String> entity = new HttpEntity(requestBody, headers);
          log.info("REQUEST BODY:::" + (((String)entity.getBody()).contains("base64Image") ? "REQUEST NOT LOGGED COS OF LENGTH" : (String)entity.getBody()));
          log.info("HEADERS::: ");
          HttpMethod exchangeMethod = httpMethod != null ? httpMethod : HttpMethod.POST;
          ResponseEntity<String> response = this.getRestTemplate().exchange(urlToCall, exchangeMethod, entity, String.class, new Object[0]);
          log.info("RESPONSE FROM SERVICE:::" + response);
          return response;
       } catch (HttpClientErrorException var8) {
          log.error("SERVICE RETURNED CLIENT ERROR :::" + var8.getStatusCode());
          log.error("SERVICE RETURNED CLIENT BODY :::" + var8.getResponseBodyAsString());
          return var8.getResponseBodyAsString() != null ? ResponseEntity.badRequest().body(var8.getResponseBodyAsString()) : null;
       } catch (HttpServerErrorException var9) {
          log.error("SERVICE RETURNED SERVER ERROR :::" + var9.getStatusCode());
          log.error("SERVICE RETURNED SERVER BODY :::" + var9.getResponseBodyAsString());
          return var9.getResponseBodyAsString() != null ? ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(var9.getResponseBodyAsString()) : null;
       } catch (ResourceAccessException var10) {
          log.info("========= SERVICE ERROR ======== CONNECTION TIME OUT : " + var10.getMessage());
          Response response = new Response();
          ResponseCodeEnum responseEnum = ResponseCodeEnum.ERROR_OCCURRED;
          if (var10.getCause() instanceof SocketException) {
             responseEnum = ResponseCodeEnum.SERVICE_UNAVAILABLE;
          }
 
          response.setMessage(responseEnum.getMessage());
          return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Helper.MAPPER.writeValueAsString(response));
       } catch (Exception var11) {
          log.error("ERROR OCCURRED ON CALLING SERVICE:::", var11);
          return var11.getMessage() != null ? ResponseEntity.badRequest().body(var11.getMessage()) : null;
       }
    }
 
    public ResponseEntity<String> exchange(String urlToCall, HttpEntity<Object> entity, HttpMethod httpMethod) throws JsonProcessingException {
       try {
          log.info("CALLING URL:::" + urlToCall);
          log.info("REQUEST BODY:::" + entity.getBody());
          HttpMethod exchangeMethod = httpMethod != null ? httpMethod : HttpMethod.POST;
          ResponseEntity<String> response = this.getRestTemplate().exchange(urlToCall, exchangeMethod, entity, String.class, new Object[0]);
          log.info("RESPONSE FROM SERVICE:::" + response);
          return response;
       } catch (HttpClientErrorException var7) {
          log.error("SERVICE RETURNED CLIENT ERROR :::" + var7.getStatusCode());
          log.error("SERVICE RETURNED CLIENT BODY :::" + var7.getResponseBodyAsString());
          return var7.getResponseBodyAsString() != null ? ResponseEntity.badRequest().body(var7.getResponseBodyAsString()) : null;
       } catch (HttpServerErrorException var8) {
          log.error("SERVICE RETURNED SERVER ERROR :::" + var8.getStatusCode());
          log.error("SERVICE RETURNED SERVER BODY :::" + var8.getResponseBodyAsString());
          return var8.getResponseBodyAsString() != null ? ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(var8.getResponseBodyAsString()) : null;
       } catch (ResourceAccessException var9) {
          log.info("========= SERVICE ERROR ======== CONNECTION TIME OUT : " + var9.getMessage());
          Response response = new Response();
          ResponseCodeEnum responseEnum = ResponseCodeEnum.ERROR_OCCURRED;
          if (var9.getCause() instanceof SocketException) {
             responseEnum = ResponseCodeEnum.SERVICE_UNAVAILABLE;
          }
 
          response.setMessage(responseEnum.getMessage());
          return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Helper.MAPPER.writeValueAsString(response));
       } catch (Exception var10) {
          log.error("ERROR OCCURRED ON CALLING SERVICE:::", var10);
          return var10.getMessage() != null ? ResponseEntity.badRequest().body(var10.getMessage()) : null;
       }
    }
 }
 