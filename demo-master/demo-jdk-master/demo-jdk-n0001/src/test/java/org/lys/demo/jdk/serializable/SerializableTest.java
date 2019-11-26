package org.lys.demo.jdk.serializable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * 序列化测试
 * - 把对象转换为字节序列的过程称为对象的序列化。
   - 把字节序列恢复为对象的过程称为对象的反序列化。
    对象的序列化主要有两种用途：
　　1） 把对象的字节序列永久地保存到硬盘上，通常存放在一个文件中；
　　2） 在网络上传送对象的字节序列。
    如果一个类，没有自定义serialVersionUID，当类被修改（如增加属性），会重新生成默认serialVersionUID
 * @author lin.yisong
 * @since 2019年11月26日
 */
public class SerializableTest {

    private static ObjectInputStream ois;

    public static void main(String[] args) throws Exception {
        serializePerson();
        Person p = deserializePerson();
        System.out.println(p.getName()+";"+p.getAge());
    }
    
    private static void serializePerson() throws FileNotFoundException,IOException {
        Person person = new Person();
        person.setName("测试实例");
        person.setAge(25);
        person.setSex("male");

        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File("D:/person.txt")));
        oo.writeObject(person);
        System.out.println("序列化成功");
        oo.close();
    }
    
    private static Person deserializePerson() throws IOException, Exception {
        ois = new ObjectInputStream(new FileInputStream(new File("D:/person.txt")));
        Person person = (Person) ois.readObject();
        System.out.println("反序列化成功");
        return person;
    }
}
