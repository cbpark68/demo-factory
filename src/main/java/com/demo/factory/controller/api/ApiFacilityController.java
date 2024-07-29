package com.demo.factory.controller.api;

import com.demo.factory.dto.PaginationDto;
import com.demo.factory.dto.facility.FacilityDto;
import com.demo.factory.dto.facility.FacilityDtoForList;
import com.demo.factory.service.facility.FacilityService;
import com.demo.factory.vo.PageRequestVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiFacilityController {
    private final FacilityService facilityService;
    private final String urlV0 = "/v0/factory/{factoryNo}/facility";

    public record Result(String facilityNo) {
    }

    @GetMapping(value = urlV0+"/list")
    public PaginationDto<FacilityDtoForList> list(@PathVariable("factoryNo") Long factoryNo, @ModelAttribute("pgrq") PageRequestVO pageRequestVO) throws Exception {
        pageRequestVO.setFactoryNo(factoryNo);
        Page<FacilityDtoForList> page = facilityService.list(pageRequestVO);
        return new PaginationDto<>(page);
    }

    @PostMapping(value = urlV0)
    public Result create(@PathVariable("factoryNo") Long factoryNo, @RequestBody FacilityDto facilityDto) throws Exception {
        facilityDto.setFacilityNo(null);
        facilityDto.setFactoryNo(String.valueOf(factoryNo));
        Long facilityNo = facilityService.create(facilityDto);
        return new Result(String.valueOf(facilityNo));
    }

    @PutMapping(value = urlV0)
    public Result modify(@PathVariable("factoryNo") Long factoryNo, @RequestBody FacilityDto facilityDto) throws Exception {
        facilityDto.setFactoryNo(String.valueOf(factoryNo));
        Long facilityNo = facilityService.modify(facilityDto);
        return new Result(String.valueOf(facilityNo));
    }

    @DeleteMapping(value = urlV0+"/{facilityNo}")
    public void remove(@PathVariable("factoryNo") Long factoryNo,@PathVariable("facilityNo") Long facilityNo) throws Exception {
        facilityService.removeByParent(factoryNo, facilityNo);
    }

}
