package com.example.zyq.kaminotetest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zyq.kaminotetest.Activity.EditNote;
import com.example.zyq.kaminotetest.Activity.LabelSelector;
import com.example.zyq.kaminotetest.Activity.MainActivity;
import com.example.zyq.kaminotetest.Class.MyNote;
import com.example.zyq.kaminotetest.R;
import com.example.zyq.kaminotetest.Utils.ActivityController;

import java.util.List;

import fragment.HomeFragment;

/**
 * Created by zyq on 2018/3/2.
 * 来源：CSDN，作者信息已忘，向原作者致歉
 */

public class NoteAdapter2 extends RecyclerView.Adapter<NoteAdapter2.NotesViewHolder> {

    private List<MyNote> myNotes;
    private Context context;

    public NoteAdapter2(List<MyNote> myNotes, Context context) {
        this.myNotes = myNotes;
        this.context = context;
    }

    //自定义ViewHolder类
    static class NotesViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView noteTitle;
        TextView noteContent;
        TextView editedDate;
        ImageButton selectLabels;

        public NotesViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            noteTitle = itemView.findViewById(R.id.note_title2);
            noteContent = itemView.findViewById(R.id.note_content2);
            editedDate = itemView.findViewById(R.id.note_edited_date2);
            selectLabels = itemView.findViewById(R.id.select_labels_for_note);

            noteTitle.setBackgroundColor(Color.argb(20, 0, 0, 0));
            noteTitle.setBackgroundResource(R.drawable.corner_view);
        }
    }


    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.note_card_item, viewGroup, false);
        final NotesViewHolder notesViewHolder = new NotesViewHolder(view);
//        final NoteAdapter.ViewHolder holder = new NoteAdapter.ViewHolder(view);
        //创建点击响应事件
        notesViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = notesViewHolder.getAdapterPosition();
                MainActivity.notePosition = position;
                //在点击项目的时候跳转到编辑页
                Intent jumpToEditNote = new Intent(view.getContext(), EditNote.class);
                jumpToEditNote.putExtra("note_position", position);
                view.getContext().startActivity(jumpToEditNote);
            }
        });

        notesViewHolder.selectLabels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = notesViewHolder.getAdapterPosition();    //获得点击的笔记位置
                //点击书签按钮时跳转到标签选择页
                Intent intent = new Intent(view.getContext(), LabelSelector.class);
                intent.putExtra("note_position", position);
                view.getContext().startActivity(intent);
            }
        });

        //创建长按事件
        notesViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Snackbar.make(view, "长按事件：", Snackbar.LENGTH_SHORT).show();
                HomeFragment.longClickPosition = notesViewHolder.getAdapterPosition();
                MainActivity.notePosition--;
                //储存位置
                SharedPreferences sharedPreference = ActivityController.activities.get(0).getSharedPreferences("notePosition",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreference.edit();
                editor.putInt("position",MainActivity.notePosition);
                editor.apply();
                return false;
            }
        });

        return notesViewHolder;
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        MyNote note = myNotes.get(position);
        holder.noteTitle.setText(note.getTitle().equals("") ? "无标题" : note.getTitle());
        holder.noteContent.setText(note.getContent().equals("") ? "无附加内容" : note.getContent());
        String lastEdited = "最后编辑：" + note.getLastEdited();
        holder.editedDate.setText(lastEdited);
    }

    //获得当前列表笔记数目
    @Override
    public int getItemCount() {
        return myNotes.size();
    }


}
