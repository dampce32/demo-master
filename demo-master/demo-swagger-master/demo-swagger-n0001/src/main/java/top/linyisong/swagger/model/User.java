package top.linyisong.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="用户")
public class User {
	
	@ApiModelProperty(value="id")
    private Long id;
	
	@ApiModelProperty(required=true,value="姓名",example="张三")
    private String name;
	
	@ApiModelProperty(required=true,value="年龄",allowableValues="range[0, 150]",example="18")
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
