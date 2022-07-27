package com.bfh.api.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author benfeihu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String name;
    private Integer age;
    private String[] hobbies;
}
