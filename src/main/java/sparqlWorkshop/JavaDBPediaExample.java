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

public class JavaDBPediaExample {
	
	public static void lerArquivoMovieLens() {

		int i=1;

		try {
			FileReader arq = new FileReader("u.item");
			BufferedReader lerArq = new BufferedReader(arq);

			String linha = lerArq.readLine(); 
			
		
			while (linha != null) {
				String [] valores = linha.split("-");
					
				//System.out.println("Linha: "+linha);
				System.out.println("Filme "+i+" Titulo: "+valores[1]);
				
				
				i++;
				
				linha = lerArq.readLine(); // lê da segunda até a última linha
				
			}

			arq.close();

			
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}

		System.out.println();

	}

	public static void main(String[] args) {
		
		lerArquivoMovieLens();

		String prefixos = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
				+ "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> "
				+ "PREFIX dbpprop: <http://dbpedia.org/property/> ";

		String query = prefixos + "SELECT DISTINCT ?property WHERE {"
				+ "?instance a <http://dbpedia.org/ontology/Film> . "
				+ "?instance ?property ?obj . "
				+ "} "
				+ "ORDER BY ?property";

		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

		ResultSet resultSet = queryExecution.execSelect();

		//ResultSetFormatter.out(System.out, resultSet);
		
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		List <QuerySolution> a = ResultSetFormatter.toList(resultSet);
	
		
		for(int i =0; i < a.size(); i++){
			
			//System.out.println("Propriedade:"+a.get(i));
		}
		
		System.out.println("Total de Propriedades:"+a.size());

		queryExecution.close();
	}
}
