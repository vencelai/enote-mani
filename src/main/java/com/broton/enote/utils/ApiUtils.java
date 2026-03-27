package com.broton.enote.utils;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.exception.ErrorCodeException;
import lombok.extern.log4j.Log4j2;

/**
 * @title API 工具
 * @description API 工具
 * @author RexKu
 */
@Log4j2
public class ApiUtils {

	private ApiUtils() {
	}

	public static <T> T getDate(RestTemplate restTemplate, String url, Class<T> responseClass) {
		log.info("get data from {} with get method", url);
		ResponseEntity<T> responseEntity = null;
		T dto = null;
		try {
			responseEntity = restTemplate.getForEntity(url, responseClass);
			dto = responseEntity.getBody();
			if (dto == null) {
				log.error("無回傳內容");
			}
		} catch (RestClientException e) {
			log.error("API utils occur exception:" + e.getMessage());
			throw new ErrorCodeException(ResultCode.ERR_4002.getCode(), ResultCode.ERR_4002.getDesc());
		} catch (Exception e) {
			log.error("API utils occur unexpected exception {}: {}", e.getClass(), e.getMessage(), e);
			throw new ErrorCodeException(ResultCode.ERR_4002.getCode(), ResultCode.ERR_4002.getDesc());
		}
		return dto;
	}

	/**
	 * @param <T>
	 * @param restTemplate
	 * @param url
	 * @param method
	 * @param bearerToken
	 * @param responseClass
	 * @return <T>
	 */
	public static <T> T exchange(RestTemplate restTemplate, String url, HttpMethod method, String bearerToken, Class<T> responseClass) {
		log.info("exchange from {} with get method", url);
		ResponseEntity<T> responseEntity = null;
		T dto = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			if (StringUtils.isNotBlank(bearerToken)) {
				headers.setBearerAuth(bearerToken);
			}
//			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
			responseEntity = restTemplate.exchange(url, method, requestEntity, responseClass);
			dto = responseEntity.getBody();
			if (dto == null) {
				log.error("無回傳內容");
			}
		} catch (RestClientException e) {
			log.error("API utils occur exception:" + e.getMessage());
			throw new ErrorCodeException(ResultCode.ERR_4002.getCode(), ResultCode.ERR_4002.getDesc() + ":API溝通發生錯誤");
		} catch (Exception e) {
			log.error("API utils occur unexpected exception {}: {}", e.getClass(), e.getMessage(), e);
			throw new ErrorCodeException(ResultCode.ERR_4002.getCode(), ResultCode.ERR_4002.getDesc());
		}
		return dto;
	}

	/**
	 * @param <T>
	 * @param restTemplate
	 * @param url
	 * @param method
	 * @param bearerToken
	 * @param responseClass
	 * @return <T>
	 */
	public static <T> T exchangeWithTypeRef(RestTemplate restTemplate, String url, HttpMethod method, Object data, String bearerToken, ParameterizedTypeReference<T> responseClass) {
		log.info("exchange from {} with get method", url);
		ResponseEntity<T> responseEntity = null;
		T dto = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			if (StringUtils.isNotBlank(bearerToken)) {
				headers.setBearerAuth(bearerToken);
			}
			HttpEntity<String> requestEntity = new HttpEntity<>(data.toString(), headers);
			responseEntity = restTemplate.exchange(url, method, requestEntity, responseClass);
			dto = responseEntity.getBody();
			if (dto == null) {
				log.error("無回傳內容");
			}
		} catch (RestClientException e) {
			log.error("API utils occur exception:" + e.getMessage());
			throw new ErrorCodeException(ResultCode.ERR_4002.getCode(), ResultCode.ERR_4002.getDesc() + ":API溝通發生錯誤");
		} catch (Exception e) {
			log.error("API utils occur unexpected exception {}: {}", e.getClass(), e.getMessage(), e);
			throw new ErrorCodeException(ResultCode.ERR_4002.getCode(), ResultCode.ERR_4002.getDesc());
		}
		return dto;
	}

	/**
	 * @param <T>
	 * @param restTemplate
	 * @param url
	 * @param params
	 * @param responseClass
	 * @param bearerToken   選填
	 * @return <T>
	 */
	public static <T> T postDate(RestTemplate restTemplate, String url, Map<String, ?> params, String bearerToken, Class<T> responseClass) {
		log.info("post data from {} with get method", url);
		ResponseEntity<T> responseEntity = null;
		T dto = null;
		//try {
			HttpHeaders headers = new HttpHeaders();
			// 有需要token才放進來
			if (StringUtils.isNotBlank(bearerToken)) {
				log.debug("have token = {}", bearerToken);
				headers.setBearerAuth(bearerToken);
			}
			headers.setContentType(MediaType.APPLICATION_JSON);
//			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			log.debug("params = {}", params);
			JSONObject json = new JSONObject(params);

			HttpEntity<String> request = new HttpEntity<String>(json.toString(), headers);
			responseEntity = restTemplate.postForEntity(url, request, responseClass);
			dto = responseEntity.getBody();
			if (dto == null) {
				log.error("無回傳內容");
			}
		/*	
		} catch (RestClientException e) {
			log.error("API utils occur exception:" + e.getMessage());
			throw new ErrorCodeException(ResultCode.ERR_4002.getCode(), ResultCode.ERR_4002.getDesc() + ":API溝通發生錯誤");
		} catch (Exception e) {
			log.error("API utils occur unexpected exception {}: {}", e.getClass(), e.getMessage(), e);
			throw new ErrorCodeException(ResultCode.ERR_4002.getCode(), ResultCode.ERR_4002.getDesc());
		}
		*/
		return dto;
	}
	
	/**
	 * @param <T>
	 * @param restTemplate
	 * @param url
	 * @param params
	 * @param responseClass
	 * @param bearerToken   選填
	 * @return <T>
	 */
	public static <T> T getData(RestTemplate restTemplate, String url, Map<String, ?> params, String bearerToken, Class<T> responseClass) {
		log.info("get data from {} with get method", url);
		ResponseEntity<T> responseEntity = null;
		T dto = null;
			HttpHeaders headers = new HttpHeaders();
			// 有需要token才放進來
			if (StringUtils.isNotBlank(bearerToken)) {
				log.debug("have token = {}", bearerToken);
				headers.setBearerAuth(bearerToken);
			}
			headers.setContentType(MediaType.APPLICATION_JSON);
//			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			log.debug("params = {}", params);
			JSONObject json = new JSONObject(params);

			HttpEntity<String> request = new HttpEntity<String>(json.toString(), headers);
			responseEntity = (ResponseEntity<T>) restTemplate.exchange(
					url,
					HttpMethod.GET,
					request,
					responseClass
					);
			dto = responseEntity.getBody();
			if (dto == null) {
				log.error("無回傳內容");
			}
		return dto;
	}

}
