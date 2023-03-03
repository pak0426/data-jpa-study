package study.datajpa.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

//ctrl + p = 파라미터 뭐가 들어가는지 나옴
public interface MemberRepository extends JpaRepository<Member, Long> {

}
