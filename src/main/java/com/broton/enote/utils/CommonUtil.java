package com.broton.enote.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import lombok.extern.log4j.Log4j2;

/**
 * 公用函式庫
 * 
 * @author vencelai
 * @date 2021年1月14日 下午6:02:27
 * 
 */
// 解決 HttpDelete 沒有 setEntity 的問題
class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
	public static final String METHOD_NAME = "DELETE";

	public String getMethod() {
		return METHOD_NAME;
	}

	public HttpDeleteWithBody(final String uri) {
		super();
		setURI(URI.create(uri));
	}

	public HttpDeleteWithBody(final URI uri) {
		super();
		setURI(uri);
	}

	public HttpDeleteWithBody() {
		super();
	}
}

@Log4j2
public class CommonUtil {
	private final static String secretKey = "bttech";
	private final static String salt = "centerzxcv";

	/**
	 * MD5加密
	 * 
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String MD5Encoding(String str) throws NoSuchAlgorithmException {
		String rval = "";

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		try {
			md5.update((str).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte b[] = md5.digest();

		int i;
		StringBuffer buf = new StringBuffer("");

		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0) {
				i += 256;
			}
			if (i < 16) {
				buf.append("0");
			}
			buf.append(Integer.toHexString(i));
		}

		rval = buf.toString();
		return rval;
	}

	/**
	 * 取得目前的 timestamp
	 * 
	 * @return
	 */
	public static String getCurrentTimestamp() {
		Date date = new Date();
		long time = date.getTime();
		return Long.toString(time);
	}

	/**
	 * 刪除檔案
	 * 
	 * @param delFile
	 * @return
	 */
	public static boolean deleteFile(String delFile) {
		boolean bool = false;
		try {
			File file = new File(delFile);
			if (file.delete()) {
				bool = true;
			}
		} catch (Exception e) {
			//
		}

		return bool;
	}

	public static String getMd5(String str) {
		MessageDigest mdInst = null;
		try {
			mdInst = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		mdInst.update(str.getBytes());// 使用指定的字節更新摘要
		byte[] md = mdInst.digest();// 獲得密文
		return byteArrToHexStr(md);
	}

	public static String byteArrToHexStr(byte[] arr) {
		int iLen = arr.length;
		// 每個byte(8位)用兩個(16進制)字符才能表示，所以字符串的長度是數組長度的兩倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arr[i];
			while (intTmp < 0) {// 把負數轉換为正數
				intTmp = intTmp + 256;
			}
			if (intTmp < 16) {// 小於0F的數需要在前面補0
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}

		return sb.toString();
	}

	/**
	 * post請求（用於請求json格式的參數）
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String doPost(String url, String params) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
//		CloseableHttpClient httpclient = createAcceptSelfSignedCertificateClient();

		HttpPost httpPost = new HttpPost(url);// 創建httpPost
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/json");
		String charSet = "UTF-8";
		StringEntity entity = new StringEntity(params, charSet);
		httpPost.setEntity(entity);
		CloseableHttpResponse response = null;
		String jsonString = "";
		try {
			response = httpclient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			jsonString = EntityUtils.toString(responseEntity);
		} catch (IOException e) {
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					log.error(e);
					e.printStackTrace();
				}
			}
			try {
				httpclient.close();
			} catch (IOException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		return jsonString;
	}

	/**
	 * delete請求（用於請求json格式的參數）
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String doDelete(String url, String params) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);

		httpDelete.setHeader("Accept", "application/json");
		httpDelete.setHeader("Content-Type", "application/json");
		String charSet = "UTF-8";
		StringEntity entity = new StringEntity(params, charSet);
		httpDelete.setEntity(entity);
		CloseableHttpResponse response = null;
		String jsonString = "";
		try {
			response = httpclient.execute(httpDelete);
			HttpEntity responseEntity = response.getEntity();
			jsonString = EntityUtils.toString(responseEntity);
		} catch (IOException e) {
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					log.error(e);
					e.printStackTrace();
				}
			}
			try {
				httpclient.close();
			} catch (IOException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		return jsonString;
	}

	public static String getCookieUid(HttpServletRequest req) {
		/*
		 * Cookie[] cookies = req.getCookies(); String cookieval = ""; String rval = "";
		 * if (cookies != null) { for (Cookie cookie : cookies) { if
		 * (cookie.getName().equals("ck_btcs")) { cookieval = cookie.getValue(); //
		 * 當delimiter用到特殊字元時，如”.”, “|”, “$”，此時要在特殊字元前面加上”\\”，才會得到正確的結果。 String decval =
		 * decrypt(cookieval, "BrotonSecurity"); String[] tokens =
		 * decval.split("#13\\$"); if (tokens[1].equals("broton")) { rval = tokens[0]; }
		 * } } } return rval;
		 */

		String rval = "";
		HttpSession session = req.getSession();
		rval = (String) session.getAttribute("btcs");
		return rval;
	}

	/**
	 * 字串 base64 加密
	 * 
	 * @param srcString
	 * @return
	 */
	public static String base64Encode(String srcString) {
		String encodedString = "";
		Encoder encoder = Base64.getEncoder();
		byte[] data;
		try {
			data = srcString.getBytes("UTF-8");
			encodedString = encoder.encodeToString(data);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodedString;
	}

	/**
	 * 字串 base64 解密
	 * 
	 * @param srcString
	 * @return
	 */
	public static String base64Decode(String srcString) {
		Decoder decoder = Base64.getDecoder();
		byte[] bytes = decoder.decode(srcString);
		String decodedString = "";
		try {
			decodedString = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decodedString;
	}

	/**
	 * 加密
	 * 
	 * @param content待加密內容
	 * @param key          加密的金鑰
	 * @return
	 */
	public static String encrypt(String content, String key) {
		log.debug("secretKey = {}", secretKey);
		log.debug("salt = {}", salt);

		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");// 構造金鑰生成器，指定為AES演算法，不區分大小寫
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getBytes());
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);// ENCRYPT_MODE指加密操作
			byte[] byteRresult = cipher.doFinal(byteContent);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteRresult.length; i++) {
				String hex = Integer.toHexString(byteRresult[i] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
				sb.append(hex.toUpperCase());
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content 待解密內容
	 * @param key     解密的金鑰
	 * @return
	 */
	public static String decrypt(String content, String key) {
		if (content.length() < 1)
			return null;
		byte[] byteRresult = new byte[content.length() / 2];
		for (int i = 0; i < content.length() / 2; i++) {
			int high = Integer.parseInt(content.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(content.substring(i * 2 + 1, i * 2 + 2), 16);
			byteRresult[i] = (byte) (high * 16 + low);
		}
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getBytes());
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);// Decrypt_mode指解密操作
			byte[] result = cipher.doFinal(byteRresult);
			return new String(result, "utf-8");// 不加utf-8，中文時會亂碼
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String doGet(String url, String params) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);// 創建httpPost
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-Type", "application/json");
		String charSet = "UTF-8";
		StringEntity entity = new StringEntity(params, charSet);
		log.debug("entity = {}", entity);
		CloseableHttpResponse response = null;
		String jsonString = "";
		try {
			response = httpclient.execute(httpGet);
			HttpEntity responseEntity = response.getEntity();
			jsonString = EntityUtils.toString(responseEntity);
		} catch (IOException e) {
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					log.error(e);
					e.printStackTrace();
				}
			}
			try {
				httpclient.close();
			} catch (IOException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		return jsonString;
	}

	// Ignore Certificate Errors 忽略證書錯誤
//	private static CloseableHttpClient createAcceptSelfSignedCertificateClient() {
//		SSLContext sslContext;
//		SSLConnectionSocketFactory connectionFactory = null;
//		try {
//			sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
//			HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
//			connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);
//		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
//			log.debug(e);
//		}
//
//		return HttpClients.custom().setSSLSocketFactory(connectionFactory).build();
//	}

	/**
	 * 列出起訖日期間內的所有日期
	 * 
	 * @param startDate
	 * @param endDate
	 * @return 返回實際的工作日天數 List
	 */
	public static List<Date> getDatesBetweenUsingJava7(Date startDate, Date endDate) {
		List<Date> datesInRange = new ArrayList<>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);

		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);
		endCalendar.set(Calendar.SECOND, 1);

		while (calendar.before(endCalendar)) {
			Date result = calendar.getTime();
			datesInRange.add(result);
			calendar.add(Calendar.DATE, 1);
		}
		return datesInRange;
	}

	/**
	 * 通過時間秒毫秒數判斷兩個時間的相隔天數
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int differentDaysByMillisecond(Date date1, Date date2) {
		int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
		return days;
	}

	/**
	 * 發送 HTTP POST (编码为 UTF-8)，Content-Type="x-www-form-urlencoded"。
	 * 
	 * @param url
	 * @param mapdata
	 * @return
	 */
	public static String sendXwwwForm(String url, Map<String, String> mapdata) {
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.addHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			if (mapdata.size() != 0) {
				Set<String> keySet = mapdata.keySet();
				Iterator<String> it = keySet.iterator();
				while (it.hasNext()) {
					String k = it.next().toString();// key
					String v = mapdata.get(k);// value
					nameValuePairs.add(new BasicNameValuePair(k, v));
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				String content = EntityUtils.toString(entity, "UTF-8");
				return content;
			}
		} catch (Exception e) {
			log.error(e);
		}
		return "取得返回值失敗";
	}

	/**
	 * 四拾五入取至指定的小數位數
	 * 
	 * @param value
	 * @param places
	 * @return
	 */
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	/**
	 * 取二日期相差的時數(小時)
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static double getTimeBetweenHours(String startDate, String endDate) {
		double d = 0;
		if (null != startDate && null != endDate) {
			try {
//    			log.debug(startDate + "," + endDate);
				Date sDate = new Date(startDate);
				Date eDate = new Date(endDate);
				long l = eDate.getTime() - sDate.getTime();
				long day = l / (24 * 60 * 60 * 1000);
				long hour = (l / (60 * 60 * 1000) - day * 24);
				d = round((double) hour, 2);
			} catch (IllegalArgumentException e) {
				// log.error(e);
			}
		}
		return d;
	}

	/**
	 * 取得指定日期 星期幾
	 * 
	 * @param dt
	 * @return
	 */
	public static String getWeekOfDate(Date dt) {
		try {
			String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (w < 0)
				w = 0;
			return weekDays[w];
		} catch (ArrayIndexOutOfBoundsException e) {
			return "none";
		}
	}

	/**
	 * 取得 UUID
	 * 
	 * @return UUID 字串
	 */
	public static String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	/**台灣手機格式檢查
	 * @param mobileNum
	 * @return boolean
	 */
	public static boolean isValidMobileNum(String mobileNum) {
		boolean bool = false; 
		mobileNum = mobileNum.trim();
		Pattern pattern = Pattern.compile("^09\\d{2}-?\\d{3}-?\\d{3}$");
		Matcher matcher = pattern.matcher(mobileNum);
		bool = matcher.matches();
		return bool;
	}
	
	/**手機號碼遮蔽 ex: 0933123456 --> 0933***456
	 * @param mobileNum
	 * @return
	 */
	public static String maskMobileNum(String mobileNum) {
		String returnVal = "";
		if (null != mobileNum && mobileNum.length() == 10) {
			returnVal = mobileNum.replaceAll("\\b(\\d{4})\\d+(\\d{3})", "$1***$2");
		}
		return returnVal;
	}
	
	/**身份證/居留證號碼遮蔽 ex: A123456789 --> A1*****789
	 * @param idNum
	 * @return
	 */
	public static String maskIDNum(String idNum) {
		String returnVal = "";
		if (null != idNum && idNum.length() == 10) {
			returnVal = idNum.replaceAll("\\b(\\S{2})\\S+(\\S{3})", "$1*****$2");
		}
		return returnVal;
	}
	
	public static String getSHA1String(String[] array , String apiKey){
		StringBuffer hexstr = new StringBuffer();
		try {
			StringBuffer sb = new StringBuffer();
			// 字符串排序
			Arrays.sort(array);
			for (int i = 0; i < array.length; i++) {
				String str = array[i];
				//	if(StringUtils.isNotBlank(StringUtils.substring(str, StringUtils.lastIndexOf(str, "=")+1))){
				sb.append(str).append("&");
				//	}
			}
			
			if(StringUtils.isNotBlank(apiKey)){
				sb.append("key=").append(apiKey);
			}else{
				sb.deleteCharAt(sb.length()-1);
			}
			
			String str = sb.toString();
			// SHA1签名生成
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());
			byte[] digest = md.digest();

			String shaHex = "";
			for (int i = 0; i < digest.length; i++) {
				shaHex = Integer.toHexString(digest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexstr.append(0);
				}
				hexstr.append(shaHex);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			hexstr=null;
		}
		
		return hexstr.toString();
	}
	
	/**亂數產出6位數字的簡訊驗證碼
     * @return
     */
    public static String generateSmsAuthCode() {
    	int returnVal = (int)((Math.random()*9+1)*100000);
    	return Integer.toString(returnVal);
    }
    
    /**手機號碼加上國家碼 0933772450 -> 886933772450
     * @param phone
     * @return
     */
    public static String phoneAddCounrty(String phone) {
		String countryPhone = "886" + phone.substring(1, phone.length());
		return countryPhone;
    }
    
    /**亂數產出指定位數的英數混合字串
     * @param strLen
     * @return
     */
    public static String generatorNewPassword(int strLen) {
		String outStr = "";
		int num = 0;
		while (outStr.length() < strLen) {
			num = (int) (Math.random() * (90 - 50 + 1)) + 50; // 亂數取編號為 50~90 的字符 (排除 0 和 1)
			if (num > 57 && num < 65)
				continue; // 排除非數字非字母
			else if (num == 73 || num == 76 || num == 79)
				continue; // 排除 I、L、O
			outStr += (char) num;
		}
		return outStr.toLowerCase();
	}
    
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
    	if (null == map) {
    		return null;
    	}
    	Object obj = beanClass.newInstance();
    	Field[] fields = obj.getClass().getDeclaredFields();
    	for (Field field : fields) {
    		int mod = field.getModifiers();
    		if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
    			continue;
    		}
    		field.setAccessible(true);
    		field.set(obj, map.get(field.getName()));
    	}
    	return obj;
    }
    
    public static Map<String, Object> objectToMap(Object obj) throws IllegalArgumentException, IllegalAccessException {
    	if (null == obj) {
    		return null;
    	}
    	Map<String, Object> map = new HashMap<String, Object>();
    	Field[] declaredFields = obj.getClass().getDeclaredFields();
    	for (Field field : declaredFields) {
    		field.setAccessible(true);
    		map.put(field.getName(), field.get(obj));
    	}
    	return map;
    }
    
    /**身分證字號與居留證(統一證)編號檢核程式
	 * @param str
	 * @return boolean
	 */
	public static boolean isValidIDorRCNumber(String str) {
	    if (str == null || "".equals(str)) {
	        return false;
	    }
	    final char[] pidCharArray = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	    // 原身分證英文字應轉換為10~33，這裡直接作個位數*9+10
	    final int[] pidIDInt = { 1, 10, 19, 28, 37, 46, 55, 64, 39, 73, 82, 2, 11, 20, 48, 29, 38, 47, 56, 65, 74, 83, 21, 3, 12, 30 };
	    // 原居留證第一碼英文字應轉換為10~33，十位數*1，個位數*9，這裡直接作[(十位數*1) mod 10] + [(個位數*9) mod 10]
	    final int[] pidResidentFirstInt = { 1, 10, 9, 8, 7, 6, 5, 4, 9, 3, 2, 2, 11, 10, 8, 9, 8, 7, 6, 5, 4, 3, 11, 3, 12, 10 };
	    // 原居留證第二碼英文字應轉換為10~33，並僅取個位數*8，這裡直接取[(個位數*8) mod 10]
	    final int[] pidResidentSecondInt = {0, 8, 6, 4, 2, 0, 8, 6, 2, 4, 2, 0, 8, 6, 0, 4, 2, 0, 8, 6, 4, 2, 6, 0, 8, 4};

	    str = str.toUpperCase();// 轉換大寫
	    final char[] strArr = str.toCharArray();// 字串轉成char陣列
	    int verifyNum = 0;

	    /* 檢查身分證字號 */
	    if (str.matches("[A-Z]{1}[1-2]{1}[0-9]{8}")) {
	        // 第一碼
	        verifyNum = verifyNum + pidIDInt[Arrays.binarySearch(pidCharArray, strArr[0])];
	        // 第二~九碼
	        for (int i = 1, j = 8; i < 9; i++, j--) {
	            verifyNum += Character.digit(strArr[i], 10) * j;
	        }
	        // 檢查碼
	        verifyNum = (10 - (verifyNum % 10)) % 10;

	        return verifyNum == Character.digit(strArr[9], 10);
	    }

	    /* 檢查統一證(居留證)編號 (2031/1/1停用) */
	    verifyNum = 0;
	    if (str.matches("[A-Z]{1}[A-D]{1}[0-9]{8}")) {
	        // 第一碼
	        verifyNum += pidResidentFirstInt[Arrays.binarySearch(pidCharArray, strArr[0])];
	        // 第二碼
	        verifyNum += pidResidentSecondInt[Arrays.binarySearch(pidCharArray, strArr[1])];
	        // 第三~八碼
	        for (int i = 2, j = 7; i < 9; i++, j--) {
	            verifyNum += Character.digit(strArr[i], 10) * j;
	        }
	        // 檢查碼
	        verifyNum = (10 - (verifyNum % 10)) % 10;

	        return verifyNum == Character.digit(strArr[9], 10);
	    }

	    /* 檢查統一證(居留證)編號 (2021/1/2實施) */
	    verifyNum = 0;
	    if (str.matches("[A-Z]{1}[89]{1}[0-9]{8}")) {
	        // 第一碼
	        verifyNum += pidResidentFirstInt[Arrays.binarySearch(pidCharArray, strArr[0])];
	        // 第二~八碼
	        for (int i = 1, j = 8; i < 9; i++, j--) {
	            verifyNum += Character.digit(strArr[i], 10) * j;
	        }
	        // 檢查碼
	        verifyNum = (10 - (verifyNum % 10)) % 10;

	        return verifyNum == Character.digit(strArr[9], 10);
	    }
	    
	    return false;
	}
	
	/**自訂義的 copyProperties, 非 null 的才 copy(僅更新欲異動的欄位)
     * @param src
     * @param target
     */
    public static void customizeCopyProperties(Object src, Object target) {
	    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	}
    
    public static String[] getNullPropertyNames (Object source) {
	    final BeanWrapper src = new BeanWrapperImpl(source);
	    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

	    Set<String> emptyNames = new HashSet<String>();
	    for (java.beans.PropertyDescriptor pd : pds) {
	        Object srcValue = src.getPropertyValue(pd.getName());
	        if (srcValue == null) emptyNames.add(pd.getName());
	    }

	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}
    
    /**取得當前的網路 IP
     * @param request 
     * @return 
     */ 
    public static String getIpAddr(HttpServletRequest request) { 
    	String ipAddress = request.getHeader("x-forwarded-for"); 
    	if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) { 
    		ipAddress = request.getHeader("Proxy-Client-IP"); 
    	} 
    	if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) { 
    		ipAddress = request.getHeader("WL-Proxy-Client-IP"); 
    	} 
    	if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) { 
    		ipAddress = request.getRemoteAddr(); 
    		if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) { 
    			//根據網卡取本機配置的IP 
    			InetAddress inet = null; 
    			try { 
    				inet = InetAddress.getLocalHost(); 
    			} catch (UnknownHostException e) { 
    				e.printStackTrace(); 
    			} 
    			ipAddress= inet.getHostAddress(); 
    		} 
    	} 
    	//對於通過多個代理的情況，第一個IP為客戶端真實IP,多個IP按照','分割 
    	if (ipAddress!=null && ipAddress.length() > 15) { 
    		//"***.***.***.***".length() = 15 
    		if (ipAddress.indexOf(",") > 0) { 
    			ipAddress = ipAddress.substring(0, ipAddress.indexOf(",")); 
    		} 
    	} 
    	return ipAddress; 
    }
}
