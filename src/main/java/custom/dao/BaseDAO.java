package custom.dao;

import com.google.common.base.Optional;
import custom.domain.*;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public abstract class BaseDAO <E extends BaseEntity> extends AbstractDAO<E>{

    public BaseDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<E> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public boolean isExist(long id) {
        Optional<E> optionalEntity = findById(id);
        if (optionalEntity.isPresent()) {
            currentSession().evict(optionalEntity.get());
            return true;
        }
        return false;
    }

    public abstract List<E> findAll();

    public abstract E create(E process);

    public abstract E update(E process);

    public abstract void delete(E process);

}
