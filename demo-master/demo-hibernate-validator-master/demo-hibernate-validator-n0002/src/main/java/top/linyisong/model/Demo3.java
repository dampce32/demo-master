package top.linyisong.model;

import org.hibernate.validator.constraints.Length;

public class Demo3 {
    @Length(min = 5, max = 17, message = "级联对象属性extField：length长度在[5,17]之间")
    private String extField;

	public String getExtField() {
		return extField;
	}

	public void setExtField(String extField) {
		this.extField = extField;
	}
}
