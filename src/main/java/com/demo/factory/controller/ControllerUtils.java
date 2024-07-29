package com.demo.factory.controller;

import com.demo.factory.common.util.CommonFileUtils;
import com.demo.factory.domain.Member;
import com.demo.factory.repository.member.MemberRepository;
import com.demo.factory.security.dto.MemberDtoForSecurity;
import com.demo.factory.service.factory.FactoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class ControllerUtils {
    private final MemberRepository memberRepository;
    private final FactoryService factoryService;

    @Value("${com.demofactory.logo-file-name}")
    private String LOGO_FILE_NAME;

    @Value("${com.demofactory.logo-upload-path}")
    private String LOGO_UPLOAD_PATH;

    @Value("${com.demofactory.model-upload-path}")
    private String MODEL_UPLOAD_PATH;


    public Member currentUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        } catch (ClassCastException e) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof MemberDtoForSecurity) {
                username = ((MemberDtoForSecurity)authentication.getPrincipal()).getUsername();
            }
        } catch (Exception e) {
            username = (String) authentication.getPrincipal();
        }
        if (username == null || username.isEmpty()) {
           return null;
        }
        return memberRepository.findByUserId(username).orElse(new Member());
    }

    public String uploadLogoFile(Long factoryNo, String originalName, byte[] fileData) throws Exception {
//        UUID uid = UUID.randomUUID();
//        String createdFileName = uid.toString() + "_" + originalName;
        String createdFileName = LOGO_FILE_NAME;

        String factoryNoStr = String.valueOf(factoryNo);
        CommonFileUtils.makeFactoryDir(LOGO_UPLOAD_PATH, factoryNoStr, factoryNoStr + File.separator + "logo");
        String uploadPath = LOGO_UPLOAD_PATH + File.separator + factoryNo + File.separator + "logo";
        File target = new File(uploadPath, createdFileName);

        FileCopyUtils.copy(fileData, target);

        return createdFileName;
    }

    public String uploadModelFile(Long factoryNo, String originalName, byte[] fileData) throws Exception {
        String factoryNoStr = String.valueOf(factoryNo);
        String uploadPath = MODEL_UPLOAD_PATH + File.separator + factoryNo + File.separator + "model";
        CommonFileUtils.deleteDir(uploadPath);
        CommonFileUtils.makeFactoryDir(MODEL_UPLOAD_PATH, factoryNoStr, factoryNoStr + File.separator + "model");
        File target = new File(uploadPath, originalName);
        FileCopyUtils.copy(fileData, target);
        CommonFileUtils.unzipFile(Paths.get(uploadPath + File.separator + originalName), Paths.get(uploadPath));
        return originalName;
    }

    public ResponseEntity<byte[]> getLogoFile(Long factoryNo) throws Exception {
        ResponseEntity<byte[]> entity = null;
        String logoFileName = factoryService.getLogoFileName(factoryNo);

        try {
            String formatName = logoFileName.substring(logoFileName.lastIndexOf(".") + 1);

            MediaType mType = CommonFileUtils.getMediaType(formatName);

            HttpHeaders headers = new HttpHeaders();

            File file = new File(LOGO_UPLOAD_PATH + File.separator + factoryNo + File.separator + "logo" + File.separator + logoFileName);
            if (mType != null) {
                headers.setContentType(mType);
            }

            entity = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
        }
        return entity;
    }

    public ResponseEntity<byte[]> getModelFile(String factoryNo,String modelFileName) {
        ResponseEntity<byte[]> entity = null;

        try {
            String url = MODEL_UPLOAD_PATH + File.separator + factoryNo +File.separator + "model" + File.separator;
            File file = new File(url + modelFileName);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("Content-Disposition", "attachment; filename=\"" + new String(modelFileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

            entity = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
        }

        return entity;
    }

}
