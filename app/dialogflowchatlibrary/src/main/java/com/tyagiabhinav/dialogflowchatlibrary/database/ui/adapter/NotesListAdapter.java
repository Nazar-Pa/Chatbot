package com.tyagiabhinav.dialogflowchatlibrary.database.ui.adapter;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.tyagiabhinav.dialogflowchatlibrary.R;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Food;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Note;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.FoodRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.NoteRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.DateConverter;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.NoteDiffUtil;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimeStampConvert;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimestampConverter;


import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.CustomViewHolder> {

    private static final String TAG = NotesListAdapter.class.getSimpleName();
    private List<Note> notes;
    public NotesListAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Note note = getItem(position);
        holder.deleteButton.setId(note.getId());
            holder.itemTitle.setText(note.getTitle());
            holder.itemDate.setText(TimestampConverter.dateToTimestamp(note.getCreatedDate()));
            holder.itemStartedAt.setText(TimestampConverter.TimeToTimestamp(note.getStartedAt()));
            holder.itemDuration.setText(note.getDuration());


    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Note getItem(int position) {
        return notes.get(position);
    }

    public void addTasks(List<Note> newNotes) {
        NoteDiffUtil noteDiffUtil = new NoteDiffUtil(notes, newNotes);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(noteDiffUtil);
        notes.clear();
        notes.addAll(newNotes);
        diffResult.dispatchUpdatesTo(this);
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTitle, itemDate, itemStartedAt, itemDuration;
        private ImageView deleteButton;
        public NoteRepository noteRepository;
        public CustomViewHolder(final View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.item_title);
            itemDate = itemView.findViewById(R.id.date);
            itemStartedAt = itemView.findViewById(R.id.startedAt);
            itemDuration = itemView.findViewById(R.id.duration);
            deleteButton = itemView.findViewById(R.id.detail_button);

            noteRepository = new NoteRepository(itemView.getContext());
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Food food = foodRepository.getFoodItem(v.getId());
                    Note note = getItem(getAdapterPosition());
                    noteRepository.deleteTask(note);
                    notes.remove(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), notes.size());
//                    itemView.setVisibility(View.GONE);
                    notifyItemRemoved(getAdapterPosition());
                    notifyDataSetChanged();

                }
            });

        }
    }




}
