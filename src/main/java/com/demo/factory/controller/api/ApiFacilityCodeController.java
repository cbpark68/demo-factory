package com.demo.factory.controller.api;

import com.demo.factory.dto.PaginationDto;
import com.demo.factory.dto.facilityCode.FacilityCodeDto;
import com.demo.factory.dto.facilityCode.FacilityCodeDtoForList;
import com.demo.factory.service.facilityCode.FacilityCodeService;
import com.demo.factory.vo.PageRequestVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiFacilityCodeController {
    private final FacilityCodeService facilityCodeService;
    private final String urlV0 = "/v0/factory/{factoryNo}/facilityCode";

    public record Result(String facilityCodeNo) {
    }

    @GetMapping(value = urlV0+"/list")
    public PaginationDto<FacilityCodeDtoForList> list(@PathVariable("factoryNo") Long factoryNo, @ModelAttribute("pgrq") PageRequestVO pageRequestVO) throws Exception {
        pageRequestVO.setFactoryNo(factoryNo);
        Page<FacilityCodeDtoForList> page = facilityCodeService.list(pageRequestVO);
        return new PaginationDto<>(page);
    }

    @PostMapping(value = urlV0)
    public Result create(@PathVariable("factoryNo") Long factoryNo, @RequestBody FacilityCodeDto facilityCodeDto) throws Exception {
        facilityCodeDto.setFacilityCodeNo(null);
        facilityCodeDto.setFactoryNo(String.valueOf(factoryNo));
        Long facilityCodeNo = facilityCodeService.create(facilityCodeDto);
        return new Result(String.valueOf(facilityCodeNo));
    }

    @PutMapping(value = urlV0)
    public Result modify(@PathVariable("factoryNo") Long factoryNo, @RequestBody FacilityCodeDto facilityCodeDto) throws Exception {
        facilityCodeDto.setFactoryNo(String.valueOf(factoryNo));
        Long facilityCodeNo = facilityCodeService.modify(facilityCodeDto);
        return new Result(String.valueOf(facilityCodeNo));
    }

    @DeleteMapping(value = urlV0+"/{facilityCodeNo}")
    public void remove(@PathVariable("factoryNo") Long factoryNo, @PathVariable("facilityCodeNo") Long facilityCodeNo) throws Exception {
        facilityCodeService.remove(facilityCodeNo,factoryNo);
    }

}
