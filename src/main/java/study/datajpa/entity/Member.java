package study.datajpa.entity;

import lombok.*;
import org.springframework.data.util.Lazy;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    /*
    JPA에서 지연로딩(Lazy Loading)은 객체를 조회할 때, 연관된 객체들을 즉시 모두 조회하지 않고, 해당 객체를 실제로 사용할 때 연관된 객체들을 조회하는 방식입니다.
    이를테면, 엔티티 A와 연관된 엔티티 B가 있다면, 엔티티 A를 조회할 때 엔티티 B는 실제로 사용될 때 조회되는 것입니다.
    지연로딩은 쿼리를 최적화할 수 있는 장점이 있습니다. 즉, 모든 연관된 객체를 한 번에 조회하는 것이 아니라,
    실제로 사용될 때만 필요한 객체들을 조회하기 때문에 쿼리의 실행 시간과 데이터 전송량을 최소화할 수 있습니다. 또한, 지연로딩은 메모리 사용을 최적화할 수 있습니다.
    하지만 지연로딩은 사용에 주의할 점도 있습니다. 지연로딩을 사용할 때, 연관된 객체를 사용하기 전에 영속성 컨텍스트(Persistence Context)가 종료될 수 있습니다.
    이 경우, 지연로딩된 객체를 사용하려 할 때 LazyInitializationException 예외가 발생할 수 있습니다.
    이를 해결하기 위해서는, 영속성 컨텍스트를 유지하는 방법이 필요합니다.
    또한, 지연로딩을 사용할 때는 데이터베이스 커넥션을 유지하고 있어야 하므로, 애플리케이션의 성능에 영향을 줄 수 있습니다.
    따라서, 지연로딩을 사용할 때는 사용하는 상황에 맞게 적절하게 사용하는 것이 중요합니다.
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

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
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
