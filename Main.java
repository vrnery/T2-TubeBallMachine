import java.awt.*;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    // Classe Ponto representa um ponto no plano (x,y)
    // Também armazena a posição desse ponto no array (pos)
    // e a distância até o ponto corrente (que é reutilizada dentro
    // do loop)
    private static class Ponto implements Comparable<Ponto> {
        public int pos;
        public double x,y;
        public double dist;
        public Ponto(int pos, double x, double y) {
            this.pos = pos;
            this.x = x;
            this.y = y;
            this.dist = 0;
        }

        // Permite ordenar a lista de pontos da menor para a maior distância
        @Override
        public int compareTo(Ponto outro) {
            if(this.dist < outro.dist)
                return -1;
            else if(this.dist > outro.dist)
                return 1;
            return 0;
        }

        @Override
        public String toString() {
            return pos+": ("+x+", "+y+" - "+dist+")";
        }
    }

    public static void main(String[] args) throws IOException {
        ArrayList<Ponto> lista = new ArrayList<>();
        Path path1 = Paths.get("dados.csv");
        // Lê o arquivo e vai acrescentando os pontos na lista
        try (Scanner sc = new Scanner(Files.newBufferedReader(path1, Charset.forName("utf8"))))
        {
            sc.useDelimiter("[;\n]");
            double x, y;
            int total = 0;
            while(sc.hasNext()) {
                String sx = sc.next();
                String sy = sc.next();
                x = Double.parseDouble(sx);
                y = Double.parseDouble(sy);
		        // Cada ponto tem um índice, que corresponde à ordem original do ponto na lista (i.e. número do vértice)
                Ponto p = new Ponto(total++, x,y);
                lista.add(p);
            }
        }

        System.out.println("Total pontos: "+lista.size());

    	// Cria o grafo com o total de pontos lidos
        EdgeWeightedGraph ewg = new EdgeWeightedGraph(lista.size());

        // Clona a lista original (para ordenar...)
        ArrayList<Ponto> ordenada = new ArrayList<>(lista);

        // Para cada ponto da lista...
        for(int pos=0; pos<lista.size(); pos++) {
            Ponto p = lista.get(pos);
            // Calcula a distância entre ele e todos os demais
            for(int pos2=0; pos2<ordenada.size(); pos2++) {
                Ponto p2 = ordenada.get(pos2);
		        // Se for o mesmo ponto...
                if (p.pos == p2.pos)
                    p2.dist = 1e10; // Seta uma distância enorme, ou seja, ele ficará no final da lista ordenada
                else
		            // Caso contrário, calcula a distância entre ele e o ponto sendo testado
                    p2.dist = Math.sqrt(Math.pow(p.x-p2.x,2)+Math.pow(p.y-p2.y,2));
            }
    	    // Ordena a segunda lista da menor para a maior distâncias
            Collections.sort(ordenada);
	        // Verificando se faz sentido o resultado...
            System.out.println(p);
            System.out.println(ordenada.get(0));
            System.out.println(ordenada.get(1));
            System.out.println(ordenada.get(2));
            System.out.println();

            // Adiciona as arestas ao grafo...
            // (pega apenas as 3 mais próximas, altere o contador abaixo para mudar isso)
            for(int cont=0; cont<3; cont++) {
                Ponto aux = ordenada.get(cont);
	    	    // Como Ponto.pos contém o índice original de cada ponto,
        		// usamos esses valores como vértices
        		// e dist como o peso (valor) da aresta
                Edge e = new Edge(p.pos, aux.pos, aux.dist);
                ewg.addEdge(e);
            }
        }
        System.out.println("Arestas: "+ewg.E());

    	// Realiza a MST usando algoritmo de Prim
        PrimMST mst = new PrimMST(ewg);

	    // Gera o arquivo de saída
        Path outpath = Paths.get("saida.txt");
        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(outpath, Charset.forName("utf8"))))
        {
            for(Edge e: ewg.edges()) {
		        // Obtém os dois vértices da aresta
                int v = e.either();
                int o = e.other(v);
                // Se e.getColor não for vazio, é porque ela faz parte da MST
                int c = e.getColor().equals("lightgray") ? 0 : 1;
                pw.println(v+" "+o+" "+c);
            }
        }
        // Visualize a saída com: python plotgraph.py saida.txt
        // (precisa ter o pacote matplotlib instalado no Python, ou usar
        //  uma distribuição que tenha ele, como Anaconda ou WinPython)
        // O programa irá desenhar todo o grafo, destacando em vermelho
        // as arestas que fazem parte da MST
    }
}
