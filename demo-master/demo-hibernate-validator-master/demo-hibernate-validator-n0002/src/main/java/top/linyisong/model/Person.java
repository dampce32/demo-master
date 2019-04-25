package top.linyisong.model;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class Person {
    @NotNull
    @Range(min = 1,max = Integer.MAX_VALUE,message = "必须大于0",groups = {GroupA.class})
    /**用户id*/
    private Integer userId;
    @NotBlank
    @Length(min = 4,max = 20,message = "必须在[4,20]",groups = {GroupB.class})
    /**用户名*/
    private String userName;
    @NotBlank
    @Range(min = 0,max = 100,message = "年龄必须在[0,100]",groups={Default.class})
    /**年龄*/
    private Integer age;
    @Range(min = 0,max = 2,message = "性别必须在[0,2]",groups = {GroupB.class})
    /**性别 0：未知；1：男；2：女*/
    private Integer sex;
    
    
    public interface GroupA {
    }

    public interface GroupB {
    }
    
    @GroupSequence({GroupA.class, GroupB.class, Default.class})
    public interface GroupOrder {
    }


	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
}