package com.mobiledesigngroup.billpie3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.html.head.Title;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import static android.content.ContentValues.TAG;

/**
 * Created by eric.
 */

public class History extends Fragment {
    private ProgressBar progBar;
    private Map<String, Paybacks> paybacksMap;
    private File pdfFolder;
    private File pdfFile;

    private Map<String, User> userMap;
    private Map<String, Paybacks> paybacksFiltered;
    private LinearLayout mainLinear;
    private String actualUser = "user1"; //TODO: Change when the login is set up
    private Map<String, Event> eventsMap;
    private DatabaseReference myDbRef;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);
        this.progBar = view.findViewById(R.id.prog_hist);
        this.mainLinear = view.findViewById(R.id.linear_hist);

        actualUser = getActivity().getIntent().getStringExtra("userId");

        final FloatingActionButton pdfFab = view.findViewById(R.id.pdf_fab);

        progBar.setVisibility(View.VISIBLE);

        this.myDbRef = FirebaseDatabase.getInstance().getReference();

        myDbRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventsMap = new HashMap<>();

                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    eventsMap.put(eventSnapshot.getKey(),
                            eventSnapshot.getValue(Event.class));
                }

                myDbRef.child("paybacks").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        paybacksFiltered = new HashMap<>();
                        paybacksMap = new HashMap<>();

                        for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                            paybacksMap.put(eventSnapshot.getKey(),
                                    eventSnapshot.getValue(Paybacks.class));
                        }
                        createUserMap();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "Paybacks: error while retrieving events", databaseError.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Events: error while retrieving events", databaseError.toException());
            }
        });

        pdfFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPDF();
            }
        });

        return view;
    }

    private void displayData() {
        filterPaybacks();
        Log.w(TAG, "PaybacksFiltered: " + this.paybacksMap.toString());
        displayDyna();
    }

    private void filterPaybacks() {
        for (Map.Entry<String, Paybacks> payMap: this.paybacksMap.entrySet()) {
            if (payMap.getValue().getPaid() && payMap.getValue().getPayer().equals(this.actualUser)) {
                this.paybacksFiltered.put(payMap.getKey(), payMap.getValue());
            }
        }
    }

    private void displayDyna() {
        for (Map.Entry<String, Paybacks> filMap: this.paybacksFiltered.entrySet()) {
            createCard(filMap.getValue().getSpending(), filMap.getValue().getDate_paid(),
                    filMap.getValue().getEvent(), filMap.getValue().getReceiver(),
                    filMap.getValue().getAmount());
        }
    }

    private void createCard(String spendingTitle, String date, String eventTitle, String name, String amount) {
        // create a new CardView
        CardView cardView = new CardView(this.getActivity());

        // set properties
        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(dpToPixel(2) ,0,dpToPixel(2),dpToPixel(8));
        cardView.setCardBackgroundColor(Color.WHITE);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(dpToPixel(2));

        // create a new View (Left blue rectangle)
        final View view1 = new View(this.getActivity());

        // set properties
        ViewGroup.LayoutParams view1Params = new ViewGroup.LayoutParams(
                dpToPixel(20),
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        view1.setBackground(getResources().getDrawable(R.drawable.rectangle_orange));
        view1.setLayoutParams(view1Params);

        // create the first LinearLayout of the card
        final LinearLayout linearView = new LinearLayout(this.getActivity());

        // set properties
        LinearLayout.LayoutParams linearFirstParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearView.setPadding(dpToPixel(16), dpToPixel(16), dpToPixel(16), dpToPixel(16));
        linearView.setLayoutParams(linearFirstParams);
        linearView.setOrientation(LinearLayout.VERTICAL);

        // create LinearLayout of the card
        final LinearLayout linearView1 = new LinearLayout(this.getActivity());

        // set properties
        LinearLayout.LayoutParams linearParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearView1.setLayoutParams(linearParams1);
        linearView1.setOrientation(LinearLayout.HORIZONTAL);

        // Create the Spending textView
        TextView titleText = new TextView(this.getActivity());

        // set properties
        titleText.setText(findSpendingTitle(spendingTitle, eventTitle));
        titleText.setTextColor(Color.BLACK);
        titleText.setTextSize(18);
        titleText.setGravity(Gravity.START);
        titleText.setTypeface(Typeface.create("@font/roboto_bold", Typeface.BOLD));
        TableLayout.LayoutParams titleTextParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        titleTextParams.setMargins(dpToPixel(22), 0, 0, 0);
        titleText.setLayoutParams(titleTextParams);

        // Create the Date textView
        TextView dateText = new TextView(this.getActivity());

        // set properties
        dateText.setText(date);
        dateText.setTextColor(Color.BLACK);
        dateText.setTextSize(13);
        dateText.setGravity(Gravity.END);
        dateText.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
        TableLayout.LayoutParams titleDateParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        titleDateParams.setMargins(dpToPixel(22), 0, 0, dpToPixel(8));
        dateText.setLayoutParams(titleDateParams);

        // Create the Event textView
        TextView eventText = new TextView(this.getActivity());

        // set properties
        eventText.setText(this.eventsMap.get(eventTitle).getTitle());
        eventText.setTextColor(Color.BLACK);
        eventText.setTextSize(12);
        eventText.setTypeface(Typeface.create("@font/roboto_light", Typeface.NORMAL));
        TableLayout.LayoutParams titleEventParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        titleEventParams.setMargins(dpToPixel(22), 0, 0, dpToPixel(8));
        eventText.setLayoutParams(titleEventParams);

        // create second LinearLayout of the card
        final LinearLayout linearView2 = new LinearLayout(this.getActivity());

        // set properties
        LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearView2.setLayoutParams(linearParams2);
        linearView2.setOrientation(LinearLayout.HORIZONTAL);

        // Create the Name textView
        TextView nameText = new TextView(this.getActivity());

        // set properties
        nameText.setText(this.userMap.get(name).full_name);
        nameText.setTextColor(Color.BLACK);
        nameText.setTextSize(15);
        nameText.setGravity(Gravity.START);
        nameText.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
        TableLayout.LayoutParams titleNameParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        titleNameParams.setMargins(dpToPixel(22), 0, 0, 0);
        nameText.setLayoutParams(titleNameParams);

        // Create the Amount textView
        TextView amountText = new TextView(this.getActivity());

        // set properties
        amountText.setText("$" + amount);
        amountText.setTextColor(Color.BLACK);
        amountText.setTextSize(15);
        amountText.setGravity(Gravity.END);
        amountText.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
        TableLayout.LayoutParams titleAmountParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        titleAmountParams.setMargins(dpToPixel(22), 0, 0, 0);
        amountText.setLayoutParams(titleAmountParams);

        cardView.addView(view1);
        cardView.addView(linearView);

        linearView1.addView(titleText);
        linearView1.addView(dateText);

        linearView2.addView(nameText);
        linearView2.addView(amountText);


        linearView.addView(linearView1);
        linearView.addView(eventText);
        linearView.addView(linearView2);

        this.mainLinear.addView(cardView);
    }

    private String findSpendingTitle(String spendingID, String eventID) {
        for(Map.Entry<String, Event> ev: this.eventsMap.entrySet()) {
            if (ev.getKey().equals(eventID)) {
                return ev.getValue().getSpendings().get(spendingID).getTitle();
            }
        }
        return "Spending Title";
    }

    private int dpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }

    private void createUserMap() {
        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

        myDbRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userMap = new HashMap<>();
                for (DataSnapshot paybackSnapshot: dataSnapshot.getChildren()) {
                    userMap.put(paybackSnapshot.getKey(), paybackSnapshot.getValue(User.class));
                }
                progBar.setVisibility(View.INVISIBLE);
                displayData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "History: error while retrieving events", databaseError.toException());
            }
        });
    }

    private void callPDF() {

        createFolder();
        try {
            Log.w(TAG, "Create PDF!!!!");
            createPdf();
        } catch (FileNotFoundException f) {
            Log.w(TAG, "FileNotFoundException: " + f.toString());
        } catch (DocumentException d) {
            Log.w(TAG, "DocumentException: " + d.toString());
        }
        viewPdf();
    }

    private void createFolder() {
        this.pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "BillPie");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
        }
    }

    private void createPdf() throws FileNotFoundException, DocumentException {

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy").format(date);

        this.pdfFile = new File(this.pdfFolder + "/" + timeStamp + ".pdf");

        // Create a PdfFont

        OutputStream output = new FileOutputStream(this.pdfFile);

        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        //Step 4 Add content
        Paragraph title = new Paragraph("History of the " + new SimpleDateFormat("dd/MM/yyyy").format(date));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );

        document.add(createFirstTable());

        //Step 5: Close the document
        document.close();

    }

    private void viewPdf(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri pdfUri = FileProvider.getUriForFile(getActivity(),
                "com.mobiledesigngroup.billpie3.GenericFileProvider", this.pdfFile);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    public PdfPTable createFirstTable() {
        PdfPTable table = new PdfPTable(5);

        table.addCell("Date");
        table.addCell("Spending Titles");
        table.addCell("Event Titles");
        table.addCell("Name");
        table.addCell("Amounts");

        for (Map.Entry<String, Paybacks> filMap: this.paybacksFiltered.entrySet()) {
            String spendingTitle = findSpendingTitle(filMap.getValue().getSpending(), filMap.getValue().getEvent());
            String datee = filMap.getValue().getDate_paid();
            String eventTitle = this.eventsMap.get(filMap.getValue().getEvent()).getTitle();
            String name = this.userMap.get(filMap.getValue().getReceiver()).full_name;
            String amount = "$" + filMap.getValue().getAmount();

            table.addCell(datee);
            table.addCell(spendingTitle);
            table.addCell(eventTitle);
            table.addCell(name);
            table.addCell(amount);
        }



        return table;
    }
}
