package com.example.usermanagement.VO;

import com.example.usermanagement.entity.UserM;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTemplateVO {
private UserM userM;
private EmailMessage emailMessage;

}
