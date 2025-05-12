package com.kedu.home.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;


@Configuration
public class GCSConfig {
	
	@Value("${gcs.key.location}")
	private String gcsKeyLocation;
	
	@Bean
	public Storage getStorage() throws IOException{
		try(InputStream is = new ClassPathResource(gcsKeyLocation).getInputStream();){
			GoogleCredentials key = GoogleCredentials.fromStream(is);
	         return StorageOptions.newBuilder().setCredentials(key).build().getService();
		}

	}
}
