import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TubeBallMachine
 */
public class TubeBallMachine {

    public static void main(String[] args) {
        System.out.println("Trabalho 2 Algoritmo e Estrutura de Dados");
        System.out.println("Autores: Arthur, Cassius, Vanderson");

        try {
            // FileReader arq = new FileReader(args[0]);
            // BufferedReader lerarq = new BufferedReader(arq);
            In in = new In(args[0]);

            File file = new File("tinyEWG.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsolutePath());
            BufferedWriter bw = new BufferedWriter(fw);

            // String linha = lerarq.readLine();
            // String splitlinha[] = new String[4];
            // splitlinha = linha.split(" ");

            // int tuboCol = Integer.parseInt(splitlinha[0]);
            // int tuboRow = Integer.parseInt(splitlinha[1]);
            int tuboCol = in.readInt();
            int tuboRow = in.readInt();

            int a, b, c, d;

            // System.out.println(tuboCol+" Tubos com " + tuboRow + " linhas");
            bw.write((tuboCol * tuboRow) + "\n");

            int tubos[][] = new int[tuboCol][tuboRow + 1];

            int totalVertices = 0;

            List<int[]> vet = new ArrayList<>();

            // List<Edge> listEd = new ArrayList<Edge>();

            for (int j = 0; j < tuboRow; j++) {
                for (int i = 0; i < tuboCol; i++) {
                    tubos[i][j] = totalVertices++;
                    if (j > 0) {
                        a = i;
                        b = j - 1;
                        c = i;
                        d = j;
                        // System.out.printf("%d(%d, %d) -> %d(%d, %d) | ", tubos[a][b], a, b,
                        // tubos[c][d], c, d);
                        // listEd.add(new Edge(tubos[i][j-1], tubos[i][j], 1));
                        vet.add(new int[] { tubos[a][b], tubos[c][d], 1 });
                    }
                }
            }

            boolean lendo = true;

            while (lendo) {
                try {
                    a = in.readInt();
                    b = in.readInt();
                    c = in.readInt();
                    d = in.readInt();
                    System.out.printf("%d(%d, %d) -> %d(%d, %d) | ", tubos[a][b], a, b, tubos[c][d], c, d);
                    vet.add(new int[] { tubos[a][b - 1], tubos[c][d - 1], 0 });
                } catch (Exception e) {
                    // System.out.println("Parou de ler: "+e.getMessage());
                    lendo = false;
                }
            }

            System.out.println(totalVertices);

            // while(lerarq.ready()) {
            // linha = lerarq.readLine();
            // splitlinha = linha.split(" ");
            // //dig.addEdge(new
            // Edge(tubos[Integer.parseInt(splitlinha[0])][(Integer.parseInt(splitlinha[1])-1)],
            // tubos[Integer.parseInt(splitlinha[2])][(Integer.parseInt(splitlinha[3])-1)],
            // 0));
            // //listEd.add(new
            // Edge(tubos[Integer.parseInt(splitlinha[0])][(Integer.parseInt(splitlinha[1])-1)],
            // tubos[Integer.parseInt(splitlinha[2])][(Integer.parseInt(splitlinha[3])-1)],
            // 0));
            // vet.add(new
            // int[]{tubos[Integer.parseInt(splitlinha[0])][(Integer.parseInt(splitlinha[1])-1)],
            // tubos[Integer.parseInt(splitlinha[2])][(Integer.parseInt(splitlinha[3])-1)],
            // 0});
            // }

            // lerarq.close();

            bw.write(vet.size() + "\n");

            for (int[] is : vet) {
                bw.write(is[0] + " " + is[1] + " " + is[2] + "\n");
            }

            bw.close();

            EdgeWeightedDigraph dig = new EdgeWeightedDigraph(new In(file.getAbsolutePath()));

            System.out.println("Total vértices: " + dig.V());
            System.out.println("Total arestas: " + dig.E());

            // int s = 0; // Vértice de origem

            for (int s = 0; s < tuboCol; s++) {
                double distTo[] = new double[dig.V()];
                DirectedEdge edgeTo[] = new DirectedEdge[dig.V()];
                IndexMinPQ<Double> minheap = new IndexMinPQ<>(dig.V());

                // Inicializa o array distTo para o maior valor possível
                // em um double (POSITIVE_INFINITY)
                for (int v = 0; v < distTo.length; v++)
                    distTo[v] = Double.POSITIVE_INFINITY;
                distTo[s] = 0; // por definição, a distância do primeiro ao primeiro é sempre ZERO

                minheap.insert(s, 0.0); // insere primeiro elemento da árvore (o vértice inicial)

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
                int mD = totalVertices-1;
                for (int t = (totalVertices-tuboCol-1); t<(totalVertices-2); t++) {
                    if (distTo[t] < distTo[mD]) {
                        mD = t;
                    }
                }
                tubos[(tuboCol-(totalVertices-mD))][tuboRow]++;

                // Exibe resultado
                for (int v = 0; v < dig.V(); v++) {
                    System.out.println(v + ": " + edgeTo[v]);
                }

            }
            
            for (int k=0; k<tuboCol; System.out.printf("Tubo%d: %d | ",k,tubos[k++][tuboRow]));
            System.out.println("");
            // System.out.println(listEd.size());

            // = new EdgeWeightedGraph();

            // = new EdgeWeightedGraph();

        } catch (IOException e) {
            System.err.println("Erro ao abrir o arquivo: " + args[0]);
        }
    }
}