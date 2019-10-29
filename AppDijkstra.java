import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

//import edu.princeton.cs.algs4;

public class AppDijkstra {	

	public static void main(String[] args) {

		// Exemplo: lendo os dados do arquivo com as classes do Sedgewick
//		EdgeWeightedDigraph dig = new EdgeWeightedDigraph(new In("tinyEWG.txt"));
//		EdgeWeightedDigraph dig = new EdgeWeightedDigraph(new In("mediumEWG.txt"));
		EdgeWeightedDigraph dig = new EdgeWeightedDigraph(new In("1000EWD.txt"));
		System.out.println("Total vértices: "+dig.V());
		System.out.println("Total arestas: "+dig.E());

		int s = 0; // Vértice de origem
		boolean showNumbers = false; // true para exibir números dos vértices

		double distTo[] = new double[dig.V()];
		DirectedEdge edgeTo[] = new DirectedEdge[dig.V()];
		IndexMinPQ<Double> minheap = new IndexMinPQ<>(dig.V());

		// Inicializa o array distTo para o maior valor possível
		// em um double (POSITIVE_INFINITY)
		for(int v=0; v<distTo.length; v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s] = 0; // por definição, a distância do primeiro ao primeiro é sempre ZERO

		minheap.insert(s, 0.0); // insere primeiro elemento da árvore (o vértice inicial)

		// Enquanto houver algum vértice na fila de prioridade...
		while(!minheap.isEmpty())
		{
			// Retira o primeiro, isto é, o vértice com menor distTo
			int v = minheap.delMin();
			// Para cada aresta a partir dele...
			for(DirectedEdge e: dig.adj(v))
			{
				int ini = e.from();
				int fim = e.to();
				// Novo caminho: soma do acumulado até este vértice + o peso da aresta
				double novaDist = distTo[ini] + e.weight();
				// Se o novo caminho for melhor que o que estiver armazenado lá...
				if(distTo[fim] > novaDist)
				{
					// Este passa a ser o melhor (mais curto)
					distTo[fim] = novaDist;
					edgeTo[fim] = e;
					// Se o vértice já estiver na fila de prioridade...
					if(minheap.contains(fim))
						// Diminui o valor associado
						minheap.decreaseKey(fim, distTo[fim]);
					else
						// Caso contrário, insere o novo vértice
						minheap.insert(fim, distTo[fim]);
				}
			}
		}

		// Exibe resultado
		for(int v=0; v<dig.V(); v++) {
			System.out.println(v+": "+edgeTo[v]);
		}

		// Descomente as linhas abaixo para visualizar os números dos
		// vértices e o vértice inicial
		String styleSheet = null;
		if(showNumbers) {
			styleSheet =
					"node {" +
							"	fill-color: black;" +
							"   text-size: 20;" +
							"}" +
							"node.marked {" +
							"	fill-color: red;" +
							"}" +
							"node.start {" +
							"   fill-color: green;" +
							"   text-size: 30;" +
							"}";
		}
		// Descomente as linhas abaixo para ocultar os números dos vértices
		// (útil para grafos GRANDES)
			else
			{
				styleSheet =
						"node { " +
								"	size: 8px; " +
								"	fill-color: #777; " +
								"	text-mode: hidden; " +
								"	z-index: 0; " +
								"}" +
								"node.marked {" +
								"   fill-color: red;" +
								"}" +
								"node.start {" +
								"   fill-color: green;" +
								"}" +
								"edge { " +
								"	shape: line;" +
								"	fill-color: #222;" +
								"	arrow-size: 3px, 2px;" +
								"}";
			}
		// API do Graphstream:

		// Cria um grafo
		Graph g = new SingleGraph("Dijkstra");

		for(int v=0; v<dig.V(); v++) {
			Node n = g.addNode(v+"");
			n.addAttribute("ui.label", v);
			if(edgeTo[v] != null)
				n.addAttribute("ui.class", "marked");
		}

		// Destaca em verde o vértice inicial
		Node n = g.getNode(s);
		n.setAttribute("ui.class", "start");
		n.setAttribute("ui.style", "size: 30;");

		for(DirectedEdge e: dig.edges()) {
			int v1 = e.from();
			int v2 = e.to();
			double w = e.weight();
			Edge ne = g.addEdge(v1+"->"+v2, v1, v2, true);
			//ne.addAttribute("ui.label", w);
		}

		for(int v=0; v<dig.V(); v++)
		{
			if(edgeTo[v] != null) {
				Edge e = g.getEdge(edgeTo[v].from()+"->"+edgeTo[v].to());
				e.addAttribute("ui.style", "size: 2px; fill-color: red;");
			}
		}

		// Solicita alta qualidade no desenho
		g.addAttribute("ui.quality");
		g.addAttribute("ui.antialias");
		
		// Caso se queira utilizar todos os recursos de CSS do visualizador avançado:
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		// Informa a style sheet a ser utilizada (se for o caso)
	    g.addAttribute("ui.stylesheet", styleSheet);
	    
	    // Exibe o grafo
		g.display();
				
	}
}
