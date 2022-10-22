package com.example.qingge_springboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private String code;
    private String msg;
    private Object data;

    public static Result success(){
        return new Result(Constants.CODE_200,null,null);
    }

    public static Result success(Object data){
        return new Result(Constants.CODE_200,null,data);
    }
    public static Result error(){
        return new Result(Constants.CODE_500,null,null);
    }
    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }
}
