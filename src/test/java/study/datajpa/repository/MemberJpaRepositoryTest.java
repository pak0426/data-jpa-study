package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void MemberTest() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void BasicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //단건 조회
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //list 조회
        List<Member> memberList = memberJpaRepository.findAll();
        assertThat(memberList.size()).isEqualTo(2);

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        //delete
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deleteCount = memberJpaRepository.count();
        assertThat(deleteCount).isEqualTo(0);

        //jpa는 update를 만들 필요가 없다 변경 감지를 통해 영속성 컨텍스트에
        //변화가 있다면 자동으로 update쿼리가 날라감
        Member member3 = new Member("member3");
        memberJpaRepository.save(member3);
        member3.setUsername("member!!!!!!");
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member member1 = new Member("member1", 28);
        Member member2 = new Member("member1", 40);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> findMember = memberJpaRepository.findByUsernameAndAgeGreaterThen("member1", 30);
        assertThat(member2).isEqualTo(findMember.get(0));
    }

    @Test
    public void findByUsername() {
        Member member = new Member("member1");
        memberJpaRepository.save(member);
        List<Member> list = memberJpaRepository.findByUsername("member1");

        assertThat(member).isEqualTo(list.get(0));
    }

    @Test
    public void findByPage() {
        //given
        Member memberA = new Member("aa", 10);
        Member memberB = new Member("bb", 12);
        Member memberC = new Member("cc", 10);
        Member memberD = new Member("dd", 10);
        Member memberE = new Member("ee", 10);
        Member memberF = new Member("ff", 15);

        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);
        memberJpaRepository.save(memberC);
        memberJpaRepository.save(memberD);
        memberJpaRepository.save(memberE);
        memberJpaRepository.save(memberF);

        //when (offset : 건너뛰고 셈, limit : 몇개 제한)
        List<Member> members = memberJpaRepository.findByPage(10, 2, 10);
        Long totalCount = memberJpaRepository.totalCount(10);

        for(Member row : members) {
            System.out.println("row = " + row);
        }
        System.out.println("totalCount = " + totalCount);


    }
}