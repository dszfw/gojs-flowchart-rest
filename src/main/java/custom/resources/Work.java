package custom.resources;

import com.google.common.base.Optional;
import custom.domain.BaseEntity;

import java.util.List;

abstract public class Work {

    public BaseEntity entity() {
        unsupported();
        return null;
    }

    public Optional<? extends BaseEntity> entityOptional() {
        unsupported();
        return null;
    }

    public List<? extends BaseEntity> entities() {
        unsupported();
        return null;
    }

    public void doWork() {
        unsupported();
    }

    private void unsupported() {
        throw new UnsupportedOperationException();
    }
}
