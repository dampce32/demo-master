package top.linyisong.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class Demo2 {
    
	@Length(min = 5, max = 17, message = "length长度在[5,17]之间")
    private String length;

    /**@Size不能验证Integer，适用于String, Collection, Map and arrays*/
    @Size(min = 1, max = 3, message = "size在[1,3]之间")
    private String age;

    @Range(min = 150, max = 250, message = "range在[150,250]之间")
    private int high;

    @Size(min = 3,max = 5,message = "list的Size在[3,5]")
    private List<String> list;
    
    @NotNull(message="级联对象不能为空")
    @Valid
    private Demo3 demo3;

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public Demo3 getDemo3() {
		return demo3;
	}

	public void setDemo3(Demo3 demo3) {
		this.demo3 = demo3;
	}
    
    
    
}
