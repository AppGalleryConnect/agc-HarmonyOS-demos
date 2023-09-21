package com.smarket.hdc2023.bean;



public class TaskList {
    public Integer id;
    public String name;//任务组/任务名称
    public String explanation;//说明
    public String remarks;//备注
    public Integer type;//类型：1-任务组、2-任务
    public Integer sort;//排序
    public String taskType;//任务类型：checkIn-签到、huntCard-寻宝卡片
    public String actionUrl;//跳转地址
    public Integer completionNum;//已经完成次数
    public Integer taskNum;//任务组下的任务数，如果类型是任务的话固定为1
    public Integer isComplete;//是否已经完成：0-否、1-是

}
