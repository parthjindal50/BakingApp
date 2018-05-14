package in.co.parthjindal.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import in.co.parthjindal.bakingapp.R;
import in.co.parthjindal.bakingapp.activities.RecipeDetailsActivity;
import in.co.parthjindal.bakingapp.models.Step;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.ViewHolder> {
    private Context mContext;
    private int mStepId;
    private int mRecipeId;
    OnStepListener mListener;
    private List<Step> mSteps;
    private String mName;


    public StepListAdapter(List<Step> mSteps, OnStepListener listener, String recipeName, int recipeId) {
        this.mListener = listener;
        this.mRecipeId = recipeId;
        this.mSteps = new ArrayList<>();
        this.mName = recipeName;
        this.mSteps.addAll(mSteps);
    }

    public interface OnStepListener {
        void onStepSelected(View v, int position);
    }


    @Override
    public void onBindViewHolder(StepListAdapter.ViewHolder holder, final int position) {
        StringBuilder sb = new StringBuilder();
        sb.append(mSteps.get(position).getId());
        sb.append(". ");
        sb.append(mSteps.get(position).getShortDescription());

        holder.stepTextView.setText(sb);

        holder.stepTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStepId = mSteps.get(position).getId();
                Log.d("StepListAdapter", "StepID: " + mStepId + "RecipeID: " + mRecipeId);
                mListener.onStepSelected(view, position);

                Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
                Type type = new TypeToken<List<Step>>() {
                }.getType();
                String steps = new GsonBuilder().create().toJson(mSteps, type);
                intent.putExtra(RecipeDetailsActivity.STEPS, steps);
                intent.putExtra("current_recipe", mName);
                intent.putExtra("STEP_ID", mStepId);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView stepTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            stepTextView = itemView.findViewById(R.id.step_text_view);
        }
    }

    @Override
    public StepListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list, parent, false);

        return new ViewHolder(view);
    }
}