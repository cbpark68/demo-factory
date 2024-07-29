package com.demo.factory.controller.api;

import com.demo.factory.controller.ControllerUtils;
import com.demo.factory.dto.PaginationDto;
import com.demo.factory.dto.factory.FactoryDtoForList;
import com.demo.factory.dto.factory.FactoryDtoForManager;
import com.demo.factory.exception.LogoFileException;
import com.demo.factory.exception.ModelFileException;
import com.demo.factory.service.factory.FactoryService;
import com.demo.factory.vo.PageRequestVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiFactoryController {
    private final FactoryService factoryService;
    private final ControllerUtils controllerUtils;

    private final String urlV1 = "/v1/factory";
    private final String urlV0 = "/v0/factory";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${com.demofactory.logo-file-name}")
    private String LOGO_FILE_NAME;

    @Value("${com.demofactory.logo-upload-path}")
    private String LOGO_UPLOAD_PATH;

    @Value("${com.demofactory.model-upload-path}")
    private String MODEL_UPLOAD_PATH;

    public record Result(String factoryNo, String userNo) {
    }

    @GetMapping(value = {urlV1 +"/list"})
    public PaginationDto<FactoryDtoForList> listV1(@ModelAttribute("pgrq") PageRequestVO pageRequestVO) throws Exception {
        Page<FactoryDtoForList> page = factoryService.restListV1(pageRequestVO);
        PaginationDto<FactoryDtoForList> pagination = new PaginationDto<>(page);
        return pagination;
    }

    @PostMapping(value = {urlV0})
    public Result create(@RequestPart(value = "factory") String factoryDtoStr, @RequestPart(value = "logoFile",required = false) MultipartFile logoFile) throws Exception {
        FactoryDtoForManager factoryDtoForManager = objectMapper.readValue(factoryDtoStr, FactoryDtoForManager.class);
        factoryDtoForManager.setFactoryNo(null); //생성임을 명확히 한다.
        factoryDtoForManager.setLogoFileImgName(LOGO_FILE_NAME); //dto에서는 불가하므로 외부에서 한다.

        Long[] resultArray = factoryService.restCreateV1(factoryDtoForManager);
        Result result = makeResult(resultArray);

        if (logoFile != null && logoFile.getSize() > 0) {
            String fileName = controllerUtils.uploadLogoFile(Long.valueOf(result.factoryNo), logoFile.getOriginalFilename(), logoFile.getBytes());
            if (fileName == null) {
                throw new LogoFileException("로그파일 등록오류 발생");
            }
        }
        return result;
    }

    @PutMapping(value = {urlV0})
    public Result modify(@RequestPart(value = "factory") String factoryDtoStr, @RequestPart(value = "logoFile",required = false) MultipartFile logoFile) throws Exception {
        FactoryDtoForManager factoryDtoForManager = objectMapper.readValue(factoryDtoStr, FactoryDtoForManager.class);
        if (logoFile != null && logoFile.getSize() > 0) {
            String fileName = controllerUtils.uploadLogoFile(Long.valueOf(factoryDtoForManager.getFactoryNo()), logoFile.getOriginalFilename(), logoFile.getBytes());
            if (fileName == null) {
                throw new LogoFileException("로그파일 등록오류 발생");
            }
        }
        Long[] resultArray = factoryService.restModifyV1(factoryDtoForManager);
        return makeResult(resultArray);
    }

    @DeleteMapping(value = urlV1+"/{factoryNo}")
    public void remove(@PathVariable("factoryNo") Long factoryNo) throws Exception {
       factoryService.remove(factoryNo);
    }

    @ResponseBody
    @GetMapping(value = urlV1 +"/{factoryNo}/logo")
    public ResponseEntity<byte[]> displayFileV1(@PathVariable("factoryNo") Long factoryNo) throws Exception {
        return controllerUtils.getLogoFile(factoryNo);
    }


    @PatchMapping(value = urlV0+"/{factoryNo}")
    public void createModel(@PathVariable("factoryNo") Long factoryNo, @RequestPart(value = "modelFile") MultipartFile modelFile) throws Exception {
        if (modelFile != null && modelFile.getSize() > 0) {
            String fileName = controllerUtils.uploadModelFile(factoryNo, modelFile.getOriginalFilename(), modelFile.getBytes());
            if (fileName == null) {
                throw new ModelFileException("모델파일 등록오류 발생");
            }
        }
    }

    @ResponseBody
    @GetMapping(urlV0 + "/{factoryNo}/model/{modelFileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("factoryNo") String factoryNo,@PathVariable("modelFileName") String modelFileName) throws Exception {
        return controllerUtils.getModelFile(factoryNo, modelFileName);
    }

    private Result makeResult(Long[] resultArray) throws Exception {
        String factoryNo = String.valueOf(resultArray[0]);
        String userNo = String.valueOf(resultArray[1]);
        return new Result(factoryNo,userNo);
    }

}
