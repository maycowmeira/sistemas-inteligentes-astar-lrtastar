/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.hexgrid;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 *
 * @author Bernardo
 * Inspirado por: http://www.redblobgames.com/grids/hexagons/
 */
public class HexBoard implements Serializable {
    ArrayList<ArrayList<Hex>> board;
    
    public HexBoard(ArrayList<Pos> aliados, ArrayList<Pos> inimigos){
    //instanciar os vertices
    board = new ArrayList();
    for(int i = 0; i < 41; i++){
        board.add(new ArrayList());
        for(int j = 0; i < 25; i++){
            board.get(i).add(new Hex(i, j, 0, 0));
        }
    }
    board.get(0).get(0).setH(Integer.MAX_VALUE);
    
    ArrayList<Pos> barreira = new ArrayList();
    
    for(Pos p : aliados){
        barreira.add(new Pos(p.x,p.y));
        barreira.add(new Pos(p.x-1,p.y-1));
        barreira.add(new Pos(p.x,p.y-1));
        barreira.add(new Pos(p.x+1,p.y));
        barreira.add(new Pos(p.x,p.y+1));
        barreira.add(new Pos(p.x-1,p.y+1));
        barreira.add(new Pos(p.x-1,p.y));
    }
    for(Pos p : inimigos){
        barreira.add(new Pos(p.x,p.y));
        barreira.add(new Pos(p.x-1,p.y-1));
        barreira.add(new Pos(p.x,p.y-1));
        barreira.add(new Pos(p.x+1,p.y));
        barreira.add(new Pos(p.x,p.y+1));
        barreira.add(new Pos(p.x-1,p.y+1));
        barreira.add(new Pos(p.x-1,p.y));
    }
    //inicializar as arestas
    for(int i = 0; i < 41; i++){
        for(int j = 0; i < 25; i++){
            if(i != 0){
                if(barreira.contains(new Pos(board.get(i-1).get(j).getX()*60,board.get(i-1).get(j).getY()*60))){
                    board.get(i).get(j).getVizinhos().add(board.get(i-1).get(j));
                }
            }
            if(i !=0 && j != 0){
                if(barreira.contains(new Pos(board.get(i-1).get(j-1).getX()*60,board.get(i-1).get(j-1).getY()*60))){
                    board.get(i).get(j).getVizinhos().add(board.get(i-1).get(j-1));
                }
                if(barreira.contains(new Pos(board.get(i).get(j-1).getX()*60,board.get(i).get(j-1).getY()*60))){
                    board.get(i).get(j).getVizinhos().add(board.get(i).get(j-1));
                }
            }
            if(i != 40){
                if(barreira.contains(new Pos(board.get(i+1).get(j).getX()*60,board.get(i+1).get(j).getY()*60))){
                    board.get(i).get(j).getVizinhos().add(board.get(i+1).get(j));
                }
            }
            if(i != 40 && j != 24){
                if(barreira.contains(new Pos(board.get(i+1).get(j+1).getX()*60,board.get(i+1).get(j+1).getY()*60))){
                    board.get(i).get(j).getVizinhos().add(board.get(i+1).get(j+1));
                }
                if(barreira.contains(new Pos(board.get(i).get(j+1).getX()*60,board.get(i).get(j+1).getY()*60))){
                    board.get(i).get(j).getVizinhos().add(board.get(i).get(j+1));
                }
            }
        }
    }
    
    }
    
    ArrayDeque<Character> Astar(int sx, int sy, int gx, int gy){
        ArrayDeque<Character> plano = new ArrayDeque();
        ArrayList<Hex> closed = new ArrayList();
        Hex current = board.get(sx).get(sy);
        ArrayList<Hex> open = new ArrayList();
        open.add(current);        
        
        while(!open.isEmpty()){
            Hex menorf = open.get(0);
            for(Hex h: open){
                if(h.getF() < menorf.getF()){
                    menorf = h;
                }
            }
            current = menorf;
            open.remove(current);
            closed.add(current);
            if(current.getX() == gx && current.getY() == gy){
                //TODO: adicionar as instruções finais a fila antes de retornar
                return plano;
            }
            for(Hex v: current.getVizinhos()){
                if(!closed.contains(v)){
                    if(!open.contains(v)){
                        open.add(v);
                        v.setParent(current);
                        v.setG(current.getG() + 1);
                    }
                    else if(current.getG() + 1 < v.getF() ){
                            v.setG(current.getG() + 1);
                            v.setParent(current);
                    }
                }
            }
        }
        //se falhou retorna um conjunto vazio de instrucoes
        return new ArrayDeque();
    }
    
    Character LRTAstar(int sx, int sy, int gx, int gy){
        Hex current = board.get(sx).get(sy);
        Hex last = current;
        for(Hex v: current.getVizinhos()){
            v.setInflacao(v.getInflacao() + 1);
        }
        Hex menorf = current.getVizinhos().get(0);
        for(Hex w: current.getVizinhos()){
            if(w.getF() + w.getInflacao() < menorf.getF() + menorf.getInflacao()){
                menorf = w;
            }
        }
        if(menorf.getX() == gx && menorf.getY() == gy)
            return 'g';
        
        //TODO comparar current com last e definir qual comando enviar para o robo
        return '\n';
    }
    
}
