package main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import main.model.Message;
import main.model.User;
import main.repos.MessageRepository;
import main.repos.UserRepository;
import main.response.AddMessageResponse;
import main.response.AuthResponse;
import main.response.MessageResponse;

@RestController
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping(path = "/api/auth")
    public AuthResponse auth() {
        AuthResponse response = new AuthResponse();
        String sessionId = getSessionId();
        User user = userRepository.getBySessionId(sessionId);
        response.setResult(user != null);
        if (user != null) {
            response.setName(user.getName());
        }
        return response;
    }

    @PostMapping(path = "/api/users")
    public HashMap<String, Boolean> addUser(HttpServletRequest request) {
        String name = request.getParameter("name");
        String sessionId = getSessionId();
        User user = new User();
        user.setName(name);
        user.setRegTime(new Date());
        user.setSessionId(sessionId);
        userRepository.save(user);

        HashMap<String, Boolean> response = new HashMap<>();
        response.put("result", true);
        return response;
    }

    private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
    @PostMapping(path = "/api/messages")
    public AddMessageResponse addMessage(HttpServletRequest request) {
        String text = request.getParameter("text");
        String sessionId = getSessionId();
        User user = userRepository.getBySessionId(sessionId);

        Date time = new Date();
        Message message = new Message();
        message.setSendTime(time);
        message.setUser(user);
        message.setText(text);
        messageRepository.save(message);

        AddMessageResponse response = new AddMessageResponse();
        response.setResult(true);
        response.setTime(format.format(time));
        return response;
    }

    @GetMapping(path = "/api/messages")
    public HashMap<String, List> getMessages() {
        ArrayList<MessageResponse> messageList = new ArrayList<>();
        Iterable<Message> messages = messageRepository.findAll();
        for (Message message:
             messages) {
            MessageResponse messageItem = new MessageResponse();
            messageItem.setName(message.getUser().getName());
            messageItem.setTime(format.format(message.getSendTime()));
            messageItem.setText(message.getText());
            messageList.add(messageItem);
        }
        HashMap<String, List> response = new HashMap<>();
        response.put("messages", messageList);
        return response;
    }

    @GetMapping(path = "/api/users")
    public List<String> getUsers() {
//        Map<String, Iterable<User>> response = new HashMap<>();
//        Iterable<User> users = userRepository.findAll();
//        response.put("users", users);
//        return response;
        return userRepository.findAll().stream().map(User::getName).collect(Collectors.toList());
    }


    private String getSessionId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }
}
