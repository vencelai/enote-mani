package com.broton.enote.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ExternalIP {
	
	public static String ipAddress;
	
	@Value("${erp.api.vip}")
	private static String erpApiVip;
	
	@PostConstruct
	public static void init() {
		/*
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
				String ip = in.readLine();
				ipAddress = ip.trim();
				log.info("取得對外 ip: {}", ipAddress);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}			
		} catch (Exception e) {
			ipAddress = erpApiVip;
			log.error("取得外部 ip 失敗 {}", e.toString());
		}
		*/
	}

	@Scheduled(cron = "0 0 0/2 * * ?")
    public void testOne() {
        //每2個小時執行一次重取外部 ip
        //init();
    }
}
