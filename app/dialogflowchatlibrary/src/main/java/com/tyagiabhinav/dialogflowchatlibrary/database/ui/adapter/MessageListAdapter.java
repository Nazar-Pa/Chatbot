package com.tyagiabhinav.dialogflowchatlibrary.database.ui.adapter;

import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.TypeConverters;

import com.tyagiabhinav.dialogflowchatlibrary.ChatbotActivity;
import com.tyagiabhinav.dialogflowchatlibrary.R;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Message;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Note;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.FoodRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.MessageRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.DateConverter;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.MessageDiffUtil;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.NoteDiffUtil;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimeStampConvert;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimestampConverter;


import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> messages;
    public MessageListAdapter(List<Message> messages) {
        this.messages = messages;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE.TYPE_FRIEND_MSG.ordinal()){
            return new FromMsgHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_friend, null));
        } else {
            return new ToMsgHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_me, null));
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = getItem(position);
        int itemViewType = getItemViewType(position);
        if(itemViewType == ITEM_TYPE.TYPE_FRIEND_MSG.ordinal()){
            ((FromMsgHolder)holder).messageText.setText(message.getBotMessage());
//            ((FromMsgHolder)holder).imageView.setImageBitmap(TimestampConverter.byteToBitmap(message.getViewImage()));
//            ImageView ii = new ImageView(this);
//            ii.setBackgroundResource(message.getView());
//            ((FromMsgHolder)holder).linearLayout.setBackgroundResource(message.getView());
        }
        else {
            ((ToMsgHolder)holder).messageText.setText(message.getUserMessage());
        }

    }


    public enum ITEM_TYPE {
        TYPE_FRIEND_MSG,
        TYPE_ME_MSG
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).isFriendMsg()) {
            return ITEM_TYPE.TYPE_FRIEND_MSG.ordinal();
        }else {
            return ITEM_TYPE.TYPE_ME_MSG.ordinal();
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public Message getItem(int position) {
        return messages.get(position);
    }

    public void addTasks(List<Message> newMessages) {
        MessageDiffUtil messageDiffUtil = new MessageDiffUtil(messages, newMessages);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(messageDiffUtil);
        messages.clear();
        messages.addAll(newMessages);
        diffResult.dispatchUpdatesTo(this);
    }

    public static class FromMsgHolder extends RecyclerView.ViewHolder {

        private TextView messageText;
        private LinearLayout linearLayout;
        private ImageView imageView;

        public FromMsgHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.botMessage);
            linearLayout = itemView.findViewById(R.id.msgLinearLayout);
            imageView = itemView.findViewById(R.id.image);

        }
    }

    public static class ToMsgHolder extends RecyclerView.ViewHolder {

        private TextView messageText;


        public ToMsgHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.userMessage);

        }
    }
}
