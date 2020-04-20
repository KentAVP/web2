package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


public class UserService {


    private static UserService instance;

    private UserService() {
    }

    public static UserService getInstance() { // #3
        if (instance == null) {        //если объект еще не создан
            instance = new UserService();    //создать новый объект
        }
        return instance;        // вернуть ранее созданный объект
    }

    /* хранилище данных */
    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());

    public Map<Long, User> db() {
       return dataBase;
    }

    /* счетчик id */
    private AtomicLong maxId = new AtomicLong(0);

    /* список авторизованных пользователей */
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());


    public List<User> getAllUsers() {
        List<User> list = new ArrayList<User>(dataBase.values());
        return list;
    }

    public User getUserById(Long id) {
        return dataBase.get(id);
    }


    public boolean addUser(User user) {
        if (dataBase.containsValue(user)) return false;
        else {
            Long z = maxId.incrementAndGet();
            dataBase.put(z, new User(z,user.getEmail(),user.getPassword()));
            //user.setId(maxId.get());
            return true;
        }
    }

    public void deleteAllUser() {
        dataBase.clear();
    }

    public boolean isExistsThisUser(User user) {
        if (dataBase.containsValue(user)) return true;
        else return false;
    }

    public List<User> getAllAuth() {
        List<User> list = new ArrayList<User>(authMap.values());
        return list;
    }

    public boolean authUser(User user) {

        if (authMap.containsValue(user) || !dataBase.containsValue(user)) return false;
        else {
            Long z = maxId.incrementAndGet();
            authMap.put(z, new User(z,user.getEmail(),user.getPassword()));
            //authMap.put(user.getId(),new User(user.getId(),user.getEmail(),user.getPassword()));
            return true;
        }
    }

    public void logoutAllUsers() {
        Iterator<Map.Entry<Long, User>> it = authMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, User> map = it.next();
            authMap.remove(map.getKey());
        }

    }

    public boolean isUserAuthById(Long id) {
        if (authMap.containsKey(id)) return true;
        else {
            return false;
        }
    }


}
