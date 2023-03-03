package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.util.Lazy;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    /*
    JPA에서 지연로딩(Lazy Loading)은 객체를 조회할 때, 연관된 객체들을 즉시 모두 조회하지 않고, 해당 객체를 실제로 사용할 때 연관된 객체들을 조회하는 방식입니다. 이를테면, 엔티티 A와 연관된 엔티티 B가 있다면, 엔티티 A를 조회할 때 엔티티 B는 실제로 사용될 때 조회되는 것입니다.
    지연로딩은 쿼리를 최적화할 수 있는 장점이 있습니다. 즉, 모든 연관된 객체를 한 번에 조회하는 것이 아니라, 실제로 사용될 때만 필요한 객체들을 조회하기 때문에 쿼리의 실행 시간과 데이터 전송량을 최소화할 수 있습니다. 또한, 지연로딩은 메모리 사용을 최적화할 수 있습니다.

    */
    @ManyToOne(fetch = FetchType.LAZY) //ManyToOne 관계에서는 fetchType.LAZY 꼭
    @JoinColumn(name = "team_id") 
    private Team team; //Team이라는 외래키를 포함하려고 함

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null) changeTeam(team);
    }

    public void changeUserName(String username) {
        this.username = username;
    }

    public void changeTeam(Team team) {
        //member, team은 객체다.
        this.team = team; //member객체 안의 team을 바꿈
        team.getMembers().add(this); //team객체 안의 team을 바꿔준다.
    }
}
