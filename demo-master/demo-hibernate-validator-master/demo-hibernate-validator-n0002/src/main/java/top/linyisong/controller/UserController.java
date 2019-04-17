package top.linyisong.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import top.linyisong.model.Demo;
import top.linyisong.model.Demo2;
import top.linyisong.model.Demo3;
import top.linyisong.model.Person;
import top.linyisong.model.Person.GroupA;
import top.linyisong.model.Person.GroupB;
import top.linyisong.model.Person.GroupOrder;
import top.linyisong.model.User;

@RequestMapping(value="/user") 
@RestController
@Validated
public class UserController {
	
	@Autowired
    private Validator validator;
    
    @RequestMapping("/model")
    public void model(){
        Demo2 demo2 = new Demo2();
        demo2.setAge("111");
        demo2.setHigh(150);
        demo2.setLength("ABCD");
        
        List<String> list = new ArrayList<String>();
        list.add("111");
        list.add("222");
        list.add("333");
        demo2.setList(list);
        
        Demo3 demo3 = new Demo3();
        demo3.setExtField("extField");
        demo2.setDemo3(demo3);
        
        Set<ConstraintViolation<Demo2>> violationSet = validator.validate(demo2);
        for (ConstraintViolation<Demo2> model : violationSet) {
            System.out.println(model.getMessage());
        }
    }
    
    @RequestMapping("/model2")
    public void model2(){
        Demo2 demo2 = new Demo2();
        demo2.setAge("111");
        demo2.setHigh(150);
        demo2.setLength("ABCDE");
        
        List<String> list = new ArrayList<String>();
        list.add("111");
        list.add("222");
        list.add("333");
        demo2.setList(list);
        
        Set<ConstraintViolation<Demo2>> violationSet = validator.validate(demo2);
        for (ConstraintViolation<Demo2> model : violationSet) {
            System.out.println(model.getMessage());
        }
    }
    
    @RequestMapping("/model3")
    public void model3(){
        Demo2 demo2 = new Demo2();
        demo2.setAge("111");
        demo2.setHigh(150);
        demo2.setLength("ABCDE");
        
        List<String> list = new ArrayList<String>();
        list.add("111");
        list.add("222");
        list.add("333");
        demo2.setList(list);
        
        Demo3 demo3 = new Demo3();
        demo3.setExtField("ABC");
        demo2.setDemo3(demo3);
        
        Set<ConstraintViolation<Demo2>> violationSet = validator.validate(demo2);
        for (ConstraintViolation<Demo2> model : violationSet) {
            System.out.println(model.getMessage());
        }
    }
    
    
    @RequestMapping("/demo5")
    public void demo5(){
        Person p = new Person();
        /**GroupA验证不通过*/
        p.setUserId(-12);
        /**GroupA验证通过*/
        //p.setUserId(12);
        p.setUserName("a");
        p.setAge(110);
        p.setSex(5);
        Set<ConstraintViolation<Person>> validate = validator.validate(p, GroupA.class, GroupB.class);
        for (ConstraintViolation<Person> item : validate) {
            System.out.println(item);
        }
    }
    
    @RequestMapping("/demo6")
    public void demo6(@Validated({GroupA.class, GroupB.class}) Person p, BindingResult result){
        if(result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError error : allErrors) {
                System.out.println(error);
            }
        }
    }
    
    @RequestMapping("/demo7")
    public void demo7(){
        Person p = new Person();
        /**GroupA验证不通过*/
        //p.setUserId(-12);
        /**GroupA验证通过*/
        p.setUserId(12);
        p.setUserName("a");
        p.setAge(110);
        p.setSex(5);
        Set<ConstraintViolation<Person>> validate = validator.validate(p, GroupOrder.class);
        for (ConstraintViolation<Person> item : validate) {
            System.out.println(item);
        }
    }
    
    @RequestMapping("/demo8")
    public void demo8(@Validated({GroupOrder.class}) Person p, BindingResult result){
        if(result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError error : allErrors) {
                System.out.println(error);
            }
        }
    }
    
    @RequestMapping("/demo4")
    public void demo4(){
        Demo demo = new Demo();
        demo.setUserName("userName");
        Set<ConstraintViolation<Demo>> validate = validator.validate(demo);
        for (ConstraintViolation<Demo> dem : validate) {
            System.out.println(dem.getMessage());
        }
    }


	@RequestMapping("/create")
    public void create(@RequestBody @Valid User user, BindingResult result){
        if(result.hasErrors()){
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
        }
    }
	
	 /**如果只有少数对象，直接把参数写到Controller层，然后在Controller层进行验证就可以了。*/
    @RequestMapping(value = "/requestParam", method = RequestMethod.GET)
    public void requestParam(@Range(min = 1, max = 9, message = "年级只能从1-9")
                      @RequestParam(name = "grade", required = true)
                      int grade,
                      
                      @Min(value = 1, message = "班级最小只能1")
                      @Max(value = 99, message = "班级最大只能99")
                      @RequestParam(name = "classroom", required = true)
                      int classroom) {
        System.out.println(grade + "," + classroom);
    }
	
}
