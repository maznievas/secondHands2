package andrey.project.com.secondhands.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import andrey.project.com.secondhands.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private List<String> items;


    public CustomSpinnerAdapter(@NonNull Context context, @NonNull List<String> items) {
        super(context, R.layout.drop_down_view, 0, items);
        this.items = items;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent, true);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return createItemView(i, view, viewGroup, false);
    }

    private View createItemView(int position, View convertView, ViewGroup parent, boolean isDropDown) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.drop_down_view, parent, false);

        TextView tvValue = (TextView) view.findViewById(R.id.textView);
        tvValue.setText(items.get(position));

        int itemMargin = (int) view.getContext().getResources().getDimension(R.dimen.small_padding); //px
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) tvValue.getLayoutParams();
        //if (position == items.size() - 1 ||  (position == 0 && !isDropDown)) {
            layoutParams.setMargins(layoutParams.getMarginStart(), itemMargin, layoutParams.getMarginEnd(), itemMargin);
//        }
//        else {
//            layoutParams.setMargins(layoutParams.getMarginStart(), itemMargin, layoutParams.getMarginEnd(), 0);
//        }
        tvValue.setLayoutParams(layoutParams);

        return view;
    }
}
