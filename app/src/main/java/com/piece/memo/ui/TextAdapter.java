package com.piece.memo.ui;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.piece.memo.R;
import com.piece.memo.database.Paragraph;
import com.piece.memo.database.Text;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {
    protected final Text text;

    @NonNull
    public Text getText() {
        return text;
    }

    public TextAdapter(@NonNull Text text) {
        this.text = text;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paragraph_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setParagraph(text.getParagraphs().get(position));
    }

    @Override
    public int getItemCount() {
        return text.getParagraphs().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected Paragraph paragraph;
        protected final EditText paragraphText;

        @NonNull
        public Paragraph getParagraph() {
            return paragraph;
        }

        public void setParagraph(@NonNull Paragraph paragraph) {
            this.paragraph = paragraph;
            paragraphText.setText(paragraph.getText());
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    v.setBackgroundColor(Color.BLUE);
                } else {
                    v.setBackgroundColor(Color.WHITE);
                }
            });
            paragraphText = itemView.findViewById(R.id.text_paragraph);
            paragraphText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    paragraph.setText(s.toString());
                    paragraph.update();
                }
            });
        }
    }
}
