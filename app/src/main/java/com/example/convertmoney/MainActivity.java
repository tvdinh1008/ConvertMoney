package com.example.convertmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    List<String> nationals;
    List<String> unitSymbol;
    int status=1;//=1 tức nhập cho dòng 1 , =2 tức nhập cho dòng 2
    TextView txt1;
    TextView txt2;
    TextView txt_unit1;
    TextView txt_unit2;
    TextView txtConvert;
    int st1=-1;
    int st2=-1; //0 là vị trí .0 còn 1 là vị trí .00   -1 là chưa có dấu "."
    /*
    trạng thái nhập vào 1 số ví dụ 29.00 (mà trước ta nhập 1 số 0 ->sau khi nhập 1 số 29.01)
    nếu nhập 2 số 0 -> ta sẽ ko nhập đc số nào nữa 29.00 ta có thể dùng C (xóa dần st) để nhập đc tiếp
    nếu chưa nhập số 0 nào -> 29.10
    */

    //TH tính ra từ txt1 ->tính ra kết quả txt2 sau đó ta click txt2 -> khi nhập số cho txt2 nó sẽ xét về 0
    boolean flag=true;//chỉ cần chuyển trạng thái thì ta xét là false sau đó ấn vào nút sẽ trở lại true

    DecimalFormat dcf;
    DecimalFormatSymbols formatSymbols;
    //
    double DOLL_DONG=23435;
    //double DONG_USD=0.00004267;
    double EURO_DONG=25631.63;
    //
    double EURO_DOLL=1.0937;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');
        dcf = new DecimalFormat("###,###,###,###.##", formatSymbols);


        nationals=new ArrayList<>();
        nationals.add("Viet Nam - Dong");
        nationals.add("United States - Dollar");
        nationals.add("Europe - Euro");

        unitSymbol=new ArrayList<>();
        unitSymbol.add("₫");
        unitSymbol.add("$");
        unitSymbol.add("€");

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,nationals);
        Spinner spinner=findViewById(R.id.spinner_view);
        Spinner spinner2=findViewById(R.id.spinner_view_2);
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        txt_unit1=findViewById(R.id.txt_unit_symbol);
        txt_unit2=findViewById(R.id.txt_unit_symbol_2);
        txtConvert=findViewById(R.id.txt_convert);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("TAG",nationals.get(position));
                txt_unit1.setText(unitSymbol.get(position));
                String value1=txt1.getText().toString();
                String value2=txt2.getText().toString();
                String unit1=txt_unit1.getText().toString();
                String unit2=txt_unit2.getText().toString();
                if(status==1) {
                    try {
                        double x1 = (double) dcf.parse(value1).doubleValue();
                        double x2 = (double) dcf.parse(value2).doubleValue();
                        if ((unit1.equals("₫") && unit2.equals("₫")) || (unit1.equals("$") && unit2.equals("$")) || (unit1.equals("€") && unit2.equals("€"))) {
                            txt2.setText(value1);
                            ConvertValue(unit1,unit2,1);
                        } else if (unit1.equals("₫") && unit2.equals("$")) {
                            x2 = x1 * 1 / DOLL_DONG;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit1,unit2,1/DOLL_DONG);
                        } else if (unit1.equals("₫") && unit2.equals("€")) {
                            x2 = x1 * 1 / EURO_DONG;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit1,unit2,1/EURO_DONG);
                        } else if (unit1.equals("$") && unit2.equals("₫")) {
                            x2 = x1 * DOLL_DONG;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit1,unit2,DOLL_DONG);
                        } else if (unit1.equals("$") && unit2.equals("€")) {
                            x2 = x1 * 1 / EURO_DOLL;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit1,unit2,1/EURO_DOLL);
                        } else if (unit1.equals("€") && unit2.equals("$")) {
                            x2 = x1 * EURO_DOLL;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit1,unit2,EURO_DOLL);
                        } else if (unit1.equals("€") && unit2.equals("₫")) {
                            x2 = x1 * EURO_DONG;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit1,unit2,EURO_DONG);
                        }
                    } catch (Exception e) {

                    }
                }
                else
                {
                    try {
                        double x1 = (double) dcf.parse(value1).doubleValue();
                        double x2 = (double) dcf.parse(value2).doubleValue();
                        if ((unit1.equals("₫") && unit2.equals("₫")) || (unit1.equals("$") && unit2.equals("$")) || (unit1.equals("€") && unit2.equals("€"))) {
                            txt1.setText(value1);
                            ConvertValue(unit2,unit1,1);
                        } else if (unit1.equals("₫") && unit2.equals("$")) {
                            x1 = x2 * DOLL_DONG;
                            txt1.setText(String.valueOf(dcf.format(x1)));
                            ConvertValue(unit2,unit1,DOLL_DONG);
                        } else if (unit1.equals("₫") && unit2.equals("€")) {
                            x1 = x2 * EURO_DONG;
                            txt1.setText(String.valueOf(dcf.format(x1)));
                            ConvertValue(unit2,unit1,EURO_DONG);
                        } else if (unit1.equals("$") && unit2.equals("₫")) {
                            x1 = x2 *1/ DOLL_DONG;
                            txt1.setText(String.valueOf(dcf.format(x1)));
                            ConvertValue(unit2,unit1,1/DOLL_DONG);
                        } else if (unit1.equals("$") && unit2.equals("€")) {
                            x1 = x2 * EURO_DOLL;
                            txt1.setText(String.valueOf(dcf.format(x1)));
                            ConvertValue(unit2,unit1,EURO_DOLL);
                        } else if (unit1.equals("€") && unit2.equals("$")) {
                            x1 = x2 *1/ EURO_DOLL;
                            txt1.setText(String.valueOf(dcf.format(x1)));
                            ConvertValue(unit2,unit1,1/EURO_DOLL);
                        } else if (unit1.equals("€") && unit2.equals("₫")) {
                            x1 = x2 *1/ EURO_DONG;
                            txt1.setText(String.valueOf(dcf.format(x1)));
                            ConvertValue(unit2,unit1,1/EURO_DONG);
                        }
                    } catch (Exception e) {

                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("TAG",nationals.get(position));
                txt_unit2.setText(unitSymbol.get(position));

                //0 VND
                //1 USD
                //2 EURO
                String value2=txt1.getText().toString();
                String value1=txt2.getText().toString();
                String unit1=txt_unit1.getText().toString();
                String unit2=txt_unit2.getText().toString();
                if(status==2) {
                    try {
                        double x1 = (double) dcf.parse(value1).doubleValue();
                        double x2 = (double) dcf.parse(value2).doubleValue();
                        if ((unit1.equals("₫") && unit2.equals("₫")) || (unit1.equals("$") && unit2.equals("$")) || (unit1.equals("€") && unit2.equals("€"))) {
                            txt1.setText(value1);
                            ConvertValue(unit2,unit1,1);
                        } else if (unit1.equals("₫") && unit2.equals("$")) {
                            x2 = x1 * DOLL_DONG;
                            txt1.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit2,unit1,DOLL_DONG);
                        } else if (unit1.equals("₫") && unit2.equals("€")) {
                            x2 = x1 * EURO_DONG;
                            txt1.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit2,unit1,EURO_DONG);
                        } else if (unit1.equals("$") && unit2.equals("₫")) {
                            x2 = x1 * 1 / DOLL_DONG;
                            txt1.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit2,unit1,1/DOLL_DONG);
                        } else if (unit1.equals("$") && unit2.equals("€")) {
                            x2 = x1 * EURO_DOLL;
                            txt1.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit2,unit1,EURO_DOLL);
                        } else if (unit1.equals("€") && unit2.equals("$")) {
                            x2 = x1 * 1 / EURO_DOLL;
                            txt1.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit2,unit1,1/EURO_DOLL);
                        } else if (unit1.equals("€") && unit2.equals("₫")) {
                            x2 = x1 * 1 / EURO_DONG;
                            txt1.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit2,unit1,1/EURO_DONG);
                        }
                    } catch (Exception e) {

                    }
                }
                else
                {
                    try {
                        double x2 = (double) dcf.parse(value1).doubleValue();//txt2
                        double x1 = (double) dcf.parse(value2).doubleValue();//txt1
                        if ((unit1.equals("₫") && unit2.equals("₫")) || (unit1.equals("$") && unit2.equals("$")) || (unit1.equals("€") && unit2.equals("€"))) {
                            txt2.setText(value2);
                            ConvertValue(unit1,unit2,1);
                        } else if (unit1.equals("₫") && unit2.equals("$")) {
                            x2 = x1 * 1/DOLL_DONG;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit1,unit2,1/DOLL_DONG);
                        } else if (unit1.equals("₫") && unit2.equals("€")) {
                            x2 = x1 *1/ EURO_DONG;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit1,unit2,1/EURO_DONG);
                        } else if (unit1.equals("$") && unit2.equals("₫")) {
                            x2 = x1 * DOLL_DONG;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit1,unit2,DOLL_DONG);
                        } else if (unit1.equals("$") && unit2.equals("€")) {
                            x2 = x1 *1/ EURO_DOLL;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit1,unit2,1/EURO_DOLL);
                        } else if (unit1.equals("€") && unit2.equals("$")) {
                            x2 = x1 * EURO_DOLL;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit1,unit2,EURO_DOLL);
                        } else if (unit1.equals("€") && unit2.equals("₫")) {
                            x2 = x1 * EURO_DONG;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                            ConvertValue(unit1,unit2,EURO_DONG);
                        }
                    } catch (Exception e) {

                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //
        txt1=findViewById(R.id.txt1);
        txt2=findViewById(R.id.txt2);

        Typeface typeface=Typeface.createFromAsset(getAssets(),"DS-DIGI.TTF");
        txt1.setTypeface(typeface,Typeface.BOLD);//đậm
        txt2.setTypeface(typeface);


        txt1.setOnClickListener(this);
        txt2.setOnClickListener(this);
        txt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(status==1)
                {
                    String value1=txt1.getText().toString();
                    String value2=txt2.getText().toString();
                    String unit1=txt_unit1.getText().toString();
                    String unit2=txt_unit2.getText().toString();

                    try {
                        double x1 = (double) dcf.parse(value1).doubleValue();
                        double x2=(double) dcf.parse(value2).doubleValue();
                        if((unit1.equals("₫") && unit2.equals("₫"))|| (unit1.equals("$")&&unit2.equals("$"))||(unit1.equals("€")&&unit2.equals("€")))
                        {
                            txt2.setText(value1);
                        }
                        else if(unit1.equals("₫")&&unit2.equals("$"))
                        {
                            x2=x1*1/DOLL_DONG;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                        }
                        else if(unit1.equals("₫")&&unit2.equals("€"))
                        {
                            x2=x1*1/EURO_DONG;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                        }
                        else if(unit1.equals("$")&&unit2.equals("₫"))
                        {
                            x2=x1*DOLL_DONG;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                        }
                        else if(unit1.equals("$")&&unit2.equals("€"))
                        {
                            x2=x1*1/EURO_DOLL;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                        }
                        else if(unit1.equals("€")&&unit2.equals("$"))
                        {
                            x2=x1*EURO_DOLL;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                        }
                        else if(unit1.equals("€")&&unit2.equals("₫"))
                        {
                            x2=x1*EURO_DONG;
                            txt2.setText(String.valueOf(dcf.format(x2)));
                        }
                    }
                    catch (Exception e)
                    {

                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(status==2)
                {
                    String value2=txt1.getText().toString();
                    String value1=txt2.getText().toString();
                    String unit1=txt_unit1.getText().toString();
                    String unit2=txt_unit2.getText().toString();

                    try {
                        double x1 = (double) dcf.parse(value1).doubleValue();
                        double x2=(double) dcf.parse(value2).doubleValue();
                        if((unit1.equals("₫") && unit2.equals("₫"))|| (unit1.equals("$")&&unit2.equals("$"))||(unit1.equals("€")&&unit2.equals("€")))
                        {
                            txt1.setText(value1);
                        }
                        else if(unit1.equals("₫")&&unit2.equals("$"))
                        {
                            x2=x1*DOLL_DONG;
                            txt1.setText(String.valueOf(dcf.format(x2)));
                        }
                        else if(unit1.equals("₫")&&unit2.equals("€"))
                        {
                            x2=x1*EURO_DONG;
                            txt1.setText(String.valueOf(dcf.format(x2)));
                        }
                        else if(unit1.equals("$")&&unit2.equals("₫"))
                        {
                            x2=x1*1/DOLL_DONG;
                            txt1.setText(String.valueOf(dcf.format(x2)));
                        }
                        else if(unit1.equals("$")&&unit2.equals("€"))
                        {
                            x2=x1*EURO_DOLL;
                            txt1.setText(String.valueOf(dcf.format(x2)));
                        }
                        else if(unit1.equals("€")&&unit2.equals("$"))
                        {
                            x2=x1*1/EURO_DOLL;
                            txt1.setText(String.valueOf(dcf.format(x2)));
                        }
                        else if(unit1.equals("€")&&unit2.equals("₫"))
                        {
                            x2=x1*1/EURO_DONG;
                            txt1.setText(String.valueOf(dcf.format(x2)));
                        }
                    }
                    catch (Exception e)
                    {

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);
        findViewById(R.id.btn_CE).setOnClickListener(this);
        findViewById(R.id.btn_C).setOnClickListener(this);
        findViewById(R.id.btn_point).setOnClickListener(this);
        //
    }

    public void ConvertValue(String unit1,String unit2, double k)
    {

        DecimalFormat df=new DecimalFormat("###,###,###.########",formatSymbols) ;
        String value="1 "+unit1+"="+" "+String.valueOf(df.format(k))+" "+unit2;
        txtConvert.setText(value);
    }


    //k là phím 1->9 được bấm
    public void setValueTxt(int k)
    {
        if(flag==false) {
            if(status==1)
            {
                txt1.setText(String.valueOf(k));
                st1=-1;
            }
            else
            {
                txt2.setText(String.valueOf(k));
                st2=-1;
            }
            flag=true;
        }else {
            String value1 = txt1.getText().toString();
            String value2 = txt2.getText().toString();
            if (status == 1) {
                if (value1.equals("0")) {
                    txt1.setText(String.valueOf(k));
                } else if (value1.contains(".")) {
                    int len = value1.length();
                    if (st1 == -1) {
                        st1++;
                        value1 = value1.substring(0, len - 2).concat(String.valueOf(k) + "0");
                        txt1.setText(value1);
                    } else if (st1 == 0) {
                        st1++;
                        value1 = value1.substring(0, len - 1).concat(String.valueOf(k));
                        txt1.setText(value1);
                    }
                } else {
                    try {
                        double x = (double) dcf.parse(value1).doubleValue();
                        x = x * 10 + k;
                        txt1.setText(String.valueOf(dcf.format(x)));
                    } catch (ParseException e) {
                        Log.v("TAG", e.toString());
                    }
                }
            } else {
                if (value2.equals("0")) {
                    txt2.setText("1");
                } else if (value2.contains(".")) {
                    int len = value2.length();
                    if (st2 == -1) {
                        st2++;
                        value2 = value2.substring(0, len - 2).concat(String.valueOf(k) + "0");
                        txt2.setText(value2);
                    } else if (st2 == 0) {
                        st2++;
                        value2 = value2.substring(0, len - 1).concat(String.valueOf(k));
                        txt2.setText(value2);
                    }
                } else {
                    try {
                        double x = (double) dcf.parse(value2).doubleValue();
                        x = x * 10 + k;
                        txt2.setText(String.valueOf(dcf.format(x)));
                    } catch (ParseException e) {
                        Log.v("TAG", e.toString());
                    }
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        Typeface typeface=Typeface.createFromAsset(getAssets(),"DS-DIGI.TTF");
        if(v.getId()==R.id.txt1)
        {
            //chỉnh đậm khi click vào txt1 hoặc txt2
            if(status!=1)
            {
                status=1;
                txt1.setTypeface(typeface, Typeface.BOLD);//đậm
                txt2.setTypeface(typeface,Typeface.NORMAL);//nhạt
                flag=false;
            }
        }
        else if(v.getId()==R.id.txt2)
        {
            if(status!=2)
            {
                status=2;
                txt2.setTypeface(typeface, Typeface.BOLD);
                txt1.setTypeface(typeface,Typeface.NORMAL);
                flag=false;
            }
        }
        else if(v.getId()==R.id.btn_0)
        {
            setValueTxt(0);
        }
        else if(v.getId()==R.id.btn_1)
        {
            setValueTxt(1);
        }
        else if(v.getId()==R.id.btn_2)
        {
            setValueTxt(2);
        }
        else if(v.getId()==R.id.btn_3)
        {
            setValueTxt(3);
        }
        else if(v.getId()==R.id.btn_4)
        {
            setValueTxt(4);
        }
        else if(v.getId()==R.id.btn_5)
        {
            setValueTxt(5);
        }
        else if(v.getId()==R.id.btn_6)
        {
            setValueTxt(6);
        }
        else if(v.getId()==R.id.btn_7)
        {
            setValueTxt(7);
        }
        else if(v.getId()==R.id.btn_8)
        {
            setValueTxt(8);
        }
        else if(v.getId()==R.id.btn_9)
        {
            setValueTxt(9);
        }
        else if(v.getId()==R.id.btn_CE)
        {
            txt1.setText("0");
            txt2.setText("0");
            st1=-1;
            st2=-1;
            flag=true;
        }
        else if(v.getId()==R.id.btn_C)
        {
            if (flag==false)
            {
                txt2.setText("0");
                txt1.setText("0");
                st1=-1;
                st2=-1;
                flag=true;
            }
            else {
                String value1 = txt1.getText().toString();
                String value2 = txt2.getText().toString();
                if (status == 1) {
                    int len = value1.length();
                    if (value1.contains(".")) {
                        //xóa dấu .00
                        if (st1 == -1 && value1.contains(".00")) {
                            value1 = value1.substring(0, len - 3);
                        } else if (st1 == 1) {
                            st1--;
                            value1 = value1.substring(0, len - 1).concat("0");
                        } else if (st1 == 0) {
                            st1--;
                            value1 = value1.substring(0, len - 2).concat("00");
                        }
                    } else if (len == 1) {
                        value1 = "0";
                    } else {
                        try {
                            double x = (double) dcf.parse(value1).doubleValue();
                            x = x / 10;
                            value1 = String.valueOf(dcf.format(x));
                            if (value1.contains("."))
                                value1 = value1.substring(0, value1.length() - 2);
                        } catch (Exception e) {
                            Log.v("TAG", e.toString());
                        }
                    }
                    txt1.setText(value1);

                } else {
                    int len = value2.length();
                    if (value2.contains(".")) {
                        //xóa dấu .00
                        if (st2 == -1 && value2.contains(".00")) {
                            value2 = value2.substring(0, len - 3);
                        } else if (st2 == 1) {
                            st2--;
                            value2 = value2.substring(0, len - 1).concat("0");
                        } else if (st2 == 0) {
                            st2--;
                            value2 = value2.substring(0, len - 2).concat("00");
                        }
                    } else if (len == 1) {
                        value2 = "0";
                    } else {
                        try {
                            double x = (double) dcf.parse(value2).doubleValue();
                            x = x / 10;
                            value2 = String.valueOf(dcf.format(x));
                            if (value2.contains("."))
                                value2 = value2.substring(0, value2.length() - 2);
                        } catch (Exception e) {
                            Log.v("TAG", e.toString());
                        }
                    }
                    txt2.setText(value2);
                }
            }
        }
        else if(v.getId()==R.id.btn_point)
        {
            if(flag==false)
            {
                if(status==1) {
                    txt1.setText("0.00");
                    st1=-1;
                }
                else {
                    txt2.setText("0.00");
                    st2=-1;
                }
                flag=true;
            }
            else {
                String value1 = txt1.getText().toString();
                String value2 = txt2.getText().toString();
                if (status == 1) {
                    if (!value1.contains(".")) {
                        value1 = value1.concat(".00");
                        txt1.setText(value1);
                    }
                } else {
                    if (!value2.contains(".")) {
                        value2 = value2.concat(".00");
                        txt2.setText(value2);
                    }
                }
            }
        }
    }
}
