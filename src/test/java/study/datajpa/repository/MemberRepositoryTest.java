package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired EntityManager em;

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

    @Test
    public void findByPage() {
        //given
        Member memberA = new Member("aa", 10);
        Member memberB = new Member("bb", 12);
        Member memberC = new Member("cc", 10);
        Member memberD = new Member("dd", 10);
        Member memberE = new Member("ee", 10);
        Member memberF = new Member("ff", 15);

        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);
        memberRepository.save(memberD);
        memberRepository.save(memberE);
        memberRepository.save(memberF);

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        //size 0 index임

        //when (offset : 건너뛰고 셈, limit : 몇개 제한)
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //DTO로도 변환 가능하다!
        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        //then
        List<Member> members = page.getContent();
        long totalElements = page.getTotalElements();

        for(Member row : members) {
            System.out.println("row = " + row);
        }
        System.out.println("totalElements = " + totalElements);
        System.out.println(members.size());
        System.out.println(page.getTotalElements());

        assertThat(members.size()).isEqualTo(3);
//        assertThat(page.getTotalElements()).isEqualTo(4);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 12));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 35));
        memberRepository.save(new Member("member5", 40));
        memberRepository.save(new Member("member6", 17));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);
        em.flush();
        em.clear();

        //entityManager.flush() 메소드를 호출하여 변경 사항을 즉시 데이터베이스에 반영할 수 있습니다.
        //entityManager.clear() 메소드를 호출하면, 영속성 컨텍스트에서 관리하는 모든 엔티티를 분리하고, 캐시를 비웁니다.
        //이렇게 하면 모든 변경 사항이 롤백되고, 모든 엔티티가 새로운 상태로 로드됩니다.

        List<Member> members = memberRepository.findByUsername("member5");
        Member member = members.get(0); //em.flush(), em.clear 하기 전엔 영속성 컨텍스트내에서는 아직 40살임
        System.out.println("member = " + member);

        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        Member member3 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        em.flush();
        em.clear();

        //when N(team 쿼리) + 1(member 쿼리)
//        List<Member> members = memberRepository.findAll(); //select member 1번만
//        List<Member> members = memberRepository.findMemberFetchJoin(); //fetch join을 하면 가짜 Proxy 객체를 담고있지 않는다.
        List<Member> members = memberRepository.findEntityGraphByUsername("member2");

        //fetch join으로 member만 조회를 했으니 member 쿼리 1번만 나가고 Team에 대한 내용은 Proxy라는 가짜객체
        //실제로 Team을 조회하게 된다면 실제 클래스를 가져온다

        /*
        JPA(Java Persistence API)에서 프록시(Proxy) 객체는 엔티티(Entity)를 지연 로딩(Lazy Loading)하기 위해 사용됩니다.
        지연 로딩은 엔티티 객체가 실제로 사용될 때까지 데이터베이스에서 로딩을 지연시키는 방법입니다. 이를 통해 성능을 최적화할 수 있습니다. 예를 들어,
        엔티티 객체가 N:1 관계를 가지고 있을 때, 대상 엔티티를 로딩하지 않고도 참조할 수 있습니다.
        JPA에서 프록시 객체는 엔티티 객체를 상속받아 생성됩니다. 이 프록시 객체는 엔티티 객체와 동일한 인터페이스를 제공하며,
        실제 데이터베이스 로딩이 필요한 시점까지는 엔티티 객체 대신 프록시 객체를 사용합니다.
        프록시 객체를 사용할 때는 주의해야 할 점이 있습니다. 예를 들어, 프록시 객체의 toString() 메서드나 equals() 메서드를 사용하면,
        프록시 객체가 초기화되어 버그가 발생할 수 있습니다. 이를 방지하기 위해서는 프록시 객체의 실제 엔티티 객체를 로딩하는 Hibernate.initialize() 메서드를 호출하거나,
        instanceof 연산자를 사용하여 프록시 객체인지 확인한 후, 필요에 따라 엔티티 객체를 직접 사용하는 것이 좋습니다.
        따라서, JPA에서 프록시 객체는 지연 로딩을 위한 유용한 도구이지만, 사용 방법을 잘 이해하고 주의해서 사용해야 합니다.
         */

        for (Member member : members) {
            System.out.println("member name = " + member.getUsername());
            System.out.println("member team class = " + member.getTeam().getClass());
            System.out.println("team name = " + member.getTeam().getName()); //select team 2번
        }

        //then
    }


}