package org.fffd.l23o6.pojo.vo.user;

import lombok.Data;

@Data
public class UserVO {
    private String username;
    private String name;
    private String phone;
    private String idn;
    private String type;
    private Integer MileagePoints;
    // done todo: 在后端添加了属性, 但未更新 VO : private Integer MileagePoints;p
}
