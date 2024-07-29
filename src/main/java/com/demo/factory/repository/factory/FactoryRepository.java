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
            "insert into factory (factory_no,factory_name,factory_status,logo_file_name) values " +
                    "(1,\"factory_admin\",\"ACTIVE\",\"logo.png\"), "+
                    "(2,\"k-factory\",\"ACTIVE\",\"logo.png\"), " +
                    "(3,\"f-factory\",\"ACTIVE\",\"logo.png\")," +
                    "(4,\"factory-4\",\"ACTIVE\",\"logo.png\");",
            nativeQuery = true)
    @Modifying
    @Transactional
    public void insertFactory();

    @Query(value =
            "insert into user (user_no,user_id,user_pw,user_name,factory_no) values "+
                    "(1,\"admin\",\"1234\",\"관리자\",1),"+
                    "(2,\"kfactory\",\"admin\",\"Factory Manager\",2),"+
                    "(3,\"sfactory\",\"admin\",\"Factory Manager\",3),"+
                    "(4,\"kfactory1\",\"admin\",\"Factory User\",2),"+
                    "(5,\"sfactory1\",\"admin\",\"Factory User\",3),"+
                    "(6,\"factory4\",\"admin\",\"Factory Manager\",4);",
            nativeQuery = true)
    @Modifying
    @Transactional
    public void insertUser();

    @Query(value =
            "insert into user_auth (user_auth_no,user_no,auth) values "+
            "(1,1,\"ROLE_ADMIN\"),"+
            "(2,2,\"ROLE_FACTORY_MANAGER\"),"+
            "(3,3,\"ROLE_FACTORY_MANAGER\"),"+
            "(4,4,\"ROLE_FACTORY_USER\"),"+
            "(5,5,\"ROLE_FACTORY_USER\"),"+
            "(6,6,\"ROLE_FACTORY_MANAGER\");",
            nativeQuery = true)
    @Modifying
    @Transactional
    public void insertAuth();

}
