package it.polito.tdp.formulaone.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {
	
	private FormulaOneDAO dao;
	private Map<Integer, Race> idMap;
	private Graph<Race, DefaultWeightedEdge> grafo;
	
	public Model() {
		dao = new FormulaOneDAO();
		idMap= new HashMap<>();
		
	}

	public List< Season> getAnni() {

		return dao.getAllSeasons();
	}

	public String creaGrafo(Season annoInput) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		dao.getRacesByYear(annoInput, idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		
		List<Adiacenza> adj= dao.getArchiePeso(annoInput, idMap);
		for(Adiacenza a: adj) {
			Graphs.addEdgeWithVertices(grafo, a.getR1(), a.getR2(), a.getPeso());
		}
		

		System.out.println("vertici: "+grafo.vertexSet().size());
		System.out.println("archi: "+grafo.edgeSet().size());
		for(DefaultWeightedEdge edge: grafo.edgeSet()) {
			System.out.println(edge + "peso:" +grafo.getEdgeWeight(edge));			
		}

		String ris = "";
		double max = 0;

		for (Adiacenza a1 : adj) {
			double peso1 = 0;
			DefaultWeightedEdge edge1 = grafo.getEdge(a1.getR1(), a1.getR2());
			peso1 = grafo.getEdgeWeight(edge1);

			if (peso1 > max) {
				max = peso1;
				ris = "Arco di peso massimo tra: " + a1.getR1().getName() + " / " + a1.getR2().getName()
						+ " con peso:  " + max + "\n";
			}
			else if(peso1 ==max) {
				max =peso1;
				ris += "Arco di peso massimo tra: " + a1.getR1().getName() + " / " + a1.getR2().getName()
						+ " con peso:  " + max + "\n";
			}
		}

		return ris;

	}
}
