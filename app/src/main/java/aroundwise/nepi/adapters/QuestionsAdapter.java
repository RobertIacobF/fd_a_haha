package aroundwise.nepi.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

import aroundwise.nepi.R;
import aroundwise.nepi.network.model.Answer;

/**
 * Created by Adonis on 5/25/2017.
 */
public class QuestionsAdapter extends SupportAnnotatedAdapter implements QuestionsAdapterBinder {

    @ViewType(
            layout = R.layout.row_multiple_poll,
            views =
                    {
                            @ViewField(id = R.id.row, type = RelativeLayout.class, name = "rlRow"),
                            @ViewField(id = R.id.ivRow, type = ImageView.class, name = "ivRow"),
                            @ViewField(id = R.id.tvRow, type = TextView.class, name = "tvRow"),
                            @ViewField(id = R.id.btn_row, type = Button.class, name = "btnRow")
                    }
    )
    public final int multipleRow = 0;

    Context context;
    List<Answer> answers;
    int positionSelected = -1;

    public QuestionsAdapter(Context context, List<Answer> answers) {
        super(context);
        this.answers = answers;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return answers != null ? answers.size() : 0;
    }

    @Override
    public void bindViewHolder(final QuestionsAdapterHolders.MultipleRowViewHolder vh, final int position) {

        Answer answer = answers.get(position);
        vh.ivRow.setSelected(false);
        vh.rlRow.setSelected(false);
        vh.tvRow.setTextColor(context.getResources().getColor(android.R.color.primary_text_dark));
        vh.tvRow.setText(answer.getAnswer());
        vh.btnRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.ivRow.setSelected(true);
                vh.rlRow.setSelected(true);
                vh.tvRow.setTextColor(Color.WHITE);
                positionSelected = position;
                notifyDataSetChanged();
            }
        });
        if (position == positionSelected) {
            vh.ivRow.setSelected(true);
            vh.rlRow.setSelected(true);
            vh.tvRow.setTextColor(Color.WHITE);
        }
        if (answer.isUserAnswered()){
            vh.ivRow.setSelected(true);
            vh.rlRow.setSelected(true);
            vh.tvRow.setTextColor(Color.WHITE);
        }
    }


}
