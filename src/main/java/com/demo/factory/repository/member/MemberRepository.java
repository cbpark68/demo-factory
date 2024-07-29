package com.demo.factory.repository.member;

import com.demo.factory.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
	@Modifying
	@Query("delete from Member m1 " +
			" where m1.userNo = " +
			" (select m.userNo from Member m " +
			"  join m.factory f " +
			"  where m.userNo = :userNo " +
			"  and f.factoryNo = :factoryNo)")
	public void removeByParent(Long factoryNo, Long userNo);

	@Modifying
	@Query("delete from MemberAuth ma where ma.userNo = :userNo")
	public void removeAuthByUserNo(Long userNo);

	@Query("select m from Member m " +
			" join fetch m.factory f " +
			" where m.userId = :userId")
	public Optional<Member> findByUserId(String userId);

	@Query("select m from Member m " +
			" join fetch m.factory f " +
			" where f.factoryNo = :factoryNo")
	public List<Member> findBySiteNo(Long factoryNo);

	@Query("select m from Member m " +
			" join fetch m.factory f " +
			" where m.userId = :userId " +
			" and f.factoryNo = :factoryNo ")
	public Optional<Member> findByUserIdAndSiteNo(String userId, Long factoryNo);

	@Query("select m from Member m " +
			" join fetch m.factory f " +
			" where m.userNo = :userNo")
    Optional<Member> findByPk(Long userNo);

}
