package aroundwise.nepi.util.views;

/**
 * Created by mihai on 29/08/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import aroundwise.nepi.R;
import aroundwise.nepi.util.Util;

public class CustomActionBar extends RelativeLayout {
    RelativeLayout layoutMain;
    ImageButton btnSearch;
    ImageButton btnFilter;
    Button btnBack, btnOk;
    RelativeLayout rlSearch;
    RelativeLayout rlMain;
    ImageButton btnCancelSearch;
    ImageButton btnLocation;
    EditText etSearch;
    boolean hasSearch;
    boolean hasSpinner;
    String title;
    CustomSpinner spinner;
    TextView tv_title;
    boolean hasBackButton;
    int titleColor;
    boolean hasBackButtonWhite;
    int backBtnColor;
    boolean hasLocation;
    boolean hasOkButton;

    public CustomActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomActionbar,
                0, 0);
        try {
            hasSearch = a.getBoolean(R.styleable.CustomActionbar_hasSearch, false);
            //hasSearch = false;
            title = a.getString(R.styleable.CustomActionbar_actionbarTitle);
            hasSpinner = a.getBoolean(R.styleable.CustomActionbar_hasSpinner, false);
            hasBackButton = a.getBoolean(R.styleable.CustomActionbar_hasBackButton, false);
            titleColor = a.getColor(R.styleable.CustomActionbar_titleColor, Color.BLACK);
            backBtnColor = a.getColor(R.styleable.CustomActionbar_backBtnColor, ContextCompat.getColor(context, R.color.appbar_title_color_dark));
            hasBackButtonWhite = a.getBoolean(R.styleable.CustomActionbar_hasBackBtnWhiteArrow, false);
            hasLocation = a.getBoolean(R.styleable.CustomActionbar_hasLocationButton, false);
            hasOkButton = a.getBoolean(R.styleable.CustomActionbar_hasOkButton, false);

        } finally {
            a.recycle();
        }

        init(context);
    }

    private void init(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_actionbar, this, true);

        tv_title = (TextView) findViewById(R.id.actionbar_title);
        layoutMain = (RelativeLayout) findViewById(R.id.actionbar_rl_main);
        btnSearch = (ImageButton) findViewById(R.id.actionbar_btn_search);
        rlSearch = (RelativeLayout) findViewById(R.id.actionbar_rl_search);
        rlMain = (RelativeLayout) findViewById(R.id.actionbar_rl_main);
        btnCancelSearch = (ImageButton) findViewById(R.id.actionbar_btn_cancel);
        btnFilter = (ImageButton) findViewById(R.id.actionbar_btn_filter);
        etSearch = (EditText) findViewById(R.id.actionbar_et_search);
        spinner = (CustomSpinner) findViewById(R.id.spinner);
        btnBack = (Button) findViewById(R.id.actionbar_btn_back);
        btnLocation = (ImageButton) findViewById(R.id.actionbar_location_button);
        btnOk = (Button) findViewById(R.id.btn_ok);
        if (hasOkButton)
            btnOk.setVisibility(VISIBLE);
        btnBack.setTextColor(backBtnColor);
        if (hasBackButtonWhite) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.icon_arror_left_white);
            btnBack.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        if (hasLocation)
            btnLocation.setVisibility(VISIBLE);
        tv_title.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNova-Regular.otf"));
        tv_title.setText(title);
        tv_title.setLetterSpacing(0.2f);
        tv_title.setTextColor(titleColor);

        if (hasSpinner) {
            spinner.setVisibility(VISIBLE);
            tv_title.setVisibility(INVISIBLE);
        } else {
            spinner.setVisibility(INVISIBLE);
            tv_title.setVisibility(VISIBLE);
        }
        if (hasBackButton) {
            btnBack.setVisibility(View.VISIBLE);
        } else
            btnBack.setVisibility(View.GONE);
        if (hasSearch) {
            btnSearch.setVisibility(View.VISIBLE);
        } else
            btnSearch.setVisibility(View.GONE);

        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rlMain.setVisibility(GONE);
                rlSearch.setVisibility(VISIBLE);
                etSearch.requestFocus();
                Util.showSoftKeyboard(context, etSearch);
            }
        });

        btnCancelSearch.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    if (motionEvent.getRawX() > size.x / 2) {
                        etSearch.setText("");
                    } else {
                        rlMain.setVisibility(VISIBLE);
                        rlSearch.setVisibility(GONE);
                        if (context instanceof Activity)
                            Util.hideSoftKeyboard((Activity) context);
                    }
                }

                return false;
            }
        });

    }

    public EditText getEtSearch() {
        return etSearch;
    }

    public void setTitleTypeFace(Typeface t) {
        tv_title.setTypeface(t);
    }

    public void setBtnFilterOnClickListener(OnClickListener listener) {
        btnFilter.setOnClickListener(listener);
    }

    public void setOnActionBarClickListner(OnClickListener clickListner) {
        if (layoutMain != null)
            layoutMain.setOnClickListener(clickListner);
    }

    public void setBtnBackOnClickListener(OnClickListener listener) {
        if (btnBack != null)
            btnBack.setOnClickListener(listener);
    }

    public void setOnOkbtnClickListener(OnClickListener listener) {
        if (btnOk != null)
            btnOk.setOnClickListener(listener);
    }

    public void setActionBarTitle(String title) {
        tv_title.setText(title);
    }

    public CustomSpinner getSpinner() {
        return spinner;
    }

    public void setBtnLocationClickListener(OnClickListener listener) {
        if (btnLocation != null)
            btnLocation.setOnClickListener(listener);
    }

    public void setTitleTextSize(int textSize) {
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    public Button getOkButton() {
        return btnOk;
    }

}
