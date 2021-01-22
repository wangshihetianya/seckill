package com.zhl.sportplay.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int id;
    private int activityID;
    private int userID;

    public Order(int activityID, int userID) {
        this.activityID = activityID;
        this.userID = userID;
    }
}
