package com.yuan.dynamic;

import android.graphics.drawable.Drawable;

public class ProcessInfo {
    public String name;
    public String packageName;
    public Drawable icon;
    public long memory;
    public boolean isUser; //true表示用户进程
    public boolean isChecked; //表示当前item是否被勾选

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", icon=" + icon +
                ", memory=" + memory +
                ", isUser=" + isUser +
                ", isChecked=" + isChecked +
                '}';
    }
}