import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.management.timer.Timer;

/**
 * TubeBallMachine
 */
public class TubeBallMachine {

    public static void main(String[] args) {
        //System.out.println("Trabalho 2 Algoritmo e Estrutura de Dados");
        //System.out.println("Autores: Arthur, Cassius, Vanderson");

        try {
            long tempIni = System.currentTimeMillis();
            In in = new In(args[0]);

            int tuboCol = in.readInt();
            int tuboRow = in.readInt();
            tuboRow++;
            int tuboSaida[] = new int[tuboCol];

            int a, b, c, d;

            int tubos[][] = new int[tuboCol][tuboRow];

            int totalVertices = 0;
            for (int q = 0; q < tuboCol; tubos[q++][0] = totalVertices++)
                ;

            List<int[]> vet = new ArrayList<>();

            for (int j = 1; j < tuboRow; j++) {
                for (int i = 0; i < tuboCol; i++) {
                    tubos[i][j] = totalVertices++;
                    a = i;
                    b = j - 1;
                    c = i;
                    d = j;
                    vet.add(new int[] { tubos[a][b], tubos[c][d], 300 });
                }
            }

            boolean lendo = true;

            while (lendo) {
                try {
                    a = in.readInt();
                    b = in.readInt();
                    c = in.readInt();
                    d = in.readInt();
                    // System.out.printf("%d(%d, %d) -> %d(%d, %d) | ", tubos[a][b], a, b,
                    // tubos[c][d], c, d);

                    int m[] = new int[] { tubos[a][b], tubos[c][d], 300 };
                    for (int n=0; n<vet.size(); n++) {
                        if ((vet.get(n)[0] == m[0]) && (vet.get(n)[1] == m[1]) && (vet.get(n)[2]==300)) {
                            //System.out.printf("%d %d %d\n",vet.get(n)[0],vet.get(n)[1],vet.get(n)[2]);
                            vet.remove(n);
                            break;
                        }
                    }
                    vet.add(new int[] { tubos[a][b], tubos[c][d], 0 });
                } catch (Exception e) {
                    // System.out.println("Parou de ler: "+e.getMessage());
                    lendo = false;
                }
            }

            // System.out.println(totalVertices);
            File file = new File("tinyEWG-"+args[0]);
            if (!file.exists())
                file.createNewFile();
            
            File complex = new File("Complex-"+args[0]);
            if(!complex.exists())
                complex.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsolutePath());
            BufferedWriter bw = new BufferedWriter(fw);

            FileWriter fwComplex = new FileWriter(complex.getAbsolutePath());
            BufferedWriter bwComplex = new BufferedWriter(fwComplex);

            bwComplex.write("Trabalho 2 Algoritmo e Estrutura de Dados"+ "\n");
            bwComplex.write("Autores: Arthur, Cassius, Vanderson"+ "\n");
            bwComplex.write("Algoritmo de Dijkstra"+ "\n");
            bwComplex.write("Total de Vertices: "+totalVertices+ "\n");
            bwComplex.write("Total de Arestas: "+vet.size()+ "\n");

            bw.write(totalVertices + "\n");

            bw.write(vet.size() + "\n");

            for (int[] is : vet)
                bw.write(is[0] + " " + is[1] + " " + is[2] + "\n");

            bw.close();

            EdgeWeightedDigraph dig = new EdgeWeightedDigraph(new In(file.getAbsolutePath()));

            //System.out.println("Total vértices: " + dig.V());
            //System.out.println("Total arestas: " + dig.E());

            for (int s = 0; s < tuboCol; s++) {
                double distTo[] = new double[dig.V()];
                DirectedEdge edgeTo[] = new DirectedEdge[dig.V()];
                IndexMinPQ<Double> minheap = new IndexMinPQ<>(dig.V());

                // Inicializa o array distTo para o maior valor possível
                // em um double (POSITIVE_INFINITY)
                for (int v = 0; v < distTo.length; v++)
                    distTo[v] = Double.POSITIVE_INFINITY;

                // por definição, a distância do primeiro ao primeiro é sempre ZERO
                distTo[s] = 0;

                // insere primeiro elemento da árvore (o vértice inicial)
                minheap.insert(s, 0.0);

                // Enquanto houver algum vértice na fila de prioridade...
                while (!minheap.isEmpty()) {
                    // Retira o primeiro, isto é, o vértice com menor distTo
                    int v = minheap.delMin();
                    // Para cada aresta a partir dele...
                    for (DirectedEdge e : dig.adj(v)) {
                        int ini = e.from();
                        int fim = e.to();
                        // Novo caminho: soma do acumulado até este vértice + o peso da aresta
                        double novaDist = distTo[ini] + e.weight();
                        // Se o novo caminho for melhor que o que estiver armazenado lá...
                        if (distTo[fim] > novaDist) {
                            // Este passa a ser o melhor (mais curto)
                            distTo[fim] = novaDist;
                            edgeTo[fim] = e;
                            // Se o vértice já estiver na fila de prioridade...
                            if (minheap.contains(fim))
                                // Diminui o valor associado
                                minheap.decreaseKey(fim, distTo[fim]);
                            else
                                // Caso contrário, insere o novo vértice
                                minheap.insert(fim, distTo[fim]);
                        }
                    }
                }

                // Verificar qual a menor saida do tubo
                int mD = totalVertices - 1;
                //System.out.println("Tubo "+s);
                //System.out.printf("Calculo: %d-%d-1 == %d\n", totalVertices, tuboCol, distTo.length);
                for (int t = (totalVertices - tuboCol); t < (totalVertices - 1); t++) {
                    System.out.print(t+":"+distTo[t]+" | "+mD+":"+distTo[mD]+" | ");
                    if (distTo[t] < distTo[mD])
                        mD = t;
                    System.out.print(mD+"\n");
                }

                //System.out.println("Posicao: "+mD);
                tuboSaida[(tuboCol - (totalVertices - mD))]++;
                //for (int w=0; w<tuboCol; System.out.printf("tubo%d = %d | ",w,tuboSaida[w++]));
                //System.out.println("");

                // Exibe resultado
                // for (int v = 0; v < dig.V(); v++)
                // System.out.println(v + ": " + edgeTo[v]);

            }

            int tuboBolaSaida = 0;
            //System.out.print(tuboBolaSaida+" = "+tuboSaida[tuboBolaSaida]);
            for (int k = 1; k < tuboCol; k++) {
                //System.out.print(" | "+k+" = "+tuboSaida[k]);
                if (tuboSaida[tuboBolaSaida] < tuboSaida[k]) { tuboBolaSaida = k; }
            }
            //System.out.printf("Tubo %d com saída de %d bolas\n",tuboBolaSaida,tuboSaida[tuboBolaSaida]);

            bwComplex.write("Quantidade de tubos: "+tuboCol+ "\n");
            bwComplex.write("Quantidade de linhas: "+(tuboRow-1)+ "\n");
            bwComplex.write("Tubo com mais saída de bolas: "+tuboBolaSaida+ "\n");
            bwComplex.write("Quantidade de bolas saindo pelo tubo "+tuboBolaSaida+": "+tuboSaida[tuboBolaSaida]+ "\n");
            bwComplex.write("Tempo de execução: "+((System.currentTimeMillis() - tempIni)/1000)+"ms"+ "\n");
            bwComplex.close();
        } catch (IOException e) {
            System.err.println("Erro ao abrir o arquivo: " + args[0]);
        }
    }
}