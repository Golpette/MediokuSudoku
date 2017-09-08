package com.puzzle.andrew.sudowordsandroid.sudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.puzzle.andrew.sudowordsandroid.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Andrew on 30/08/2017.
 */
public class Sudoku extends AppCompatActivity{

    GridView sudokuGrid;
    ImageButton sudokuButton;

    ArrayList<Integer> row, checks;
    ArrayList<Integer> correct;
    ArrayList<ArrayList<Integer>> cols;
    ArrayList<ArrayList<Integer>> boxes;
    int[][] grid = new int [9][9];
    int x = 11, y = 11;
    Random rand;
    boolean complete = false;

    protected void onCreate(Bundle savedInstanceState) {
        correct = new ArrayList<Integer>();
        row = new ArrayList<Integer>();
        checks = new ArrayList<Integer>();
        boxes = new ArrayList<ArrayList<Integer>>();
        cols = new ArrayList<ArrayList<Integer>>();
        rand = new Random();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku_menu);

        sudokuButton = (ImageButton)findViewById(R.id.crossword);
        generateSudoku(grid);
        makeGrid(grid);
    }

    public void generateSudoku(int[][] grid){
        boxes.clear();
        cols.clear();
        row.clear();
        checks.clear();
        correct.clear();
        if(!complete){
            for (int i = 0; i < x-2; i++){
                int bound = 9;

                ArrayList<Integer> tempRow = new ArrayList<Integer>();
                ArrayList<Integer> tempBox = new ArrayList<Integer>();
                for (int j = 0; j < y-2; j++){
                    if(j == 0){
                        for(int k = 1; k < 10; k++){
                            row.add(k);
                            if(i == 0){
                                ArrayList<Integer> box = new ArrayList<Integer>();
                                boxes.add(box);
                                ArrayList<Integer> temps = new ArrayList<Integer>();
                                cols.add(temps);
                            }
                        }
                    }
                    tempRow.clear();
                    tempBox.clear();
                    if(!cols.isEmpty()){
                        for(Integer a : cols.get(j)){
                            if(row.contains(a) && bound > 0){
                                tempRow.add(a);
                                row.remove(a);
                                bound--;
                            }
                        }
                    }
                    if(!boxes.isEmpty()){
                        for(Integer b : boxes.get((i/3)*3+(j/3))){
                            if(row.contains(b) && bound > 0){
                                tempBox.add(b);
                                row.remove(b);
                                bound--;
                            }
                        }
                    }
                    if(row.isEmpty()){
                        if(i != 8 || j != 8){
                            generateSudoku(grid);
                        }else{
                            complete = true;
                            break;
                        }
                    }
                    if(bound == 0){
                        complete = true;
                        break;
                        //generateSudoku();
                    }else{
                        if(!cols.isEmpty()){
                            int temp = (rand.nextInt(bound));
                            int insertion = row.get(temp);
                            //numbers[i][j].setText(""+insertion);
                            correct.add(insertion);
                            checks.add(row.get(temp));
                            cols.get(j).add(row.get(temp));
                            boxes.get(((i/3)*3)+(j/3)).add(row.get(temp));
                            row.remove(row.get(temp));
                            bound--;
                            for(Integer a: tempRow){
                                if(!row.contains(a)){
                                    row.add(a);
                                    bound++;
                                }
                            }
                            for(Integer b: tempBox){
                                if(!row.contains(b)){
                                    row.add(b);
                                    bound++;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(!correct.isEmpty()){
            for(int i = 0; i < x-2; i++) {
                for (int j = 0; j < y - 2; j++) {



                    //field.setText(""+ correct.get(j*(x-2) + i));
                    grid[i][j] = correct.get(j * (x - 2) + i);
                }
            }
        }
    }

    public void makeGrid(int [][] grid){
        grid = SudokuMethods.makeMedium(grid);
        for(int i = 0; i < x-2; i++){
            for (int j = 0; j < y-2; j++){
                android.widget.GridLayout sudGrid = (android.widget.GridLayout) findViewById(R.id.sudokuGrid);
                EditText field = (EditText) sudGrid.getChildAt(i * 9 + j);
                //SudokuMethods.makeMedium(grid);
                if(grid[i][j]!=0) {
                    field.setText("" + grid[i][j]);
                }
            }
        }
    }
}