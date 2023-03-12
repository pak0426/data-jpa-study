package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;


@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @PersistenceContext EntityManager em;
    @Autowired MemberRepository memberRepository;

    @Test
    public void test() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush(); //영속성 컨텍스트(Persistence Context)에 저장된 변경 사항을 데이터베이스에 즉시 반영하는 역할을 한다.
        em.clear(); //영속성 컨텍스트 초기화

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("member : " + member);
            System.out.println("-> member.team : " + member.getTeam());
        }
    }

    @Test
    public void jpaEventBaseEntity() throws Exception {
        //given
        Member member1 = new Member("member1", 10);

        memberRepository.save(member1); //@PrePersist

        Thread.sleep(100);
        member1.setUsername("member11"); //@PreUpdate

        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findById(member1.getId()).get();

        //then
        System.out.println("findMember = " + findMember);
        System.out.println("getCreateDate = " + findMember.getCreateDate());
        System.out.println("getLastModifiedDate = " + findMember.getLastModifiedDate());
        System.out.println("getCreateBy = " + findMember.getCreateBy());
        System.out.println("getLastModifiedDate = " + findMember.getLastModifiedDate());
    }
}