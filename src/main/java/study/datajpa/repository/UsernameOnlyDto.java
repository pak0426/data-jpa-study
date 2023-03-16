package study.datajpa.repository;

public class UsernameOnlyDto {
    private String username;

    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
