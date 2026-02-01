package com.Research.Assistant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResearchRequest {
    private String operation; // summarize, suggest, explain, etc.
    private String content;   // text input
}
