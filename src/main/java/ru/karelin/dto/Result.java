package ru.karelin.dto;


public class Result {
    boolean success;
    public Result(){
        this.success=true;
    }
    public Result(boolean isSuccess){
        this.success=isSuccess;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
