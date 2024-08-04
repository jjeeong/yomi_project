package kr.co.iei.restr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlogSearchResult {
    private String title;
    private String link;
    private String description;
    private String bloggerName;
    private String postDate;
}