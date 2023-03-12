package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    //엔티티가 영구 저장소에 저장되기 전(예: 데이터베이스에 저장하기 전)에 호출되는 콜백 메서드를 지정하는 데 사용됩니다.
    //ex) 엔티티의 필드 값을 자동으로 설정하거나, 필수 필드의 존재 여부를 확인하거나, 엔티티의 유효성을 검증하는 등의 작업을 수행할 수 있습니다.
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createDate = now;
        this.updateDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        updateDate = LocalDateTime.now();
    }


}
