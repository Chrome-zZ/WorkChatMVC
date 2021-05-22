package main.repos;

import org.springframework.data.repository.CrudRepository;

import main.model.Message;

public interface MessageRepository extends CrudRepository<Message, Integer> {
}
