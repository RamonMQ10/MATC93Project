package sparqlWorkshop;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Resource;

public class JavaDBPediaExample {

	public static List<String> lerArquivoMovieLens() {

		int i = 1;
		List<String> filmes = new ArrayList<String>();

		try {
			FileReader arq = new FileReader("u.item");
			BufferedReader lerArq = new BufferedReader(arq);

			String linha = lerArq.readLine();

			while (linha != null) {

				String[] valores = linha.split("#");

				if (valores.length > 1) {

					String filme[] = valores[1].split(" ");

					String tituloFilme = filme[0];

					for (int x = 1; x < filme.length - 1; x++) {

						tituloFilme = tituloFilme.concat(" " + filme[x]);
					}

					filmes.add(tituloFilme);

					
					System.out.println("Filme " + i + " Titulo: " + tituloFilme);

					i++;
				}
				
				linha = lerArq.readLine(); // lê da segunda até a última linha

			}

			arq.close();

		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}

		System.out.println();

		return filmes;

	}

	public static void main(String[] args) {

		List<String> filmes = lerArquivoMovieLens();

		int t = 0;

		for (int i = 0; i < filmes.size(); i++) {

			if (filmes.get(i).contains("'")) {

				t++;
			}

		}
		
		System.out.println("tem ': " + t);
		
		int filmesEncontradosDBPedia = 0;

		String prefixos = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
				+ "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> "
				+ "PREFIX dbpprop: <http://dbpedia.org/property/> ";

		for (int i = 0; i < filmes.size(); i++) {

			if (!filmes.get(i).contains("'")) {

				String query2 = prefixos + "SELECT DISTINCT ?name ?country ?abstract ?budget ?director "

						+ "WHERE { "

						+ "?instance a <http://dbpedia.org/ontology/Film>. "

						+ " ?instance foaf:name ?name . "

						+ " FILTER REGEX (?name, '^" + filmes.get(i) + "$', 'i'). " + " OPTIONAL { "
						+ "    ?instance dbpprop:country ?country  " + " } " + "OPTIONAL { "
						+ "?instance dbpedia-owl:abstract ?abstract . " + " FILTER (LANG(?abstract) = 'en'). " + "} "
						+ "OPTIONAL { " + "?instance dbpedia-owl:budget ?budget  " + "} " + "OPTIONAL { "
						+ " ?instance dbpedia-owl:director ?director  " + "} " + "}";

				QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql",
						query2);

				ResultSet resultSet = queryExecution.execSelect();

				// ResultSetFormatter.out(System.out, resultSet);

				List<QuerySolution> querySolution = ResultSetFormatter.toList(resultSet);

				if (!querySolution.isEmpty()) {
					filmesEncontradosDBPedia++;
				}

				for (int z = 0; z < querySolution.size(); z++) {
					System.out.println("Filme " + filmesEncontradosDBPedia + ": " + querySolution.get(z));
				}

				System.out.println("Filme: " + filmes.get(i) + " Total de Propriedades:" + querySolution.size());

				queryExecution.close();

			}
		}
		
		System.out.println("\nTotal de Filmes MovieLens: " + filmes.size());
		System.out.println("Total de Filmes Encontrados DBPedia: " + filmesEncontradosDBPedia);
	}
}
