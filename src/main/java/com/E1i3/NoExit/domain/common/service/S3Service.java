package com.E1i3.NoExit.domain.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	private S3Client s3Client;

	public S3Service(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	// 단일 이미지 파일 업로드
	public String uploadFile(MultipartFile imgFile, String folder) {
		// 파일이 비어있는지 확인해줘야하나?
		if (imgFile == null) {
			throw new NoSuchElementException("프로필 이미지를 등록하십시오.");
		}

		// 저장한 새로운 이름 생성
		String fileName = createFileName(imgFile.getOriginalFilename());
		String fileUrl = null;
		// S3에 저장하고 저장된 url 리턴해서 데이터베이스에 저장
		try{
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(folder + "/" + fileName)
				.contentType(imgFile.getContentType())
				.contentLength(imgFile.getSize())
				.build();
			s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(imgFile.getInputStream(), imgFile.getSize()));

			fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(folder + "/" + fileName)).toExternalForm();
		}catch(IOException e){
			throw new RuntimeException("이미지 저장 실패");
		}
		return fileUrl;
	}


	// 다중 이미지 파일 업로드
	public List<String> uploadMultiFile(List<MultipartFile> multipartFile) {
		List<String> imgUrlList = new ArrayList<>();

		for (MultipartFile file : multipartFile) {
			// 리스트에 존재하는 파일 하나씩 fileNameList에 추가
			String uuidFileName = createFileName(file.getOriginalFilename());

			// s3에 저장
			try(InputStream inputStream = file.getInputStream()) {
				PutObjectRequest putObjectRequest = PutObjectRequest.builder()
					.bucket(bucketName)
					.key(uuidFileName)
					.contentType(file.getContentType())
					.contentLength(file.getSize())
					.build();
				s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
				imgUrlList.add(s3Client.utilities().getUrl(a -> a.bucket(bucketName).key(uuidFileName)).toExternalForm());
			} catch(IOException e) {
				throw new RuntimeException("이미지 저장 실패");
			}
		}
		return imgUrlList;
	}

	// 이미지파일명 중복 방지 (uuid를 이용한 파일명 생성)
	private String createFileName(String fileName) {
		return UUID.randomUUID().toString().concat(getFileExtension(fileName));
	}

	// 파일명 유효성 검사 (확장자 검사)
	private String getFileExtension(String fileName) {
		if (fileName.isEmpty()) {
			throw new IllegalArgumentException("파일 이름이 너무 짧습니다.");
		}
		ArrayList<String> fileValidate = new ArrayList<>();
		fileValidate.add(".jpg");
		fileValidate.add(".jpeg");
		fileValidate.add(".png");
		fileValidate.add(".JPG");
		fileValidate.add(".JPEG");
		fileValidate.add(".PNG");
		String idxFileName = fileName.substring(fileName.lastIndexOf("."));
		if (!fileValidate.contains(idxFileName)) {
			throw new IllegalArgumentException("파일 확장자를 확인해주세요");
		}
		return fileName.substring(fileName.lastIndexOf("."));
	}

}
