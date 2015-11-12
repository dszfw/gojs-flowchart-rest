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

    public abstract Optional<E> findById(Long id);

    public abstract List<E> findAll();

    public abstract E create(E process);

    public abstract E update(E process);

    public abstract void delete(E process);

    public abstract boolean isExist(long id);

}
