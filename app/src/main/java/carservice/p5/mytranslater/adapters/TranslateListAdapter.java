package carservice.p5.mytranslater.adapters;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import carservice.p5.mytranslater.R;
import carservice.p5.mytranslater.mvp.Models.TranslateModel;
import io.reactivex.disposables.CompositeDisposable;


public class TranslateListAdapter extends RecyclerView.Adapter<TranslateListAdapter.ViewHolder> {
    private Context context;
    private List<TranslateModel> modelList;
    private ListenerPost listenerPost;
    private ListenerDelete listenerDelete;

    public TranslateListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<TranslateModel> modelList)
    {
        notifyDataSetChanged();
        this.modelList = modelList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View blockRoot;
        TextView create_date;
        TextView title;
        TextView txt_comment;
        ImageButton btnDel;

        ViewHolder(View holderView) {
            super(holderView);

            blockRoot = (View) holderView.findViewById(R.id.blockRoot);
            create_date = (TextView) holderView.findViewById(R.id.date_create);
            title = (TextView) holderView.findViewById(R.id.title);
            txt_comment = (TextView) holderView.findViewById(R.id.comment);
            btnDel = (ImageButton) holderView.findViewById(R.id.delete);
        }
    }


    @NonNull
    @Override
    public TranslateListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void setListenerPost(ListenerPost listenerPost) {
        this.listenerPost = listenerPost;
    }

    public void setListenerDeletePost(ListenerDelete listenerDelete) {
        this.listenerDelete = listenerDelete;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        try {

             TranslateModel model =  modelList.get(position);
             int id = model.getId();
             String textIn = model.getTextIn();
             String textTo = model.getTextTo();

            //Вывод данных

            viewHolder.title.setText(textIn);
            viewHolder.txt_comment.setText(textTo);

            viewHolder.blockRoot.setOnClickListener(v -> {
                if(listenerPost!=null)
                listenerPost.onClick(id);
            });

            viewHolder.btnDel.setOnClickListener(view -> {
                deletePost(viewHolder.itemView, position);
                if(listenerDelete!=null)
                listenerDelete.onClick(id);
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deletePost(View view, final int position) {

        modelList.remove(position);
        notifyDataSetChanged();

        view.animate().scaleX(0).scaleY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                notifyItemRemoved(position);
               // notifyDataSetChanged();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

    }


    public interface ListenerPost {
         void onClick(int position);
    }

    public interface ListenerDelete {
         void onClick(int position);
    }
}
