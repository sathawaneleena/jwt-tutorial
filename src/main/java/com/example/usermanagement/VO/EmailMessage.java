package com.example.usermanagement.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage implements Serializable {
    private String to;
    private String subject;
    private String message;
}

