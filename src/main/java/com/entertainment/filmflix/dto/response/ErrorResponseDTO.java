package com.entertainment.filmflix.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, String> messages;
}
