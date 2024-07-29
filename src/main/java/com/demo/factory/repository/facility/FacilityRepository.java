package com.demo.factory.repository.facility;

import com.demo.factory.domain.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility,Long>, FacilityRepositoryCustom {
    @Query("select f " +
            " from Facility f " +
            " join fetch f.factory ft " +
            " where f.facilityName = :facilityName " +
            " and ft.factoryNo = :factoryNo")
    public Optional<Facility> findByFacilityNameAndFactoryNo(String facilityName, Long factoryNo);

    @Query("select f " +
            " from Facility f " +
            " join fetch f.factory ft " +
            " where ft.factoryNo = :factoryNo")
    public List<Facility> findByFactoryNo(Long factoryNo);

    @Query("select f " +
            " from Facility f " +
            " join fetch f.facilityCode fc" +
            " where fc.facilityCodeNo = :facilityCodeNo")
    public List<Facility> findByFacilityCodeNo(Long facilityCodeNo);

    @Modifying
    @Query("delete from Facility f1 " +
            " where f1.facilityNo in " +
            " (select f.facilityNo " +
            " from Facility f " +
            " join f.factory ft " +
            " where ft.factoryNo = :factoryNo)")
    public void deleteByFactoryNo(Long factoryNo);

    @Modifying
    @Query("delete from Facility f1 " +
            " where f1.facilityNo in " +
            " (select f.facilityNo " +
            " from Facility f " +
            " join f.facilityCode fc " +
            " where fc.facilityCodeNo = :facilityCodeNo)")
    public void deleteByFacilityCodeNo(Long facilityCodeNo);

    @Query(" select f " +
            " from Facility f " +
            " join fetch f.factory ft " +
            " join fetch f.facilityCode fc " +
            " where ft.factoryNo = :factoryNo " +
            " and f.facilityNo = :facilityNo " +
            " and fc.facilityCodeNo = :facilityCodeNo")
    public Optional<Facility> findByParent( Long factoryNo, Long facilityCodeNo,Long facilityNo);

    @Modifying
    @Query(" delete from Facility f1 " +
            " where f1.facilityNo in " +
            " (select f.facilityNo " +
            " from Facility f " +
            " join f.factory ft "+
            " where f.facilityNo = :facilityNo " +
            " and ft.factoryNo = :factoryNo) ")
    public void deleteByParent(Long factoryNo, Long facilityNo);

    @Query(" select f " +
            " from Facility f " +
            " join fetch f.factory ft " +
            " join fetch f.facilityCode fc " +
            " where f.facilityNo = :facilityNo ")
    Optional<Facility> findByPk(Long facilityNo);

    @Query(" select f " +
            " from Facility f " +
            " join fetch f.factory ft " +
            " join fetch f.facilityCode fc " +
            " where f.facilityNo = :facilityNo " +
            " and ft.factoryNo = :factoryNo ")
    public Optional<Facility> findByPkAndFactoryNo(Long facilityNo, Long factoryNo);

    public void deleteAll();
}
