package com.entertainment.filmflix.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> extends Response<T>{
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T response;
}
