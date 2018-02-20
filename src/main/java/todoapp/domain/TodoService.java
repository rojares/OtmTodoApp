package todoapp.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import todoapp.dao.TodoDao;
import todoapp.dao.UserDao;

public class TodoService {
    private List<Todo> todos;
    private TodoDao todoDao;
    private UserDao userDao;
    private User loggedIn;

    public TodoService(TodoDao todoDao, UserDao userDao) {
        this.userDao = userDao;
        this.todoDao = todoDao;
    }
    
    public void createTodo(String content, User user) {
        Todo todo = new Todo(content, user);
        todoDao.create(todo);   
    }
    
    public List<Todo> getUndone() {
        if (loggedIn==null) {
            return new ArrayList<>();
        }
        
        return todoDao.getAll()
                .stream()
                .filter(t->{
                    System.out.println(t.getUser());
                    return t.getUser().equals(loggedIn);
                })
                .filter(t->!t.isDone())
                .collect(Collectors.toList());
    }
    
    public void markDone(int id) {
        todoDao.setDone(id);
    }
          
    public boolean login(String username) {
        User user = userDao.findUsername(username);
        if ( user==null) {
            return false;
        }
        
        loggedIn = user;
        
        return true;
    }
    
    public User getLoggedUser() {
        return loggedIn;
    }
        
    public void logout(){
      loggedIn = null;  
    }
     
    public boolean createUser(String username, String name) {   
        User user = new User(username, name);
        if (userDao.findUsername(username)!=null){
            return false;
        }
        userDao.create(user);
        return true;
    }
}
