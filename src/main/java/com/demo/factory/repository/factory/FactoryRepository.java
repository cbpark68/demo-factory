package com.demo.factory.repository.factory;

import com.demo.factory.domain.Factory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FactoryRepository extends JpaRepository<Factory,Long>, FactoryRepositoryCustom {
    @Query("select f from Factory f where f.factoryNo = :factoryNo")
    Optional<Factory> findByPk(Long factoryNo);

    @Query("select f from Factory f ")
    List<Factory> findAllForOption();

    List<Factory> findByFactoryName(String factoryName);


    @Query(value =
            "insert into dt_site (factory_no,factory_name,factory_status,logo_file_name) values " +
                    "(1,\"site_admin\",\"ACTIVE\",\"logo.png\"), "+
                    "(2,\"k-factory\",\"ACTIVE\",\"logo.png\"), " +
                    "(3,\"미래학습관\",\"ACTIVE\",\"logo.png\")," +
                    "(4,\"site-4\",\"ACTIVE\",\"logo.png\");",
            nativeQuery = true)
    @Modifying
    @Transactional
    public void insertFactory();

    @Query(value =
            "insert into dt_user (user_no,user_id,user_pw,user_name,site_no) values "+
                    "(1,\"admin\",\"1234\",\"관리자\",1),"+
                    "(2,\"kfactory\",\"admin\",\"Site Manager\",2),"+
                    "(3,\"sfactory\",\"admin\",\"Site Manager\",3),"+
                    "(4,\"kfactory1\",\"admin\",\"Site User\",2),"+
                    "(5,\"sfactory1\",\"admin\",\"Site User\",3),"+
                    "(6,\"site4\",\"admin\",\"Site Manager\",4);",
            nativeQuery = true)
    @Modifying
    @Transactional
    public void insertUser();

    @Query(value =
            "insert into dt_user_auth (user_auth_no,user_no,auth) values "+
            "(1,1,\"ROLE_ADMIN\"),"+
            "(2,2,\"ROLE_SITE_MANAGER\"),"+
            "(3,3,\"ROLE_SITE_MANAGER\"),"+
            "(4,4,\"ROLE_SITE_USER\"),"+
            "(5,5,\"ROLE_SITE_USER\"),"+
            "(6,6,\"ROLE_SITE_MANAGER\");",
            nativeQuery = true)
    @Modifying
    @Transactional
    public void insertAuth();

}
