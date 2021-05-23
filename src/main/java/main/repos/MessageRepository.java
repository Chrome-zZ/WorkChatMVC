package main.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import main.model.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
