package com.broton.enote.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import lombok.extern.log4j.Log4j2;

/**
 * @title
 * @description
 * @author ivanchen
 */
@Log4j2
class CommonUtilTest {

	/**
	 * Test method for
	 * {@link com.broton.ktvConsumer.utils.CommonUtil#encrypt(String, String)}
	 */
	@Test
	void testEncrypt() {
		String key = "broton";
		String encSrc = "admin";
		String encVal = CommonUtil.encrypt(encSrc, key);
		log.debug("加密後: {}", encVal);
	}

	/**
	 * Test method for
	 * {@link com.broton.ktvConsumer.utils.CommonUtil#decrypt(String, String)}
	 */
	@Test
	void testDecrypt() {
		String key = "broton";
		String encVal = "AD304D37D5DEEC4ECFD0DAE240C2C534";
		String decVal = CommonUtil.decrypt(encVal, key);
		log.debug("解密後: {}", decVal);
		assertEquals("admin", decVal);
	}

	/**
	 * Test method for
	 * {@link com.broton.ktvConsumer.utils.CommonUtil#generateUUID()}
	 */
	@Test
	void testGenerateUUID() {
		String uuid = CommonUtil.generateUUID();
		log.debug("uuid = {}", uuid);
		assertEquals(false, StringUtils.isBlank(uuid));
	}

}
