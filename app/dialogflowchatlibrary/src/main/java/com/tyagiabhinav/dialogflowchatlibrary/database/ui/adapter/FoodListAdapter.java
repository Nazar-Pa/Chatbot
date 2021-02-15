package com.tyagiabhinav.dialogflowchatlibrary.database.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.tyagiabhinav.dialogflowchatlibrary.ChatbotActivity;
import com.tyagiabhinav.dialogflowchatlibrary.R;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Food;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Note;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.FoodRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.FoodDiffUtil;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.NoteDiffUtil;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimestampConverter;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.CustomViewHolder> {


    private static final String TAG = FoodListAdapter.class.getSimpleName();
    public List<Food> foods;
    public FoodListAdapter(List<Food> foods) {
        this.foods = foods;

    }



    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Food food = getItem(position);


        //My Notes page`inde Title ve Time`in gorunmesi burda olur

            holder.deleteButton.setId(food.getId());
            holder.foodPortion.setText(String.valueOf(food.getPortion()) + " portions");
            holder.foodCreatedAt.setText(TimestampConverter.TimeToTimestamp(food.getFoodCreatedAt()));
            holder.foodDate.setText(TimestampConverter.dateToTimestamp(food.getFoodCreatedDate()));
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }


    public Food getItem(int position) {
        return foods.get(position);
    }

    public void addTasks(List<Food> newFoods) {
        FoodDiffUtil foodDiffUtil = new FoodDiffUtil(foods, newFoods);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(foodDiffUtil);
        foods.clear();
        foods.addAll(newFoods);
        diffResult.dispatchUpdatesTo(this);
    }



    protected class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView foodPortion, foodDate, foodCreatedAt;
        public ImageView deleteButton;
        public FoodRepository foodRepository;

        public CustomViewHolder(final View itemView) {
            super(itemView);

            foodPortion = itemView.findViewById(R.id.item_title);
            foodDate = itemView.findViewById(R.id.date);
            foodCreatedAt = itemView.findViewById(R.id.foodCreatedAt);
            deleteButton = itemView.findViewById(R.id.detail_button2);

            foodRepository = new FoodRepository(itemView.getContext());

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Food food = foodRepository.getFoodItem(v.getId());
                    Food food = getItem(getAdapterPosition());
                    foodRepository.deleteTask(food);
                    foods.remove(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), foods.size());
                    notifyItemRemoved(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

        }




        public void onClick(View view){

            if(view.equals(deleteButton)){
                Log.d(TAG, "Nazar delete button  clicked");
                foods.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), foods.size());
            }
        }


    }


//    public void onClickDeleteButton(View view){
//        foodSize = foodRepository.ItemsCount();
//        if(foodSize!=0){
//            int id = view.getId();
//
//            Food food = foodRepository.getFoodItem(id);
//            foodRepository.deleteTask(food);
//            //foodRepository.deleteFood(food);
//            //foodRepository.deleteFoodItem(id);
//        }
//    }

}
