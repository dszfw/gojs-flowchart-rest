package custom;

import custom.domain.BaseEntity;
import custom.domain.Process;
import custom.dto.BaseDTO;
import custom.dto.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static URI uriForCreated(BaseEntity entry, Class resourceClass) {
        return UriBuilder.fromResource(resourceClass)
                .path(Long.toString(entry.getId()))
                .build();
    }

    public static Response created(BaseEntity en, URI uri) {
        return Response.created(uri)
                .entity(en.createDto())
                .build();
    }

    public static Response error(StatusType statusType, String msg) {
        return Response.status(statusType)
                .entity(new ErrorDTO(msg))
                .build();
    }

    public static Response ok(BaseEntity entity) {
        return Response.ok(entity.createDto())
                .build();
    }

    public static List<BaseDTO> collectionDto(List<Process> processes) {
        List<BaseDTO> dtos = new ArrayList<>();
        for (Process process : processes) {
            dtos.add(process.createDto());
        }
        return dtos;
    }
}
