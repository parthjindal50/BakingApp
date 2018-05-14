package in.co.parthjindal.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import in.co.parthjindal.bakingapp.R;
import in.co.parthjindal.bakingapp.models.Recipe;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private int id;
    private Context mContext;
    private OnStepClickListener mListener;
    private ArrayList<Recipe> mRecipe;
    private String mRecipeName;

    public interface OnStepClickListener {
        void onStepSelected(View v, int position, int id);
    }

    public HomeListAdapter(ArrayList<Recipe> mRecipe, Context context, OnStepClickListener listener) {
        this.mRecipe = mRecipe;
        this.mListener = listener;
        this.mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_name)
        TextView recipeName;
        @BindView(R.id.recipe_servings)
        TextView recipeServings;
        @BindView(R.id.recipe_cardview)
        CardView recipeCardView;
        @BindView(R.id.recipe_icon)
        ImageView recipeIcon;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public HomeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeListAdapter.ViewHolder holder, final int position) {
        mRecipeName = mRecipe.get(position).getName();
        String imageUrl = mRecipe.get(position).getmImageUrl();

        Glide.with(mContext)
                .load(imageUrl)
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .into(holder.recipeIcon);
        holder.recipeName.setText(mRecipeName);
        holder.recipeServings.setText(String.valueOf(mRecipe.get(position).getServings()));

        holder.recipeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = mRecipe.get(position).getId();
                mListener.onStepSelected(view, position, id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecipe.size();
    }
}
