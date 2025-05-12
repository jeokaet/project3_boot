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
<<<<<<< HEAD
=======
import com.kedu.home.dao.FileDAO;
import com.kedu.home.dto.AddRegionDTO;
import com.kedu.home.dto.FileDTO;

>>>>>>> 6958f9dcb20c4f3476f675ab993ca8d6bb302960

@Service
public class FileService {
	
	@Autowired
	private Storage storage;
	
<<<<<<< HEAD
	@Value("${gcs.bucket.name}")
	private String bucketName;

	public void upload(MultipartFile file) throws Exception{
=======
	@Autowired
	private FileDAO fileDao;
	
	@Value("${gcs.bucket.name}")
	private String bucketName;

	public int upload(AddRegionDTO dto) throws Exception{
		MultipartFile file = dto.getFile();
		System.out.println("파일 이름 : " + file.getOriginalFilename());

>>>>>>> 6958f9dcb20c4f3476f675ab993ca8d6bb302960
		String sysName = UUID.randomUUID() + " _ "+file.getOriginalFilename();
		
		BlobId blobId = BlobId.of(bucketName, sysName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
				
		storage.create(blobInfo, file.getBytes());
<<<<<<< HEAD
=======

		
		String filePath = String.format("https://storage.googleapis.com/%s/%s", bucketName, sysName);

		
		FileDTO fileDto = new FileDTO();
		fileDto.setSysname(sysName);
		fileDto.setOriname(file.getOriginalFilename());
		fileDto.setFilePath(filePath);
		
		int imageId = fileDao.saveImage(fileDto);
		
		return imageId;
		
>>>>>>> 6958f9dcb20c4f3476f675ab993ca8d6bb302960
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
