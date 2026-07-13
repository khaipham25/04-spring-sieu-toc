package vn.hoidanit.springsieutoc.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserFilterRequestDTO {
    private String name;
    private String address;
    private String email;
    private String roleName;

}
