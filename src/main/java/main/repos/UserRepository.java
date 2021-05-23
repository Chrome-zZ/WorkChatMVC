package main.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import main.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User getBySessionId(String sessionId);
}
