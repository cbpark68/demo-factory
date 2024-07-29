package com.demo.factory.service.factory;

import com.demo.factory.domain.Factory;
import com.demo.factory.dto.factory.FactoryDto;
import com.demo.factory.dto.factory.FactoryDtoForList;
import com.demo.factory.dto.factory.FactoryDtoForManager;
import com.demo.factory.vo.PageRequestVO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FactoryService {

    public Page<FactoryDtoForList> restListV1(PageRequestVO pageRequestVO) throws Exception;

    public Long[] restCreateV1(FactoryDtoForManager factoryDtoForManager) throws Exception;

    public Long[] restModifyV1(FactoryDtoForManager factoryDtoForManager) throws Exception;

    public List<FactoryDto> formList() throws Exception;

    public Long formCreate(FactoryDto factoryDto) throws Exception;

    public Long formModify(FactoryDto factoryDto) throws Exception;

    public Factory find(Long factoryNo) throws Exception;

    public FactoryDto findDto(Long factoryNo) throws Exception;

    public void remove(Long factoryNo) throws Exception;

    public String getLogoFileName(Long factoryNo) throws Exception;


}
