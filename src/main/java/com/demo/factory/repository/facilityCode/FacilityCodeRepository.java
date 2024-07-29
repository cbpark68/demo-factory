package com.demo.factory.repository.facilityCode;

import com.demo.factory.domain.FacilityCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FacilityCodeRepository extends JpaRepository<FacilityCode,Long>, FacilityCodeRepositoryCustom {
    @Query("select f " +
            " from FacilityCode f " +
            " join f.factory ft " +
            " where ft.factoryNo = :factoryNo")
    public List<FacilityCode> findByFactoryNo(Long factoryNo);

    @Modifying
    @Query("delete from FacilityCode f1 " +
            " where f1.facilityCodeNo in " +
            " (select f.facilityCodeNo " +
            " from FacilityCode f " +
            " join f.factory ft " +
            " where ft.factoryNo = :factoryNo)")
    public void deleteByFactoryNo(Long factoryNo);

    @Modifying
    @Query("delete from FacilityCode fc1 " +
            " where fc1.facilityCodeNo in " +
            " (select fc.facilityCodeNo " +
            " from FacilityCode fc " +
            " join fc.factory ft " +
            " where fc.facilityCodeNo = :facilityCodeNo " +
            " and ft.factoryNo = :factoryNo)")
    public void deleteByPkAndFactoryNo(Long facilityCodeNo, Long factoryNo);

    @Query("select fc " +
            " from FacilityCode fc " +
            " join fc.factory ft " +
            " where fc.facilityCodeNo = :facilityCodeNo " +
            " and ft.factoryNo = :factoryNo")
    public Optional<FacilityCode> findByPkAndFactoryNo(Long facilityCodeNo, Long factoryNo);

    @Query("select fc " +
            " from FacilityCode fc " +
            " join fc.factory ft " +
            " where fc.facilityCodeNo = :facilityCodeNo ")
    Optional<FacilityCode> findByPk(Long facilityCodeNo);

    public void deleteAll();
}
