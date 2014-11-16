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
 */
public class HexBoard implements Serializable {
    ArrayList<ArrayList<Hex>> board;
    
    public HexBoard(ArrayList<Pos> aliados, ArrayList<Pos> inimigos){
    //instanciar os vertices
    //inicializar as arestas
    
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
    
    Character LRTAstar(int sx, int sy){
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
        
        //TODO comparar current com last e definir qual comando enviar para o robo
        return '\n';
    }
    
}
