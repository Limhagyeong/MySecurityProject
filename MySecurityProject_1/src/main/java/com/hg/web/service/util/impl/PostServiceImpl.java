package com.hg.web.service.util.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hg.web.common.exception.BadRequestException;
import com.hg.web.dto.api.ResponseDTO;
import com.hg.web.dto.util.PostInsertDTO;
import com.hg.web.dto.util.PostSelectDTO;
import com.hg.web.mapper.util.PostMapper;
import com.hg.web.service.util.PostService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
// 클라이언트로부터 받은 이미지를 버킷에 올림
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
	
	private final AmazonS3 amazonS3;
	private final PostMapper postingmapper;
	
	private static final long MAX_FILE_SIZE = 1 * 1024 * 1024; // 1MB (테스트용)
	
	@Value("${s3.bucket}")
	private String bucket;
	

	// 게시물 작성
	@Override
	@Transactional
	public ResponseEntity<ResponseDTO<Void>> insertPosting(PostInsertDTO postDTO) {
		try{
			postingmapper.insertContent(postDTO); // content DB 저장
		}catch(Exception e) {
			throw new RuntimeException("DB 저장 실패: " + e.getMessage()); // 롤백
		}

		return this.insertImgUrl(postDTO); // S3 이미지 업로드
	}
	
	// S3에 이미지 업로드
	@Override
	public ResponseEntity<ResponseDTO<Void>> insertImgUrl(PostInsertDTO postDTO){
	    
		try {
	        return this.uploadImageToS3(postDTO);
	    } catch (Exception e) {
	        throw new RuntimeException("이미지 업로드 실패: " + e.getMessage()); // 롤백
	    }
	    
	  }
	
	
	private ResponseEntity<ResponseDTO<Void>> uploadImageToS3(PostInsertDTO postDTO){
    	
    	String originalFilename=postDTO.getImg().getOriginalFilename();
		String extension=originalFilename.substring(originalFilename.lastIndexOf(".")+1); // 확장자 추출

		String s3FileName=UUID.randomUUID().toString().substring(0,10) + originalFilename; //  원본 파일명 유지 + 파일명 중복 방지
		
		 InputStream is = null; 
		 
		try {
			is = postDTO.getImg().getInputStream(); // InputStream => 파일의 데이터를 한줄씩 바이트 단위로 읽어들임 (데이터에 접근하는
																	// 방식)
			byte[] bytes = IOUtils.toByteArray(is); // 바이트 배열을 통해 데이터를 메모리에 저장 (바이트 길이는 파일 크기와 동일)
			
			if (bytes.length > MAX_FILE_SIZE) {
	            throw new BadRequestException("파일 크기가 5MB를 초과합니다."); // 사용자 정의 예외 처리
	        }
			
			ObjectMetadata metaData=new ObjectMetadata();
			metaData.setContentType("image/"+extension); // 파일 확장자
			metaData.setContentLength(bytes.length); // 파일 크기
			ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
			
			bucket=bucket.trim();
			PutObjectRequest putObjectRequest=new PutObjectRequest(bucket, s3FileName, byteArrayInputStream, metaData);
			amazonS3.putObject(putObjectRequest); // 이미지를 S3에 저장
			
			// 포스트 번호, 이미지 URL DB 저장
			postDTO.setP_num(postDTO.getP_num());
			postDTO.setP_img_url(amazonS3.getUrl(bucket, s3FileName).toString());
			postingmapper.insertImgUrl(postDTO);
		} catch (Exception e) {
			// S3에 업로드된 이미지 삭제
	        if (s3FileName!=null) {
	            amazonS3.deleteObject(bucket, s3FileName);
	        }
			throw new RuntimeException("S3 업로드 실패: " + e.getMessage()); // 롤백
		}finally {
			if (is != null) {
	            try {
	                is.close();
	            } catch (IOException e) {
	                throw new RuntimeException("InputStream 닫기 실패: " + e.getMessage());
	            }
	        }
		}
		return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<>(),HttpStatus.OK); //성공 		
	}

	// 게시물 리스트
	@Override
	public ResponseEntity<ResponseDTO<List<PostSelectDTO>>> selectPost(String username) {
		// TODO Auto-generated method stub
		
		List<PostSelectDTO> data=postingmapper.selectPost(username);
		
		return new ResponseEntity<ResponseDTO<List<PostSelectDTO>>> (new ResponseDTO<List<PostSelectDTO>>(data),HttpStatus.OK);
	}
	

	// 게시물 업데이트
	@Override
	public ResponseEntity<ResponseDTO<Void>> updatePosting(PostInsertDTO postDTO) {
		// TODO Auto-generated method stub
		try{
			postingmapper.updateContent(postDTO); // content 업데이트
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<>(),HttpStatus.OK); //성공 	
	}

	// 게시물 삭제
	@Override
	public ResponseEntity<ResponseDTO<Void>> deletePosting(int pNum) {
		// TODO Auto-generated method stub
		try {
			// S3 버킷 이미지 삭제
			String imgUrl=postingmapper.S3imgUrl(pNum);
			if(imgUrl!=null) {
				 String s3FileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
		         amazonS3.deleteObject(bucket, s3FileName);
			}
			// DB 게시물 삭제
			postingmapper.deleteContent(pNum);
			// DB 이미지 삭제
			postingmapper.deleteImg(pNum);
			
		}catch(Exception e) {
			throw new BadRequestException("게시물 삭제 실패");
		}
		return new ResponseEntity<ResponseDTO<Void>>(new ResponseDTO<>(), HttpStatus.OK); 
	}
}