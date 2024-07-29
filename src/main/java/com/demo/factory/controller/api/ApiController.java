package com.demo.factory.controller.api;

import com.demo.factory.dto.facility.FacilityDto;
import com.demo.factory.dto.facilityCode.FacilityCodeDto;
import com.demo.factory.repository.facility.FacilityRepository;
import com.demo.factory.repository.facilityCode.FacilityCodeRepository;
import com.demo.factory.repository.factory.FactoryRepository;
import com.demo.factory.security.dto.MemberDtoForSecurity;
import com.demo.factory.service.facility.FacilityService;
import com.demo.factory.service.facilityCode.FacilityCodeService;
import com.demo.factory.service.factory.FactoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ApiController {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final FacilityService facilityService;

    private final FacilityCodeService facilityCodeService;

    private final FacilityRepository facilityRepository;

    private final FacilityCodeRepository facilityCodeRepository;

    private final FactoryRepository factoryRepository;

    private final FactoryService factoryService;

    @GetMapping("/whois")
    @ResponseBody
    public String whois() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "anonymous";
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        } catch (ClassCastException e) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof MemberDtoForSecurity) {
                username = ((MemberDtoForSecurity)authentication.getPrincipal()).getUsername();
            }
        } catch (Exception e) {
            try {
                username = (String) authentication.getPrincipal();
            } catch (Exception exception) {
                username = "anonymous";
            }
        }
        return username;
    }

    @GetMapping("/createTestData")
    @ResponseBody
    public String makeData() throws Exception {
        facilityRepository.deleteAll();
        facilityCodeRepository.deleteAll();

        factoryService.remove(4L);
        factoryService.remove(3L);
        factoryService.remove(2L);
        factoryService.remove(1L);
        factoryRepository.deleteAll();

        factoryRepository.insertFactory();
        factoryRepository.insertUser();
        factoryRepository.insertAuth();

        String s2ServerFacilityCode = String.valueOf(facilityCodeService.create(new FacilityCodeDto("2", "server2factoryCode", "서버2설비코드", "{info}")));
        String s2LineFacilityCode = String.valueOf(facilityCodeService.create(new FacilityCodeDto("2", "line2factoryCode", "라인2설비코드", "{info}")));
        String s3ServerFacilityCode = String.valueOf(facilityCodeService.create(new FacilityCodeDto("3", "server3factoryCode", "서버3설비코드", "{info}")));
        String s3LineFacilityCode = String.valueOf(facilityCodeService.create(new FacilityCodeDto("3", "line3factoryCode", "라인3설비코드", "{info}")));

        String s2Server1Facility = String.valueOf(facilityService.create(new FacilityDto("2", s2ServerFacilityCode, "server1", "Y")));
        String s2Server2Facility = String.valueOf(facilityService.create(new FacilityDto("2", s2ServerFacilityCode, "server2", "Y")));
        String s2Line1Facility = String.valueOf(facilityService.create(new FacilityDto("2", s2LineFacilityCode, "line1", "Y")));
        String s2Line2Facility = String.valueOf(facilityService.create(new FacilityDto("2", s2LineFacilityCode, "line2", "Y")));
        String s3Server1Facility = String.valueOf(facilityService.create(new FacilityDto("3", s3ServerFacilityCode, "server1", "Y")));
        String s3Server2Facility = String.valueOf(facilityService.create(new FacilityDto("3", s3ServerFacilityCode, "server2", "Y")));
        String s3Line1Facility = String.valueOf(facilityService.create(new FacilityDto("3", s3LineFacilityCode, "line1", "Y")));
        String s3Line2Facility = String.valueOf(facilityService.create(new FacilityDto("3", s3LineFacilityCode, "line2", "Y")));

        return "testData is created!";
    }
}
