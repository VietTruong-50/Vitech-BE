package com.hust.vitech.Request;

import lombok.Data;

@Data
public class CategoryRequest {

    private String name;

    private String description;

    private byte[] categoryImageByte;

//    private Long parentId;
}
