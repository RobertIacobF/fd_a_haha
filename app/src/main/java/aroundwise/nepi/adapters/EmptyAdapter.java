package aroundwise.nepi.adapters;

import android.content.Context;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import aroundwise.nepi.R;

/**
 * Created by mihai on 29/09/16.
 */
public class EmptyAdapter extends SupportAnnotatedAdapter implements EmptyAdapterBinder {

    @ViewType(
            layout = R.layout.item_empty_row,
            views =
                    {
                            @ViewField(id = R.id.tvEmtpy, type = TextView.class, name = "tvEmpty")
                    }

    )
    public static final int emptyRow = 0;


    private String message;

    public EmptyAdapter(Context context, String message) {
        super(context);
        this.message = message;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void bindViewHolder(EmptyAdapterHolders.EmptyRowViewHolder vh, int position) {
        vh.tvEmpty.setText(message);
    }
}
