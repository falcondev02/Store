package academy.tochkavhoda.dao;

import academy.tochkavhoda.models.Product;

import java.util.List;

public interface Dao<T, ID> {
    T findById(ID id);

    List<T> findAll();

    T insert(T obj);
}