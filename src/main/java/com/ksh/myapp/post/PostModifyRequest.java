package com.ksh.myapp.post;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class PostModifyRequest {

    private String menu;
    private String title;
    private String content;
    private String image;

}