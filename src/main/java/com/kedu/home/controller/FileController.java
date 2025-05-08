//package com.kedu.home.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.kedu.home.services.FileService;
//
//@RestController
//@RequestMapping("/gcs")
//public class FileController {
//	
//	@Autowired
//	private FileService fServ;
//
//	@PostMapping("/up")
//	public ResponseEntity<Void> upload(MultipartFile file) throws Exception {
//		System.out.println(file.getOriginalFilename());
//		//GCS 파일 업로드
//		fServ.upload(file);
//
//		//DB에 파일정보 저장하는 코드
//		return ResponseEntity.ok().build();
//	}
//	@GetMapping("/list")
//	public ResponseEntity<List<String>> getListfiles() throws Exception{
//		System.out.println("Connection Complete");
//		return ResponseEntity.ok(fServ.getListfiles());
//		
//	}
//	@GetMapping("/down/{fileName}")
//	public ResponseEntity<byte[]> download(@PathVariable String fileName) throws Exception{
//		System.out.println("download Start");
//		String oriName = fileName.split("_")[1];
//		oriName = new String(oriName.getBytes(),"ISO-8859-1");
//		byte[] fileContents = fServ.download(fileName);
//		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+oriName+"\"")
//                .body(fileContents);
//	}
//	@DeleteMapping("/del/{fileName}")
//	public ResponseEntity<Void> delete(@PathVariable String fileName) throws Exception{
//		System.out.println("Delete protocol");
//		fServ.delete(fileName);
//		return ResponseEntity.ok().build();
//	}
//}
