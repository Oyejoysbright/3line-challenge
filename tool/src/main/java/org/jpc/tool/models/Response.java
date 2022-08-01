package org.jpc.tool.models;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@SuppressWarnings({"unchecked", "rawtypes"})
public class Response {
    @Builder.Default
    private Boolean hasError = false;
    private String message;
    private Object data;

    public List<Object> parseDataToArray() {
        return (ArrayList) this.data;
    }
}
