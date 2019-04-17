package top.linyisong.model;

import top.linyisong.CaseMode;
import top.linyisong.CheckCase;

public class Demo{
    @CheckCase(value = CaseMode.LOWER,message = "userName必须是小写")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

