package academy.tochkavhoda.dao;

import academy.tochkavhoda.models.Product;
import academy.tochkavhoda.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserDao implements Dao<User, Integer> {
    private Map<Integer, User> storage = new ConcurrentHashMap<>();

    @Override
    public User findById(Integer id) {
        return storage.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public User insert(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    public boolean existsById(int id) {
        return storage.containsKey(id);
    }

    public void deleteById(int id) {
        storage.remove(id);
    }
}
