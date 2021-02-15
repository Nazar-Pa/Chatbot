package com.tyagiabhinav.dialogflowchatlibrary.database.util;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.tyagiabhinav.dialogflowchatlibrary.database.model.Food;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Note;

import java.util.List;

public class FoodDiffUtil extends DiffUtil.Callback {


    List<Food> oldFoodList;
    List<Food> newFoodList;


    public FoodDiffUtil(List<Food> oldFoodList, List<Food> newFoodList) {
        this.oldFoodList = oldFoodList;
        this.newFoodList = newFoodList;
    }

    @Override
    public int getOldListSize() {
        return oldFoodList.size();
    }

    @Override
    public int getNewListSize() {
        return newFoodList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFoodList.get(oldItemPosition).getId() == newFoodList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFoodList.get(oldItemPosition).equals(newFoodList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
