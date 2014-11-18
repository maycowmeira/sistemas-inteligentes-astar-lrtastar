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
    
    public HexBoard(ArrayList<Pos> aliados, ArrayList<Pos> inimigos, Pos objetivo){
        //instanciar os vertices
        board = new ArrayList();
        for(int i = 0; i < 41; i++)
            board.add(new ArrayList());
        
        for(int i = 0; i < 41; i++)        
            for(int j = 0; j < 25; j++)
                    board.get(i).add(new Hex(i, j));
        
        board.get(3).get(10).setH(Integer.MAX_VALUE);

        ArrayList<Pos> barreira = new ArrayList();

        for(Pos p : aliados){
            barreira.add(new Pos(p.x * 60 ,p.y  * 60 ));
            barreira.add(new Pos((p.x-1) * 60,(p.y-1) * 60));
            barreira.add(new Pos(p.x * 60,(p.y-1) * 60));
            barreira.add(new Pos((p.x+1) * 60,(p.y) * 60));
            barreira.add(new Pos(p.x * 60,(p.y+1)* 60));
            barreira.add(new Pos((p.x-1)*60,(p.y+1)*60));
            barreira.add(new Pos((p.x-1) * 60,p.y));
        }
        for(Pos p : inimigos){
            barreira.add(new Pos(p.x * 60 ,p.y  * 60 ));
            barreira.add(new Pos((p.x-1) * 60,(p.y-1) * 60));
            barreira.add(new Pos(p.x * 60,(p.y-1) * 60));
            barreira.add(new Pos((p.x+1) * 60,(p.y) * 60));
            barreira.add(new Pos(p.x * 60,(p.y+1)* 60));
            barreira.add(new Pos((p.x-1)*60,(p.y+1)*60));
            barreira.add(new Pos((p.x-1) * 60,p.y));
        }
        //inicializar as arestas
        for(int i = 0; i < 41; i++){
            for(int j = 0; j < 25; j++){
                //TODO: Arrumar essa parte que ta completamente errada
                if(i != 0){
                    //if(!barreira.contains(new Pos(board.get(i-1).get(j).getX()*60,board.get(i-1).get(j).getY()*60))){
                        board.get(i).get(j).getVizinhos().add(board.get(i-1).get(j));
                    //}
                }
                if(i !=0 && j != 0){
                    //if(!barreira.contains(new Pos(board.get(i-1).get(j-1).getX()*60,board.get(i-1).get(j-1).getY()*60))){
                        board.get(i).get(j).getVizinhos().add(board.get(i-1).get(j-1));
                    //}
                    //if(!barreira.contains(new Pos(board.get(i).get(j-1).getX()*60,board.get(i).get(j-1).getY()*60))){
                        board.get(i).get(j).getVizinhos().add(board.get(i).get(j-1));
                    //}
                }
                if(i != 40){
                    //if(!barreira.contains(new Pos(board.get(i+1).get(j).getX()*60,board.get(i+1).get(j).getY()*60))){
                        board.get(i).get(j).getVizinhos().add(board.get(i+1).get(j));
                    //}
                }
                if(i != 40 && j != 24){
                    //if(!barreira.contains(new Pos(board.get(i+1).get(j+1).getX()*60,board.get(i+1).get(j+1).getY()*60))){
                        board.get(i).get(j).getVizinhos().add(board.get(i+1).get(j+1));
                    //}
                    //if(!barreira.contains(new Pos(board.get(i).get(j+1).getX()*60,board.get(i).get(j+1).getY()*60))){
                        board.get(i).get(j).getVizinhos().add(board.get(i).get(j+1));
                    //}
                }
            }
        }
        Hex goal = new Hex(objetivo.getX(),objetivo.getY());
        int xGoalCubo, yGoalCubo, zGoalCubo;
        xGoalCubo = goal.getX() - (goal.getY() - (goal.getY()&1))/2;
        zGoalCubo = goal.getY();
        yGoalCubo = -xGoalCubo-zGoalCubo;
        
        int xCubo, yCubo, zCubo;
        
        for(int i = 0; i < 41; i++){
            for(int j = 0; i < 25; i++){
                xCubo = board.get(i).get(j).getX() - (board.get(i).get(j).getY() - (board.get(i).get(j).getY()&1))/2;
                zCubo = board.get(i).get(j).getY();
                yCubo = -xCubo-zCubo;
                
                board.get(i).get(j).setH(
                        Math.max(
                            Math.max(
                                Math.abs(xCubo - xGoalCubo), 
                                Math.abs(yCubo - yGoalCubo)
                            ),
                            Math.abs(zCubo - zGoalCubo)
                        )
                    );
            }
        }
    }
    
    public ArrayDeque<Pos> Astar(int sx, int sy, int gx, int gy){
        ArrayDeque<Pos> plano = new ArrayDeque();
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
                while(current.getX() != sx && current.getY() != sy){
                    plano.push(new Pos(current.getX() * 3600, current.getY() * 3600));
                    current = current.getParent();
                }
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
        return plano;
    }
    
    public Pos LRTAstar(int sx, int sy, int gx, int gy){
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
            return new Pos(-1, -1);
        return new Pos(menorf.getX() * 3600, menorf.getY() * 3600);
    }
    
}
