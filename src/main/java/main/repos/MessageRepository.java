package main.repos;

import main.model.Message;
import main.model.User;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message,Integer> {

}
