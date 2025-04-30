package com.kedu.home.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

@Service
public class FileService {
	
	@Autowired
	private Storage storage;
	
	@Value("${gcs.bucket.name}")
	private String bucketName;

	public void upload(MultipartFile file) throws Exception{
		String sysName = UUID.randomUUID() + " _ "+file.getOriginalFilename();
		
		BlobId blobId = BlobId.of(bucketName, sysName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
				
		storage.create(blobInfo, file.getBytes());
	}

	public List<String> getListfiles() throws Exception {
		//DB에서 목록을 불러와야 하지만, GCS에서 직접 파일목록을 가지고 오는 예제
		List<String> listfiles = new ArrayList<>();
		for(Blob blob : storage.list(bucketName).iterateAll()){
			listfiles.add(blob.getName());
		}
		return listfiles;
	}

	public byte[] download(String fileName) throws Exception{
		BlobId blobId = BlobId.of(bucketName, fileName);
		Blob target = storage.get(blobId);
		return target.getContent();
	}

	public boolean delete(String fileName) {
		BlobId blobId = BlobId.of(bucketName, fileName);
		boolean result = storage.delete(blobId);
		return result;
	}
}
