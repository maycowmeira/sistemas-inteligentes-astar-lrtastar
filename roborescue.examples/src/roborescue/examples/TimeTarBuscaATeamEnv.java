/* Este programa eh modelo para ser utilizado na tarefa de busca A* x LRTA*
 da disciplina de Sistemas Inteligentes 1.
 O time TarBuscaA vai salvar seu aliado que está como refém do inimigo. 
 */
package roborescue.examples;

import atuador.AtuadorSincrono;
import br.edu.utfpr.hexgrid.*;
import jason.RoborescueEnv;
import jason.asSyntax.Structure;
import java.rmi.RemoteException;
import java.util.ArrayDeque;
import robocode.rescue.RobotInfo;
import robocode.rescue.interfaces.RMIRobotInterface;

public class TimeTarBuscaATeamEnv extends RoborescueEnv {

    private static final String nomeTime = "TimeTarBuscaA";
    private final int numRobos = 5;
    private RMIRobotInterface[] aliados;
    private RobotInfo[] inimigos;
    private char meuLadoCampo;
    private RobotInfo[] robos;
    private AtuadorSincrono atuador;
    private Boolean primeiraVez = true;

    //Para inicializacoes necessarias
    @Override
    public void setup() {
        try {
            aliados = getServerRef().getTeamInterfaces(nomeTime);
            meuLadoCampo = aliados[0].getRobotInfo().getX() > 200 ? 'e' : 'd';
            atuador = new AtuadorSincrono();

            /* O aliados[0] do time A eh o refem e inicia posicionado na 
             ** extremidade oposta do campo em relacao aos seus companheiros.
             ** Este robô deve ser resgatado.
             */
            System.out.println(nomeTime + ": pos robo REFEM X="
                    + aliados[0].getRobotInfo().getX()
                    + " Y=" + aliados[0].getRobotInfo().getY());

            /* o robo[1] do time A eh o salvador do refem
             ** vc pode escolher entre mexer ou nao nao posicao inicial dele
             */
            System.out.println(nomeTime + ": pos robo SALVADOR X="
                    + aliados[1].getRobotInfo().getX()
                    + " Y=" + aliados[1].getRobotInfo().getY());

            /*DONE- posicionar os robos aleatoriamente para o A*
             */
            if (meuLadoCampo == 'e') {
                aliados[2].setTurnRight((int)(Math.random() * (90) ) - 45);
                aliados[2].setAhead((int)(Math.random() * (700 - 100) ) + 100);
                aliados[2].execute();

                aliados[3].setTurnRight((int)(Math.random() * (90) ) - 45);
                aliados[3].setAhead(((int)(Math.random() * (700 - 100) ) + 100));
                aliados[3].execute();

                aliados[4].setTurnRight((int)(Math.random() * (90) ) - 45);
                aliados[4].setAhead(((int)(Math.random() * (700 - 100) ) + 100));
                aliados[4].execute();
            } else {
                aliados[4].setTurnRight((int)(Math.random() * (90) ) - 45);
                aliados[4].setAhead(((int)(Math.random() * (700 - 100) ) + 100));
                aliados[4].execute();

                aliados[3].setTurnRight((int)(Math.random() * (90) ) - 45);
                aliados[3].setAhead(((int)(Math.random() * (700 - 100) ) + 100));
                aliados[3].execute();

                aliados[2].setTurnRight((int)(Math.random() * (90) ) - 45);
                aliados[2].setAhead(((int)(Math.random() * (700 - 100) ) + 100));
                aliados[2].execute();
            }

            /* Obtem informacoes dos robos do time inimigo - as posicoes dos 
             ** robos inimigos devem ser fixadas no outro time - o que eh
             ** importante para executar os algoritmos de busca
             */
            inimigos = new RobotInfo[numRobos];

            // observar que o arg eh o proprio nome do time para impedir trapacas
            inimigos = getServerRef().getEnemyTeamInfo(nomeTime);

            System.out.println("*** Inimigos de " + myTeam + " ***");
            for (int i = 0; i < numRobos; i++) {
                System.out.println(" [" + i + "]: X=" + inimigos[i].getX()
                        + " Y=" + inimigos[i].getY());
            }
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public boolean executeAction(String ag, Structure action) {
        return true;
    }

    public void mainLoop() throws RemoteException {
        robos = getServerRef().getMyTeamInfo(myTeam);

        RobotInfo refem = robos[0];
        double xRefem = refem.getX();
        double yRefem = refem.getY();
        RMIRobotInterface[] teamRef = getTeamRef();

        System.out.println("salvador " + (int) teamRef[1].getRobotInfo().getX() + ", "
                + (int) teamRef[1].getRobotInfo().getY());
        System.out.println("   refem " + (int) xRefem + ", " + (int) yRefem);

        // aguarda o robo aliado 3 acabar seu movimento pois eh o que vai
        // mais longe
        if (teamRef[3].getDistanceRemaining() <= 0.1 && teamRef[2].getDistanceRemaining() <= 0.1 && teamRef[4].getDistanceRemaining() <= 0.1) {

            // para entao mandar o salvador se movimentar (evita colisoes)
            if (primeiraVez) {
                primeiraVez = false;
                ArrayDeque<Character> filaDeAcoes = new ArrayDeque();
                //TODO Planeja o caminho usando o A*
                
                //Executa o caminho achado pelo A*
                while(!filaDeAcoes.isEmpty()){
                    
                }
            }
        }

    }

    @Override
    public void end() {
        try {
            super.getEnvironmentInfraTier().getRuntimeServices().stopMAS();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        TimeTarBuscaATeamEnv team = new TimeTarBuscaATeamEnv();
        team.init(new String[]{nomeTime, "localhost"});

        while (true) {
            try {
                team.mainLoop();
                Thread.sleep(20);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
