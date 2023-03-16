package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    @Value("#{target.username + ' ' + target.age}") //open Projection
    String getUsername();
}
