
package dao;

import java.util.List;

public interface GenericDAO<T> {
    public abstract void update(T t) throws Exception;
    public abstract void deleteLogico(Long id) throws Exception;
    public abstract T findById(Long id) throws Exception;
    public abstract List<T> findAll() throws Exception;
}
