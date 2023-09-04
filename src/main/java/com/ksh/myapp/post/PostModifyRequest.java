package com.ksh.myapp.post;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostModifyRequest {

    private String title;
    private String review;
    //    @Column(nullable = false)



}
