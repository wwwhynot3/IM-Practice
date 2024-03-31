package com.huanglb.common.Msg;

public class MapperMsg extends Msg {
    public static final MapperMsg QUERY_SUCCESS = new MapperMsg(200, "查询成功");
    public static final MapperMsg QUERY_FAILURE = new MapperMsg(400, "查询失败");
    public static final MapperMsg INVALID_LOGIN = new MapperMsg(400, "用户名或密码错误");
    public static final MapperMsg SUCCESSFUL_LOGIN = new MapperMsg(200, "登陆成功");
    public static final MapperMsg MODIFY_SUCCESS = new MapperMsg(200, "修改成功");
    public static final MapperMsg MODIFY_FAILURE = new MapperMsg(400, "修改失败");
    public static final MapperMsg DELETE_SUCCESS = new MapperMsg(200, "删除成功");
    public static final MapperMsg DELETE_FAILURE = new MapperMsg(400, "删除失败");
    public static final MapperMsg ADD_SUCCESS = new MapperMsg(200, "添加成功");
    public static final MapperMsg ADD_FAILURE = new MapperMsg(400, "添加失败");
    public static final MapperMsg UPLOAD_FILE_SUCCESS = new MapperMsg(200, "文件上传成功");
    public static final MapperMsg UPLOAD_FILE_FAILURE = new MapperMsg(400, "文件上传失败");

    protected MapperMsg(int code, String msg) {
        super(code, msg);
    }
}
