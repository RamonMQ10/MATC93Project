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
import Model.Movie;

public class JavaDBPediaExample {
	
	static BancoDados bancoDados;

	public static List<Movie> lerArquivoMovieLens() {

		int i = 1;
		List<Movie> filmes = new ArrayList<Movie>();

		try {
			FileReader fileReader = new FileReader("u.item");
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String linha = bufferedReader.readLine();

			while (linha != null) {

				String[] valores = linha.split("#");
				String generos="";
				if (valores.length > 1) {

					String filme[] = valores[1].split(" ");

					String tituloFilme = filme[0];

					for (int x = 1; x < filme.length - 1; x++) {

						tituloFilme = tituloFilme.concat(" " + filme[x]);
					}
					
					for (int  j= 5; j < valores.length; j++) {
						
						if(valores[j].equals("1")){
							
							String genero = buscaGenero(j);
							
							if(generos.equals("")){
								
								generos = generos.concat(genero);
							}else{
								generos = generos.concat(", "+genero);
								
							}
						}

						
					}

					Movie movie = new Movie(tituloFilme,generos,"","","","","","","","","");
					filmes.add(movie);

					System.out.println("Filme " + i + " Titulo: " + tituloFilme+" Generos: "+generos);

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
	
	public static String buscaGenero(int posicao){
		
		String genero="";
		
		switch(posicao){
		
		case 5: genero="Unknown"; break;
		case 6: genero="Action"; break;
		case 7: genero="Adventure"; break;
		case 8: genero="Animation"; break;
		case 9: genero="Children"; break;
		case 10: genero="Comedy"; break;
		case 11: genero="Crime"; break;
		case 12: genero="Documentary"; break;
		case 13: genero="Drama"; break;
		case 14: genero="Fantasy"; break;
		case 15: genero="Film-Noir"; break;
		case 16: genero="Horror"; break;
		case 17: genero="Musical"; break;
		case 18: genero="Mystery"; break;
		case 19: genero="Romance"; break;
		case 20: genero="Sci-Fi"; break;
		case 21: genero="Thriller"; break;
		case 22: genero="War"; break;
		case 23: genero="Western"; break;
		
		}
		
		return genero;
	}

	public static void insertData(Movie movie) {

		if (!movie.getName().equals("")) {
			String dados[] = movie.getName().split("\"");
			movie.setName(dados[1]);
		}

		if (!movie.getCountry().equals("")) {
			String dados[] = movie.getCountry().split("\"");
			movie.setCountry(dados[1]);
		}

		if (!movie.getVarAbstract().equals("")) {
			String dados[] = movie.getVarAbstract().split("\"");
			movie.setVarAbstract(dados[1]);
		}

		if (!movie.getBudget().equals("")) {
			String dados[] = movie.getBudget().split("\"");
			movie.setBudget(dados[1]);
		}

		if (!movie.getReleaseDate().equals("")) {
			String dados[] = movie.getReleaseDate().split("\"");
			movie.setReleaseDate(dados[1]);
		}

		if (!movie.getRuntime().equals("")) {
			String dados[] = movie.getRuntime().split("\"");
			 movie.setRuntime(dados[1]);
		}

		if (!movie.getAlternateTitle().equals("")) {
			String dados[] = movie.getAlternateTitle().split("\"");
			movie.setAlternateTitle(dados[1]);
		}
		
		
		bancoDados.insereFilme(movie);

	}

	public static void main(String[] args) {

		bancoDados = new BancoDados();
		
		List<Movie> filmes = lerArquivoMovieLens();

		int t = 0, possuiApenas1Resultado = 0, possuiMaisDe1Resultado = 0;

		for (int i = 0; i < filmes.size(); i++) {

			if (filmes.get(i).getName().contains("'")) {

				t++;
			}

		}

		System.out.println("tem ': " + t);

		int filmesEncontradosDBPedia = 0;

		String prefixos = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
				+ "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> "
				+ "PREFIX dbpprop: <http://dbpedia.org/property/> ";

		for (int i = 0; i < filmes.size(); i++) {

			if (!filmes.get(i).getName().contains("'")) {

				String nomeFilme = filmes.get(i).getName();
				String starring="" , director="" , producer="";
				
				String queryGeral = prefixos
						+ "SELECT DISTINCT ?name ?country ?abstract ?budget  ?releaseDate ?runtime ?alternateTitle "

						+ "WHERE { "

						+ "?instance a <http://dbpedia.org/ontology/Film>. "

						+ " ?instance foaf:name ?name . "

						+ " FILTER REGEX (?name, '^" + nomeFilme + "$', 'i'). " + " OPTIONAL { "
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
							filmes.get(i).setName(dados[z]);
						} else if (dados[z].contains("?country")) {
							filmes.get(i).setCountry(dados[z]);
						} else if (dados[z].contains("?abstract")) {
							filmes.get(i).setVarAbstract(dados[z]);
						} else if (dados[z].contains("?budget")) {
							filmes.get(i).setBudget(dados[z]);
						} else if (dados[z].contains("?releaseDate")) {
							filmes.get(i).setReleaseDate(dados[z]);
						} else if (dados[z].contains("?runtime")) {
							filmes.get(i).setRuntime(dados[z]);
						} else if (dados[z].contains("?alternateTitle")) {
							filmes.get(i).setAlternateTitle(dados[z]);
						}

					}
				
					
					String queryStarring  = prefixos
							+ "SELECT DISTINCT ?name ?starring "

							+ "WHERE { "

							+ "?instance a <http://dbpedia.org/ontology/Film>. "

							+ " ?instance foaf:name ?name . "

							+ " FILTER REGEX (?name, '^" + nomeFilme + "$', 'i'). "
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

							+ " FILTER REGEX (?name, '^" + nomeFilme + "$', 'i'). "
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

							+ " FILTER REGEX (?name, '^" + nomeFilme + "$', 'i'). "
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
					
					filmes.get(i).setStarring(starring); filmes.get(i).setDirector(director);; filmes.get(i).setProducer(producer);
					insertData(filmes.get(i));
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
