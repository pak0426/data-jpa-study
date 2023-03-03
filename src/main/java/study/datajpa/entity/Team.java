package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
public class Team {
    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    //JPA mappedBy는 두 개체 간의 양방향 관계에서 소유 측을 정의하는데 사용한다.
    //Member, Team중에서 외래키를 필요로 하는 곳은 Member이므로 Team에서 oneToMany에 mappedBy를 사용해 소유 측임을 선언한다.
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
