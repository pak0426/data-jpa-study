package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void saveMember() {
        System.out.println("memberRepository = " + memberRepository.getClass());

        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(member.getUsername()).isEqualTo(findMember.getUsername());
        assertThat(member.getId()).isEqualTo(findMember.getId());
        assertThat(member).isEqualTo(findMember);
    }

    @Test
    public void BasicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //list 조회
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //delete
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);

        //jpa는 update를 만들 필요가 없다 변경 감지를 통해 영속성 컨텍스트에
        //변화가 있다면 자동으로 update쿼리가 날라감
        Member member3 = new Member("member3");
        memberRepository.save(member3);
        member3.setUsername("member!!!!!!");
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member member1 = new Member("member1", 28);
        Member member2 = new Member("member1", 40);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMember = memberRepository.findByUsernameAndAgeGreaterThan("member1", 30);

        assertThat(member2).isEqualTo(findMember.get(0));
    }

    @Test
    public void findByUsername() {
        Member member = new Member("member1");
        memberRepository.save(member);
        List<Member> list = memberRepository.findByUsername("member1");

        assertThat(member).isEqualTo(list.get(0));
    }

    @Test
    public void findUser() {
        Member m1 = new Member("member1", 11);
        memberRepository.save(m1);

        List<Member> list = memberRepository.findUser("member1", 11);
        Member findMember = list.get(0);

        assertThat(m1).isEqualTo(findMember);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("member1", 11);
        Member m2 = new Member("member2", 11);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> list = memberRepository.findUsernameList();
    }

    @Test
    public void findMemberDto() {
        Team t1 = new Team("team1");
        Team t2 = new Team("team2");

        teamRepository.save(t1);
        teamRepository.save(t2);

        Member m1 = new Member("member1", 11, t1);
        Member m2 = new Member("member2", 11, t2);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<MemberDto> list = memberRepository.findMemberDto();

    }

    @Test
    public void findByNames() {
        Member m1 = new Member("member1", 11);
        Member m2 = new Member("member2", 11);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> list = memberRepository.findByNames(memberRepository.findUsernameList());

        for(Member row : list) {
            System.out.println("row = " + row);
        }
    }

    @Test
    public void returnType() {
        Member m1 = new Member("member1", 11);
        Member m2 = new Member("member2", 11);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");

        Member bbb = memberRepository.findMemberByUsername("BBB");

        Optional<Member> ccc = memberRepository.findOptionalByUsername("CCC");
        
        //JPA는 nullPointException이 발생하지 않는다.
        //따라서 null 체크를 하는게 쓸모 없는 코드임
    }
}