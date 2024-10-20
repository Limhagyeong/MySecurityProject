package com.hg.web.service.post.impl;

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
import com.hg.web.dto.post.ImgDTO;
import com.hg.web.dto.post.PostInsertDTO;
import com.hg.web.dto.post.PostSelectDTO;
import com.hg.web.mapper.post.PostMapper;
import com.hg.web.service.post.PostService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
	
	private final AmazonS3 amazonS3;
	private final PostMapper postingmapper;
	
	private static final long MAX_FILE_SIZE = 1 * 1024 * 1024; // 1MB (테스트용)
	
	@Value("${s3.bucket}")
	private String bucket;
	

	// 게시물 삽입, 수정
	@Override
	@Transactional
	public ResponseEntity<ResponseDTO<Void>> Posting(PostInsertDTO postDTO) {
		
		int pNum=postDTO.getP_num();
		
		if(postingmapper.countPost(pNum) == 0) { // 게시물이 없다면
			try{
				postingmapper.insertContent(postDTO);
			}catch(Exception e) {
				
				throw new RuntimeException("DB 저장 실패: " + e.getMessage()); // 롤백
			}
			
		}else { // 게시물이 있다면
			try {
				
				if("Y".equals(postDTO.getUpdated())) { // 이미지 업데이트 O => 기존 S3 객체 삭제
					List<String> imgUrls=postingmapper.S3imgUrl(pNum);
					for(String imgUrl:imgUrls) {
						String s3FileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
					    amazonS3.deleteObject(bucket, s3FileName);
					}
				}
				
				postingmapper.updatePost(postDTO); // 내용 업데이트
				
			}catch(Exception e) {
				throw new RuntimeException("S3 삭제 실패: " + e.getMessage()); // 롤백
			}
		}

		if("N".equals(postDTO.getUpdated())) { // 이미지 업데이트 X
			return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<>(),HttpStatus.OK); //성공 		
			
		}else {
			return this.uploadImageToS3(postDTO); // S3 이미지 업로드
		}
		
		
	}
	
	private ResponseEntity<ResponseDTO<Void>> uploadImageToS3(PostInsertDTO postDTO){
		
		List<MultipartFile> images=postDTO.getImg();
		List<String> pImgNumList=postDTO.getPImgNum();
		
		// 게시물 번호 배열 int 변환
		int[] postNum=new int[pImgNumList.size()];
		
	    for (int i=0; i<pImgNumList.size(); i++) 
	    {
	        postNum[i] = Integer.parseInt(pImgNumList.get(i).trim()); 
	    }
		
		for(int i=0; i<images.size(); i++) {
		
	    MultipartFile img = images.get(i);
			
		String originalFilename=img.getOriginalFilename();
		String extension=originalFilename.substring(originalFilename.lastIndexOf(".")+1); // 확장자 추출

		String s3FileName=UUID.randomUUID().toString().substring(0,10) + originalFilename; //  원본 파일명 유지 + 파일명 중복 방지
		
		InputStream is = null; 
		 
		try {
			is = img.getInputStream(); // InputStream => 파일의 데이터를 한줄씩 바이트 단위로 읽어들임 (데이터에 접근하는
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
			System.out.println("se"+s3FileName);
			amazonS3.putObject(putObjectRequest); // 이미지를 S3에 저장
			
			// 포스트 번호, 이미지 URL DB 저장
			if("Y".equals(postDTO.getUpdated())) { // 수정 Update
				// DB URL 업데이트
				postDTO.setP_img_url(amazonS3.getUrl(bucket, s3FileName).toString());
				ImgDTO imgDTO = new ImgDTO();
				imgDTO.setPImgNum(postNum[i]); // 각 이미지의 고유 ID
				imgDTO.setPImgUrl(amazonS3.getUrl(bucket, s3FileName).toString());
				
		        postingmapper.updateImg(imgDTO); // 이미지 업데이트

			}else { // 최초 Insert
				postDTO.setP_num(postDTO.getP_num());
				postDTO.setP_img_url(amazonS3.getUrl(bucket, s3FileName).toString());
				postingmapper.insertImgUrl(postDTO);
			}
			
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
	}
		return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<>(),HttpStatus.OK); //성공 		
	}

	// 게시물 리스트
	@Override
	public ResponseEntity<ResponseDTO<List<PostSelectDTO>>> selectPost(String username) {
		// TODO Auto-generated method stub
		
		// 게시물 get
		List<PostSelectDTO> data=postingmapper.selectPost(username);

		 for (PostSelectDTO post : data) {
		        String imgUrl=post.getImgUrl();
		        String pImgNum=post.getPImgNum();
		        if (imgUrl != null) {
		            List<String> imgUrlList=Arrays.asList(imgUrl.split(","));
		            List<String> pImgNumList=Arrays.asList(pImgNum.split(","));
		            post.setImgUrls(imgUrlList);
		            post.setPImgNumList(pImgNumList);
		        } else {
		            post.setImgUrls(new ArrayList<>());
		        }
		    }
		return new ResponseEntity<ResponseDTO<List<PostSelectDTO>>> (new ResponseDTO<List<PostSelectDTO>>(data),HttpStatus.OK);
	 }

	// 게시물 삭제
	@Override
	public ResponseEntity<ResponseDTO<Void>> deletePost(int pNum) {
		// TODO Auto-generated method stub
		try {
			// S3 버킷 이미지 삭제
			List<String> imgUrls = postingmapper.S3imgUrl(pNum);
			
			if(imgUrls!=null) {
				for(String imgUrl : imgUrls) {
					String s3FileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
					amazonS3.deleteObject(bucket, s3FileName);
				}
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
