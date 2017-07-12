package com.example.mihai.quartogame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PrivateKey;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //declarea variabilelor pentru nr max de celule pe linii
    final static int maxN=4;
    final static int maxFBwidth=2;
    final static int maxFBlength=8;
    final static int maxFBNr=16;
    //crearea ImageVieului pentry arayul celulelor
    private ImageView[][] ivCell=new ImageView[maxN][maxN];
    private ImageView[][] boxCell=new  ImageView[maxFBwidth][maxFBlength];
    private ImageView[][] SboxCell=new  ImageView[maxFBwidth][maxFBNr];
    private ImageView[][] boxSCell=new ImageView[1][1];
    private Context context;
    private Drawable[] drawCell = new Drawable[40];

    private Button btnPlay;
    private TextView textViewTurn;


    private int[][] valueCell =new int[maxN][maxN];
    private int winnerGame;
    private boolean firstMove;
    private boolean firstSelection;
    private int xMove,yMove;
    private int xBox,yBox;
    private int turn;
    private int figure;


    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        spinner = (Spinner) findViewById(R.id.spinnerSelectDifficulty);
        adapter =  ArrayAdapter.createFromResource(this,R.array.difficulties,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

          loadResouces();
        designBoardGame();
      //  designFiguresBox();
        designFiguresBoxLine1();
        designFiguresBoxLine2();
        designSelectedFigBox();
        cancelSelectedFigure();
        setListener();
    }

    private void setListener() {
        btnPlay = (Button) findViewById(R.id.button_new_game);
        textViewTurn = (TextView) findViewById(R.id.textViewdisplayTurn);

        btnPlay.setText("New Game");
        textViewTurn.setTextColor(Color.parseColor("#f4425f"));
        textViewTurn.setTextSize(14);
        textViewTurn.setText("Pess New Game To Start!");

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init_game();
                play_game();
                
            }
        });

    }

    private void play_game() {

        Random r = new Random();
        turn = r.nextInt(2)+1; //r.nextInt(2) returns value in[0,1]


        if (turn == 1){//Human turn first
            textViewTurn.setTextSize(14);
            Toast.makeText(context,"Human Makes Fist Move!",Toast.LENGTH_SHORT).show();
            playerTurn();
            }
            else
            {
                //AI turn
                textViewTurn.setTextSize(14);
                Toast.makeText(context,"Automatron Makes Fist Move!",Toast.LENGTH_SHORT).show();
                aiTurn();
            }

    }

    private void aiTurn() {

        int x = 0,y =0;
         figure=38;
        Random xpos = new Random();
        x = xpos.nextInt(4);
        Random ypos =new Random();
        y = ypos.nextInt(4);

        Random fig = new Random();
        figure = fig.nextInt(16);
        textViewTurn.setTextSize(14);
        textViewTurn.setText("AI's turn!");

        if(firstMove){
            firstMove = false;
                xMove=x;
                yMove=y;
                make_a_move();

        }
        else
            {    //-apelam metoda pentru accesarea figurii selectate de HUMAN
                //gasirea celei mai bune miscari

            }
    }

    private void make_a_move() {
         figure = 38;
        Random fig = new Random();
        figure = fig.nextInt(16);
        ivCell[xMove][yMove].setImageDrawable(drawCell[figure]);
        // ivCell[xMove][yMove].setImageDrawable(drawCell[turn]);
        if (turn == 1){//human
            turn=(1+2)-turn;
            aiTurn();
        }else {//ai turn
            turn = 3-turn;
            playerTurn();

        }

    }

    private void playerTurn() {
        figure =38;
        textViewTurn.setTextSize(14);
        textViewTurn.setText("Human");
        isClicked = false;
    }

    private void drawFigureinSbox(){
        Button buttonCancel =(Button) findViewById(R.id.btCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   boxSCell[xMove][yMove].setImageDrawable(drawCell[turn]);
                boxSCell[xMove][yMove].setImageDrawable(drawCell[38]);
            }
        });

        Button buttonConfirm =(Button) findViewById(R.id.btConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aiTurn();
                //atribuirea figurii selectate de catre human -->>to AI !
            }
        });

        //   boxSCell[xMove][yMove].setImageDrawable(drawCell[turn]);
        boxSCell[xMove][yMove].setImageDrawable(drawCell[figure]);
        if (turn == 1){//human
            turn=(1+2)-turn;
            aiTurnSelect();
        }else {//ai turn
            turn = 3-turn;
           //figure =
            playerTurnSelect();


        }


    }


    private void playerTurnSelect() {
        textViewTurn.setTextSize(14);
        textViewTurn.setText("Human turn to select Figure!");
        isSelected = false;
    }


    private void aiTurnSelect() {

        int x = 0,y =0;
        Random fxpos = new Random();
        x = fxpos.nextInt(8);
        Random fypos =new Random();
        y = fypos.nextInt(2);

        textViewTurn.setTextSize(14);
        textViewTurn.setText("AI turn to select figure!");

        if(firstSelection){
            firstSelection = false;
            xBox=x;
            yBox=y;
            drawFigureinSbox();

        }
        else
        {
            //try to find best move

        }
    }


    private void init_game() {
        //creating UI before the game start
        firstMove = true;
        winnerGame = 0;

        for ( int i=0;i<maxN;i++){

            for (int j=0;j<maxN;j++){

                ivCell[i][j].setImageDrawable(drawCell[38]);
            }
        }
    }


    private void loadResouces() {
        drawCell[39] = context.getResources().getDrawable(R.drawable.cell_bg);
        drawCell[37] = context.getResources().getDrawable(R.drawable.se_cell_bg);

        drawCell[38] = null;
        //Drawables pentru figurile active
        drawCell[0] = context.getResources().getDrawable(R.drawable.v0000);
        drawCell[1] = context.getResources().getDrawable(R.drawable.v0001);
        drawCell[2] = context.getResources().getDrawable(R.drawable.v0010);
        drawCell[3] = context.getResources().getDrawable(R.drawable.v0011);
        drawCell[4] = context.getResources().getDrawable(R.drawable.v0100);
        drawCell[5] = context.getResources().getDrawable(R.drawable.v0101);
        drawCell[6] = context.getResources().getDrawable(R.drawable.v0110);
        drawCell[7] = context.getResources().getDrawable(R.drawable.v0111);
        drawCell[8] = context.getResources().getDrawable(R.drawable.v1000);
        drawCell[9] = context.getResources().getDrawable(R.drawable.v1001);
        drawCell[10] = context.getResources().getDrawable(R.drawable.v1010);
        drawCell[11] = context.getResources().getDrawable(R.drawable.v1011);
        drawCell[12] = context.getResources().getDrawable(R.drawable.v1100);
        drawCell[13] = context.getResources().getDrawable(R.drawable.v1101);
        drawCell[14] = context.getResources().getDrawable(R.drawable.v1110);
        drawCell[15] = context.getResources().getDrawable(R.drawable.v1111);

        //Drawables  p/u figurile selectate(blur)
        drawCell[17] = context.getResources().getDrawable(R.drawable.uv0000);
        drawCell[18] = context.getResources().getDrawable(R.drawable.uv0001);
        drawCell[19] = context.getResources().getDrawable(R.drawable.uv0010);
        drawCell[20] = context.getResources().getDrawable(R.drawable.uv0011);
        drawCell[21] = context.getResources().getDrawable(R.drawable.uv0100);
        drawCell[22] = context.getResources().getDrawable(R.drawable.uv0101);
        drawCell[23] = context.getResources().getDrawable(R.drawable.uv0110);
        drawCell[24] = context.getResources().getDrawable(R.drawable.uv0111);
        drawCell[25] = context.getResources().getDrawable(R.drawable.uv1000);
        drawCell[26] = context.getResources().getDrawable(R.drawable.uv1001);
        drawCell[27] = context.getResources().getDrawable(R.drawable.uv1010);
        drawCell[28] = context.getResources().getDrawable(R.drawable.uv1011);
        drawCell[29] = context.getResources().getDrawable(R.drawable.uv1100);
        drawCell[30] = context.getResources().getDrawable(R.drawable.uv1101);
        drawCell[31] = context.getResources().getDrawable(R.drawable.uv1110);
        drawCell[32] = context.getResources().getDrawable(R.drawable.uv1111);

    }

    private boolean isSelected;
    private boolean isClicked;
    @SuppressLint("NewApi")
    private void designBoardGame(){


        int cellSize= Math.round(ScreenWidth()/maxN);
        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(cellSize*maxN,cellSize);
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(cellSize,cellSize);

        LinearLayout LinBoarGame = (LinearLayout) findViewById(R.id.LayoutBoard);



        for (int i=0;i<maxN;i++){
            LinearLayout linRow = new LinearLayout(context);

            for (int j=0;j<maxN;j++){

                ivCell[i][j] =new ImageView(context);

                ivCell[i][j].setBackground(drawCell[39]);
                final int x =i;
                final int y =j;
                ivCell[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (turn == 1 || !isClicked){ //human turn
                            isClicked = true;
                            xMove = x;
                            yMove = y;
                            make_a_move();

                        }
                    }
                });
                linRow.addView(ivCell[i][j],lpCell);
            }
            LinBoarGame.addView(linRow,lpRow);
        }
    }



    @SuppressLint("NewApi")
    private void designFiguresBoxLine1(){

        int cellSize= Math.round(ScreenWidthFBox()/maxFBlength);

        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(cellSize*maxFBlength,cellSize);
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(cellSize,cellSize);

        LinearLayout LinFigBox = (LinearLayout) findViewById(R.id.LayoutFigures);


        for (int i=0;i<1;i++){
            LinearLayout linRow = new LinearLayout(context);


            for (int j=0;j<maxFBlength;j++){

                boxCell[i][j] =new ImageView(context);
                boxCell[i][j].setBackground(drawCell[j]);
                final int x =i;
                final int y =j;
                boxCell[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (turn == 1 || !isSelected){ //human turn
                            isSelected = true;
                            xBox = x;
                            yBox = y;
                            figure =y;
                            drawFigureinSbox();
                            boxCell[x][y].setBackground(drawCell[y+17]);
                        }
                    }
                });

                linRow.addView(boxCell[i][j],lpCell);

            }
            LinFigBox.addView(linRow,lpRow);
        }


    }

    @SuppressLint("NewApi")
    private void designFiguresBoxLine2(){

        int cellSize= Math.round(ScreenWidthFBox()/maxFBlength);

        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(cellSize*maxFBlength,cellSize);
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(cellSize,cellSize);

        LinearLayout LinFigBox = (LinearLayout) findViewById(R.id.LayoutFigures);


        for (int i=0;i<1;i++){
            LinearLayout linRow = new LinearLayout(context);


            for (int j=maxFBlength;j<maxFBNr;j++){

                SboxCell[i][j] =new ImageView(context);
                SboxCell[i][j].setBackground(drawCell[j]);
                final int x =i;
                final int y =j;
                SboxCell[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (turn == 1 || !isSelected){ //human turn
                            isSelected = true;
                            xBox = x;
                            yBox = y;
                            figure =y;
                            drawFigureinSbox();
                            SboxCell[x][y].setBackground(drawCell[y+17]);

                        }
                    }
                });


                linRow.addView(SboxCell[i][j],lpCell);

            }
            LinFigBox.addView(linRow,lpRow);
        }


    }

    @SuppressLint("NewApi")
    private void designSelectedFigBox() {

        int cellSize= Math.round(ScreenWidthSFBox()/4);
        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(cellSize*2,cellSize);
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(cellSize,cellSize);

        LinearLayout LinSelFigBox = (LinearLayout) findViewById(R.id.LayoutSelectedFigure);

        for (int i=0;i<1;i++){
            LinearLayout linRow = new LinearLayout(context);
            //make a row
            for (int j=0;j<1;j++){

                boxSCell[i][j] =new ImageView(context);
                boxSCell[i][j].setBackground(drawCell[37]);
                linRow.addView(boxSCell[i][j],lpCell);
            }
            LinSelFigBox.addView(linRow,lpRow);
        }

    }

   private void cancelSelectedFigure (){
       Button btnCancel =(Button) findViewById(R.id.btCancel);
       btnCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               boxSCell[xBox][yBox].setImageDrawable(drawCell[38]);
               drawFigureinSbox();
           }
       });


    }


    private float ScreenWidth(){

        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels/2;
    }

    private float ScreenWidthFBox(){
        Resources resources = context.getResources();
        DisplayMetrics dmfb =resources.getDisplayMetrics();
        return dmfb.widthPixels-12;

    }

    private float ScreenWidthSFBox(){

        Resources resources =context.getResources();
        DisplayMetrics dmsfb = resources.getDisplayMetrics();
        return dmsfb.widthPixels;
    }

//    @SuppressLint("NewApi")
//    private void designFiguresBox(){
//
//        int cellSize= Math.round(ScreenWidthFBox()/maxFBlength);
//        //int fn;
//        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(cellSize*maxFBlength,cellSize);
//        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(cellSize,cellSize);
//
//        LinearLayout LinFigBox = (LinearLayout) findViewById(R.id.LayoutFigures);
//        //creating cells
//
//        for (int i=0;i<maxFBwidth;i++){
//            LinearLayout linRow = new LinearLayout(context);
//
//            //make a row
//            for (int j=0;j<maxFBlength;j++){
//
//                boxCell[i][j] =new ImageView(context);
//                boxCell[i][j].setBackground(drawCell[j]);
//
//                //make a cell
//                //need to set backgroud default for cell
//                //cell has 3 stuses empty/default, player, cpu.
//                // for(int fn=0;fn<16;fn++){
//
//                //  }
//
//                linRow.addView(boxCell[i][j],lpCell);
//
//            }
//            LinFigBox.addView(linRow,lpRow);
//        }
//
//
//    }


}
