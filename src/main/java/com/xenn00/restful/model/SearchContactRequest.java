package com.xenn00.restful.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchContactRequest {
    private String name;

    private String email;

    private String phone;

    @NotNull
    private Integer size;

    @NotNull
    private Integer page;
}
