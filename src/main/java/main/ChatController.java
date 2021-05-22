package main;

import main.model.Message;
import main.model.User;
import main.repos.MessageRepository;
import main.repos.UserRepository;
import main.response.AuthResponse;
import main.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @GetMapping(path = "/api/auth")
    public AuthResponse auth(){
        AuthResponse response = new AuthResponse();
        String sessionId = getSessionId();
        User user = userRepository.getBySessionId(sessionId);
        response.setResult(user !=null);
        if(user != null){
            response.setName(user.getName());
        }
        return response;
    }
    @PostMapping(path = "/api/users")
    public HashMap<String, Boolean> addUser(HttpServletRequest request){
        String name = request.getParameter("name");
        String sessionId = getSessionId();
        User user = new User();
        user.setName(name);
        user.setRegTime(new Date());
        user.setSessionId(sessionId);
        userRepository.save(user);
        HashMap<String, Boolean> response = new HashMap<>();
        return response;
    }

    /*@GetMapping(path = "/api/users")
    public HashMap<String,Boolean> loadUsers(){
        HashMap<String,Boolean> response = new HashMap<>();
        String sessionId = getSessionId();
        ArrayList <User> users = userRepository.findAllBy();
        response.put("result", user !=null);
        return response;
    }*/

    public HashMap<String, Boolean> addMessage(HttpServletRequest request){
        String text = request.getParameter("text");
        String sessionId = getSessionId();
        User user = userRepository.getBySessionId(sessionId);
        Message message = new Message();
        message.setTime(new Date());
        message.setUserid(user.getId());
        message.setText(text);
        messageRepository.save(message);
        HashMap<String, Boolean> response = new HashMap<>();
        response.put("result", true);
        return response;
    }
    @GetMapping(path = "/api/messages")
    public HashMap<String,List> getMessages(){
        ArrayList<MessageResponse> messageList= new ArrayList<>();
        Iterable <Message> messages = messageRepository.findAll();
        for (Message message: messages
             ) {
            MessageResponse messageItem = new MessageResponse();
            messageItem.setName(userRepository.getById(message.getUserid()).getName());
            messageItem.setTime(message.getTime());
            messageItem.setText(message.getText());
            messageList.add(messageItem);
        }
        HashMap<String,List> response = new HashMap<>();
        response.put("messages", messageList);
        return response;
    }


    private String getSessionId(){
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }
}
