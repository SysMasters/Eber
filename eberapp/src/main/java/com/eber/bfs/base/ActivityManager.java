package com.eber.bfs.base;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by admin on 2016/4/26.
 * TODO Activity 管理类
 */
public class ActivityManager {
    private static ActivityManager instance;
    private static Stack<Activity> stack;

    public static ActivityManager getInstance(){
        if(instance == null){
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加一个activity实例
     * @param activity
     */
    public void addActivity(Activity activity){
        if(activity == null){
            return;
        }
        if(stack == null){
            stack = new Stack<>();
        }
        stack.add(activity);
    }

    /**
     * 删除一个activity实例
     * @param activity
     */
    public void removeActivity(Activity activity){
        if(activity == null){
            return;
        }
        if(stack == null){
            stack = new Stack<>();
        }
        if(stack.contains(activity)){
            stack.remove(activity);
        }
    }

    /**
     * 获取栈顶的activity
     * @return
     */
    public Activity getTopActivity(){
        if(stack == null || stack.size() == 0){
            return null;
        }
        return stack.lastElement();
    }

    public void exitAllBarringStackTop(){
        if(stack != null){
            for(Activity activity : stack){
                if (activity != getTopActivity()) {
                    stack.remove(activity);
                    activity.finish();
                }
            }
        }
    }

    /**
     * 退出所有栈中的activity
     */
    public void exitAll(){
        if(stack != null){
            for(Activity activity : stack){
                stack.remove(activity);
                activity.finish();
            }
        }
    }

}
