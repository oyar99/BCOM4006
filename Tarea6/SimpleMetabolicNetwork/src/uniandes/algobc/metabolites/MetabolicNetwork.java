package uniandes.algobc.metabolites;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
/**
 * Represents a metabolic network of reactions on metabolites
 * @author Jorge Duitama
 */
public class MetabolicNetwork {

	private Map<String,Enzyme> enzymes = new TreeMap<String,Enzyme>(); 
	private Map<String,Metabolite> metabolites = new TreeMap<String,Metabolite>();
	private Set<String> compartments = new TreeSet<String>();
	private Map<String,Reaction> reactions = new TreeMap<String,Reaction>();
	/**
	 * Adds a new gene product that can catalyze reactions
	 * @param product New gene product
	 */
	public void addEnzyme(Enzyme enzyme) {
		enzymes.put(enzyme.getId(), enzyme);
	}
	/**
	 * Adds a new metabolite. If a metabolite with the given name is already added, it 
	 * @param metabolite New metabolite
	 */
	public void addMetabolite(Metabolite metabolite) {
		metabolites.put(metabolite.getId(), metabolite);
		compartments.add(metabolite.getCompartment());
	}
	/**
	 * Adds a new reaction
	 * @param r New reaction between metabolites
	 */
	public void addReaction(Reaction r) {
		reactions.put(r.getId(),r);
		
	}
	/**
	 * Returns the gene product with the given id
	 * @param id of the product to search
	 * @return GeneProduct with the given id
	 */
	public Enzyme getEnzyme (String id) {
		return enzymes.get(id);
	}
	/**
	 * Returns the metabolite with the given id
	 * @param id of the metabolite to search
	 * @return Metabolite with the given id
	 */
	public Metabolite getMetabolite (String id) {
		return metabolites.get(id);
	}
	/**
	 * @return List of metabolites in the network
	 */
	public List<Metabolite> getMetabolitesList() {
		return new ArrayList<Metabolite>(metabolites.values());
	}
	/**
	 * @return List of reactions in the network
	 */
	public List<Reaction> getReactionsList () {
		return new ArrayList<Reaction>(reactions.values());
	}
	/**
	 * @return List of products - metabolites that do not participate as substratum
	 */
	public List<Metabolite> getProducts() {
		List<Metabolite> metabolitesList = new ArrayList<>();
		List<Reaction> reactions = this.getReactionsList();

		for (Reaction r: reactions) {
			for (ReactionComponent rr: r.getProducts()) {
				metabolitesList.add(rr.getMetabolite());
			}
		}

		HashMap<String, Metabolite> products = new HashMap<String, Metabolite>();

		for (Metabolite m: metabolitesList) {
			products.put(m.getId(), m);
		}

		for (Reaction r:reactions) {
			for (ReactionComponent rr: r.getReactants()) {
				products.remove(rr.getMetabolite().getId());
			}
		}

		return new ArrayList<>(products.values());
	}
	/**
	 * @return List of substratum - metabolites that do not participate as products
	 */
	public List<Metabolite> getSubstratum() {
		List<Metabolite> metabolitesList = this.getMetabolitesList();

		HashMap<String, Metabolite> substratum = new HashMap<String, Metabolite>();

		for (Metabolite m: metabolitesList) {
			substratum.put(m.getId(), m);
		}

		List<Reaction> reactions = this.getReactionsList();

		for (Reaction r:reactions) {
			for (ReactionComponent rr: r.getProducts()) {
				substratum.remove(rr.getMetabolite().getId());
			}
		}

		return new ArrayList<>(substratum.values());
	}

	/**
	 * 
	 * @return Lista de adyacencia representada como un mapa donde cada llave es el nodo de origen, y el valor
	 * corresponde a un conjunto de nodos con los que es adyacente. Cada entrada en el conjunto es una pareja
	 * donde la llave es el id del nodo destino y el valor es el peso del eje.
	 */
	public HashMap<String, HashMap<String, Integer>> getMetabolismGraph() {
		HashMap<String, HashMap<String, Integer>> adj = new HashMap<>();

		List<Reaction> reactions = this.getReactionsList();

		for (Reaction r: reactions) {
			for (ReactionComponent sub: r.getReactants()) {
				for (ReactionComponent prod: r.getProducts()) {
					if (adj.containsKey(sub.getMetabolite().getId())) {
						HashMap<String, Integer> neigh = adj.get(sub.getMetabolite().getId());
						if (neigh.containsKey(prod.getMetabolite().getId())) {
							neigh.put(prod.getMetabolite().getId(), neigh.get(prod.getMetabolite().getId()) + 1);
						} else {
							neigh.put(prod.getMetabolite().getId(), 1);
						}
						adj.put(sub.getMetabolite().getId(), neigh);
					} else {
						HashMap<String, Integer> neigh = new HashMap<>();
						neigh.put(prod.getMetabolite().getId(), 1);
						adj.put(sub.getMetabolite().getId(), neigh);
					}
				}
			}
		}

		return adj;
	}

	public void outputGraph(HashMap<String, HashMap<String, Integer>> adj) {
		// Specify the file path
        String filePath = "adj.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

			writer.write("Metabolito 1" +"\t" + "Metabolito 2" + "\t" + "Peso" + "\n");

			for (String u: adj.keySet()) {
				for (Entry<String, Integer> v: adj.get(u).entrySet())
					writer.write(u +"\t" + v.getKey() + "\t" + v.getValue() + "\n");
			}
        } catch (IOException e) {
            // :(
        }
	}
	
	public static void main(String[] args) throws IOException {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network = loader.loadNetwork(args[0]);
		System.out.println("Enzymes");
		for(Enzyme enzyme:network.enzymes.values()) {
			System.out.println(enzyme.getId()+" "+enzyme.getName());
		}
		System.out.println();
		
		List<Metabolite> metabolitesList = network.getMetabolitesList();
		System.out.println("Loaded "+metabolitesList.size()+" metabolites: ");
		for(Metabolite m:metabolitesList) {
			System.out.println(m.getId()+" "+m.getName()+" "+m.getCompartment()+" "+m.getChemicalFormula());
		}
		System.out.println();
		List<Reaction> reactions = network.getReactionsList();
		System.out.println("Loaded "+reactions.size()+" reactions");

		List<Metabolite> substratum = network.getSubstratum();

		// Metabolitos que solo participan como sustratos
		System.out.println("Sustratos: ");
		for (Metabolite m: substratum) {
			System.out.println(m.getId() + " " + m.getName());
		}

		List<Metabolite> products = network.getProducts();

		// Metabolitos que solo participan como productos
		System.out.println("products: ");
		for (Metabolite m: products) {
			System.out.println(m.getId() + " " + m.getName());
		}

		network.outputGraph(network.getMetabolismGraph());
	}
}
