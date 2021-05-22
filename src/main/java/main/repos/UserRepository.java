package main.repos;

import org.springframework.data.repository.CrudRepository;

import main.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User getBySessionId(String sessionId);
}
