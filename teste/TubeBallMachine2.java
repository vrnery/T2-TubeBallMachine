import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class TubeBallMachine2 {
    public static void main(String[] args) {
        try {
            long tempIni = System.currentTimeMillis();
            In in = new In(args[0]);

            int tuboCol = in.readInt();
            int tuboRow = in.readInt();

            int tuboSaida[] = new int[tuboCol];
            int tubos[][][] = new int[tuboCol][tuboRow][2];

            boolean lendo = true;
            while(lendo) {
                try {
                    tubos[in.readInt()][in.readInt()] = new int[] {in.readInt(), in.readInt()};
                } catch (Exception e) {
                    lendo = false;
                }
            }

            int auxCol, auxRow, auxPosCol;

            for (int col = 0; col < tuboCol; col++) {
                auxCol = col;
                for (auxRow = 0; auxRow < tuboRow; auxRow++) {
                    if (tubos[auxCol][auxRow][1] != 0) {
                        auxPosCol = tubos[auxCol][auxRow][0];
                        auxRow = tubos[auxCol][auxRow][1];
                        auxCol = auxPosCol;
                    }
                }
                tuboSaida[auxCol]++;
            }

            int tuboMais = 0;
            for (int k = 1; k<tuboCol; k++) {
                if (tuboSaida[tuboMais] < tuboSaida[k])
                    tuboMais = k;
            }

            File complex = new File("Complex-"+args[0]);
            if(!complex.exists())
                complex.createNewFile();

            FileWriter fwComplex = new FileWriter(complex.getAbsolutePath());
            BufferedWriter bwComplex = new BufferedWriter(fwComplex);

            bwComplex.write("Trabalho 2 Algoritmo e Estrutura de Dados"+ "\n");
            bwComplex.write("Autores: Arthur, Cassius, Vanderson"+ "\n");
            bwComplex.write("Algoritmo de Dijkstra"+ "\n");
            bwComplex.write("Quantidade de tubos: "+tuboCol+ "\n");
            bwComplex.write("Quantidade de linhas: "+tuboRow+ "\n");
            bwComplex.write("Tubo com mais saída de bolas: "+tuboMais+ "\n");
            bwComplex.write("Quantidade de bolas saindo pelo tubo "+tuboMais+": "+tuboSaida[tuboMais]+ "\n");
            bwComplex.write("Tempo de execução: "+((System.currentTimeMillis() - tempIni)/1000)+"s"+ "\n");
            bwComplex.close();
        } catch (Exception e) {
            System.err.println("Erro ao abrir o arquivo: "+args[0]);
        }
    }
}