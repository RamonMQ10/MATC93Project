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

import Database.BancoDados;

public class JavaDBPediaExample {
	
	static BancoDados bancoDados;

	public static List<String> lerArquivoMovieLens() {

		int i = 1;
		List<String> filmes = new ArrayList<String>();

		try {
			FileReader fileReader = new FileReader("u.item");
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String linha = bufferedReader.readLine();

			while (linha != null) {

				String[] valores = linha.split("#");

				if (valores.length > 1) {

					String filme[] = valores[1].split(" ");

					String tituloFilme = filme[0];

					for (int x = 1; x < filme.length - 1; x++) {

						tituloFilme = tituloFilme.concat(" " + filme[x]);
					}

					filmes.add(tituloFilme);

					//System.out.println("Filme " + i + " Titulo: " + tituloFilme);

					i++;
				}

				linha = bufferedReader.readLine(); // lê da segunda até a última linha

			}

			fileReader.close();

		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}

		System.out.println();

		return filmes;

	}

	public static void insertData(String name, String country, String varAbstract, String budget, String releaseDate,
			String runtime, String alternateTitle, String starring , String director , String producer) {

		if (!name.equals("")) {
			String dados[] = name.split("\"");
			name = dados[1];
		}

		if (!country.equals("")) {
			String dados[] = country.split("\"");
			country = dados[1];
		}

		if (!varAbstract.equals("")) {
			String dados[] = varAbstract.split("\"");
			varAbstract = dados[1];
		}

		if (!budget.equals("")) {
			String dados[] = budget.split("\"");
			budget = dados[1];
		}

		if (!releaseDate.equals("")) {
			String dados[] = releaseDate.split("\"");
			releaseDate = dados[1];
		}

		if (!runtime.equals("")) {
			String dados[] = runtime.split("\"");
			runtime = dados[1];
		}

		if (!alternateTitle.equals("")) {
			String dados[] = alternateTitle.split("\"");
			alternateTitle = dados[1];
		}

		bancoDados.insereFilme(name, country, varAbstract, budget, releaseDate, runtime, alternateTitle, starring , director , producer);

	}

	public static void main(String[] args) {

		bancoDados = new BancoDados();
		
		List<String> filmes = lerArquivoMovieLens();

		int t = 0, possuiApenas1Resultado = 0, possuiMaisDe1Resultado = 0;

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

				String name = "", country = "", varAbstract = "", budget = "", releaseDate = "", runtime = "",
						alternateTitle = "", starring = "", director = "", producer = "";

				String queryGeral = prefixos
						+ "SELECT DISTINCT ?name ?country ?abstract ?budget  ?releaseDate ?runtime ?alternateTitle "

						+ "WHERE { "

						+ "?instance a <http://dbpedia.org/ontology/Film>. "

						+ " ?instance foaf:name ?name . "

						+ " FILTER REGEX (?name, '^" + filmes.get(i) + "$', 'i'). " + " OPTIONAL { "
						+ "    ?instance dbpprop:country ?country  " + " } " + "OPTIONAL { "
						+ "?instance dbpedia-owl:abstract ?abstract . " + " FILTER (LANG(?abstract) = 'en'). " + "} "
						+ "OPTIONAL { " + "?instance dbpedia-owl:budget ?budget  " + "} " + "OPTIONAL { "
						+ " ?instance dbpedia-owl:releaseDate ?releaseDate  " + "} " + "OPTIONAL { "
						+ " ?instance dbpedia-owl:runtime ?runtime  " + "} " + "OPTIONAL { "
						+ " ?instance dbpprop:alternateTitle ?alternateTitle  " + "} " + "}";

				QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql",
						queryGeral);

				ResultSet resultSet = queryExecution.execSelect();


				List<QuerySolution> querySolution = ResultSetFormatter.toList(resultSet);

				if (!querySolution.isEmpty()) {
					filmesEncontradosDBPedia++;
				}

				for (int z = 0; z < querySolution.size(); z++) {
				//	System.out.println("Filme " + filmesEncontradosDBPedia + ": " + querySolution.get(z));
				}

				if (querySolution.size() == 1) {
					
					String dados[] = querySolution.get(0).toString().split("\\) \\(");

					for (int z = 0; z < dados.length; z++) {
						
						if (dados[z].contains("?name")) {
							name = dados[z];
						} else if (dados[z].contains("?country")) {
							country = dados[z];
						} else if (dados[z].contains("?abstract")) {
							varAbstract = dados[z];
						} else if (dados[z].contains("?budget")) {
							budget = dados[z];
						} else if (dados[z].contains("?releaseDate")) {
							releaseDate = dados[z];
						} else if (dados[z].contains("?runtime")) {
							runtime = dados[z];
						} else if (dados[z].contains("?alternateTitle")) {
							alternateTitle = dados[z];
						}

					}
					
					String queryStarring  = prefixos
							+ "SELECT DISTINCT ?name ?starring "

							+ "WHERE { "

							+ "?instance a <http://dbpedia.org/ontology/Film>. "

							+ " ?instance foaf:name ?name . "

							+ " FILTER REGEX (?name, '^" + filmes.get(i) + "$', 'i'). "
							+ "OPTIONAL { " + "?instance dbpedia-owl:starring ?starring  " + "} " + "}";

					QueryExecution queryExecutionStarring = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql",
							queryStarring );

					ResultSet resultSetStarring  = queryExecutionStarring .execSelect();

					
					List<QuerySolution> querySolutionStarring  = ResultSetFormatter.toList(resultSetStarring);

					if (!querySolutionStarring .isEmpty()) {
					
						for (int z = 0; z < querySolutionStarring.size(); z++) {
							
							if (querySolutionStarring.get(z).contains("?starring")) {
								String dados2 [] = querySolutionStarring.get(z).toString().split(">");
								String dados3[] = dados2[0].split("/");
								
								if(starring.equals("")){
									starring = dados3[dados3.length-1];
								}else{
									starring = starring.concat(", "+dados3[dados3.length-1]);
								}
			
							}
							
					    }
						
					}
					
					
					String queryDirector  = prefixos
							+ "SELECT DISTINCT ?name ?director "

							+ "WHERE { "

							+ "?instance a <http://dbpedia.org/ontology/Film>. "

							+ " ?instance foaf:name ?name . "

							+ " FILTER REGEX (?name, '^" + filmes.get(i) + "$', 'i'). "
							+ "OPTIONAL { " + "?instance dbpedia-owl:director ?director  " + "} " + "}";

					QueryExecution queryExecutionDirector = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql",
							queryDirector );

					ResultSet resultSetDirector  = queryExecutionDirector .execSelect();


					List<QuerySolution> querySolutionDirector  = ResultSetFormatter.toList(resultSetDirector);

					if (!querySolutionDirector .isEmpty()) {
					
						for (int z = 0; z < querySolutionDirector.size(); z++) {
							
							if (querySolutionDirector.get(z).contains("?director")) {
								String dados2 [] = querySolutionDirector.get(z).toString().split(">");
								String dados3[] = dados2[0].split("/");
								
								if(director.equals("")){
									director = dados3[dados3.length-1];
								}else{
									director = director.concat(", "+dados3[dados3.length-1]);
								}
			
							}
							
					    }
						
					}
					
					
					String queryProducer  = prefixos
							+ "SELECT DISTINCT ?name ?producer "

							+ "WHERE { "

							+ "?instance a <http://dbpedia.org/ontology/Film>. "

							+ " ?instance foaf:name ?name . "

							+ " FILTER REGEX (?name, '^" + filmes.get(i) + "$', 'i'). "
							+ "OPTIONAL { " + "?instance dbpedia-owl:producer ?producer  " + "} " + "}";

					QueryExecution queryExecutionProducer = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql",
							queryProducer );

					ResultSet resultSetProducer  = queryExecutionProducer .execSelect();


					List<QuerySolution> querySolutionProducer  = ResultSetFormatter.toList(resultSetProducer);

					if (!querySolutionProducer .isEmpty()) {
					
						for (int z = 0; z < querySolutionProducer.size(); z++) {
							
							if (querySolutionProducer.get(z).contains("?producer")) {
								String dados2 [] = querySolutionProducer.get(z).toString().split(">");
								String dados3[] = dados2[0].split("/");
								
								if(producer.equals("")){
									producer = dados3[dados3.length-1];
								}else{
									producer = producer.concat(", "+dados3[dados3.length-1]);
								}
			
							}
							
					    }
						
					}
					

					insertData(name, country, varAbstract, budget, releaseDate, runtime, alternateTitle, starring , director , producer);
					System.out.println("Inseriu: " + possuiApenas1Resultado);
					possuiApenas1Resultado++;
				} else if (querySolution.size() > 1) {
					possuiMaisDe1Resultado++;
				}


				queryExecution.close();

			}
		}

		System.out.println("\nTotal de Filmes MovieLens: " + filmes.size());
		System.out.println("Total de Filmes Encontrados DBPedia: " + filmesEncontradosDBPedia);
		System.out.println("Filmes que possuem apenas de 1 resultado:: " + possuiApenas1Resultado);
		System.out.println("\nFilmes que possuem mais de 1 resultado: " + possuiMaisDe1Resultado);
	}
}
