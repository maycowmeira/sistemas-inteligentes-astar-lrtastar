/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.hexgrid;

import java.util.ArrayList;

/**
 *
 * @author Bernardo
 */
public class HexBoard {
    private ArrayList<Hex> vizinhos;
    private int x, y;
    private int g; //custo para chegar
    private int h; //custo estimado até o objetivo

    public HexBoard(int x, int y, int g, int h) {
        this.vizinhos = new ArrayList();
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = h;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public ArrayList<Hex> getVizinhos() {
        return vizinhos;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    
    
}
