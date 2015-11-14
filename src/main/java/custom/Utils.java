package custom;

import custom.domain.BaseEntity;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class Utils {

    public static URI uriForIdentifiable(BaseEntity entry, Class resourceClass) {
        return uriForIdentifiable(entry.getId(), resourceClass);
    }

    public static URI uriForIdentifiable(long id, Class resourceClass) {
        return UriBuilder.fromResource(resourceClass)
                .path(Long.toString(id))
                .build();
    }

    public static URI uriForCollection(Class resourceClass) {
        return UriBuilder.fromResource(resourceClass).build();
    }

}
